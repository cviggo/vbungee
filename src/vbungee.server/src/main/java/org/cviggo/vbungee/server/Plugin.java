package org.cviggo.vbungee.server;

import java.util.logging.Logger;

/**
 * Created by viggo on 19-07-2014.
 */



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


    private Logger logger;

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, new PluginListener(this));
        logger = getLogger();
        logInfo("Enabled");
    }

    public void logInfo(String message){
        logger.info(message);
    }

    public void logWarn(String message){
        logger.warning(message);
    }

    public void logSevere(String message){
        logger.severe(message);
    }


}
