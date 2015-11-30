package com.clouby.tetris.game.block;

import android.graphics.Canvas;
import android.graphics.Color;

import com.clouby.tetris.game.GameView;

//game canvas, contains <#rows>*<#cols> implementation
public class GameCanvas{

    private int backColor = Color.BLACK, frontColor = Color.DKGRAY;

    private int rows, cols;
    private int score = 0;
    private int scoreForLevelUpdate = 0;

    private TetrisBox[][] boxes;
    private int boxWidth, boxHeight;

    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

    public int getFrontColor() {
        return frontColor;
    }

    public void setFrontColor(int frontColor) {
        this.frontColor = frontColor;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScoreForLevelUpdate() {
        return scoreForLevelUpdate;
    }

    public void setScoreForLevelUpdate(int scoreForLevelUpdate) {
        this.scoreForLevelUpdate = scoreForLevelUpdate;
    }

    //after upgrade level, reset the score to zero
    public void resetScoreForLevelUpdate() {
        scoreForLevelUpdate -= GameView.PER_LEVEL_SCORE;
    }

    public TetrisBox getBoxes(int row, int col) {
        if (row < 0 || row > boxes.length - 1
                || col < 0 || col > boxes[0].length - 1)
            return null;
        return (boxes[row][col]);
    }

    //change the size of box dynamically based on the size of window
    public void fanning(Canvas canvas) {
        boxWidth = canvas.getWidth() / cols;
        boxHeight = canvas.getHeight() / rows;
    }

    public synchronized void removeLine(int row) {
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < cols; j++) {
                boxes[i][j] = (TetrisBox) ((TetrisBox) boxes[i - 1][j].clone());
            }
        }

        score += GameView.PER_LINE_SCORE;
        scoreForLevelUpdate += GameView.PER_LINE_SCORE;
//        repaint();
    }

    //reset the canvas
    public void reset() {
        score = 0;
        scoreForLevelUpdate = 0;
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[i].length; j++)
                boxes[i][j].setColor(false);
        }

//        repaint();
    }

//    public void paintComponent(Canvas canvas) {
//
//        canvas.setColor(frontColor);
//        for (int i = 0; i < boxes.length; i++) {
//            for (int j = 0; j < boxes[i].length; j++) {
//                //use background color or foreground color to draw each box
//                canvas.setColor(boxes[i][j].isColor() ? frontColor : backColor);
//                canvas.fill3DRect(j * boxWidth, i * boxHeight,
//                        boxWidth, boxHeight, true);
//            }
//        }
//    }

}
