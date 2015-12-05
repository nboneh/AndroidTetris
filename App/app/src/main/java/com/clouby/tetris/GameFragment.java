package com.clouby.tetris;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clouby.tetris.game.GameView;

/**
 * Created by Shirong on 11/19/2015.
 */
public class GameFragment extends Fragment implements View.OnClickListener {

    private GameView gameView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);
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

        gameView = (GameView) v.findViewById(R.id.game_surfaceView);

        gameView.setGameOverHanlder(new Handler(getActivity().getMainLooper()) {
                                        public void handleMessage(Message msg) {
                                            //Checks if fragment is still attached
                                            if (isAdded()) {
                                                gameOver(msg.arg1);
                                            }

                                        }
                                    }
        );

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pause_button:
                pause();
                pauseAlertDialog();
                break;
            case R.id.left_button:
                ((GameView) (getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().moveLeft();
                break;
            case R.id.right_button:
                ((GameView) (getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().moveRight();
                break;
            case R.id.down_button:
                ((GameView) (getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().moveDown();
                break;
            case R.id.transform_button:
                ((GameView) (getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().turnNext();
                break;

        }
    }

    public void gameOver(int score) {
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

    public void pauseAlertDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void pause() {
        gameView.pauseGame();
    }

    @Override
    public void onPause() {
        gameView.resumeGame();
    }
}
