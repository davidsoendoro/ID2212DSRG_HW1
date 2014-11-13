/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaserver.handler.HangmanHandler;
import javaserver.helper.DictionaryHelper;

/**
 *
 * @author davidsoendoro
 */
public class JavaServer {

    public static DictionaryHelper dictionaryHelper;
    private static boolean isListening = true;
    private static ArrayList<Socket> clientSockets;
    private static ServerSocket serverSocket;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port = 8080;
        int threadNameCounter = 0;
        clientSockets = new ArrayList<>();
        
        dictionaryHelper = new DictionaryHelper();
        InputHandler inputHandler = new InputHandler();
        inputHandler.start();
        
        try {
            serverSocket = new ServerSocket(port);
            while(isListening) {
                try {
                    // wait for a client connection request
                    Socket clientSocket;
                    clientSocket = serverSocket.accept();
                    clientSockets.add(clientSocket);
                    
                    HangmanHandler handler = new HangmanHandler(clientSocket, 
                            "Thread-" + ++threadNameCounter);
                    handler.setPriority(handler.getPriority() + 1);
                    handler.start();
                } catch (SocketException ex) {
//                    Logger.getLogger(JavaServer.class.getName()).log(
//                            Level.SEVERE, null, ex);
                    System.out.println("Thread is closed!");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(JavaServer.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        
        System.out.println("Thank you for running JavaServer! See you soon "
                + "in the future");
    }
    
    private static class InputHandler extends Thread {

        @Override
        public void run() {
            Scanner in = new Scanner(System.in);
            String input;
            
            while(isListening) {
                System.out.print("> ");
                input = in.nextLine();
                if(input.equals("quit")) {
                    try {
                        isListening = false;
                        serverSocket.close();
                    } catch (IOException ex) {
                        Logger.getLogger(JavaServer.class.getName()).log(
                                Level.SEVERE, null, ex);
                    }
                }
                else {
                    System.out.println("Command not realized - type 'help' for "
                            + "list of commands");
                }
            }
        }

    }

}
