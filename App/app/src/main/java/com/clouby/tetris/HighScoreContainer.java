package com.clouby.tetris;

/**
 * Created by nboneh on 11/15/2015.
 */

public class HighScoreContainer {
    private int score = 0;
    private String name = "Clouby";
    private long timestamp = 0;
    private boolean sent = true;

    public int getScore() {
        return score;
    }
    void setScore(int score) {
        this.score = score;
    }
    public String getName() {
        return name;
    }
    void setName(String name) {
        this.name = name;
    }
    long getTimestamp() {
        return timestamp;
    }
    void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    boolean isSent() {
        return sent;
    }
    void setSent(boolean sent) {
        this.sent = sent;
    }
}
