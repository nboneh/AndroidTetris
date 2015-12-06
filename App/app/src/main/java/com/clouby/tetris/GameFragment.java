package com.clouby.tetris;

import android.app.AlertDialog;
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

    private GameThread gameThread;
    private BoardView boardView;



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
        Context context =getActivity();
        switch(v.getId()){
            case R.id.pause_button:
                pause();
                break;
            case R.id.left_button:
                playSound(3, 1);
                boardView.moveLeft();
                break;
            case R.id.right_button:
                boardView.moveRight();
                playSound(3, 1);
                break;
            case R.id.down_button:
                boardView.moveDown();
                break;
            case R.id.transform_button:
                boardView.rotateNext();

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


    private void pause() {
        playSound(3, 1);
        musicPlayer.stop();
        gameThread.setRunning(false);

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
