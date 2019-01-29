/**
 * Класс: ViewActivity
 * Цель: окно для чтения открытой книги
 * Автор: Шумаков А.А.
 */
package com.android.fileexplorerdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;


public class ViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        String name = intent.getStringExtra("curBook");
        setTitle(name.substring(35, name.length()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);


        File curFile = new File(name);

        PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.fromFile(curFile).load();
    }
}
