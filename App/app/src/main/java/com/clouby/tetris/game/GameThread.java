package com.clouby.tetris.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    public static Canvas canvas;
    public int FPS = 30;
    public double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView){
        super();
        this.surfaceHolder= surfaceHolder;
        this.gameView = gameView;
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

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            //try locking the canvas for pixel editing
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameView.update();
                    this.gameView.draw(canvas);
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
