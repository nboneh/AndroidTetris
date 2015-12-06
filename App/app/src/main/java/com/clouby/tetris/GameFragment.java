package com.clouby.tetris;

<<<<<<< HEAD
import android.app.AlertDialog;
=======
import android.app.Activity;
import android.content.DialogInterface;
>>>>>>> 64225fffc45b01ddbc328546af7bc40e87b61333
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clouby.tetris.game.BoardView;
import com.clouby.tetris.game.GameThread;

import java.util.HashMap;

/**
 * Created by Shirong on 11/19/2015.
 */
public class GameFragment extends Fragment implements View.OnClickListener {

<<<<<<< HEAD
    private GameThread gameThread;
    private BoardView boardView;


=======
    private GameView gameView;
    private Sounds sounds ;
>>>>>>> 64225fffc45b01ddbc328546af7bc40e87b61333

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

        return v;
    }

    @Override
    public  void onClick(View v){
        switch(v.getId()){
            case R.id.pause_button:
                pause();
                break;
            case R.id.left_button:
<<<<<<< HEAD
                playSound(3, 1);
                boardView.moveLeft();
                break;
            case R.id.right_button:
                boardView.moveRight();
                playSound(3, 1);
=======
                sounds.playSound(3);
                ((GameView)(getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().moveLeft();
                break;
            case R.id.right_button:
                ((GameView)(getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().moveRight();
             sounds.playSound( 3);
>>>>>>> 64225fffc45b01ddbc328546af7bc40e87b61333
                break;
            case R.id.down_button:
                boardView.moveDown();
                break;
            case R.id.transform_button:
<<<<<<< HEAD
                boardView.rotateNext();

                playSound(3, 1);
=======
                ((GameView) (getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().turnNext();
                ((GameView)(getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().turnNext();
              sounds.playSound(3);
>>>>>>> 64225fffc45b01ddbc328546af7bc40e87b61333
                break;

        }
    }

    private void gameOver(int score) {
        //Popping up to main menu screen to
        getActivity().getSupportFragmentManager().popBackStack();

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

<<<<<<< HEAD

    private void pause() {
        playSound(3, 1);
        musicPlayer.stop();
        gameThread.setRunning(false);
=======
    public void pauseAlertDialog() {
        Context context = getActivity();
        sounds.playSound(3);
        sounds.stopMusic();
>>>>>>> 64225fffc45b01ddbc328546af7bc40e87b61333

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Pause");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Resume",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        gameThread.setRunning(true);
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Main Menu", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getActivity().getSupportFragmentManager().popBackStack();
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    @Override
    public void onPause() {
        super.onPause();
        pause();
    }



}
