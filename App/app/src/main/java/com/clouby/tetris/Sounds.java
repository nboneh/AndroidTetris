package com.clouby.tetris;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;


import java.util.HashMap;
import com.clouby.tetris.R;

import static android.app.PendingIntent.getActivity;


/**
 * Created by Raymond on 2015/12/4.
 */
public class Sounds {
    SoundPool sp;
    HashMap<Integer,Integer> spMap;
    public MediaPlayer musicPlayer;
    Settings settings;
    public void loadMusic(Context context){
      //  Context context = getActivity();
        AudioManager audioManager=(AudioManager)context.getSystemService(context.AUDIO_SERVICE);
       // audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,(int) settings.getMusicVolume(),0);
        musicPlayer = MediaPlayer.create(context, R.raw.music);
        musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        musicPlayer.setLooping(true);
     //   musicPlayer.setVolume(settings.getMusicVolume(),settings.getMusicVolume());
        musicPlayer.start();
    }
    public void playSound(Context context,int sound,int number){
       // Context context =getActivity();
        AudioManager am=(AudioManager)context.getSystemService(context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC,(int) settings.getSoundVolume(),60);
        float audioMaxVolume=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float audioCurrentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
       // float volumeRatio=audioCurrentVolume/audioMaxVolume;
        // float volumeRatio= settings.getSoundVolume()/audioMaxVolume;
        float volumeRatio= settings.getSoundVolume();
        sp.play(
                spMap.get(sound),
                volumeRatio,
                volumeRatio,
                1,
                number,
                1
        );
    }


    public void initSoundPool(Context context){
        sp=new SoundPool(
                5,
                AudioManager.STREAM_MUSIC,
                0
        );
        spMap=new HashMap<Integer,Integer>();
       // Context context =getActivity();
        spMap.put(1, sp.load(context, R.raw.drop_free, 1));
        spMap.put(2, sp.load(context, R.raw.tetris_free, 1));
        spMap.put(3, sp.load(context, R.raw.key_free, 1));
        // spMap.put(4, sp.load(context,R.raw.music, 1));


    }
}
