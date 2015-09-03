/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import static java.lang.System.out;
import java.util.Observable;
import java.util.Observer;
import shared.ProtocolStrings;

/**
 *
 * @author Nikolaj
 */
public class Connection   {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintStream out;

    public BufferedReader getIn() {
        return in;
    }
    
    public Connection(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintStream(socket.getOutputStream());
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }
 
    @Override
    public String toString(){
        return String.format("Connected to %s | at port: %s", socket.getInetAddress(),socket.getPort());
    }
}
