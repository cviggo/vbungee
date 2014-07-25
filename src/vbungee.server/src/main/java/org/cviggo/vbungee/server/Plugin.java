package org.cviggo.vbungee.server;

import org.simpleframework.http.Query;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.logging.Logger;

// The JSON-RPC 2.0 Base classes that define the
// JSON-RPC 2.0 protocol messages
// The JSON-RPC 2.0 server framework package


public class Plugin extends net.md_5.bungee.api.plugin.Plugin {



    /*

    ManagementFactory.getRuntimeMXBean().getName()


    * Runtime rt = Runtime.getRuntime();
  if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1)
     rt.exec("taskkill " +....);
   else
     rt.exec("kill -9 " +....);
    *
    *
    * */

    public class MyContainer implements Container {

        @Override
        public void handle(Request request, Response response) {
            try {
                PrintStream body = response.getPrintStream();
                long time = System.currentTimeMillis();

                response.setValue("Content-Type", "text/html");
                response.setValue("Server", "VBungee");
                response.setDate("Date", time);
                response.setDate("Last-Modified", time);




                final Query query = request.getQuery();
                final int id = query.containsKey("id") ? query.getInteger("id") : -1;


                body.println("OK: " + id);
                body.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private Logger logger;

    Container container;
    Server server;
    Connection connection;
    SocketAddress address;


    private void initServer() {

        try {
            container = new MyContainer();
            server = new ContainerServer(container);
            connection = new SocketConnection(server);
            address = new InetSocketAddress(8080);

            connection.connect(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadSettings(){

    }

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, new PluginListener(this));
        logger = getLogger();

        initServer();
        // https://jersey.java.net/documentation/latest/getting-started.html


        logInfo("Enabled");
    }

    @Override
    public void onDisable() {

    }

    public void logInfo(String message) {
        logger.info(message);
    }

    public void logWarn(String message) {
        logger.warning(message);
    }

    public void logSevere(String message) {
        logger.severe(message);
    }


}

