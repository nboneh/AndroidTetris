package com.clouby.tetris;

import android.content.DialogInterface;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

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

import java.util.HashMap;

/**
 * Created by Shirong on 11/19/2015.
 */
public class GameFragment extends Fragment implements View.OnClickListener {

    private GameView gameView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    SoundPool sp;
    HashMap<Integer,Integer> spMap;
    private MediaPlayer musicPlayer;
   // Sounds sounds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initSoundPool();
        loadMusic();

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
    public  void onClick(View v){
        Context context =getActivity();
        switch(v.getId()){
            case R.id.pause_button:
                //TODO change later
                pause();
                pauseAlertDialog();
                break;
            case R.id.left_button:
                playSound(3, 1);
                ((GameView)(getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().moveLeft();
                break;
            case R.id.right_button:
                ((GameView)(getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().moveRight();
                playSound(3, 1);
                break;
            case R.id.down_button:
                ((GameView) (getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().moveDown();
                break;
            case R.id.transform_button:
                ((GameView) (getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().turnNext();
                ((GameView)(getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().turnNext();

                playSound(3, 1);
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

    public void pauseAlertDialog() {
        playSound(3, 1);
        musicPlayer.stop();

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


    public void initSoundPool(){
        sp=new SoundPool(
                5,
                AudioManager.STREAM_MUSIC,
                0
        );
        spMap=new HashMap<Integer,Integer>();
        Context context =getActivity();
        spMap.put(1, sp.load(context, R.raw.drop_free, 1));
        spMap.put(2, sp.load(context, R.raw.tetris_free, 1));
        spMap.put(3, sp.load(context, R.raw.key_free, 1));

    }
    public void playSound(int sound,int number){
        Context context =getActivity();
        AudioManager am=(AudioManager)context.getSystemService(context.AUDIO_SERVICE);
        float audioMaxVolume=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float audioCurrentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumeRatio=audioCurrentVolume/audioMaxVolume;

        sp.play(
                spMap.get(sound),
                volumeRatio,
                volumeRatio,
                1,
                number,
                1
        );
    }
    public void loadMusic(){
        Context context = getActivity();
        AudioManager audioManager=(AudioManager)context.getSystemService(context.AUDIO_SERVICE);

        musicPlayer = MediaPlayer.create(context, R.raw.music);
        musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        musicPlayer.setLooping(true);

        musicPlayer.start();
    }


}
