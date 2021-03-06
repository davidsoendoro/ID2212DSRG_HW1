/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaserver.hangman;

import java.util.HashMap;
import javaserver.helper.DictionaryHelper;

/**
 *
 * @author davidsoendoro
 */
public class HangmanGame {
    private int score;
    private int attempt;
    private String word;
    private String answeredWord;

    public HangmanGame() {
        this.score = 0;
        this.newWord();
    }

    public int getScore() {
        return score;
    }

    public int getAttempt() {
        return attempt;
    }

    public String getWord() {
        return word;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public void setWord(String word) {
        this.word = word;
    }
    
    public String getStringRepresentation() {
        return "score=" + score + 
                "&attempt=" + attempt +
                "&word=" + answeredWord;
    }

    public void processParameter(String inputParameters) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        String[] parametersSplit = inputParameters.split("&");
        
        for(String parameter : parametersSplit) {
            String[] keyValue = parameter.split("=");
            if(keyValue.length >= 2) {
                parameters.put(keyValue[0], keyValue[1]);                
            }
            else if(keyValue.length >= 1){
                parameters.put(keyValue[0], "");
            }
        }
        
        String param;
        if((param = parameters.get("word")) != null) {
            param = param.toLowerCase();
            if(param.length() > 1) {
                processWord(param);
            }
            else {
                processLetter(param);
            }
        }
    }
    
    private void processLetter(String letter) {
        if(this.word.contains(letter) && !this.answeredWord.contains(letter)) {
            String newAnsweredWord = "";
            for(int i = 0; i < this.word.length(); i++) {
                if(this.word.charAt(i) == letter.charAt(0)) {
                    newAnsweredWord += letter;
                    if(newAnsweredWord.equals(this.word)) {
                        this.score += 1;
                    }
                }
                else {
                    newAnsweredWord += this.answeredWord.charAt(i);
                }
            }
            this.answeredWord = newAnsweredWord;
            if(this.answeredWord.equals(this.word)) {
                this.score += 1;
            }
        }
        else {
            processWrongAnswer();
        }
    }
    
    private void processWord(String word) {
        if(word.equals(this.word)) {
            this.answeredWord = word;
            this.score += 1;
        }
        else {
            processWrongAnswer();
        }
    }
    
    public void newWord() {
        this.attempt = 10;
        this.word = randomizeWord();
        this.answeredWord = "";
        for(int i=0; i<this.word.length(); i++) {
            answeredWord += "-";
        }
    }
    
    private String randomizeWord() {
        int randomValue = (int) (Math.random() * 
                (double) DictionaryHelper.dictionary.size());
        
        return DictionaryHelper.dictionary.get(randomValue);
//        return "test";
    }

    private void processWrongAnswer() {
        this.attempt -= 1;  
        if(this.attempt == 0) {
            if(this.score > 0) this.score -= 1;
            this.answeredWord = this.word;
        }
    }
}
