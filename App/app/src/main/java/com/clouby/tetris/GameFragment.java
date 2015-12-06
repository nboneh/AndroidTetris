package com.clouby.tetris;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clouby.tetris.game.BoardView;
import com.clouby.tetris.game.GameThread;

/**
 * Created by Shirong on 11/19/2015.
 */
public class GameFragment extends Fragment implements View.OnClickListener, View.OnKeyListener {

    private GameThread gameThread;
    private BoardView boardView;
    private Sounds sounds ;
    private  AlertDialog pauseDialog;

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
        (v.findViewById(R.id.down_button)).setOnClickListener(this);
        //transform
        (v.findViewById(R.id.transform_button)).setOnClickListener(this);
        boardView = ((BoardView) v.findViewById(R.id.game_surfaceView));

        gameThread = new GameThread((BoardView) v.findViewById(R.id.game_surfaceView));

        gameThread.setGameOverHandler(new Handler(getActivity().getMainLooper()) {
                                          public void handleMessage(Message msg) {
                                              //Checks if fragment is still attached
                                              if (isAdded()) {
                                                  gameOver(msg.arg1);
                                              }

                                          }
                                      }
        );

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
            case R.id.down_button:
                boardView.hardDrop();
                break;
            case R.id.transform_button:
                boardView.rotateNext();
                break;

        }
    }

    private void gameOver(int score) {
        //Popping up to main menu screen to
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
        fragmentTransaction.commit();
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

}
