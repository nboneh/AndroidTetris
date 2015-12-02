package com.clouby.tetris.game.block.Shape;

import android.graphics.Color;

//"S" shape
public class SShape extends Shape {
    public SShape() {
        super(new int[]{0x4620, 0x6c00, 0x4620, 0x6c00});
        setColor(Color.GREEN);
    }
}
