/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaserver.handler;

/**
 *
 * @author davidsoendoro
 */
public class GenericHandler extends Thread {
    
    private final boolean isDebug = true;
    
    protected void writeOutput(String output) {
        if(isDebug) {
            System.out.println(output);
        }
    }
    
}
