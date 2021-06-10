package org.glowacki.grpc;

//import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Server that manages startup/shutdown of a {@code DAQClient} server.
 */
public class Server
    implements Runnable
{
    private static final Logger logger =
        Logger.getLogger(Server.class.getName());

    private io.grpc.Server gserver;

    /**
     * Await termination on the main thread since the grpc library uses
     * daemon threads.
     */
    private void blockUntilShutdown()
        throws InterruptedException
    {
        if (gserver != null) {
            gserver.awaitTermination();
        }
    }

    public void run()
    {
        try {
            start();
            blockUntilShutdown();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void shutdown()
        throws InterruptedException
    {
        if (gserver != null) {
            gserver.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void start()
        throws IOException
    {
        /* The port on which the server should run */
        int port = 50051;
        gserver = ServerBuilder.forPort(port)
            .addService(new DAQClientImpl())
            .build()
            .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    // Use stderr here since the logger may have been reset by
                    // its JVM shutdown hook.
                    System.err.println("*** shutting down gRPC server since" +
                                       " JVM is shutting down");
                    try {
                        Server.this.shutdown();
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.err);
                    }
                    System.err.println("*** server shut down");
                }
            });
    }

    /**
     * Main launches the server from the command line.
     */
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
