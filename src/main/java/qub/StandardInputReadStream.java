package qub;

import java.io.IOException;

/**
 * A ReadStream that reads bytes from the StandardInput stream of the process.
 */

public class StandardInputReadStream extends ReadStreamBase
{
    @Override
    public boolean isOpen()
    {
        return true;
    }

    @Override
    public boolean close()
    {
        return false;
    }

    @Override
    public int readBytes(byte[] output, int startIndex, int length) throws IOException
    {
        return System.in.read(output, startIndex, length);
    }
}