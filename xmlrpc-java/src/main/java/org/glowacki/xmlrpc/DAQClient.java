package org.glowacki.xmlrpc;

public class DAQClient
{
    private String name;
    private boolean debug;

    public DAQClient()
    {
        this("Unknown", false);
    }

    public DAQClient(String name, boolean debug)
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
    {
        if (debug) {
            System.err.println("CommitSubrun[" + toString() + "] num " +
                               subnum + " time " + timeString);
        }

        return "CommitSubrun";
    }

    public String configure(String filename)
    {
        if (debug) {
            System.err.println("Configure[" + toString() + "] " + filename);
        }

        return "Configure";
    }

    public String connect(Object[] connections)
    {
        if (debug) {
            System.err.println("Connect[" + toString() + "] *%d" +
                               connections.length);
        }

        return "Connect";
    }

    public String startRun(int runnum)
    {
        if (debug) {
            System.err.println("StartRun[" + toString() + "] num " +
                               runnum);
        }

        return "StartRun";
    }

    public String stopRun()
    {
        if (debug) {
            System.err.println("StopRun[" + toString() + "]");
        }

        return "StopRun";
    }
}
