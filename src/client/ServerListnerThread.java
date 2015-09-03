/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Observable;

/**
 *
 * @author Nikolaj
 */
public class ServerListnerThread extends Observable implements Runnable{
    BufferedReader reader;

    public ServerListnerThread(BufferedReader reader) {
        this.reader = reader;
    }
    
    @Override
    public void run() {
        while(true){
            try { 
                String line = reader.readLine();
                setChanged();
                notifyObservers(line);
                Thread.sleep(20);
            } catch (IOException | InterruptedException ex) {
                break;
            }
        }
    }

    
    
}
