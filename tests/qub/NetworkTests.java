package qub;

public class NetworkTests
{
    public static void test(TestRunner runner, Function1<Test,Network> creator)
    {
        runner.testGroup(Network.class, () ->
        {
            runner.testGroup("createTCPClient(IPv4Address,int)", () ->
            {
                runner.test("with null remoteIPAddress", (Test test) ->
                {
                    final Network network = creator.run(test);
                    final Result<TCPClient> tcpClientResult = network.createTCPClient(null, 80);
                    test.assertError(new IllegalArgumentException("remoteIPAddress cannot be null."), tcpClientResult);
                });

                runner.test("with -1 remotePort", (Test test) ->
                {
                    final Network network = creator.run(test);
                    final Result<TCPClient> tcpClientResult = network.createTCPClient(IPv4Address.parse("127.0.0.1"), -1);
                    test.assertError(new IllegalArgumentException("remotePort (-1) must be between 1 and 65535."), tcpClientResult);
                });

                runner.test("with 0 remotePort", (Test test) ->
                {
                    final Network network = creator.run(test);
                    final Result<TCPClient> tcpClientResult = network.createTCPClient(IPv4Address.parse("127.0.0.1"), 0);
                    test.assertError(new IllegalArgumentException("remotePort (0) must be between 1 and 65535."), tcpClientResult);
                });

                runner.test("with valid arguments but no server listening", (Test test) ->
                {
                    final Network network = creator.run(test);
                    final Result<TCPClient> tcpClientResult = network.createTCPClient(IPv4Address.parse("127.0.0.1"), 38827);
                    test.assertError(new java.net.ConnectException("Connection refused: connect"), tcpClientResult);
                });

                runner.test("with valid arguments and server listening", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();
                    final Network network = creator.run(test);

                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5 };

                    final int port = 8080;

                    final AsyncAction serverTask = asyncRunner.schedule(() ->
                    {
                        final Result<TCPServer> tcpServerResult = network.createTCPServer(IPv4Address.localhost, port);
                        test.assertSuccess(tcpServerResult);
                        try (final TCPServer tcpServer = tcpServerResult.getValue())
                        {
                            test.assertEqual(IPv4Address.localhost, tcpServer.getLocalIPAddress());
                            test.assertEqual(port, tcpServer.getLocalPort());
                            final Result<TCPClient> acceptedClientResult = tcpServer.accept();
                            test.assertSuccess(acceptedClientResult);
                            try (final TCPClient acceptedClient = acceptedClientResult.getValue())
                            {
                                test.assertSuccess(bytes, acceptedClient.readBytes(bytes.length));
                                test.assertSuccess(true, acceptedClient.write(bytes));
                            }
                        }
                    });

                    final AsyncAction clientTask = asyncRunner.schedule(() ->
                    {
                        final Result<TCPClient> tcpClientResult = network.createTCPClient(IPv4Address.localhost, port);
                        test.assertSuccess(tcpClientResult);
                        try (final TCPClient tcpClient = tcpClientResult.getValue())
                        {
                            test.assertEqual(IPv4Address.localhost, tcpClient.getLocalIPAddress());
                            test.assertNotEqual(port, tcpClient.getLocalPort());
                            test.assertEqual(IPv4Address.localhost, tcpClient.getRemoteIPAddress());
                            test.assertEqual(port, tcpClient.getRemotePort());
                            test.assertSuccess(true, tcpClient.write(bytes));
                            test.assertSuccess(bytes, tcpClient.readBytes(bytes.length));
                        }
                    });

                    AsyncRunnerBase.awaitAll(clientTask, serverTask);
                });
            });

            runner.testGroup("createTCPServer(int)", () ->
            {
                runner.test("with -1 localPort", (Test test) ->
                {
                    final Network network = creator.run(test);
                    final Result<TCPServer> tcpServerResult = network.createTCPServer(-1);
                    test.assertError(new IllegalArgumentException("localPort (-1) must be between 1 and 65535."), tcpServerResult);
                });

                runner.test("with 0 localPort", (Test test) ->
                {
                    final Network network = creator.run(test);
                    final Result<TCPServer> tcpServerResult = network.createTCPServer(0);
                    test.assertError(new IllegalArgumentException("localPort (0) must be between 1 and 65535."), tcpServerResult);
                });

                runner.test("with 8088 localPort", (Test test) ->
                {
                    final Network network = creator.run(test);
                    final Result<TCPServer> tcpServerResult = network.createTCPServer(8088);
                    test.assertSuccess(tcpServerResult);
                    test.assertSuccess(tcpServerResult.getValue().dispose());
                });
            });
        });
    }
}