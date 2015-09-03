/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ProtocolStrings;
import utils.ServerLogger;

/**
 *
 * @author Nikolaj
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private ClientHandlerListener server;
    private PrintStream out;

    public ClientHandler(Socket socket, ClientHandlerListener server) {
        this.socket = socket;
        this.server = server;
    }
           
    @Override
    public void run() {
        try {
            out = new PrintStream(socket.getOutputStream());
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            
            String input;
            while (!(input = br.readLine()).equals(ProtocolStrings.STOP)) {                
                server.sendMessage(input, this);               
            }
            
            server.removeHandler(this);
        } catch (IOException ex) {
            ServerLogger.severe(String.format("Exception in ClientHandler : %s", ex));
            server.removeHandler(this);
        }
    }

    public void sendMessage(String msg){
        out.println(msg);
    }
}
