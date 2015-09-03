/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.Socket;
import server.ClientHandler;

/**
 *
 * @author Nikolaj
 */
public interface ClientHandlerListener {
    void removeHandler(ClientHandler handler);
    
    void sendMessage(String msg , ClientHandler sender);
}
