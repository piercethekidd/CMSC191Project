package com.example.kidde.cmsc191project;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by kidde on 20/05/2017.
 */

public class GameDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "game";
    private static final int DB_VERSION = 1;
    private AssetManager mngr;

    GameDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mngr = context.getAssets();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IMAGE("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT);");
        db.execSQL("CREATE TABLE USER("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "LEVEL INTEGER, "
                + "COIN INTEGER);");
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(mngr.open("strings.txt")));
            String line;
            String imageName = "image";

            while((line = reader.readLine()) != null) {

                ContentValues wordValue = new ContentValues();
                wordValue.put("NAME", line);
                db.insert("IMAGE", null, wordValue);

            }

            ContentValues userValues = new ContentValues();
            userValues.put("LEVEL", 1);
            userValues.put("COIN", 300);
            db.insert("USER", null, userValues);


        }
        catch (Exception e) {
            System.out.println("Error\n");
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS IMAGE");
        onCreate(db);
    }

}
