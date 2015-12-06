package com.clouby.tetris.game;

import android.util.Log;

import com.clouby.tetris.game.block.Shape;
import com.clouby.tetris.game.block.ShapeFactory;
import com.clouby.tetris.game.block.TetrisBox;

import java.util.Random;

//game panel, contains <#rows>*<#cols> implementation
public class BoardPanel extends Panel {
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
    public final static String[] shapeType = new String[]{"I", "J", "L", "O", "S", "T", "Z"};

    private final static long TIME_TILL_PIECE_MOVE_DOWN = 500;
    private  long pieceMoveDownCounter = 0;


    //borderWidth/BoxWidth
    private float borderWidthRatio = 0.01f;
    //borderHeight/BoxHeight
    private float borderHeightRatio = 0.015f;

    private int score = 0;
    private int scoreForLevelUpdate = 0;

    private Shape activeShape;
    Random rand;

    public BoardPanel(int rows, int cols) {
        super(rows,cols);
        rand = new Random();
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

    public void addTetrisShapeObject(Shape shape){
        super.addTetrisShapeObject(shape);
        this.activeShape = shape;

    }

    private void eraseActiveShape(){
        if(activeShape == null) return;

        int key = 0x8000;

        int x = activeShape.getX();
        int y = activeShape.getY();
        int style = activeShape.getStyle();

        for (int i = y; i < (y+Shape.BOXES_ROWS); i++) {
            for (int j = x; j < (x +Shape.BOXES_COLS); j++) {
                TetrisBox tetrisBox = getBox(i,j);
                boolean isPartOfShape = ((style & key) != 0);
                if(isPartOfShape) {
                    tetrisBox.setInActive();
                }
                key >>= 1;
            }
        }
    }

    private boolean isMovable( int newX, int newY){
        if(activeShape == null) return false;
        return isMovable(newX, newY, activeShape.getStyle());
    }

    private boolean isMovable(int rotation){
        if(activeShape == null) return false;
        return isMovable(activeShape.getX(), activeShape.getY(),activeShape.getStyleForRotation(rotation));
    }

    private boolean isMovable( int newX, int newY, int style){
        if(activeShape == null) return false;
        eraseActiveShape();
        int key = 0x8000;
        for (int i = newY; i < (newY+Shape.BOXES_ROWS); i++) {
            for (int j = newX; j < (newX +Shape.BOXES_COLS); j++) {
                TetrisBox tetrisBox = getBox(i,j);
                boolean isPartOfShape = ((style & key) != 0);
                if(isPartOfShape) {
                    if(tetrisBox == null || tetrisBox.isActive()){
                        addTetrisShapeObject(activeShape);
                        return false;
                    }

                }

                key >>= 1;
            }
        }
        addTetrisShapeObject(activeShape);
        return true;
    }

    //can it move to new position
    public boolean moveTo( int newX, int newY) {
        if(!isMovable(newX, newY)) return false;

        eraseActiveShape();
        activeShape.setX(newX);
        activeShape.setY(newY);
        addTetrisShapeObject(activeShape);

        return true;
    }

    public void turnTo(int rotation){
        if(activeShape == null) return;
        if(!isMovable(rotation))return;
        eraseActiveShape();
        activeShape.setRotation(rotation);
        addTetrisShapeObject(activeShape);
    }

    public void turnToNext(){
        if(activeShape == null) return;

        turnTo(activeShape.getNextRotation());
    }

    public void moveRight(){
        if(activeShape == null) return;
        moveTo(activeShape.getX() + 1, activeShape.getY());
    }

    public void moveLeft(){
        if(activeShape == null) return;
        moveTo(activeShape.getX() - 1, activeShape.getY());
    }

    public boolean moveDown(){
        if(activeShape == null) return false;
       return moveTo(activeShape.getX(), activeShape.getY()+1);
    }

    public void update(long timeInMilliseconds){
        if(activeShape == null) {
            generateNewPiece();
        }
        Log.d("Time", timeInMilliseconds + "");

        if(pieceMoveDownCounter >= TIME_TILL_PIECE_MOVE_DOWN){
            pieceMoveDownCounter -= TIME_TILL_PIECE_MOVE_DOWN;
            if(!moveDown())
                activeShape = null;
        }

        pieceMoveDownCounter += timeInMilliseconds;
    }

    private void generateNewPiece(){
       Shape shape = ShapeFactory.createShape(shapeType[ rand.nextInt(shapeType.length)]);
        addTetrisShapeObject(shape);

    }

}
