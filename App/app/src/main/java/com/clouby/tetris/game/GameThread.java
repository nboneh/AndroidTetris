package com.clouby.tetris.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    private SurfaceHolder boardSurfaceHolder;
    private BoardView board;
    private volatile boolean running;
    private volatile boolean kill;
    private long prevTime;


    public GameThread( BoardView boardView){
        super();
        this.boardSurfaceHolder= boardView.getHolder();
        this.board = boardView;

        prevTime = 0;
        kill = false;
        running = true;

    }


    @Override
    public void run(){
        while(!kill) {
            while (running) {
                long t = System.currentTimeMillis() - prevTime;
                if(prevTime == 0)
                    t = prevTime;
                Canvas canvas = null;

                //try locking the canvas for pixel editing
                try {
                    canvas = this.boardSurfaceHolder.lockCanvas();
                    synchronized (boardSurfaceHolder) {
                        this.board.update(t);
                        prevTime = System.currentTimeMillis();
                        this.board.draw(canvas);
                    }
                } catch (Exception e) {
                } finally {
                    if (canvas != null) {
                        try {
                            boardSurfaceHolder.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
            prevTime = 0;
        }
        return;

    }


    public void setRunning(boolean b){
        running = b;
    }

    public void kill(){
        kill = true;
    }

}
