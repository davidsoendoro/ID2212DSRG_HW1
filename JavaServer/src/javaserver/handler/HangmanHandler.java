/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaserver.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaserver.hangman.HangmanGame;

/**
 *
 * @author davidsoendoro
 */
public class HangmanHandler extends GenericHandler {

    private final Socket socket;
    private HangmanGame hangmanGame;
    private boolean isEnded;
    private PrintWriter printWriter;
    private final int DISCONNECT_TIME = 3;
    private int disconnectCounter;
    
    public HangmanHandler(Socket socket) {
        this.socket = socket;
        this.initialize();
    }
    
    public HangmanHandler(Socket socket, String threadName) {
        this.socket = socket;
        this.initialize();
        this.setName(threadName);
    }
    
    private final void initialize() {
        this.hangmanGame = null;
        this.isEnded = false;
        this.disconnectCounter = 0;        
    }
    
    @Override
    public void run() {
        try {
            int i = 0;
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
            while(!isEnded) {
                String str, completeString;
                completeString = "";
                String parametersString;
                parametersString = "";

                writeOutput("Waiting for packet - " + ++i);
                int in = rd.read();
                if(in == -1) {
                    while(in == -1 && disconnectCounter < DISCONNECT_TIME) {
                        in = rd.read();
                        writeOutput("Try to wait... " + disconnectCounter);
                        Thread.sleep(1000);
                        disconnectCounter += 1;
                    }
                }
                
                completeString += (char) in;
                
                if(disconnectCounter >= DISCONNECT_TIME) {
                    isEnded = true;
                    writeOutput("USER IS DISCONNECTED!");
                    break;
                }
                disconnectCounter = 0;
                
                // GET HEADER
                while((str = rd.readLine()) != null && !str.trim().equals("")) {
                    completeString += str + "\n";
                }
                     
                if(completeString.contains("updateGame")) {
                    // GET BODY
                    while((str = rd.readLine()) != null && !str.trim().equals("")) {
                        completeString += str + "\n";
                        parametersString += str;
                    }                
                }
                
                writeOutput(completeString);
                writeOutput("===========");

                executeAPI(completeString, parametersString);
            }
            
            writeOutput("Thread is closing...");
            
            // close the socket and wait for another connection
            if(socket != null) {
                socket.close();               
                writeOutput("Thread is closed!");
            }
        } catch (IOException ex) {
            Logger.getLogger(HangmanHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(HangmanHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(printWriter != null) {
                printWriter.close();
            }
        }
    }

    private void executeAPI(String str, String parametersString) {
        if(str.contains("startGame")) {
            startGame();
        }
        else if(str.contains("endGame")) {
            endGame();
        }
        else if(str.contains("updateGame")) {
            updateGame(parametersString);
        }
    }

    /**
     * Handler that will be called if server receives "startGame" message
     * If it is the start of the game it will initialize hangmanGame
     * This function will send back score, attempt, and new word to client
     */
    private void startGame() {
        printWriter = null;
            
        try {
            // communicate with a client via clientSocket
            printWriter = new PrintWriter(socket.getOutputStream());
            
            // Construct header
            String header = "200 OK";
            
            // Construct new HangmanGame
            if(this.hangmanGame == null) {
                this.hangmanGame = new HangmanGame();                
            }
            else {
                this.hangmanGame.newWord();
            }
            
            // Construct body
            String body = this.hangmanGame.getStringRepresentation();
            
            printWriter.println(header); // send GET request
            printWriter.println();
            printWriter.println(body);
            printWriter.println();
            
            printWriter.flush();
        } catch (IOException ex) {
            Logger.getLogger(HangmanHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handler that will be called if server receives "endGame" message
     * Server will reply with 200 OK and empty body
     * Server will close the socket and stop the thread
     */
    private void endGame() {        
        printWriter = null;
            
        try {
            // communicate with a client via clientSocket
            printWriter = new PrintWriter(socket.getOutputStream());
            
            // Construct header
            String header = "200 OK";
            
            // Construct body
            String body = "";
            
            printWriter.println(header); // send GET request
            printWriter.println();
            printWriter.println(body);
            printWriter.println();
            
            printWriter.flush();           
            
            isEnded = true;
        } catch (IOException ex) {
            Logger.getLogger(HangmanHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handler that will be called if server receives "updateGame" message
     * Server will reply with 200 OK and the condition after update is applied
     * Server will calculate the game; if the word is answered: +1 score, if
     * attempt less or equal than 0: -1 score
     * It should handle if the input is a word or a letter
     * @param parametersString is the newly guessed letter or word
     */
    private void updateGame(String parametersString) {
        printWriter = null;
            
        try {
            // communicate with a client via clientSocket
            printWriter = new PrintWriter(socket.getOutputStream());
            
            // Construct header
            String header = "200 OK";
            
            // Update HangmanGame
            this.hangmanGame.processParameter(parametersString);
            
            // Construct body
            String body = this.hangmanGame.getStringRepresentation();
            
            printWriter.println(header); // send GET request
            printWriter.println();
            printWriter.println(body);
            printWriter.println();
            
            printWriter.flush();
        } catch (IOException ex) {
            Logger.getLogger(HangmanHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
