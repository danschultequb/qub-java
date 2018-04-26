package qub;

import java.io.IOException;

public interface ByteReadStream extends Disposable, Iterator<Byte>
{
    Result<Byte> readByte();

    Result<byte[]> readBytes(int bytesToRead);

    Result<Integer> readBytes(byte[] outputBytes);

    Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length);

    Result<byte[]> readAllBytes();

    void setExceptionHandler(Action1<IOException> exceptionHandler);

    java.io.InputStream asInputStream();

    CharacterReadStream asCharacterReadStream();

    CharacterReadStream asCharacterReadStream(CharacterEncoding characterEncoding);

    LineReadStream asLineReadStream();

    LineReadStream asLineReadStream(CharacterEncoding characterEncoding);

    LineReadStream asLineReadStream(boolean includeNewLines);

    LineReadStream asLineReadStream(CharacterEncoding characterEncoding, boolean includeNewLines);
}
