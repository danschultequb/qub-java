package qub;

public interface TCPClient extends ByteWriteStream, ByteReadStream
{
    /**
     * Get the local IP address that this client is connected to.
     * @return The local IP address that this client is connected to.
     */
    IPv4Address getLocalIPAddress();

    /**
     * Get the local port that this client is connected to.
     * @return The local port that this client is connected to.
     */
    int getLocalPort();

    /**
     * Get the remote IP address that this client is connected to.
     * @return The remote IP address that this client is connected to.
     */
    IPv4Address getRemoteIPAddress();

    /**
     * Get the remote port that this client is connected to.
     * @return The remote port that this client is connected to.
     */
    int getRemotePort();

    /**
     * Get this TCPClient's internal ByteReadStream.
     * @return The internal ByteReadStream of this TCPClient.
     */
    ByteReadStream getReadStream();

    /**
     * Get this TCPClient's internal ByteWriteStream.
     * @return The internal ByteWriteStream of this TCPClient.
     */
    ByteWriteStream getWriteStream();

    @Override
    default Result<Byte> readByte()
    {
        return getReadStream().readByte();
    }

    @Override
    default Result<byte[]> readBytes(int bytesToRead)
    {
        return getReadStream().readBytes(bytesToRead);
    }

    @Override
    default Result<Integer> readBytes(byte[] outputBytes)
    {
        return getReadStream().readBytes(outputBytes);
    }

    @Override
    default Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        return getReadStream().readBytes(outputBytes, startIndex, length);
    }

    @Override
    default Result<byte[]> readAllBytes()
    {
        return getReadStream().readAllBytes();
    }

    @Override
    default Result<byte[]> readBytesUntil(byte stopByte)
    {
        return getReadStream().readBytesUntil(stopByte);
    }

    @Override
    default Result<byte[]> readBytesUntil(byte[] stopBytes)
    {
        return getReadStream().readBytesUntil(stopBytes);
    }

    @Override
    default Result<byte[]> readBytesUntil(Iterable<Byte> stopBytes)
    {
        return getReadStream().readBytesUntil(stopBytes);
    }

    @Override
    default CharacterReadStream asCharacterReadStream()
    {
        return getReadStream().asCharacterReadStream();
    }

    @Override
    default CharacterReadStream asCharacterReadStream(CharacterEncoding characterEncoding)
    {
        return getReadStream().asCharacterReadStream(characterEncoding);
    }

    @Override
    default Result<Integer> write(byte toWrite)
    {
        return this.getWriteStream().write(toWrite);
    }

    @Override
    default Result<Integer> write(byte[] toWrite)
    {
        return this.getWriteStream().write(toWrite);
    }

    @Override
    default Result<Integer> write(byte[] toWrite, int startIndex, int length)
    {
        return getWriteStream().write(toWrite, startIndex, length);
    }

    @Override
    default void ensureHasStarted()
    {
        getReadStream().ensureHasStarted();
    }

    @Override
    default boolean hasStarted()
    {
        return getReadStream().hasStarted();
    }

    @Override
    default boolean hasCurrent()
    {
        return getReadStream().hasCurrent();
    }

    @Override
    default Byte getCurrent()
    {
        return getReadStream().getCurrent();
    }

    @Override
    default boolean next()
    {
        return getReadStream().next();
    }

    @Override
    default Byte takeCurrent()
    {
        return getReadStream().takeCurrent();
    }

    @Override
    default boolean any()
    {
        return getReadStream().any();
    }

    @Override
    default int getCount()
    {
        return getReadStream().getCount();
    }

    @Override
    default Byte first()
    {
        return getReadStream().first();
    }

    @Override
    default Byte first(Function1<Byte, Boolean> condition)
    {
        return getReadStream().first(condition);
    }

    @Override
    default Byte last()
    {
        return getReadStream().last();
    }

    @Override
    default Byte last(Function1<Byte, Boolean> condition)
    {
        return getReadStream().last(condition);
    }

    @Override
    default boolean contains(Byte value)
    {
        return getReadStream().contains(value);
    }

    @Override
    default boolean contains(Function1<Byte, Boolean> condition)
    {
        return getReadStream().contains(condition);
    }

    @Override
    default Iterator<Byte> take(int toTake)
    {
        return getReadStream().take(toTake);
    }

    @Override
    default Iterator<Byte> skip(int toSkip)
    {
        return getReadStream().skip(toSkip);
    }

    @Override
    default Iterator<Byte> skipUntil(Function1<Byte, Boolean> condition)
    {
        return getReadStream().skipUntil(condition);
    }

    @Override
    default Iterator<Byte> where(Function1<Byte, Boolean> condition)
    {
        return getReadStream().where(condition);
    }

    @Override
    default <U> Iterator<U> map(Function1<Byte, U> conversion)
    {
        return getReadStream().map(conversion);
    }

    @Override
    default <U> Iterator<U> instanceOf(Class<U> type)
    {
        return getReadStream().instanceOf(type);
    }

    @Override
    default java.util.Iterator<Byte> iterator()
    {
        return getReadStream().iterator();
    }
}
