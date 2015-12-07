package com.clouby.tetris.game;

/**
 * Created by nboneh on 12/6/2015.
 */
public interface BoardPanelListener {
    public void playEnd(BoardPanel boardPanel);
    public void gameOver(int score);
}
