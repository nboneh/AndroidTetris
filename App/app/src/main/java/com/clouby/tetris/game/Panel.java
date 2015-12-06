package com.clouby.tetris.game;

import android.graphics.Canvas;
import android.util.Log;

import com.clouby.tetris.game.block.Shape;
import com.clouby.tetris.game.block.TetrisBox;

/**
 * Created by nboneh on 12/5/2015.
 */
public class Panel {

    //all possible TetrisBox on the canvas
    protected  TetrisBox[][] boxes;
    protected int boxWidth, boxHeight;
    protected  int rows, cols;

    //borderWidth/BoxWidth
    private float borderWidthRatio = 0.01f;
    //borderHeight/BoxHeight
    private float borderHeightRatio = 0.015f;



    public Panel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        //initialize rows*cols TetrisBox
        boxes = new TetrisBox[rows][cols];
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[i].length; j++) {
                boxes[i][j] = TetrisBox.createEmptyTetrisBox();
            }
        }
    }


    public void addTetrisShapeObject(Shape shape){
        int x = shape.getX();
        int y = shape.getY();
        int style = shape.getStyle();
        int key = 0x8000;
        for (int i = y; i < (y+ Shape.BOXES_ROWS); i++) {
            for (int j = x; j < (x+ Shape.BOXES_COLS); j++) {
                TetrisBox box = getBox(i,j);
                boolean isPartOfShape = ((style & key) != 0);
                if(box != null && isPartOfShape)
                     box.setActive(shape.getColor());
                key >>= 1;
            }
        }

    }

    public TetrisBox getBox(int row, int col) {
        if (row < 0 || row > boxes.length - 1
                || col < 0 || col > boxes[0].length - 1)
            return null;
        return (boxes[row][col]);
    }


    public void draw(Canvas canvas){
        float boxWidth = canvas.getWidth() / cols;
        float boxHeight = canvas.getHeight() / rows;

        for(int i=0; i<boxes.length; ++i){
            for(int j=0; j<boxes[i].length; ++j){
                TetrisBox box = boxes[i][j];
                if(box.isActive()) {
                    canvas.drawRect((j + borderWidthRatio) * boxWidth,
                            (i + borderHeightRatio) * boxHeight,
                            (j + 1 - borderWidthRatio) * boxWidth,
                            (i + 1 - borderHeightRatio) * boxHeight,
                            box.getPaint());
                }
            }
        }
    }


    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

}
