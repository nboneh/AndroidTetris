package com.clouby.tetris;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nboneh on 11/28/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static DatabaseHandler inst;

    private static final int DATABASE_VERSION = 1;

    int NUMBER_OF_LOCAL_HIGHSCORES = 5;

    // Database Name
    private static final String DATABASE_NAME = "tetrisDB";

    // Highscores table name
    private static final String HIGHSCORE_TABLE_NAME = "highscores";

    // Highscores Table Columns names
    public static final String KEY_SCORE = "score";
    public static final String KEY_ALIAS = "alias";
    public static final String KEY_SENT_TO_WEBAPP = "sent";

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHandler getInstance(Context context) {
        if (inst == null)
            inst = new DatabaseHandler(context.getApplicationContext());
        return inst;
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

    public void insetScore(int score, String alias){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SCORE, score);
        values.put(KEY_ALIAS, alias);
        values.put(KEY_SENT_TO_WEBAPP, false);
        db.insert(HIGHSCORE_TABLE_NAME, null, values);
        db.close();
    }

    public boolean madeLocalHighscores(int score){
        String selectQuery = "SELECT  " + KEY_SCORE + " FROM " + HIGHSCORE_TABLE_NAME + " ORDER BY " +  KEY_SCORE + " DESC LIMIT " + NUMBER_OF_LOCAL_HIGHSCORES;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        boolean madeHighScores = false;

        int totalChecks = 0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if(score > cursor.getInt(0)){
                    madeHighScores = true;
                    break;
                }
                totalChecks++;
            } while (cursor.moveToNext());
        }

        if(!madeHighScores && totalChecks < NUMBER_OF_LOCAL_HIGHSCORES){
            madeHighScores = true;
        }

        return madeHighScores;
    }

    public void sendHighscoresToWebApp(){

    }
}
