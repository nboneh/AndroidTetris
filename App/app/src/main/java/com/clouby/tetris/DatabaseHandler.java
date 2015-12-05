package com.clouby.tetris;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by nboneh on 11/28/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    // Highscores Table Columns names
    public static final String KEY_SCORE = "score";
    public static final String KEY_ALIAS = "alias";
    public static final String KEY_SENT_TO_WEBAPP = "sent";
    private static final int DATABASE_VERSION = 1;
    private static final String WEB_APP_URL = "https://androidtetrisscores.appspot.com/highscore";
    // Database Name
    private static final String DATABASE_NAME = "tetrisDB";

    // Highscores table name
    private static final String HIGHSCORE_TABLE_NAME = "highscores";
    private static DatabaseHandler instance;
    int NUMBER_OF_LOCAL_HIGHSCORES = 5;
    private ConnectivityManager cm;


    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static DatabaseHandler getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHandler(context.getApplicationContext());
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CROWD_MAG_TABLE = "CREATE TABLE " + HIGHSCORE_TABLE_NAME + "("
                + KEY_SCORE + " INT, " + KEY_ALIAS + " STRING, " + KEY_SENT_TO_WEBAPP + " BOOLEAN)";
        db.execSQL(CREATE_CROWD_MAG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + HIGHSCORE_TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public void insetScore(int score, String alias) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SCORE, score);
        values.put(KEY_ALIAS, alias);
        values.put(KEY_SENT_TO_WEBAPP, false);
        db.insert(HIGHSCORE_TABLE_NAME, null, values);

    }

    public boolean madeLocalHighscores(int score) {
        String selectQuery = "SELECT  " + KEY_SCORE + " FROM " + HIGHSCORE_TABLE_NAME + " ORDER BY " + KEY_SCORE + " DESC LIMIT " + NUMBER_OF_LOCAL_HIGHSCORES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        boolean madeHighScores = false;

        int totalChecks = 0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if (score > cursor.getInt(0)) {
                    madeHighScores = true;
                    break;
                }
                totalChecks++;
            } while (cursor.moveToNext());
        }

        if (!madeHighScores && totalChecks < NUMBER_OF_LOCAL_HIGHSCORES) {
            madeHighScores = true;
        }

        return madeHighScores;
    }

    public void sendHighscoresToWebApp() {
        if (!phonehasInternetConnection())
            return;
        final SQLiteDatabase db = this.getWritableDatabase();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                String selectQuery = "SELECT * FROM " + HIGHSCORE_TABLE_NAME + " WHERE " + KEY_SENT_TO_WEBAPP + " = 0";
                Cursor cursor = db.rawQuery(selectQuery, null);
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {

                        int score = cursor.getInt(0);
                        String alias = cursor.getString(1);
                        String urlParameters = "&score=" + score + "&alias=" + alias;
                        try {
                            URL url = new URL(WEB_APP_URL);
                            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);
                            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

                            writer.write(urlParameters);
                            writer.flush();

                            conn.getResponseCode();
                            writer.close();

                            String updateStatement = "UPDATE " + HIGHSCORE_TABLE_NAME + " SET " + KEY_SENT_TO_WEBAPP + " = 1 " +
                                    " WHERE " + KEY_ALIAS + " = '" + alias + "'" + " AND " + KEY_SCORE + " = " + score;
                            db.execSQL(updateStatement);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } while (cursor.moveToNext());
                }
            }

        };
        Thread t = new Thread(r);
        t.start();
    }

    public void clearLocalHighScores() {
        SQLiteDatabase db = this.getWritableDatabase();
        String statement = "DELETE FROM " + HIGHSCORE_TABLE_NAME;
        db.execSQL(statement);
    }

    private boolean phonehasInternetConnection() {
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    //Returns false if the user phone is not connected to the internet
    public boolean getOnlineHighScores(final Handler h) {
        if (!phonehasInternetConnection())
            return false;

        final SQLiteDatabase db = this.getWritableDatabase();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    URL website = new URL(WEB_APP_URL);

                    URLConnection connection = website.openConnection();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream()));

                    StringBuilder response = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null)
                        response.append(inputLine);

                    List<HighScoreContainer> highscoreList = new ArrayList();

                    JSONArray jsonHighscores = new JSONArray(response.toString());
                    for (int i = 0; i < jsonHighscores.length(); i++) {
                        JSONObject jsonHighscore = jsonHighscores.getJSONObject(i);

                        HighScoreContainer container = new HighScoreContainer();
                        container.setScore(jsonHighscore.getInt("score"));
                        container.setAlias(jsonHighscore.getString("alias"));
                        highscoreList.add(container);
                    }
                    Message msg = new Message();
                    msg.obj = highscoreList;
                    h.sendMessage(msg);

                } catch (MalformedURLException e) {
                } catch (IOException e) {
                } catch (JSONException e) {
                }
            }

        };
        Thread t = new Thread(r);
        t.start();
        return true;
    }

    public List<HighScoreContainer> getLocalHighScores() {
        String selectQuery = "SELECT  * FROM " + HIGHSCORE_TABLE_NAME + " ORDER BY " + KEY_SCORE + " DESC LIMIT " + NUMBER_OF_LOCAL_HIGHSCORES;
        SQLiteDatabase db = this.getReadableDatabase();

        List<HighScoreContainer> highscoreList = new ArrayList();

        Cursor cursor = db.rawQuery(selectQuery, null);
        int total = 0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HighScoreContainer container = new HighScoreContainer();
                container.setScore(cursor.getInt(0));
                container.setAlias(cursor.getString(1));
                highscoreList.add(container);
                total++;
            } while (cursor.moveToNext());
        }

        int leftToAdd = NUMBER_OF_LOCAL_HIGHSCORES - total;
        for (int i = 0; i < leftToAdd; i++) {
            HighScoreContainer container = new HighScoreContainer();
            container.setScore(0);
            container.setAlias("Tetris");
            highscoreList.add(container);
        }
        return highscoreList;
    }
}
