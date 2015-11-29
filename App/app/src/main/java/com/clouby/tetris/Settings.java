package com.clouby.tetris;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private static final String MUSIC_VOLUME_KEY = "musicVolume";
    private static final String SOUND_VOLUME_KEY = "soundVolume";
    private static final String THEME_KEY = "theme";
    private static final String ALIAS_KEY = "alias";

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
        theme = sharedPref.getInt(THEME_KEY, 0);
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

 /*   public static void loadOnlineHighScores(final Handler h){
        Runnable r = new Runnable(){
            @Override
            public void run() {
                try{
                    URL website = new URL(WEB_URL);

                    URLConnection connection = website.openConnection();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream()));

                    StringBuilder response = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null)
                        response.append(inputLine);

                    in.close();
                    Message msg = new Message();
                    msg.obj = response.toString();
                    h.sendMessage(msg);
                } catch (MalformedURLException e) {
                } catch (IOException e) {
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    public static boolean madeHighScore(int score){
        for(int i = 0; i < size; i++){
            if(localHighscores[i].getScore() < score){
                return true;
            }
        }
        return false;
    }

    public static void addScore(int score, String name){
        Settings.name = name;
        for(int i = 0; i < size; i++){
            if(localHighscores[i].getScore() < score){
                for(int j = size-1; j > i; j--){
                    localHighscores[j] = localHighscores[j-1];
                }
                HighScoreContainer scoree = new HighScoreContainer();
                scoree.setScore(score);
                scoree.setTimestamp(System.currentTimeMillis());
                scoree.setSent(false);
                scoree.setName(name);
                localHighscores[i] = scoree;
                break;
            }
        }
    }

    public static void updateOnlineScores(){
        Runnable r = new Runnable(){
            @Override
            public void run() {
                for(int i = 0; i < size; i ++){
                    HighScoreContainer scoreCont= localHighscores[i];
                    if(!scoreCont.isSent()){
                        String urlParameters = "&date=" + scoreCont.getTimestamp()
                                + "&score=" + scoreCont.getScore()
                                + "&name=" + scoreCont.getName()
                                + "&pass=" + PASSWORD;
                        try{
                            URL url = new URL(WEB_URL);
                            HttpURLConnection conn=  (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);
                            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

                            writer.write(urlParameters);
                            writer.flush();

                            conn.getResponseCode();
                            writer.close();

                            scoreCont.setSent(true);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }*/
}