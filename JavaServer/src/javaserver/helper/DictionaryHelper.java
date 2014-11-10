/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaserver.helper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author davidsoendoro
 */
public class DictionaryHelper {

    public static ArrayList<String> dictionary;

    public DictionaryHelper() {
        dictionary = new ArrayList<>();
        
        FileInputStream fs= null;
        try {
            fs = new FileInputStream("dictionary.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            for(int i = 0; i < 25143; ++i) {
                String line = br.readLine();
                dictionary.add(line.toLowerCase());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DictionaryHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DictionaryHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(fs != null) {
                    fs.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(DictionaryHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
