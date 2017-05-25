package com.example.kidde.cmsc191project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    SQLiteOpenHelper gameHelper = null;
    SQLiteDatabase db = null;
    ArrayList<Button> buttonList = new ArrayList<Button>();
    ArrayList<Button> letterList = new ArrayList<Button>();
    String answerText = "";
    int currentLevel;
    int inputNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameHelper = new GameDatabaseHelper(this);
        db = gameHelper.getReadableDatabase();

        initializeBtn();
        newWord();


    }

    private void initializeBtn () {
        buttonList.add((Button)findViewById(R.id.lbutton1));
        buttonList.add((Button)findViewById(R.id.lbutton2));
        buttonList.add((Button)findViewById(R.id.lbutton3));
        buttonList.add((Button)findViewById(R.id.lbutton4));
        buttonList.add((Button)findViewById(R.id.lbutton5));
        buttonList.add((Button)findViewById(R.id.lbutton6));
        buttonList.add((Button)findViewById(R.id.lbutton7));
        buttonList.add((Button)findViewById(R.id.lbutton8));
        buttonList.add((Button)findViewById(R.id.lbutton9));
        buttonList.add((Button)findViewById(R.id.lbutton10));
        buttonList.add((Button)findViewById(R.id.lbutton11));
        buttonList.add((Button)findViewById(R.id.lbutton12));


    }
    private void newWord () {

        Cursor lvlCursor = db.query("USER", new String[] {"LEVEL"},
                "_id = ?",
                new String[] {Integer.toString(1)}
                , null, null, null);
        if(lvlCursor.moveToFirst()) {
            currentLevel = lvlCursor.getInt(0);
        }
        else currentLevel = 1;
        Cursor cursor = db.query("IMAGE", new String[] {"NAME"},
                "_id = ?",
                new String[] {Integer.toString(currentLevel)}
                , null, null, null);

        if(cursor.moveToFirst()){
            final String nameText = cursor.getString(0);
            int resourceID = getResources().getIdentifier(nameText, "drawable", getPackageName());
            ImageView img = (ImageView) findViewById(R.id.imageView1);
            img.setImageResource(resourceID);

            String scrambleText = nameText;
            int wordLength = nameText.length();
            for(int i = 0; i < 12-wordLength; i++){
                Random r = new Random();
                char c = (char) (r.nextInt(26) + 'a');
                scrambleText = scrambleText + c;
            }

            scrambleText = scramble(new Random(), scrambleText);
            populateButtons(scrambleText);

            for(int i = 0; i < nameText.length(); i++) {
                Button txt = new Button(this);
                txt.setText(".");
                txt.setEnabled(false);
                txt.setBackgroundColor(Color.BLACK);
                txt.setTextColor(Color.WHITE);


                LinearLayout ll = (LinearLayout)findViewById(R.id.linearlayout1);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                txt.setLayoutParams(lp);
                ll.addView(txt);

                letterList.add(txt);
            }

            System.out.println(nameText);


            for(final Button btn: buttonList) {
                final String s = String.valueOf(btn.getText());
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(inputNo == nameText.length()) return;
                        v.setEnabled(false);
                        answerText = answerText + s;
                        updateEmptySpaces();
                        System.out.println(answerText);
                        inputNo += 1;
                        if(answerText.equals(nameText)){
                            ContentValues newVal = new ContentValues();

                            if(inputNo!=13) newVal.put("LEVEL", currentLevel+1);
                            else {
                                newVal.put("LEVEL", 1);
                                currentLevel = 1; 
                            }
                            db.update("USER", newVal, "_id = ?",
                                    new String[] {Integer.toString(1)});
                            answerText = "";
                            letterList.clear();
                            inputNo = 0;
                            LinearLayout layout = (LinearLayout) findViewById(R.id.linearlayout1);
                            layout.removeAllViews();

                            newWord();

                        }
                    }
                });
            }

            final String scrambleCopy = scrambleText;
            Button clr = (Button) findViewById(R.id.clrbutton);
            clr.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    inputNo = 0;
                    answerText = "";
                    updateEmptySpaces();
                    populateButtons(scrambleCopy);

                }
            });


        }
        cursor.close();
    }

    public static String scramble(Random random, String inputString) {
        char a[] = inputString.toCharArray();

        // Scramble the letters using the standard Fisher-Yates shuffle,
        for(int i=0; i<a.length; i++ )
        {
            int j = random.nextInt(a.length);
            // Swap letters
            char temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }

        return new String(a);
    }

    public void populateButtons (String str) {
        for(int i = 0; i < str.length(); i++) {
            buttonList.get(i).setText(String.valueOf(str.charAt(i)));
            buttonList.get(i).setEnabled(true);
        }
    }

    public void updateEmptySpaces(){
        int i;
        for(i = 0; i < answerText.length(); i++){
            letterList.get(i).setText(String.valueOf(answerText.charAt(i)));
        }
        if(i==0) {
            for(Button btn : letterList) {
                btn.setText(".");
            }
        }
    }
    @Override
    protected void onDestroy () {
        db.close();
        super.onDestroy();

    }

}
