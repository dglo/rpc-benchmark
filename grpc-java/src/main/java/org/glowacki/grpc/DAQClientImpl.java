package org.glowacki.grpc;

import io.grpc.stub.StreamObserver;

public class DAQClientImpl
    extends DAQClientGrpc.DAQClientImplBase
{
    private boolean debug;

    public DAQClientImpl()
    {
        this(false);
    }

    public DAQClientImpl(boolean debug)
    {
        this.debug = debug;
    }

    @Override
    public void configure(ConfigureRequest req,
                          StreamObserver<ClientReply> responseObserver)
    {
        if (debug) {
            System.out.println("Configure[" + toString() + "] " +
                               req.getFilename());
        }
        ClientReply reply =
            ClientReply.newBuilder().setResult("Configure").build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void connect(ConnectRequest req,
                        StreamObserver<ClientReply> responseObserver)
    {
        if (debug) {
            System.out.println("Connect[" + toString() + "] *" +
                               req.getConnectionsCount());
        }
        ClientReply reply =
            ClientReply.newBuilder().setResult("Connect").build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void startRun(StartRunRequest req,
                         StreamObserver<ClientReply> responseObserver)
    {
        if (debug) {
            System.out.println("StartRun[" + toString() + "] " +
                               req.getRunNum());
        }
        ClientReply reply =
            ClientReply.newBuilder().setResult("StartRun").build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void stopRun(EmptyRequest req,
                        StreamObserver<ClientReply> responseObserver)
    {
        if (debug) {
            System.out.println("StopRun[" + toString() + "]");
        }
        ClientReply reply =
            ClientReply.newBuilder().setResult("StopRun").build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
