package com.clouby.tetris;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;


import java.util.HashMap;

import static android.app.PendingIntent.getActivity;


/**
 * Created by Raymond on 2015/12/4.
 */
public  class Sounds {
    private SoundPool sp;
    private HashMap<Integer, Integer> spMap;
    public  MediaPlayer musicPlayer;
    static Settings settings;
    private static Sounds instance = null;
    private Context context;
    private Sounds(Context c) {
        context =c;
        AudioManager audioManager=(AudioManager)c.getSystemService(c.AUDIO_SERVICE);
        musicPlayer = MediaPlayer.create(context, R.raw.music);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
        musicPlayer.setVolume(30,30);
       // audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) settings.getMusicVolume(), 0);
       // musicPlayer.setVolume(settings.getMusicVolume(), settings.getMusicVolume());
    }
    public static Sounds GetInstance(Context context)
    {
        if (instance == null) {
            instance = new Sounds(context);
        }
        return instance;

    }
    public void pauseMusic(){
        musicPlayer.pause();
    }
    public void playMusic(){
        initSoundPool();
        loadMusic();
        musicPlayer.start();
    }
    public void stopMusic(){
        musicPlayer.stop();
    }
    public void loadMusic(){
        musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        musicPlayer.setLooping(true);
        musicPlayer.setVolume(30, 30);
    }
    public void playSound(int Id) {
        AudioManager am = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        //am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) settings.getSoundVolume(), 60);
        float audioMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float audioCurrentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumeRatio = audioCurrentVolume / audioMaxVolume;
        // float volumeRatio= settings.getSoundVolume()/audioMaxVolume;
        // float volumeRatio= settings.getSoundVolume();
        sp.play(
                spMap.get(Id),
                volumeRatio,
                volumeRatio,
                1,
                1,
                1
        );
    }


    private void initSoundPool() {
        sp = new SoundPool(
                5,
                AudioManager.STREAM_MUSIC,
                0
        );
        spMap = new HashMap<Integer, Integer>();
        spMap.put(1, sp.load(context, R.raw.drop_free, 1));
        spMap.put(2, sp.load(context, R.raw.tetris_free, 1));
        spMap.put(3, sp.load(context, R.raw.key_free, 1));


    }


}