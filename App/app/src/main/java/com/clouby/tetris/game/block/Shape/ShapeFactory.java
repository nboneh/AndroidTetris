package com.clouby.tetris.game.block.Shape;

public class ShapeFactory {
    public Shape createShape(String type) {
        Shape shape = null;

        if (type.equals("I")) {
            shape = new IShape();
        } else if (type.equals("J")) {
            shape = new JShape();
        } else if (type.equals("L")) {
            shape = new LShape();
        } else if (type.equals("O")) {
            shape = new OShape();
        } else if (type.equals("S")) {
            shape = new SShape();
        } else if (type.equals("T")) {
            shape = new TShape();
        } else if (type.equals("Z")) {
            shape = new ZShape();
        }

        return shape;
    }
}
