package com.clouby.tetris.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.clouby.tetris.R;
import com.clouby.tetris.game.block.GamePanel;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    //score per line
    public final static int PER_LINE_SCORE = 100;
    //score needed to upgrade level
    public final static int PER_LEVEL_SCORE = PER_LINE_SCORE * 20;
    //maximum level
    public final static int MAX_LEVEL = 10;
    //default level
    public final static int DEFAULT_LEVEL = 5;

    //background image
    public int backgroundImageID;
    public final BitmapFactory.Options imageOptions = new BitmapFactory.Options();

    private MainThread thread;
    private Background bg;
    private GamePanel gamePanel;


    public GameView(Context context, AttributeSet attr) {
        super(context, attr);

        //add the callback to the surfaceHolder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        //make gamePanel focusable so it can handle events
        setFocusable(true);

        //set image info
        backgroundImageID= R.drawable.galaxy_6;
        imageOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), backgroundImageID, imageOptions);

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
        bg = new Background(BitmapFactory.decodeResource(getResources(), backgroundImageID));
//        gamePanel = new GamePanel(20, 12);
        gamePanel = new GamePanel(20, 10);

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
        gamePanel.update();
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            final float scaleFactorX = ((float) getWidth()) / imageOptions.outWidth;
            final float scaleFactorY = ((float) getHeight()) / imageOptions.outHeight;

            //savedCanvas
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            //not sure whether need to call super.draw()
            super.draw(canvas);
            bg.draw(canvas);
            //return to the saved state, prevent it from keeping scaling
            canvas.restoreToCount(savedState);

            gamePanel.draw(canvas);
        }
    }

}
