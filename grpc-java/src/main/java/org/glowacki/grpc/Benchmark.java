package org.glowacki.grpc;

public class Benchmark
{
    private static final int port = 50051;

    private static final String formatNano(long nanoseconds)
    {
        final double seconds = ((double) nanoseconds) / 1E9;
        return String.format("%6.3fs", seconds);
    }

    public static void main(String[] args)
        throws Exception
    {
        int reps = 1000;
        if (args.length >= 1) {
            reps = Integer.parseInt(args[0]);
        }

        final long initStart = System.nanoTime();

        Client client = new Client("localhost", port);

        final long initTime = System.nanoTime() - initStart;

        System.out.println("Run " + reps + " reps");

        final long runStart = System.nanoTime();

        for (int i = 0; i < reps; i++) {
            client.singleDAQRun(false);
        }

        final long runTime = System.nanoTime() - runStart;

        final long stopStart = System.nanoTime();
        client.shutdown();
        final long stopTime = System.nanoTime() - stopStart;

        System.out.print("Init:  " + formatNano(initTime));
        System.out.print("      Run:   " + formatNano(runTime));
        System.out.print("      Stop:  " + formatNano(stopTime));
        System.out.println("      Total: " +
                           formatNano(initTime + runTime + stopTime));
    }
}
