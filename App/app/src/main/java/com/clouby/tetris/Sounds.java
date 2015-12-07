package com.clouby.tetris;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;


/**
 * Created by Raymond on 2015/12/4.
 */
public class Sounds {
    private SoundPool sp;
    private HashMap<Integer, Integer> spMap;
    public MediaPlayer musicPlayer;
    static Settings settings;
    AudioManager audioManager;
    private Context context;
    private static Sounds instance = null;
    boolean musicStopped;

    public static final int PLAY_END = 0;
    public static final int GAME_OVER = 1;
    public static final int LINE_CLEAR = 2;
    public static final int TETRIS = 3;

    private Sounds(Context c) {
        settings = Settings.getInstance(c);
        this.context = c;
        musicStopped = true;
        audioManager = (AudioManager) c.getSystemService(c.AUDIO_SERVICE);
        initSoundPool(c);
    }

    public static Sounds GetInstance(Context context) {
        if (instance == null) {
            instance = new Sounds(context);
        }
        return instance;

    }

    public void pauseMusic() {
        musicPlayer.pause();
    }

    public void playMusic() {
        if (musicStopped) {
            musicPlayer = MediaPlayer.create(context, R.raw.music);
            musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            musicPlayer.setLooping(true);
            musicPlayer.setVolume(settings.getMusicVolume()/5, settings.getMusicVolume()/5 );
        }
        musicPlayer.start();
        musicStopped = false;
    }

    public void stopMusic() {
        musicPlayer.stop();
        musicStopped = true;
    }

    synchronized  public void playSound(int Id) {
        float settingsVolume = settings.getSoundVolume();

        sp.play(
                spMap.get(Id),
                settingsVolume,
                settingsVolume,
                1,
                0,
                1
        );
    }


    private void initSoundPool(Context context) {
        sp = new SoundPool(
                5,
                AudioManager.STREAM_MUSIC,
                0
        );
        spMap = new HashMap<Integer, Integer>();
        spMap.put(GAME_OVER, sp.load(context, R.raw.gameover, 1));
        spMap.put(LINE_CLEAR, sp.load(context, R.raw.lineclear, 1));
        spMap.put(TETRIS, sp.load(context, R.raw.tetris, 1));
        spMap.put(PLAY_END, sp.load(context, R.raw.playend, 1));


    }


}