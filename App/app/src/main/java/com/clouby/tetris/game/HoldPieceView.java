package com.clouby.tetris.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.clouby.tetris.game.block.Shape;

/**
 * Created by nboneh on 12/6/2015.
 */
public class HoldPieceView extends SurfaceView  implements SurfaceHolder.Callback,BoardViewListener {
    private Panel panel;
    boolean allowExchange;
    Shape holdShape;

    public HoldPieceView(Context context) {
        super(context);
        init();

    }

    public HoldPieceView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    private void init(){
        //add the callback to the surfaceHolder to intercept events
        getHolder().addCallback(this);
        //make gamePanel focusable so it can handle events
        setFocusable(true);

        this.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = this.getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

        panel = new Panel(Shape.BOXES_ROWS, Shape.BOXES_COLS);
    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public Shape exchange(Shape s){
        if(s == null)
            return null;
        try {
            Shape temp = null;
            if(holdShape != null)
               temp = holdShape.clone();
            holdShape = s.clone();
            panel.clear();
            panel.addTetrisShapeObject(holdShape);
            allowExchange = false;
            return temp;
        } catch(CloneNotSupportedException e){
            return null;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        panel.draw(canvas, true);
    }

    @Override
    public void playEnd(BoardPanel boardPanel) {
        allowExchange = true;
    }
    
    public boolean isAllowExchange(){
        return allowExchange;
    }

    public void gameOver(int score) {

    }
}
