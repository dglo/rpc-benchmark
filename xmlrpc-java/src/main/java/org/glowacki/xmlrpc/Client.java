package org.glowacki.xmlrpc;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class Client
{
    private XmlRpcClient client;

    public Client(String host, int port)
    {
        URL url;
        try {
            url= new URL("http", host, port, "");
        } catch (MalformedURLException mux) {
            mux.printStackTrace();
            System.exit(1);
            return;
        }

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(url);

        client = new XmlRpcClient();
        client.setConfig(config);
    }

    public void singleDAQRun(boolean verbose)
    {
        Object[] params;
        Object result;

        params = new Object[] { "run_config.xml", };
        try {
            result = client.execute("xmlrpc.configure", params);
            if (verbose) System.out.println("configure: " + result);
        } catch (XmlRpcException xrx) {
            System.err.print("Method \"xmlrpc.configure\" failed:");
            xrx.printStackTrace();
        }

        params = new Object[] {
            new Object[] {"i", "component", 0, "localhost", 12345},
        };
        try {
            result = client.execute("xmlrpc.connect", params);
            if (verbose) System.out.println("connect: " + result);
        } catch (XmlRpcException xrx) {
            System.err.print("Method \"xmlrpc.connect\" failed:");
            xrx.printStackTrace();
        }

        params = new Object[] { 123456, };
        try {
            result = client.execute("xmlrpc.startRun", params);
            if (verbose) System.out.println("start_run: " + result);
        } catch (XmlRpcException xrx) {
            System.err.print("Method \"xmlrpc.startRun\" failed:");
            xrx.printStackTrace();
        }

        params = new Object[0];
        try {
            result = client.execute("xmlrpc.stopRun", params);
            if (verbose) System.out.println("stop_run: " + result);
        } catch (XmlRpcException xrx) {
            System.err.print("Method \"xmlrpc.stopRun\" failed:");
            xrx.printStackTrace();
        }
    }

    public void shutdown()
    {
        // do nothing
    }

    public static final void main(String[] args)
    {
        final String host = "127.0.0.1";
        final int port = 50051;

        Client client = new Client("localhost", port);
        try {
            client.singleDAQRun(true);
        } finally {
            client.shutdown();
        }
    }
}
