package com.clouby.tetris.game;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.clouby.tetris.R;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    //score per line
    public final static int PER_LINE_SCORE = 100;
    //score needed to upgrade level
    public final static int PER_LEVEL_SCORE = PER_LINE_SCORE * 20;
    //maximum level
    public final static int MAX_LEVEL = 10;
    //default level
    public final static int DEFAULT_LEVEL = 5;

    //pixels of the GameView, check the background picture pixel
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1600;

    private MainThread thread;
    private Background bg;


    public GameView(Context context, AttributeSet attr) {
        super(context, attr);

        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        //make gamePanel focusable so it can handle events
        setFocusable(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        while(retry){
            try {
                thread.setRunning(false);
                thread.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            retry = false;
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.galaxy_6));

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return super.onTouchEvent(event);
    }

    public void update(){
        bg.update();
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            final float scaleFactorX = ((float) getWidth()) / WIDTH;
            final float scaleFactorY = ((float) getHeight()) / HEIGHT;

            //savedCanvas
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            //not sure whether need to call super.draw()
            super.draw(canvas);
            bg.draw(canvas);

            //return to the saved state, prevent it from keeping scaling
            canvas.restoreToCount(savedState);
        }
    }

}
