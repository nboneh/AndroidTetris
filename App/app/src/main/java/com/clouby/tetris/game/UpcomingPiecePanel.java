package com.clouby.tetris.game;

import android.graphics.Color;

import com.clouby.tetris.game.block.Shape;
import com.clouby.tetris.game.block.ShapeFactory;
import com.clouby.tetris.game.block.TetrisBox;

import java.util.LinkedList;
import java.util.Random;

public class UpcomingPiecePanel extends Panel {
    private final static int WHITE_COLOR = Color.parseColor("#FFFFFF");
    //borderWidth/BoxWidth
    private float borderWidthRatio = 0.01f;
    //borderHeight/BoxHeight
    private float borderHeightRatio = 0.015f;

    public static int UPCOMING_PIECES_SIZE = 4;

    LinkedList<Shape> upComingPieces = new LinkedList<Shape>();

    Random rand;

    public UpcomingPiecePanel(int rows, int cols) {
        super(rows, cols);
        rand = new Random();
        initUpComingPieces();
        addTetrisObjects();
    }

    public void initUpComingPieces() {
        for (int i = 0; i < UPCOMING_PIECES_SIZE; ++i) {
            upComingPieces.add(ShapeFactory.createShape(BoardPanel.shapeType[rand.nextInt(BoardPanel.shapeType.length)]));
        }
    }

    //retrieve and remove the head of the queue, add one to the back
    public Shape getNextPiece() {
        upComingPieces.add(ShapeFactory.createShape(BoardPanel.shapeType[rand.nextInt(BoardPanel.shapeType.length)]));
        Shape shape = upComingPieces.poll();
        addTetrisObjects();
        return shape;
    }

    //set all boxes status based on the upComingPieces
    public void addTetrisObjects() {
        for (int k = 0; k < UPCOMING_PIECES_SIZE; ++k) {
            Shape currShape = upComingPieces.get(k);
            int style = currShape.getStyle();
            int key = 0x8000;

            //4 small blocks
            for (int i = 0; i < Shape.BOXES_ROWS; ++i) {
                for (int j = 0; j < Shape.BOXES_COLS; ++j) {
                    TetrisBox box = getBox(k * UPCOMING_PIECES_SIZE + i, j);

                    //set all box inactive whatever
                    box.setInActive();

                    boolean isPartOfShape = ((style & key) != 0);
                    if (isPartOfShape)
                        box.setActive(currShape.getColor());

                    key >>= 1;

                }
            }
        }

    }

}
