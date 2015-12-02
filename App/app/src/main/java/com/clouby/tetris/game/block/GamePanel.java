package com.clouby.tetris.game.block;

import android.graphics.Canvas;
import android.graphics.Color;

import com.clouby.tetris.game.block.Shape.Shape;

//game panel, contains <#rows>*<#cols> implementation
public class GamePanel {
    //score per line
    public final static int PER_LINE_SCORE = 100;
    //score needed to upgrade level
    public final static int PER_LEVEL_SCORE = PER_LINE_SCORE * 20;
    //maximum level
    public final static int MAX_LEVEL = 10;
    //default level
    public final static int DEFAULT_LEVEL = 5;

    //to smooth the level upgrade
    public final static int LEVEL_FLATNESS_FACTOR = 3;
    //time delay between levels in unit of microseconds
    public final static int TIME_BETWEEN_LEVELS = 50;

    //number of shapes
    public final static int TYPE_OF_SHAPE = 7;
    public final static String[] shapeType = new String[]{"I", "J", "L", "O", "S", "T", "Z"};

    //borderWidth/BoxWidth or borderHeight/BoxHeight
    private float borderWidthRatio = 0.01f;

    private int rows, cols;
    private int score = 0;
    private int scoreForLevelUpdate = 0;

    //all possible TetrisBox on the canvas
    private TetrisBox[][] boxes;
    private int boxWidth, boxHeight;

    private  TetrisShapeStore tetrisShapeStore;

    public GamePanel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        //initialize rows*cols TetrisBox
        boxes = new TetrisBox[rows][cols];
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[i].length; j++) {
                boxes[i][j] = new TetrisBox(TetrisShapeStore.backgroundColor);
            }
        }

        tetrisShapeStore = new TetrisShapeStore("T");
        tetrisShapeStore.setStyle(0);
        tetrisShapeStore.setColor();
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
        scoreForLevelUpdate -= PER_LEVEL_SCORE;
    }

    public TetrisBox getBox(int row, int col) {
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

        score += PER_LINE_SCORE;
        scoreForLevelUpdate += PER_LINE_SCORE;
        update();
    }

    //reset the canvas
    public void reset() {
        score = 0;
        scoreForLevelUpdate = 0;
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[i].length; j++)
                boxes[i][j].setColor(TetrisShapeStore.backgroundColor);
        }

        update();
    }

    public void draw(Canvas canvas){
        fanning(canvas);

        for(int i=0; i< Shape.BOXES_ROWS; ++i){
            for(int j=0; j<Shape.BOXES_COLS; ++j){
                //draw the boxes if and only the color of the box is not the default backgroundColor
                if(tetrisShapeStore.getBoxes()[i][j].getColor()!=TetrisShapeStore.backgroundColor) {
                    canvas.drawRect((tetrisShapeStore.getX() + j + borderWidthRatio) * boxWidth, (tetrisShapeStore.getY() + i + borderWidthRatio) * boxHeight,
                            (tetrisShapeStore.getX() + j + 1 - borderWidthRatio) * boxWidth, (tetrisShapeStore.getY() + i + 1 - borderWidthRatio) * boxHeight,
                            tetrisShapeStore.getBoxes()[i][j].getPaint());
                }
            }
        }
    }

    //dynamic things
    public void update(){
        tetrisShapeStore.moveDown(this);

    }

}
