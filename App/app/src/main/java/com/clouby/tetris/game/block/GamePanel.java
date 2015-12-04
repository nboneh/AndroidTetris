package com.clouby.tetris.game.block;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.clouby.tetris.game.block.Shape.ShapeFactory;

import java.util.ArrayList;
import java.util.Random;

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

    //borderWidth/BoxWidth
    private float borderWidthRatio = 0.01f;
    //borderHeight/BoxHeight
    private float borderHeightRatio = 0.015f;

    private int rows, cols;
    private int score = 0;
    private int scoreForLevelUpdate = 0;

    //all possible TetrisBox on the canvas
    private TetrisBox[][] boxes;
    private int boxWidth, boxHeight;

    ShapeFactory shapeFactory = new ShapeFactory();;

    Random rand = new Random();

    private ArrayList<TetrisShapeObject> tetrisShapeObjectList = new ArrayList<TetrisShapeObject>();

    public GamePanel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        //initialize rows*cols TetrisBox
        boxes = new TetrisBox[rows][cols];
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[i].length; j++) {
                boxes[i][j] = new TetrisBox(TetrisShapeObject.backgroundColor);
            }
        }

        TetrisShapeObject tetrisShapeObject  = new TetrisShapeObject(this);
        tetrisShapeObject.setShape(shapeFactory, shapeType[rand.nextInt(7)]);
        tetrisShapeObject.setStyle(rand.nextInt(4));
        tetrisShapeObject.setColor();

        tetrisShapeObjectList.add(tetrisShapeObject);
    }

    public void addTetrisShapeObject(int shape, int rotation){
            if(tetrisShapeObjectList.size()>=100){
            //get rid of the first 50 elements because they are useless
            tetrisShapeObjectList.subList(0, 50).clear();
        }

        TetrisShapeObject tetrisShapeObject = new TetrisShapeObject(this);
        tetrisShapeObject.setShape(shapeFactory, shapeType[shape]);
        tetrisShapeObject.setStyle(rotation);
        tetrisShapeObject.setColor();
        tetrisShapeObjectList.add(tetrisShapeObject);

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

    /**
     * examine the canvas to see whether there is line full or not, if there is, remove, increase score, possibly, upgrade level
     */
    public void checkFullLine() {
        for (int i = 0; i < this.rows; i++) {
            int row = -1;
            boolean fullLineColorBox = true;
            for (int j = 0; j < this.cols; j++) {
                //check ith row, jth col
                if (boxes[i][j].getColor()== TetrisShapeObject.backgroundColor) {
                    //not backgroundColor
                    fullLineColorBox = false;
                    break;
                    //next Row
                }
            }
            if (fullLineColorBox) {
                row = i--;
                this.removeLine(row);
                //full line, remove it
            }
        }
    }

    //reset the canvas
    public void reset() {
        score = 0;
        scoreForLevelUpdate = 0;
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[i].length; j++)
                boxes[i][j].setColor(TetrisShapeObject.backgroundColor);
        }

        update();
    }

    public void setBoxColor(int i, int j, int color){
        boxes[i][j].setColor(color);
    }

    public void setBoxPaint(int i, int j, Paint paint){
        boxes[i][j].setPaint(paint);
    }

    public void draw(Canvas canvas){
        fanning(canvas);

        checkFullLine();

        for(int i=0; i<boxes.length; ++i){
            for(int j=0; j<boxes[i].length; ++j){
                //draw the boxes if and only if the color of the box is not the default backgroundColor
                if(boxes[i][j].getColor()!=TetrisShapeObject.backgroundColor){
                    canvas.drawRect((j + borderWidthRatio) * boxWidth,
                            (i + borderHeightRatio) * boxHeight,
                            (j + 1 - borderWidthRatio) * boxWidth,
                            (i + 1 - borderHeightRatio) * boxHeight,
                            boxes[i][j].getPaint());
                }

            }
        }
    }


    //dynamic things
    public void update() {
        if (!tetrisShapeObjectList.get(tetrisShapeObjectList.size()-1).moveDown(this) ){
            addTetrisShapeObject(rand.nextInt(7), rand.nextInt(4));
        }
    }

    public  TetrisShapeObject getLastBlockObject(){
        if(tetrisShapeObjectList.size()>=1){
            return tetrisShapeObjectList.get(tetrisShapeObjectList.size()-1);
        }
        else{
            return null;
        }
    }

    public void moveLeft() {
        tetrisShapeObjectList.get(tetrisShapeObjectList.size()-1).moveLeft(this);
    }

    public void moveRight() {
        tetrisShapeObjectList.get(tetrisShapeObjectList.size()-1).moveRight(this);
    }

    public void moveDown() {
        tetrisShapeObjectList.get(tetrisShapeObjectList.size()-1).moveDown(this);
    }

    public void turnNext(){
        tetrisShapeObjectList.get(tetrisShapeObjectList.size()-1).turnNext(this);
    }

    /**
     * check whether the top row is occupied to tell whether the game is over
     * @return boolean, true-game over, false-game not over
     */
    private boolean isGameOver() {
        for (int i = 0; i < this.getCols(); i++) {
            if (boxes[0][i].getColor()!=TetrisShapeObject.backgroundColor) return true;
        }
        return false;
    }

}
