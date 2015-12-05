package com.clouby.tetris;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private static final String MUSIC_VOLUME_KEY = "musicVolume";
    private static final String SOUND_VOLUME_KEY = "soundVolume";
    private static final String THEME_KEY = "theme";
    private static final String ALIAS_KEY = "alias";

    public static final int LIGHT_THEME = 0;
    public static final int DARK_THEME = 1;

    private float musicVolume;
    private float soundVolume;
    private int theme;
    private String alias;

    private static Settings instance = null;

    SharedPreferences sharedPref;

    private Settings(Context context) {
        sharedPref = ((Activity) context).getPreferences(Context.MODE_PRIVATE);
        load();
    }

    public static Settings getInstance(Context context) {
        if (instance == null) {
            instance = new Settings(context);
        }
        return instance;
    }


    public void load() {
        musicVolume = sharedPref.getFloat(MUSIC_VOLUME_KEY, 1);
        soundVolume = sharedPref.getFloat(SOUND_VOLUME_KEY, 1);
        theme = sharedPref.getInt(THEME_KEY, LIGHT_THEME);
        alias = sharedPref.getString(ALIAS_KEY, "Tetris");
    }

    public void save() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(MUSIC_VOLUME_KEY, musicVolume);
        editor.putFloat(SOUND_VOLUME_KEY, soundVolume);
        editor.putInt(THEME_KEY, theme);
        editor.putString(ALIAS_KEY, alias);
        editor.commit();
    }


    public void setSoundVolume(float soundVolume) {
        this.soundVolume = soundVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}