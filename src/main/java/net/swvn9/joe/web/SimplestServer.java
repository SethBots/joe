package net.swvn9.joe.web;

import org.eclipse.jetty.server.Server;

/**
 * The simplest possible Jetty server.
 */
@SuppressWarnings("ALL")
public class SimplestServer
{
    public static void main( String[] args ) throws Exception
    {
        Server server = new Server(8080);
        server.start();
        server.dumpStdErr();
        server.join();
    }
}
