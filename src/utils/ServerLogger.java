/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Nikolaj
 */
public class ServerLogger {

    private static final Logger logger;

    static {
        logger = Logger.getLogger("Log");
        FileHandler fh;
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler(System.getProperty("user.dir") + "/ServerLogFile.log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (IOException | SecurityException ex) { //If we ever reach this point im gonna cry
            Logger.getLogger(ServerLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void severe(String msg) {
        logger.severe(msg);
    }

    public static void close() {
        synchronized (logger) {
            for (Handler h : logger.getHandlers()) {
                h.close();
            }
        }
    }
}
