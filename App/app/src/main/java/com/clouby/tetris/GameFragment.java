package com.clouby.tetris;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.clouby.tetris.game.BoardPanel;
import com.clouby.tetris.game.BoardPanelListener;
import com.clouby.tetris.game.BoardView;
import com.clouby.tetris.game.GameThread;

/**
 * Created by Shirong on 11/19/2015.
 */
public class GameFragment extends Fragment implements View.OnClickListener, View.OnKeyListener, BoardPanelListener {

    private GameThread gameThread;
    private BoardView boardView;
    private Sounds sounds ;
    private  AlertDialog pauseDialog;
    private EditText scoreText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sounds = Sounds.GetInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_game, container, false);
        sounds.playMusic();
        //pause button
        (v.findViewById(R.id.pause_button)).setOnClickListener(this);
        //left button
        (v.findViewById(R.id.left_button)).setOnClickListener(this);
        //right button
        (v.findViewById(R.id.right_button)).setOnClickListener(this);
        //down button
        (v.findViewById(R.id.soft_drop_button)).setOnClickListener(this);
        (v.findViewById(R.id.hard_drop_button)).setOnClickListener(this);
        //transform
        (v.findViewById(R.id.transform_button)).setOnClickListener(this);

        scoreText = (EditText)v.findViewById(R.id.score_text);
        boardView = ((BoardView) v.findViewById(R.id.game_surfaceView));
        boardView.addListener(this);

        gameThread = new GameThread(boardView);

        gameThread.start();

        v.setFocusableInTouchMode(true);
        v.requestFocus();

        v.setOnKeyListener(this);

        return v;
    }

    @Override
    public  void onClick(View v){
        switch(v.getId()){
            case R.id.pause_button:
                pause();
                break;
            case R.id.left_button:
                boardView.moveLeft();
                break;
            case R.id.right_button:
                boardView.moveRight();
                break;
            case R.id.hard_drop_button:
                boardView.hardDrop();
                break;
            case R.id.soft_drop_button:
                boardView.softDrop();
                break;
            case R.id.transform_button:
                boardView.rotateNext();
                break;

        }
    }

    private void pause() {
        gameThread.setRunning(false);
        sounds.pauseMusic();
        if(pauseDialog == null) {
            pauseDialog = new AlertDialog.Builder(getActivity()).create();
            pauseDialog.setTitle("Pause");
            pauseDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Resume",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            gameThread.setRunning(true);
                            sounds.playMusic();
                            dialog.dismiss();
                        }
                    });
            pauseDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Main Menu", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    dialog.dismiss();
                }
            });
        }
        if(isAdded())
             pauseDialog.show();

    }

    @Override
    public void onPause() {
        super.onPause();
        pause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        gameThread.kill();
        sounds.stopMusic();
    }

    @Override
    public boolean onKey( View v, int keyCode, KeyEvent event )
    {
        if( keyCode == KeyEvent.KEYCODE_BACK )
        {
            pause();
            return true;
        }
        return false;
    }

    public void playEnd(final BoardPanel boardPanel){

        Handler mainHandler = new Handler(getActivity().getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {   //Popping up to main menu screen to
                StringBuilder sb = new StringBuilder();
                sb.append("Score:\n" +boardPanel.getScore());
                sb.append("\n Level:" + boardPanel.getLevel());
                sb.append("\n Goal: " + boardPanel.lineTillNextLevel());
                scoreText.setText(sb.toString());
            }
        };
        mainHandler.post(myRunnable);
    }
    public void gameOver(final int score){

        Handler mainHandler = new Handler(getActivity().getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {   //Popping up to main menu screen to
                getActivity().getSupportFragmentManager().popBackStack();

                sounds.playSound(Sounds.GAME_OVER);
                //Transition to game over fragment
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();

                Fragment fragment = new GameOverFragment();
                Bundle args = new Bundle();
                args.putInt("score", score);
                fragment.setArguments(args);
                fragmentTransaction
                        .replace(R.id.fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();} // This is your code
        };
        mainHandler.post(myRunnable);

    }


}
