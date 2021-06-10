package org.glowacki.thrift;

import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class DAQClientImpl
    implements DAQClient.Iface
{
    private String name;
    private boolean debug;

    public DAQClientImpl()
    {
        this("Unknown", false);
    }

    public DAQClientImpl(String name, boolean debug)
    {
        if (name == null) {
            throw new Error("Please specify a DAQ client name");
        }

        this.name = name;
        this.debug = debug;
    }

    public String toString()
    {
        return name;
    }

    public String commitSubrun(int subnum, String timeString)
        throws TException
    {
        if (debug) {
            System.err.println("CommitSubrun[" + toString() + "] num " +
                               subnum + " time " + timeString);
        }

        return "CommitSubrun";
    }

    public String configure(String filename)
        throws TException
    {
        if (debug) {
            System.err.println("Configure[" + toString() + "] " + filename);
        }

        return "Configure";
    }

    public String connect(List<Connection> connections)
        throws TException
    {
        if (debug) {
            System.err.println("Connect[" + toString() + "] *" +
                               connections.size());
        }

        return "Connect";
    }

    public String startRun(int runnum)
        throws TException
    {
        if (debug) {
            System.err.println("StartRun[" + toString() + "] num " +
                               runnum);
        }

        return "StartRun";
    }

    public String stopRun()
        throws TException
    {
        if (debug) {
            System.err.println("StopRun[" + toString() + "]");
        }

        return "StopRun";
    }
}
