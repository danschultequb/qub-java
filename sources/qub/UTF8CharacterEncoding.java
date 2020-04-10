package qub;

public class UTF8CharacterEncoding implements CharacterEncoding
{
    @Override
    public Result<Integer> encode(char character, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        return Result.create(() -> UTF8CharacterEncoding.writeEncodedCharacter(character, byteWriteStream));
    }

    @Override
    public Result<Integer> encode(String text, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(text, "text");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream.isDisposed()");

        return Result.create(() ->
        {
            int result = 0;
            for (final char character : Strings.iterate(text))
            {
                result += UTF8CharacterEncoding.writeEncodedCharacter(character, byteWriteStream);
            }
            return result;
        });
    }

    @Override
    public Result<Integer> encode(char[] characters, int startIndex, int length, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertStartIndex(startIndex, characters.length);
        PreCondition.assertLength(length, startIndex, characters.length);
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream.isDisposed()");

        return Result.create(() ->
        {
            int result = 0;
            for (int i = 0; i < length; ++i)
            {
                result += UTF8CharacterEncoding.writeEncodedCharacter(characters[startIndex + i], byteWriteStream);
            }
            return result;
        });
    }

    @Override
    public Result<char[]> decode(byte[] bytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertStartIndex(startIndex, bytes.length);
        PreCondition.assertLength(length, startIndex, bytes.length);

        return Result.create(() ->
        {
            final List<Character> characters = List.create();
            final Iterator<Byte> byteIterator = Iterator.create(bytes, startIndex, length);
            while (true)
            {
                final Character decodedCharacter = decodeNextCharacter(byteIterator)
                    .catchError(EndOfStreamException.class)
                    .await();
                if (decodedCharacter != null)
                {
                    characters.add(decodedCharacter);
                }
                else
                {
                    break;
                }
            }
            return Array.toCharArray(characters).await();
        });
    }

    @Override
    public Result<Character> decodeNextCharacter(Iterator<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        Result<Character> result;
        if (!bytes.next())
        {
            result = Result.error(new EndOfStreamException());
        }
        else if (bytes.getCurrent() == null)
        {
            result = Result.success();
        }
        else
        {
            final Byte firstByte = bytes.getCurrent();
            final int firstByteSignificantBitCount = Bytes.getSignificantBitCount(firstByte);
            switch (firstByteSignificantBitCount)
            {
                case 0:
                    result = Result.success((char)firstByte.intValue());
                    break;

                case 1:
                    result = Result.error(new IllegalArgumentException("Expected a leading byte, but found a continuation byte (" + Bytes.toHexString(firstByte) + ") instead."));
                    break;

                case 2:
                case 3:
                case 4:
                    result = decodeMultiByteCharacter(firstByte, firstByteSignificantBitCount, bytes);
                    break;

                default:
                    result = Result.error(new IllegalArgumentException("Found an invalid leading byte (" + Bytes.toHexString(firstByte) + ")."));
                    break;
            }
        }
        return result;
    }

    private static Result<Character> decodeMultiByteCharacter(byte firstByte, int expectedBytesInCharacter, Iterator<Byte> bytes)
    {
        PreCondition.assertGreaterThanOrEqualTo(expectedBytesInCharacter, 1, "expectedBytesInCharacter");
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertTrue(bytes.hasCurrent(), "bytes.hasCurrent()");

        Result<Character> result;
        switch (expectedBytesInCharacter)
        {
            case 2:
                if (!bytes.next() || bytes.getCurrent() == null)
                {
                    result = Result.error(new IllegalArgumentException("Missing second byte in 2-byte character sequence."));
                }
                else
                {
                    final int unsignedFirstByte = Bytes.toUnsignedInt(firstByte);
                    if (0xD8 <= unsignedFirstByte && unsignedFirstByte <= 0xDF)
                    {
                        result = Result.error(new IllegalArgumentException("Byte " + Bytes.toHexString(firstByte, true) + " is invalid because bytes between 0xD800 and 0xDFFF are reserved in UTF-8 encoding."));
                    }
                    else
                    {
                        final Byte secondByte = bytes.getCurrent();
                        final int secondByteSignificantBitCount = Bytes.getSignificantBitCount(secondByte);
                        if (secondByteSignificantBitCount != 1)
                        {
                            result = Result.error(new IllegalArgumentException("Expected continuation byte (10xxxxxx), but found " + Bytes.toHexString(secondByte) + " instead."));
                        }
                        else
                        {
                            final int firstByteLastFiveBits = Bytes.toUnsignedInt(firstByte) & 0x1F;
                            final int secondByteLastSixBits = Bytes.toUnsignedInt(secondByte) & 0x3F;
                            final int resultBits = (firstByteLastFiveBits << 6) | secondByteLastSixBits;
                            result = Result.success((char)resultBits);
                        }
                    }
                }
                break;

            default:
                for (int i = 1; bytes.hasCurrent() && i < expectedBytesInCharacter; ++i)
                {
                    bytes.next();
                }
                result = Result.error(new NotSupportedException("Decoding UTF-8 encoded byte streams with characters composed of 3 or more bytes are not supported."));
                break;
        }

        return result;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return CharacterEncoding.equals(this, rhs);
    }

    private static int writeEncodedCharacter(char value, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream.isDisposed()");

        int result = 0;

        final int characterAsInt = (int)value;
        if (characterAsInt <= 0x00007F)
        {
            byteWriteStream.write((byte)characterAsInt).await();
            result = 1;
        }
        else if (characterAsInt <= 0x0007FF)
        {
            final int firstFiveBits = (characterAsInt >>> 6) & 0x1F;
            final byte firstByte = (byte)(0xC0 | firstFiveBits);
            byteWriteStream.write(firstByte).await();

            final int lastSixBits = characterAsInt & 0x3F;
            final byte secondByte = (byte)(0x80 | lastSixBits);
            byteWriteStream.write(secondByte).await();
            result = 2;
        }
        else if (characterAsInt <= 0x00FFFF)
        {
            final int firstFourBits = (characterAsInt >>> 12) & 0xF;
            final byte firstByte = (byte)(0xB0 | firstFourBits);
            byteWriteStream.write(firstByte).await();

            final int middleSixBits = (characterAsInt >>> 6) & 0x3F;
            final byte secondByte = (byte)(0x80 | middleSixBits);
            byteWriteStream.write(secondByte).await();

            final int lastSixBites = characterAsInt & 0x3F;
            final byte thirdByte = (byte)(0x80 | lastSixBites);
            byteWriteStream.write(thirdByte).await();
            result = 3;
        }
        else if (characterAsInt <= 0x10FFFF)
        {
            final int firstThreeBits = (characterAsInt >>> 18) & 0x7;
            final byte firstByte = (byte)(0xF0 | firstThreeBits);
            byteWriteStream.write(firstByte).await();

            final int firstMiddleSixBits = (characterAsInt >>> 12) & 0x3F;
            final byte secondByte = (byte)(0x80 | firstMiddleSixBits);
            byteWriteStream.write(secondByte).await();

            final int secondMiddleSixBits = (characterAsInt >>> 6) & 0x3F;
            final byte thirdByte = (byte)(0x80 | secondMiddleSixBits);
            byteWriteStream.write(thirdByte).await();

            final int lastSixBits = (characterAsInt & 0x3F);
            final byte fourthByte = (byte)(0x80 | lastSixBits);
            byteWriteStream.write(fourthByte).await();
            result = 4;
        }

        PostCondition.assertBetween(1, result, 4, "result");

        return result;
    }
}
