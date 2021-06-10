package org.glowacki.xmlrpc;

import java.net.InetAddress;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

public class Server
{
    private static final int port = 50051;

    public static void main(String[] args)
        throws Exception
    {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);

        WebServer webServer = new WebServer(port);

        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        phm.addHandler("xmlrpc", DAQClient.class);

        System.out.println("== Supported methods");
        for (String method : phm.getListMethods()) {
            System.out.println("  " + method);
        }

        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
        xmlRpcServer.setHandlerMapping(phm);

        XmlRpcServerConfigImpl serverConfig =
            (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);

        webServer.start();
    }
}
