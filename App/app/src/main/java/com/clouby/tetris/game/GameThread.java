package com.clouby.tetris.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    private SurfaceHolder boardSurfaceHolder;
    private SurfaceHolder upcomingPieceSurfaceHolder;
    private SurfaceHolder holdPieceSurfaceHolder;

    private BoardView board;
    private UpcomingPieceView upcomingPieceBoard;
    private HoldPieceView holdPieceView;
    private volatile boolean running;
    private volatile boolean kill;
    private long prevTime;


    public GameThread(BoardView boardView, UpcomingPieceView upcomingPieceBoard, HoldPieceView holdPieceView) {
        super();
        this.boardSurfaceHolder = boardView.getHolder();
        this.upcomingPieceSurfaceHolder = upcomingPieceBoard.getHolder();
        this.holdPieceSurfaceHolder = holdPieceView.getHolder();

        this.board = boardView;
        this.upcomingPieceBoard = upcomingPieceBoard;
        this.holdPieceView = holdPieceView;

        prevTime = 0;
        kill = false;
        running = true;
    }


    @Override
    public void run() {
        while (!kill) {
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

                Canvas upcomingCanvas = null;
                //try locking the canvas for pixel editing
                try {
                    upcomingCanvas = this.upcomingPieceSurfaceHolder.lockCanvas();
                    synchronized (upcomingPieceSurfaceHolder) {
                        this.upcomingPieceBoard.draw(upcomingCanvas);
                    }

                } catch (Exception e) {
                } finally {
                    if (upcomingCanvas != null) {
                        try {
                            upcomingPieceSurfaceHolder.unlockCanvasAndPost(upcomingCanvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                Canvas holdpieceCanvas = null;
                //try locking the canvas for pixel editing
                try {
                    holdpieceCanvas = this.holdPieceSurfaceHolder.lockCanvas();
                    synchronized (holdPieceSurfaceHolder) {
                        this.holdPieceView.draw(holdpieceCanvas);
                    }

                } catch (Exception e) {
                } finally {
                    if (holdpieceCanvas != null) {
                        try {
                            holdPieceSurfaceHolder.unlockCanvasAndPost(holdpieceCanvas);
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


    public void setRunning(boolean b) {
        running = b;
    }

    public void kill() {
        kill = true;
    }

}
