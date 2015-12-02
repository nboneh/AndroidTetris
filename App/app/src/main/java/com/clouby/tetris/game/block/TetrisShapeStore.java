package com.clouby.tetris.game.block;

import android.graphics.Color;

import com.clouby.tetris.game.block.Shape.Shape;
import com.clouby.tetris.game.block.Shape.ShapeFactory;

public class TetrisShapeStore {
    //http://www.color-hex.com/
    public final static int backgroundColor = Color.parseColor("#66cdaa");

    //position of shape
    private int x = 0, y = 0;
    //current style
    private int style;

    ShapeFactory shapeFactory = new ShapeFactory();
    private TetrisBox[][] boxes = new TetrisBox[Shape.BOXES_ROWS][Shape.BOXES_COLS];

    Shape shape;

    public TetrisShapeStore(String type){
        shape = shapeFactory.createShape(type);

        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[i].length; j++) {
                boxes[i][j] = new TetrisBox(backgroundColor);
            }
        }

    }

    public void setShape( String type) {
        this.shape = shapeFactory.createShape(type);
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int rotation) {
        this.style = shape.getSTYLES()[rotation%Shape.SHAPE_STATUS_NUMBER];
    }

    public TetrisBox[][] getBoxes() {
        return boxes;
    }

    //set Color of shape
    public void setColor(int style){
        int key = 0x8000;
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[i].length; j++) {
                boolean isColor = ((style & key) != 0);
                boxes[i][j].setColor(isColor?shape.getColor():backgroundColor );
                key >>= 1;
            }
        }

    }

    public void setColor(){
        setColor(this.style);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    //pause move
    public void pauseMove() {

        shape.setPausing(true);
    }

    //resume move
    public void resumeMove() {
        shape.setPausing(false);
    }

    //stop move
    public void stopMove() {
        shape.setMoving(false);
    }

    //erase, not show until next canvas draw
    private void erase(GamePanel gamePanel) {
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[i].length; j++) {
                if (boxes[i][j].getColor()!= backgroundColor) {
                    TetrisBox box = gamePanel.getBox(i + y, j + x);
                    if (box == null) continue;
                    box.setColor(backgroundColor);
                }
            }
        }
    }

    //display current block
    private void display(GamePanel gamePanel) {
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[i].length; j++) {
                if (boxes[i][j].getColor()!= backgroundColor) {
                    TetrisBox box = gamePanel.getBox(y + i, x + j);
                    if (box == null) continue;
                    box.setColor(backgroundColor);
                }
            }
        }
    }

    //can it move to new position
    private boolean isMoveAble(GamePanel gamePanel, int newRow, int newCol) {
        erase(gamePanel);
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[i].length; j++) {
                if (boxes[i][j].getColor()!=backgroundColor) {
                    TetrisBox box = gamePanel.getBox(newRow + i, newCol + j);
                    //if current position can not be box, or it is occupied
                    if (box == null || (box.getColor()!=backgroundColor)) {
                        display(gamePanel);
                        return false;
                    }
                }
            }
        }
        display(gamePanel);
        return true;
    }

    //move to new position
    private synchronized boolean moveTo(GamePanel gamePanel, int newRow, int newCol) {
        //can not move at new position or stopped
        if (!isMoveAble(gamePanel, newRow, newCol) || !shape.isMoving()) return false;
        erase(gamePanel);
        //display at new position
        y = newRow;
        x = newCol;

        display(gamePanel);

        return true;
    }

    //can we turn it to other style
    private boolean isTurnAble(GamePanel gamePanel, int newStyle) {
        int key = 0x8000;
        erase(gamePanel);
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[i].length; j++) {
                //check new 4*4 grid
                if ((newStyle & key) != 0) {
                    //check whether it is in the game plate
                    TetrisBox box = gamePanel.getBox(y + i, x + j);
                    if (box == null || box.getColor()!=backgroundColor) {
                        display(gamePanel);
                        return false;
                    }
                }
                key >>= 1;
            }
        }
        display(gamePanel);
        return true;
    }

    //change style
    private boolean turnTo(GamePanel gamePanel, int newStyle) {
        if (!isTurnAble(gamePanel, newStyle) || !shape.isMoving()) return false;

        erase(gamePanel);
        int key = 0x8000;
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[i].length; j++) {
                boolean isColor = ((newStyle & key) != 0);
                boxes[i][j].setColor(shape.getColor());
                key >>= 1;
            }
        }
        style = newStyle;

        display(gamePanel);

        return true;
    }

    public void moveLeft(GamePanel gamePanel) {
        moveTo(gamePanel, y, x - 1);
    }

    public void moveRight(GamePanel gamePanel) {
        moveTo(gamePanel, y, x + 1);
    }

    public void moveDown(GamePanel gamePanel) {
        moveTo(gamePanel, y + 1, x);
    }

    public void turnNext(GamePanel gamePanel) {
        for (int i = 0; i < Shape.SHAPE_STATUS_NUMBER; i++) {
            if (shape.getSTYLES()[i] == style){
                int newStyle = shape.getSTYLES()[(i+1)% Shape.SHAPE_STATUS_NUMBER];
                turnTo(gamePanel, newStyle);
                return;
            }

        }
    }



}
