package com.clouby.tetris.game;

import android.graphics.Color;
import android.util.Log;

import com.clouby.tetris.Sounds;
import com.clouby.tetris.game.block.Shape;
import com.clouby.tetris.game.block.ShapeFactory;
import com.clouby.tetris.game.block.TetrisBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//game panel, contains <#rows>*<#cols> implementation
public class BoardPanel extends Panel {

    private final static int WHITE_COLOR = Color.parseColor("#FFFFFF");
    //maximum level
    private final static int MAX_LEVEL = 10;
    private final static int LINE_CLEAR_TILL_NEXT_LEVEL = 15;

    //number of shapes
    public final static String[] shapeType = new String[]{"I", "J", "L", "O", "S", "T", "Z"};

    private final static long TIME_TILL_PIECE_MOVE_DOWN = 500;
    private final static long CLEAR_TIME_ANIMATION = 100;

    private  long lineClearCounter = 0;
    private  long pieceMoveDownCounter = 0;


    //borderWidth/BoxWidth
    private float borderWidthRatio = 0.01f;
    //borderHeight/BoxHeight
    private float borderHeightRatio = 0.015f;

    private int score = 0;
    private int scoreForLevelUpdate = 0;

    private Shape activeShape;
    Random rand;
    int level = 1;

    private List<Integer> linesToClear;

    private  Sounds sounds;

    public BoardPanel(int rows, int cols,Sounds sounds) {
        super(rows,cols);
        rand = new Random();
        linesToClear = new ArrayList<>();
        this.sounds = sounds;
    }

    public int getScore() {
        return score;
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
                if( tetrisBox != null && isPartOfShape) {
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
                if(i < 0)
                    continue;
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

    public void softDrop(){
        if(moveDown())
              score++;
    }

    public void hardDrop(){
        while(moveDown())
            score+=2;
    }


    public void checkForLines(){
        boolean isClearLine;
        for (int i = 0; i < boxes.length; i++) {
            isClearLine = true;
            for (int j = 0; j < boxes[i].length; j++) {
               TetrisBox box = getBox(i,j);
                if(!box.isActive()) {
                    j = boxes[i].length;
                    isClearLine = false;
                }
            }
            if(isClearLine)
                linesToClear.add(i);
        }

        switch(linesToClear.size()) {
            case 0:
                sounds.playSound(Sounds.PLAY_END);
                break;
            case 1:
                sounds.playSound(Sounds.LINE_CLEAR);
                score += 40 * level;
                break;
            case 2:
                sounds.playSound(Sounds.LINE_CLEAR);
                score += 100 * level;
                break;
            case 3:
                sounds.playSound(Sounds.LINE_CLEAR);
                score += 300 * level;
                break;
            case 4:
                sounds.playSound(Sounds.TETRIS);
                score += 1200 * level;
                break;
        }



        for(int i : linesToClear) {
            for (int j = 0; j < boxes.length; j++) {
                TetrisBox box = getBox(i,j);
                box.setActive(WHITE_COLOR);
            }
        }

    }

    private void removeLinesToClear(){
        for(int line : linesToClear) {
            //Clearing line
            for (int j = 0; j < boxes[line].length; j++) {
                TetrisBox box = getBox(line,j);
                box.setInActive();
            }
            //Shifting line down
           for(int i = line; i >= 0; i--){
                for(int j = 0; j < boxes[i].length; j++){
                    TetrisBox box = getBox(i,j);
                    TetrisBox replaceBox = getBox(i-1, j);
                    if(replaceBox != null && box != null){
                        if(!replaceBox.isActive())
                            box.setInActive();
                        else
                             box.setActive(replaceBox.getColor());
                    }

                }
            }
        }

      linesToClear.clear();
    }

    public void update(long timeInMilliseconds){
        if(!linesToClear.isEmpty()){
            if(lineClearCounter >= CLEAR_TIME_ANIMATION){
                removeLinesToClear();
                lineClearCounter = 0;
            }
            lineClearCounter += timeInMilliseconds;
        } else {
            if(activeShape == null) {
                generateNewPiece();
            }

            if(pieceMoveDownCounter >= TIME_TILL_PIECE_MOVE_DOWN){
                pieceMoveDownCounter -= TIME_TILL_PIECE_MOVE_DOWN;
                if(!moveDown()) {
                    activeShape = null;
                    checkForLines();
                }
                Log.d("Score", score +"");
            }

            pieceMoveDownCounter += timeInMilliseconds;
        }

    }

    private void generateNewPiece(){
       Shape shape = ShapeFactory.createShape("I");
        shape.setY(-2);
        shape.setX(cols/2);
        addTetrisShapeObject(shape);

    }

}
