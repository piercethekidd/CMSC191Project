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
                + "NAME TEXT, "
                + "PICTURE TEXT);");
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(mngr.open("strings.txt")));
            String line;
            String imageName = "image";
            int count = 0;
            while((line = reader.readLine()) != null) {
                count += 1;
                ContentValues wordValue = new ContentValues();
                wordValue.put("NAME", line);
                wordValue.put("PICTURE", imageName + count + ".jpg");
                db.insert("IMAGE", null, wordValue);
                count += 3;
            }

        }
        catch (Exception e) {
            System.out.println("Error\n");
        }


    }

    private static void insertPicture (SQLiteDatabase db, String name, String image) {
        ContentValues pictureValues = new ContentValues();
        pictureValues.put("NAME", name);
        pictureValues.put("PICTURE", image);

        db.insert("IMAGE", null, pictureValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
