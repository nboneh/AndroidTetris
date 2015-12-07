package com.clouby.tetris.game;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.clouby.tetris.R;
import com.clouby.tetris.Settings;
import com.clouby.tetris.Sounds;
import com.clouby.tetris.game.block.Shape;

import java.util.ArrayList;
import java.util.List;


public class BoardView extends SurfaceView implements SurfaceHolder.Callback{
    //background image
    public int backgroundImageID;
    public final BitmapFactory.Options imageOptions = new BitmapFactory.Options();

    private Background bg;
    private BoardPanel boardPanel;

    List<BoardViewListener> listeners;

    private static BoardView instance;

    public BoardView(Context context) {
        super(context);
        init(context);

    }

    public void addListener(BoardViewListener listener){
        if(listeners == null) {
            listeners = new ArrayList<>();
            boardPanel.setListeners(listeners);
        }
        listeners.add(listener);
    }


    public BoardView(Context context, AttributeSet attr) {
        super(context, attr);
       init(context);

    }

    private void init(Context context){
        //add the callback to the surfaceHolder to intercept events
        getHolder().addCallback(this);
  //make gamePanel focusable so it can handle events
        setFocusable(true);

        //set image info
        backgroundImageID= R.drawable.galaxy_6;
        if(Settings.getInstance(context).getTheme() == Settings.DARK_THEME)
            backgroundImageID= R.drawable.galaxy_2;
        
        imageOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), backgroundImageID, imageOptions);

        bg = new Background(BitmapFactory.decodeResource(getResources(), backgroundImageID));
        boardPanel = new BoardPanel(20, 10, Sounds.GetInstance(context));
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return super.onTouchEvent(event);
    }

    //return false if game over
    public void update(long t){
        boardPanel.update(t);
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

            boardPanel.draw(canvas, false);

        }

    }

    public int getScore(){return boardPanel.getScore();}

    public boolean didLose(){ return  false;}

    public void hardDrop(){boardPanel.hardDrop();}
    public void softDrop(){boardPanel.softDrop();}
    public void moveLeft(){boardPanel.moveLeft();}
    public void moveRight(){boardPanel.moveRight();}
    public void rotateNext(){boardPanel.turnToNext();}

    public void setActiveShape(Shape s){boardPanel.makeActiveShape(s);}

    public Shape getActiveShape(){ return  boardPanel.getActiveShape();}

}
