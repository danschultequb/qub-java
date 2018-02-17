package qub;

import java.io.IOException;

/**
 * An interface for writing bytes to a stream.
 */
public interface ByteWriteStream extends Stream
{
    void setExceptionHandler(Action1<Exception> exceptionHandler);

    /**
     * Write the provided byte to this ByteWriteStream.
     * @param toWrite The byte to write to this stream.
     */
    boolean write(byte toWrite);

    /**
     * Write the provided bytes to this ByteWriteStream.
     * @param toWrite The bytes to write to this stream.
     */
    default boolean write(byte[] toWrite)
    {
        return write(toWrite, 0, toWrite == null ? 0 : toWrite.length);
    }

    /**
     * Write the provided subsection of bytes to this ByteWriteStream.
     * @param toWrite The array of bytes that contains the bytes to write to this stream.
     * @param startIndex The start index of the subsection inside toWrite to write.
     * @param length The number of bytes to write.
     */
    default boolean write(byte[] toWrite, int startIndex, int length)
    {
        boolean result = true;
        if (toWrite != null && length > 0)
        {
            final int afterEndIndex = Math.minimum(startIndex + length, toWrite.length);
            for (int i = startIndex; i < afterEndIndex; ++i)
            {
                result = write(toWrite[i]);
                if (!result)
                {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Write all of the bytes from the provided byteReadStream to this ByteWriteStream.
     * @param byteReadStream The ByteReadStream to read from.
     * @return Whether or not the write was successful.
     */
    default boolean writeAll(ByteReadStream byteReadStream)
    {
        boolean result = false;

        if (byteReadStream != null && isOpen() && byteReadStream.isOpen())
        {
            final byte[] buffer = new byte[1024];
            int bytesRead = byteReadStream.readBytes(buffer);

            if (bytesRead > 0)
            {
                result = true;
            }

            while (bytesRead > 0)
            {
                write(buffer, 0, bytesRead);
                bytesRead = byteReadStream.readBytes(buffer);
            }
        }

        return result;
    }

    /**
     * Convert this ByteWriteStream to a CharacterWriteStream that uses UTF-8 for its character
     * encoding.
     * @return A CharacterWriteStream that wraps around this ByteWriteStream.
     */
    default CharacterWriteStream asCharacterWriteStream()
    {
        return asCharacterWriteStream(CharacterEncoding.UTF_8);
    }

    /**
     * Convert this ByteWriteStream to a CharacterWriteStream that uses the provided character
     * encoding.
     * @param encoding The encoding to use to convert characters to bytes.
     * @return A CharacterWriteStream that wraps around this ByteWriteStream.
     */
    default CharacterWriteStream asCharacterWriteStream(CharacterEncoding encoding)
    {
        return encoding == null ? null : new ByteWriteStreamToCharacterWriteStream(this, encoding);
    }

    /**
     * Convert this ByteWriteStream to a LineWriteStream that uses UTF-8 for its character
     * encoding and '\n' as its line separator.
     * @return A LineWriteStream that wraps around this ByteWriteStream.
     */
    default LineWriteStream asLineWriteStream()
    {
        final CharacterWriteStream characterWriteStream = asCharacterWriteStream();
        return characterWriteStream.asLineWriteStream();
    }

    /**
     * Convert this ByteWriteStream to a LineWriteStream that uses the provided character encoding
     * and '\n' as its line separator.
     * @param encoding The encoding to use to convert characters to bytes.
     * @return A LineWriteStream that wraps around this ByteWriteStream.
     */
    default LineWriteStream asLineWriteStream(CharacterEncoding encoding)
    {
        final CharacterWriteStream characterWriteStream = asCharacterWriteStream(encoding);
        return characterWriteStream == null ? null : characterWriteStream.asLineWriteStream();
    }

    /**
     * Convert this ByteWriteStream to a LineWriteStream that uses UTF-8 for its character
     * encoding and the provided line separator.
     * @param lineSeparator The separator to insert between lines.
     * @return A LineWriteStream that wraps around this ByteWriteStream.
     */
    default LineWriteStream asLineWriteStream(String lineSeparator)
    {
        final CharacterWriteStream characterWriteStream = asCharacterWriteStream();
        return characterWriteStream.asLineWriteStream(lineSeparator);
    }

    /**
     * Convert this ByteWriteStream to a LineWriteStream that uses the provided character encoding
     * and the provided line separator.
     * @param encoding The encoding to use to convert characters to bytes.
     * @param lineSeparator The separator to insert between lines.
     * @return A LineWriteStream that wraps around this ByteWriteStream.
     */
    default LineWriteStream asLineWriteStream(CharacterEncoding encoding, String lineSeparator)
    {
        final CharacterWriteStream characterWriteStream = asCharacterWriteStream(encoding);
        return characterWriteStream == null ? null : characterWriteStream.asLineWriteStream(lineSeparator);
    }
}
