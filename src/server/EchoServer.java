/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import static java.lang.System.out;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ServerLogger;
import utils.Utils;

/**
 *
 * @author Nikolaj
 */
public class EchoServer implements ClientHandlerListener {

    private final List<ClientHandler> clients = new ArrayList<>();
    private static final Properties properties = Utils.initProperties("server.properties");

    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ServerLogger.close();
        }));
        new EchoServer().run();
    }

    public void run() throws IOException {
        ExecutorService ex = Executors.newCachedThreadPool();
        
        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");

        try (ServerSocket SSocket = new ServerSocket()) {
            SSocket.bind(new InetSocketAddress(ip, port));
            ServerLogger.info("Server Started");
            
            Thread clientAccepterThread = initAccepterThread(SSocket, ex);
            clientAccepterThread.start();
                      
            takeInput();
            
            ex.shutdownNow();
            clientAccepterThread.interrupt();
        }
    }

    private void takeInput() throws IOException {
        BufferedReader consoleBr = new BufferedReader(new InputStreamReader(System.in));
        
        String input = null;
        while (!(input = consoleBr.readLine()).equals("Exit")) {
            System.out.println("Type Exit to stop the server");
        }
    }

    private Thread initAccepterThread(ServerSocket SSocket, ExecutorService ex) {
        return new Thread(() -> {
            do {
                try {
                    Socket currentSocket = SSocket.accept();
                    ClientHandler client = new ClientHandler(currentSocket, this);
                    clients.add(client);
                    ServerLogger.info(String.format("Client connected from %s | Client count: %s", currentSocket.getInetAddress(), clients.size()));
                    ex.execute(client);
                } catch (IOException exce) {
                    ServerLogger.info("Exception happened in client thread : " + exce);
                    break;
                }
            } while (true);
        });
    }

    @Override
    public void removeHandler(ClientHandler handler) {
        ServerLogger.info(String.format("Client %d: Disconnected", clients.indexOf(handler) + 1));
        clients.remove(clients.indexOf(handler));
    }

    @Override
    public void sendMessage(String msg, ClientHandler sender) {
        ServerLogger.info(String.format("Client %d sent: %s", clients.indexOf(sender) + 1, msg));
        for (ClientHandler handler : clients) {
            handler.sendMessage(String.format("Client %d: %s", clients.indexOf(sender) + 1, msg));
        }
    }

}
