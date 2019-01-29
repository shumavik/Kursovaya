package com.android.fileexplorerdemo;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;

public class TopActivity extends Activity {
    HashMap<Button, Button> bookDel = new HashMap<>();

    File rootFolder = Environment.getExternalStorageDirectory();
    File topDir = new File(rootFolder.getAbsolutePath() + "/World of books"+"/Top");

    MediaPlayer clickSound;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TopActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Избранное");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);


        //////////////С П И С О К  К Н И Г///////////////////////////
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        rootFolder = Environment.getExternalStorageDirectory();
        rootFolder = new File(rootFolder.getAbsolutePath() + "/World of books" + "/Top");
        File[] filesArray = rootFolder.listFiles();

        for (int i = 0; i < filesArray.length; i++) {

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));


            Button delTop = new Button (getApplicationContext());
            delTop.setBackgroundResource(R.drawable.deltop);
            delTop.setGravity(0);
            delTop.setOnClickListener(onClickListenerDelTop);
            tableRow.addView(delTop);


            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.books);

            tableRow.addView(imageView);
            TextView tv = new TextView (getApplicationContext());


            String str = String.valueOf(filesArray[i]);


            Button bookBtn = new Button (getApplicationContext());

            bookBtn.setText(str.substring(39, str.length()));
            bookBtn.setTextColor(Color.BLACK);
            bookBtn.setGravity(0);
            bookBtn.setOnClickListener(onClickListener);


            tableRow.addView(bookBtn);

            bookDel.put(delTop, bookBtn);

            tableRow.addView(tv);
            tableLayout.addView(tableRow, i);


        }
        /////////////////////////////////////////////////////////////

    }

    View.OnClickListener onClickListenerDelTop = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickSound = MediaPlayer.create(TopActivity.this, R.raw.click);
            clickSound.start();
            if (v.getClass() == Button.class) {
                Toast.makeText(TopActivity.this, Environment.getExternalStorageDirectory().getAbsolutePath() +
                                "/World of books/Top/" + ((Button) v).getText().toString(),
                        Toast.LENGTH_LONG).show();


                String selected = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/World of books/Top/" + bookDel.get((Button) v).getText().toString();

                File delFile = new File(selected);
                if (delFile.delete()) {
                    Toast.makeText(TopActivity.this, "Книга удалена из Избранного",
                            Toast.LENGTH_LONG).show();

                    TopActivity.super.recreate();

                } else {
                    Toast.makeText(TopActivity.this, "Ошибка! Не получилось удалить книгу из Избранного",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getClass() == Button.class) {
                clickSound = MediaPlayer.create(TopActivity.this, R.raw.click);
                clickSound.start();
                Toast.makeText(TopActivity.this, Environment.getExternalStorageDirectory().getAbsolutePath() +
                                "/World of books/Top/" + ((Button) v).getText().toString(),
                        Toast.LENGTH_LONG).show();

                String curBook = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/World of books/" + ((Button) v).getText().toString();
                Intent intent = new Intent(TopActivity.this, ViewActivity.class);
                intent.putExtra("curBook", curBook);
                startActivity(intent);
            }
        }
    };

}

