package com.clouby.tetris.game;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    private SurfaceHolder boardSurfaceHolder;
    private BoardView board;
    private volatile boolean running;
    private volatile boolean kill;
    private long prevTime;

    private Handler gameOverHandler;

    public GameThread( BoardView boardView){
        super();
        this.boardSurfaceHolder= boardView.getHolder();
        this.board = boardView;

        prevTime = System.currentTimeMillis();
        kill = false;
        running = true;

    }

    public void setGameOverHandler(Handler gameOverHandler){
        this.gameOverHandler = gameOverHandler;
    }

    @Override
    public void run(){
        while(!kill) {
            while (running) {
                long t = System.currentTimeMillis() - prevTime;
                Canvas canvas = null;

                //try locking the canvas for pixel editing
                try {
                    canvas = this.boardSurfaceHolder.lockCanvas();
                    synchronized (boardSurfaceHolder) {
                        this.board.update(t);
                        prevTime = System.currentTimeMillis();
                        /*if(lose){
                            Message msg = new Message();
                            msg.arg1 = board.getScore();
                            gameOverHandler.sendMessage(msg);
                            return;
                        }*/
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
