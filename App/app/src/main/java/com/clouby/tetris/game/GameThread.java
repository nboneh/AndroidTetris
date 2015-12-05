package com.clouby.tetris.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import com.clouby.tetris.GameFragment;
import com.clouby.tetris.R;

import android.os.Handler;

public class GameThread extends Thread {
    public static Canvas canvas;
    public int FPS = 30;
    public double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;

    private Handler gameOverHandler;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView){
        super();
        this.surfaceHolder= surfaceHolder;
        this.gameView = gameView;

    }

    public void setGameOverHandler(Handler gameOverHandler){
        this.gameOverHandler = gameOverHandler;
    }

    @Override
    public void run(){
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        //1000 frames
        long targetTime= 1000/FPS;
        //20000 frames
//        long targetTime= 20000/FPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            //try locking the canvas for pixel editing
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    //running
                    if(running){
                        boolean lose = !this.gameView.update();
                        if(lose){
                            Message msg = new Message();
                            msg.arg1 = gameView.getScore();
                            gameOverHandler.sendMessage(msg);
                            return;
                        }
                        this.gameView.draw(canvas);
                    }

                }
            } catch (Exception e) {
            }

            finally {
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime= targetTime- timeMillis;

            try{
                this.sleep(waitTime);
            }catch (Exception e){}

            totalTime += System.nanoTime()-startTime;
            frameCount++;
            if(frameCount == FPS ){
                this.averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount =0;
                totalTime =0;
                System.out.println(averageFPS);
            }
        }

    }


    public void setRunning(boolean b){
        running = b;
    }

}
