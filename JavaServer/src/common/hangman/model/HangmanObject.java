/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.hangman.model;

import java.io.Serializable;

/**
 *
 * @author davidsoendoro
 */
public class HangmanObject implements Serializable {

    // Request
    private final String command;
    
    // Response
    private int score;
    private int attempt;
    private String word;

    public HangmanObject(String command) {
        this.command = command;
    }
    
    public HangmanObject(String command, int score, int attempt, String word) {
        this.command = command;
        this.score = score;
        this.attempt = attempt;
        this.word = word;
    }

    public String getCommand() {
        return command;
    }
    
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
    
}
