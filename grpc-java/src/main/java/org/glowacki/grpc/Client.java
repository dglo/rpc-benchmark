package org.glowacki.grpc;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client
{
    private static final Logger logger =
        Logger.getLogger(Client.class.getName());

    private static final int port = 50051;

    private final Channel channel;
    private final DAQClientGrpc.DAQClientBlockingStub blockingStub;

    public Client(String host, int port)
    {
        this(createChannel(host, port));
    }

    /**
     * Construct client for accessing server using the existing channel.
     */
    public Client(Channel channel)
    {
        // remember channel so we can shut it down later
        this.channel = channel;

        // Passing Channels to code makes code easier to test and makes it
        // easier to reuse Channels.
        blockingStub = DAQClientGrpc.newBlockingStub(channel);
    }

    private static final Channel createChannel(String host, int port)
    {
        // server address
        final String target = host + ":" + port;

        // Create a communication channel to the server, known as a Channel.
        // Channels are thread-safe and reusable. It is common to create
        // channels at the beginning of your application and reuse them until
        // the application shuts down.
        return ManagedChannelBuilder.forTarget(target)
            // Channels are secure by default (via SSL/TLS). For the example
            // we disable TLS to avoid needing certificates.
            .usePlaintext()
            .build();
    }

    public static ConnectRequest.Connection createConnection(String type,
                                                             String compName,
                                                             int compNum,
                                                             String host,
                                                             int port)
    {
        ConnectRequest.Connection.Builder builder =
            ConnectRequest.Connection.newBuilder();

        builder.setType(type);
        builder.setCompName(compName);
        builder.setCompNum(compNum);
        builder.setHost(host);
        builder.setPort(port);

        return builder.build();
    }

    public String configure(String filename)
    {
        ConfigureRequest request =
            ConfigureRequest.newBuilder().setFilename(filename).build();

        ClientReply reply;
        try {
            reply = blockingStub.configure(request);
            return reply.getResult();
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "configure failed: {0}", e.getStatus());
            return null;
        }
    }

    public String connect(List<ConnectRequest.Connection> connections)
    {
        ConnectRequest.Builder builder = ConnectRequest.newBuilder();
        builder.addAllConnections(connections);
        ConnectRequest request = builder.build();

        ClientReply reply;
        try {
            reply = blockingStub.connect(request);
            return reply.getResult();
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "connect failed: {0}", e.getStatus());
            return null;
        }
    }

    public void shutdown()
    {
        // ManagedChannels use resources like threads and TCP connections.
        // To prevent leaking these resources the channel should be shut
        // down when it will no longer be used. If it may be used again
        // leave it running.
        if (channel instanceof ManagedChannel) {
            try {
                ((ManagedChannel) channel).shutdownNow()
                    .awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException exc) {
                System.err.println("ERROR: Shutdown interrupted");
                exc.printStackTrace();
            }
        }
    }

    // walk server through a 'run'
    public void singleDAQRun(boolean verbose)
    {
        String result;

        result = configure("run_config.xml");
        if (verbose) System.out.println("configure: " + result);

        List<ConnectRequest.Connection> connections =
            Collections.singletonList(createConnection("i", "component", 0,
                                                       "localhost", 12345));
        result = connect(connections);
        if (verbose) System.out.println("connect: " + result);

        result = startRun(123456);
        if (verbose) System.out.println("start_run: " + result);

        result = stopRun();
        if (verbose) System.out.println("stop_run: " + result);
    }

    public String startRun(int runNum)
    {
        StartRunRequest request =
            StartRunRequest.newBuilder().setRunNum(runNum).build();

        ClientReply reply;
        try {
            reply = blockingStub.startRun(request);
            return reply.getResult();
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "startRun failed: {0}", e.getStatus());
            return null;
        }
    }

    public String stopRun()
    {
        EmptyRequest request = EmptyRequest.newBuilder().build();

        ClientReply reply;
        try {
            reply = blockingStub.stopRun(request);
            return reply.getResult();
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "stopRun failed: {0}", e.getStatus());
            return null;
        }
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
