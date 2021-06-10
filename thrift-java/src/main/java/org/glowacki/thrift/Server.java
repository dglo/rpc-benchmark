package org.glowacki.thrift;

import org.glowacki.thrift.DAQClient;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

public class Server
{
    private static final int port = 50051;

    private TServer server;

    public void run()
        throws TTransportException
    {
        TServerTransport serverTransport = new TServerSocket(port);
        server = new TSimpleServer(new TServer.Args(serverTransport)
                                   .processor(new DAQClient.Processor<>(new DAQClientImpl())));

        System.out.print("Starting the server... ");

        server.serve();

        System.out.println("done.");
    }

    public void shutdown()
    {
        if (server != null && server.isServing()) {
            System.out.print("Stopping the server... ");

            server.stop();

            System.out.println("done.");
        }
    }

    public static void main(String[] args)
        throws Exception
    {
        final Server server = new Server();
        try {
            server.run();
        } finally {
            server.shutdown();
        }
    }
}
