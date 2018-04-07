package qub;

public class JavaNetworkTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaNetwork.class, () ->
        {
            runner.testGroup("createTCPClient(IPv4Address,int)", () ->
            {
                runner.test("with null remoteIPAddress", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(test.getParallelAsyncRunner());
                    final Result<TCPClient> tcpClientResult = network.createTCPClient(null, 80);
                    test.assertError(new IllegalArgumentException("remoteIPAddress cannot be null."), tcpClientResult);
                });

                runner.test("with -1 remotePort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(test.getParallelAsyncRunner());
                    final Result<TCPClient> tcpClientResult = network.createTCPClient(IPv4Address.parse("127.0.0.1"), -1);
                    test.assertError(new IllegalArgumentException("remotePort must be greater than 0."), tcpClientResult);
                });

                runner.test("with 0 remotePort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(test.getParallelAsyncRunner());
                    final Result<TCPClient> tcpClientResult = network.createTCPClient(IPv4Address.parse("127.0.0.1"), 0);
                    test.assertError(new IllegalArgumentException("remotePort must be greater than 0."), tcpClientResult);
                });

                runner.test("with valid arguments but no server listening", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(test.getParallelAsyncRunner());
                    final Result<TCPClient> tcpClientResult = network.createTCPClient(IPv4Address.parse("127.0.0.1"), 38827);
                    test.assertError(new java.net.ConnectException("Connection refused: connect"), tcpClientResult);
                });

                runner.test("with valid arguments and server listening", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();
                    final JavaNetwork network = new JavaNetwork(asyncRunner);

                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5 };

                    final IPv4Address localhost = IPv4Address.parse("127.0.0.1");
                    final int port = 8080;

                    final AsyncAction serverTask = network.createTCPServerAsync(localhost, port)
                        .then((Result<TCPServer> tcpServerResult) ->
                        {
                            test.assertSuccess(tcpServerResult);
                            try (final TCPServer tcpServer = tcpServerResult.getValue())
                            {
                                final Result<TCPClient> acceptedClientResult = tcpServer.accept();
                                test.assertSuccess(acceptedClientResult);

                                try (final TCPClient acceptedClient = acceptedClientResult.getValue())
                                {
                                    test.assertEqual(bytes, acceptedClient.readBytes(bytes.length));
                                    test.assertTrue(acceptedClient.write(bytes));
                                }
                            }
                        });

                    final AsyncAction clientTask = network.createTCPClientAsync(localhost, port)
                        .then((Result<TCPClient> tcpClientResult) ->
                        {
                            test.assertSuccess(tcpClientResult);
                            try (final TCPClient tcpClient = tcpClientResult.getValue())
                            {
                                test.assertTrue(tcpClient.write(bytes));
                                test.assertEqual(bytes, tcpClient.readBytes(bytes.length));
                            }
                        });

                    serverTask.await();
                    clientTask.await();
                });
            });

            runner.testGroup("createTCPServer(int)", () ->
            {
                runner.test("with -1 localPort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(test.getParallelAsyncRunner());
                    final Result<TCPServer> tcpServerResult = network.createTCPServer(-1);
                    test.assertTrue(tcpServerResult.hasError());
                    test.assertEqual("localPort must be greater than 0.", tcpServerResult.getErrorMessage());
                });

                runner.test("with 0 localPort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(test.getParallelAsyncRunner());
                    final Result<TCPServer> tcpServerResult = network.createTCPServer(0);
                    test.assertTrue(tcpServerResult.hasError());
                    test.assertEqual("localPort must be greater than 0.", tcpServerResult.getErrorMessage());
                });

                runner.test("with 8088 localPort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(test.getParallelAsyncRunner());
                    final Result<TCPServer> tcpServerResult = network.createTCPServer(8088);
                    test.assertSuccess(tcpServerResult);
                    test.assertSuccess(tcpServerResult.getValue().dispose());
                });
            });
        });
    }
}
