/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port = 8080;
        
        dictionaryHelper = new DictionaryHelper();
        
        try {
            ServerSocket serverSocket;
            serverSocket = new ServerSocket(port);
            while(true) {
                try {
                    // wait for a client connection request
                    Socket clientSocket;
                    clientSocket = serverSocket.accept(); 
                    
                    HangmanHandler handler = new HangmanHandler(clientSocket);
                    handler.setPriority(handler.getPriority() + 1);
                    handler.start();
                } catch (SocketException ex) {
                    Logger.getLogger(JavaServer.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(JavaServer.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }
    
}
