/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hangmanclient;

import common.hangman.model.HangmanObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
/**
 *
 * @author rohitgoyal
 */
public class ConnectionThread implements Runnable {
    Socket socket;
    String serverIP;
    String serverPort;
    String requestParam;
    Boolean isConnected=false;
    Object caller;
    
    public ConnectionThread(String iP,String port){
        serverIP=iP;
        serverPort=port;
    }
    
    public void setRequestParam(String s){
        requestParam=s;
    }
    
    public void setCaller(Object obj){
        caller=obj;
    }
    
    @Override
    public void run(){
        System.out.println(requestParam);
        String str;
        if(!isConnected){
            try {   
                socket = new Socket(serverIP,Integer.parseInt(serverPort));
                isConnected=true;
                // socket=new Socket("130.229.171.175",8080);
                
//                ObjectOutputStream oos = null;
//                HangmanObject hangmanObject = new HangmanObject("startGameObj");
//
//                oos = new ObjectOutputStream(socket.getOutputStream());
//                oos.writeObject(hangmanObject);
//                System.out.println("Send Object!");
//
//                ObjectInputStream ois = null;
//
//                ois = new ObjectInputStream(socket.getInputStream());
//                Object incomingObject = ois.readObject();
//                if(incomingObject.getClass() == HangmanObject.class) {
//                    HangmanObject response = (HangmanObject) incomingObject;
//                    System.out.println(response.getScore());
//                }
                
                PrintWriter wr = new PrintWriter(socket.getOutputStream());
                wr.println("GET startGame HTTP/1.0");
                wr.println();
                wr.flush();
                BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    
                while ((str = rd.readLine()) != null && !str.trim().equals("")) {
                    System.out.println(str);
                }
                    
                while ((str = rd.readLine()) != null && !str.trim().equals("")) {
                    System.out.println(str);
                    ((StartWindow)this.caller).didReceiveResponse(str);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e);
            }
        }
        else{
            if(requestParam.equals("endGame")){
                try {
                    PrintWriter wr = new PrintWriter(socket.getOutputStream());
                    wr.println("GET endGame HTTP/1.0");
                    wr.println();
                    wr.flush();
                    isConnected=false;
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
            else if(requestParam.equals("startGame")){
                try{
                    PrintWriter wr = new PrintWriter(socket.getOutputStream());
                    wr.println("GET startGame HTTP/1.0");
                    wr.println();
                    wr.flush();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while ((str = rd.readLine()) != null && !str.trim().equals("")) {
                        System.out.println(str);
                    }
                    while ((str = rd.readLine()) != null && !str.trim().equals("")) {
                        System.out.println(str);
                        ((MainWindow)this.caller).didReceiveResponse1(str);
                    }
                } catch (IOException e) {
                    System.err.println(e);
                }            
            }
            else if(requestParam.equals("startGameObj")){
                try{
                    ObjectOutputStream oos = null;
                    HangmanObject hangmanObject = new HangmanObject("startGameObj");
                    
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(hangmanObject);
                    System.out.println("Send Object!");
                    
                    ObjectInputStream ois = null;
                    
                    ois = new ObjectInputStream(socket.getInputStream());
                    Object incomingObject = ois.readObject();
                    if(incomingObject.getClass() == HangmanObject.class) {
                        HangmanObject response = (HangmanObject) incomingObject;
                        System.out.println(response.getScore());
                    }
                    
//                    BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    while ((str = rd.readLine()) != null && !str.trim().equals("")) {
//                        System.out.println(str);
//                    }
//                    while ((str = rd.readLine()) != null && !str.trim().equals("")) {
//                        System.out.println(str);
//                        ((MainWindow)this.caller).didReceiveResponse1(str);
//                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println(e);
                }            
            }
            else {
                try{
                    PrintWriter wr = new PrintWriter(socket.getOutputStream());
                    wr.println("POST updateGame HTTP/1.0");
                    wr.println();
                    wr.println("word=" + requestParam);
                    wr.println();
                    wr.flush();
                    
                    BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while ((str = rd.readLine()) != null && !str.trim().equals("")) {
                        System.out.println(str);

                    }
                    while ((str = rd.readLine()) != null && !str.trim().equals("")) {
                        System.out.println(str);
                        ((MainWindow)this.caller).didReceiveResponse1(str);
                    }
                }
                catch(IOException e){
                    System.err.println(e);
                }
            }
        }
    }
}
