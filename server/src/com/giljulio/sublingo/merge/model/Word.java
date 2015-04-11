package com.giljulio.sublingo.merge.model;

/**
* Created by Gil on 07/04/15.
*/
public class Word {

    int probability;
    String word;

    public Word(String word, int probability) {
        this.probability = probability;
        this.word = word;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
