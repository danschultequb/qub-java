package qub;

public class JavaNetwork implements Network
{
    private final Clock clock;

    private JavaNetwork(Clock clock)
    {
        PreCondition.assertNotNull(clock, "clock");

        this.clock = clock;
    }

    public static JavaNetwork create(Clock clock)
    {
        return new JavaNetwork(clock);
    }

    @Override
    public Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort)
    {
        Network.validateRemoteIPAddress(remoteIPAddress);
        Network.validateRemotePort(remotePort);

        Result<TCPClient> result;
        try
        {
            final byte[] remoteIPAddressBytes = remoteIPAddress.toBytes();
            final java.net.InetAddress remoteInetAddress = java.net.InetAddress.getByAddress(remoteIPAddressBytes);
            final java.net.Socket socket = new java.net.Socket(remoteInetAddress, remotePort);
            result = JavaTCPClient.create(socket);
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort, Duration timeout)
    {
        Network.validateRemoteIPAddress(remoteIPAddress);
        Network.validateRemotePort(remotePort);
        Network.validateTimeout(timeout);
        PreCondition.assertBetween(0, timeout.toMilliseconds().getValue(), Integers.maximum, "timeout.toMilliseconds().getValue()");

        Result<TCPClient> result;
        try
        {
            final byte[] remoteIPAddressBytes = remoteIPAddress.toBytes();
            final java.net.InetAddress remoteInetAddress = java.net.InetAddress.getByAddress(remoteIPAddressBytes);
            final java.net.SocketAddress socketAddress = new java.net.InetSocketAddress(remoteInetAddress, remotePort);
            final java.net.Socket socket = new java.net.Socket();
            socket.connect(socketAddress, (int)timeout.toMilliseconds().getValue());

            result = JavaTCPClient.create(socket);
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort, DateTime timeout)
    {
        Network.validateRemoteIPAddress(remoteIPAddress);
        Network.validateRemotePort(remotePort);
        Network.validateTimeout(timeout);

        final DateTime now = this.clock.getCurrentDateTime();
        final Duration timeoutDuration = timeout.minus(now);
        return this.createTCPClient(remoteIPAddress, remotePort, timeoutDuration);
    }

    @Override
    public Result<TCPServer> createTCPServer(int localPort)
    {
        Network.validateLocalPort(localPort);

        return JavaTCPServer.create(localPort, clock);
    }

    @Override
    public Result<TCPServer> createTCPServer(IPv4Address localIPAddress, int localPort)
    {
        Network.validateLocalIPAddress(localIPAddress);
        Network.validateLocalPort(localPort);

        return JavaTCPServer.create(localIPAddress, localPort, clock);
    }
}
