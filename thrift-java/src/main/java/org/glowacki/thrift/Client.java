package org.glowacki.thrift;

import java.util.Collections;
import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class Client
{
    private static final int port = 50051;

    private TTransport transport;
    private DAQClient.Client tclient;

    public Client(String host, int port)
        throws TTransportException
    {
        transport = new TSocket(host, port);
        transport.open();

        TProtocol protocol = new TBinaryProtocol(transport);
        tclient = new DAQClient.Client(protocol);
    }

    public void shutdown()
    {
        transport.close();
    }

    // walk server through a 'run'
    public void singleDAQRun(boolean verbose)
        throws TException
    {
        String result;

        result = tclient.configure("run_config.xml");
        if (verbose) System.out.println("configure: " + result);

        Connection connection =
            new Connection("i", "component", 0, "localhost", 12345);
        List<Connection> connections =
            Collections.singletonList(connection);
        result = tclient.connect(connections);
        if (verbose) System.out.println("connect: " + result);

        result = tclient.startRun(123456);
        if (verbose) System.out.println("start_run: " + result);

        result = tclient.stopRun();
        if (verbose) System.out.println("stop_run: " + result);
    }

    public static void main(String[] args)
        throws Exception
    {
        Client client = new Client("localhost", port);
        try {
            client.singleDAQRun(true);
        } finally {
            client.shutdown();
        }
    }
}
