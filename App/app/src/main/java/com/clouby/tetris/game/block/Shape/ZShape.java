package com.clouby.tetris.game.block.Shape;

import android.graphics.Color;

//"Z" shape
public class ZShape extends Shape {
    public ZShape() {
        super(new int[]{0x2640, 0xc600, 0x2640, 0xc600});
        setColor(Color.RED);
    }
}
