package com.clouby.tetris.game;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.clouby.tetris.R;

public class UpcomingPieceView extends SurfaceView implements SurfaceHolder.Callback {
    private UpcomingPiecePanel upcomingPiecePanel;

    public UpcomingPieceView(Context context) {
        super(context);
        init();

    }

    public UpcomingPieceView(Context context, AttributeSet attr) {
        super(context, attr);
        init();

    }

    private void init() {
        //add the callback to the surfaceHolder to intercept events
        getHolder().addCallback(this);
        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        upcomingPiecePanel = new UpcomingPiecePanel(16, 4);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        upcomingPiecePanel.draw(canvas);
    }

}

