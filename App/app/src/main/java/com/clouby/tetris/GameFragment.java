package com.clouby.tetris;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clouby.tetris.game.GameView;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Shirong on 11/19/2015.
 */
public class GameFragment extends Fragment implements View.OnClickListener{

    SoundPool sp;
    HashMap<Integer,Integer> spMap;
    private MediaPlayer musicPlayer;
   // Sounds sounds;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // Context context =getActivity();
       // sounds.initSoundPool(context);
       // sounds.loadMusic(context);
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

        return v;
    }


    @Override
    public  void onClick(View v){
        Context context =getActivity();
        switch(v.getId()){
            case R.id.pause_button:
                //TODO change later
               // sounds.playSound(context, 3, 1);		//play music
                playSound(3, 1);
                musicPlayer.stop();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.left_button:
               // sounds.playSound(context, 3, 1);		//play music
                playSound(3, 1);
                ((GameView)(getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().moveLeft();
                break;
            case R.id.right_button:
                ((GameView)(getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().moveRight();
               // sounds.playSound(context, 3, 1);
                playSound(3, 1);
                break;
            case R.id.down_button:
                ((GameView)(getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().moveDown();
                break;
            case R.id.transform_button:
                ((GameView)(getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().turnNext();
               // sounds.playSound(context,3,1);
                playSound(3, 1);
                break;

        }
    }
    private void gameOver() {
        //Popping up to main menu screen to
        getActivity().getSupportFragmentManager().popBackStack();

        //Transition to game over fragment
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();

        Random rand = new Random();
        Fragment fragment = new GameOverFragment();
        Bundle args = new Bundle();
        args.putInt("score", rand.nextInt(100));
        fragment.setArguments(args);
        fragmentTransaction
                .replace(R.id.fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
       // spMap.put(4, sp.load(context,R.raw.music, 1));


    }
    public void playSound(int sound,int number){
        Context context =getActivity();
        AudioManager am=(AudioManager)context.getSystemService(context.AUDIO_SERVICE);
     // am.setStreamVolume(AudioManager.STREAM_MUSIC,(int) settings.getSoundVolume(),60);
        float audioMaxVolume=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float audioCurrentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumeRatio=audioCurrentVolume/audioMaxVolume;

        // float volumeRatio= settings.getSoundVolume()/audioMaxVolume;
        //float volumeRatio= Settings.getSoundVolume();
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
        //audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,);
        musicPlayer = MediaPlayer.create(context, R.raw.music);
        musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        musicPlayer.setLooping(true);
       // musicPlayer.setVolume();
        musicPlayer.start();
    }

}
