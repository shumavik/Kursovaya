/**
 * Класс: MainActivity
 * Цель: реализация списка книг
 * Автор: Шумаков А.А.
 */
package com.android.fileexplorerdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {
    Button buttonOpenDialog;
    Button buttonUp;
    TextView textFolder;


    static final int CUSTOM_DIALOG_ID = 0;
    ListView dialog_ListView;

    HashMap<Button, Boolean> starFlag = new HashMap<>();
    HashMap<Button, Button> topAdd = new HashMap<>();
    HashMap<Button, Button> bookDel = new HashMap<>();

    File root;
    File curFolder;
    File rootFolder = Environment.getExternalStorageDirectory();

    private List<String> fileList = new ArrayList<String>();

    ProgressDialog progressDialog;
    private MediaPlayer clickSound;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Список книг");

        // получаем SharedPreferences, которое работает с файлом настроек
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        Boolean notif = sp.getBoolean("notif", false); //получаем значение по ключу

        if (notif == true) {
            MyNotifyStart.notifyStart(MainActivity.this);
        }

        // Создание папок
        CreateDir.funcCreateDir(rootFolder, MainActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //////////////////////////С П И С О К  К Н И Г///////////////////////////
        //Создание табличной разметки
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        //Получение пути к папке 'World of books'
        rootFolder = Environment.getExternalStorageDirectory();
        rootFolder = new File(rootFolder.getAbsolutePath() + "/World of books");

        File[] filesArray = rootFolder.listFiles(); //Получение списка файлов (книг)

        // Динамическая разметка Активити
        for (int i = 0; i < filesArray.length; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            //Сохдание кнопки 'Добавления в извбранное' (звезда)
            Button btnTop = new Button (getApplicationContext());
            btnTop.setBackgroundResource(R.drawable.staroff);
            starFlag.put(btnTop, false);
            btnTop.setGravity(0);
            btnTop.setOnClickListener(onClickListenerBtnTop);

            //Создание значка (книга)
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.books);

            TextView tv = new TextView (getApplicationContext());

            //Создание кнопки с названием книги
            String str = String.valueOf(filesArray[i]);
            Button bookBtn = new Button (getApplicationContext());
            bookBtn.setText(str.substring(35, str.length())); //Обрезаем путь, чтобы оставить только название книги
            bookBtn.setTextColor(Color.BLACK);
            bookBtn.setGravity(0);
            bookBtn.setOnClickListener(onClickListener);
            topAdd.put(btnTop,bookBtn);


            //Создание кнопки удаления книги
            Button btnDel = new Button (getApplicationContext());
            btnDel.setBackgroundResource(R.drawable.del);
            btnDel.setGravity(0);
            btnDel.setOnClickListener(onClickListenerBtnDel);
            bookDel.put(btnDel, bookBtn);


            //Добавляем кнопки, если выбранный файл не являевтся папкой
            String subName = (bookBtn.getText()).toString();
            if (subName.equals("Top") == false) {
                tableRow.addView(btnDel);
                tableRow.addView(btnTop);
                tableRow.addView(imageView);
                tableRow.addView(bookBtn);
            }

            File topFolder = new File(rootFolder.getAbsolutePath() + "/Top/" + bookBtn.getText());
            // Устанавливаем звезды, если книга есть в папке Top
            if ((topFolder).exists()) {
                btnTop.setBackgroundResource(R.drawable.staron);
                starFlag.put(btnTop, true);
            }
            tableRow.addView(tv);
            tableLayout.addView(tableRow, i);

        }
        /////////////////////////////////////////////////////////////

        // Создание проводника
        buttonOpenDialog = (Button) findViewById(R.id.opendialog);
        buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(CUSTOM_DIALOG_ID);
            }
        });

        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        curFolder = root;
    }

    // Обработка нажатия на кнопку добавления в закладки
    View.OnClickListener onClickListenerBtnTop = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Boolean sounds = sp.getBoolean("sounds", false);
            if (sounds == true) {
                clickSound = MediaPlayer.create(MainActivity.this, R.raw.click);
                clickSound.start();
            }
            addTop.funcAddTop(v, starFlag, topAdd, rootFolder,MainActivity.this);
        }
    };

    // Обработка удаления книги
    View.OnClickListener onClickListenerBtnDel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Boolean sounds = sp.getBoolean("sounds", false);
            if (sounds == true) {
                clickSound = MediaPlayer.create(MainActivity.this, R.raw.click);
                clickSound.start();
            }
            if (v.getClass() == Button.class) {
                delBook.funcDelBook(v, bookDel, MainActivity.this);

                MainActivity.super.recreate();
            }
        }
    };

    // Обработчик нажатия на кнопку для просмотра книги
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getClass() == Button.class) {
                Toast.makeText(MainActivity.this, Environment.getExternalStorageDirectory().getAbsolutePath() +
                                "/World of books/" + ((Button) v).getText().toString(),
                        Toast.LENGTH_LONG).show();

                String curBook = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/World of books/" + ((Button) v).getText().toString();
                Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                intent.putExtra("curBook", curBook);
                startActivity(intent);

            }
        }
    };



    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_top:
                Boolean sounds = sp.getBoolean("sounds", false);
                if (sounds == true) {
                    clickSound = MediaPlayer.create(MainActivity.this, R.raw.click);
                    clickSound.start();
                }
                Intent intent = new Intent(MainActivity.this, TopActivity.class);
                startActivity(intent);
                break;
            case R.id.action_settings:
                Boolean sounds2 = sp.getBoolean("sounds", false);
                if (sounds2 == true) {
                    clickSound = MediaPlayer.create(MainActivity.this, R.raw.click);
                    clickSound.start();
                }
                intent = new Intent(MainActivity.this, PrefActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }


    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dialog = null;

        switch (id) {
            case CUSTOM_DIALOG_ID:
                dialog = new Dialog(MainActivity.this);
                AlertDialog.Builder Sdialog = new AlertDialog.Builder(this);
                dialog.setContentView(R.layout.dialoglayout);
                dialog.setTitle("Выберите книгу");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                textFolder = (TextView) dialog.findViewById(R.id.folder);
                buttonUp = (Button) dialog.findViewById(R.id.up);
                buttonUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListDir(curFolder.getParentFile());
                    }
                });

                dialog_ListView = (ListView) dialog.findViewById(R.id.dialoglist);
                dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @TargetApi(Build.VERSION_CODES.O)
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)  {
                        final File selected = new File(fileList.get(position));
                        String pathSelected = selected.toString();
                        try {
                            String myPattern = "([.])";
                            Pattern myP = Pattern.compile(myPattern);
                            Matcher myM = myP.matcher(pathSelected);
                            String findText = new String();
                            while (myM.find()) {
                                findText += pathSelected.substring(myM.start(), myM.end());
                            }

                            if (findText.equals(".") &&
                                    !pathSelected.substring(pathSelected.length() - 4, pathSelected.length()).equals(".pdf"))
                                throw new IsNotCorrectFormatException(selected);

                            if(selected.isDirectory()) {
                                ListDir(selected);
                            } else {

                                String pattern = "([^/]+)$";
                                String filePath = selected.toString();
                                Pattern p = Pattern.compile(pattern);
                                Matcher m = p.matcher(filePath);
                                String fileName = new String();

                                while(m.find()) {
                                    fileName += filePath.substring(m.start(), m.end());
                                }

                                Toast.makeText(MainActivity.this, selected.toString() + " добавлен в список книг",
                                        Toast.LENGTH_SHORT).show();


                                final String dest = rootFolder.getAbsolutePath() + "/";

                                //ProgressDialog dialog = ProgressDialog
                                //        .show(MainActivity.this,"","Пожалуйста, подождите, происходит копирование!", true);

                                progressDialog = new ProgressDialog(MainActivity.this);
                                progressDialog.setTitle("Выполняется копирование");
                                progressDialog.setMessage("Пожалуйста подождите...");
                                progressDialog.setCancelable(false);
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.show();


                                final String finalFileName = fileName;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        myCopyFunc(selected, dest, finalFileName);

                                        progressDialog.dismiss();
                                    }
                                }).start();

                                //myCopyFunc(selected,dest, fileName); // Копирование файла


                                MainActivity.super.recreate();


                                dismissDialog(CUSTOM_DIALOG_ID);
                            }

                        } catch (IsNotCorrectFormatException e) {
                            Toast.makeText(MainActivity.this, "Выберите книгу в формате .pdf",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;

        }

        return dialog;

    }



    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {

        super.onPrepareDialog(id, dialog);

        switch (id) {
            case CUSTOM_DIALOG_ID:
                ListDir(curFolder);
                Boolean sounds = sp.getBoolean("sounds", false);
                if (sounds == true) {
                    clickSound = MediaPlayer.create(MainActivity.this, R.raw.click);
                    clickSound.start();
                }
                break;
        }

    }

    void ListDir(File f) {

        if(f.equals(root)) {
            buttonUp.setEnabled(false);
        } else {
            buttonUp.setEnabled(true);
        }

        curFolder = f;
        textFolder.setText(f.getPath());

        File[] files = f.listFiles();
        fileList.clear();

        for(File file : files) {
            fileList.add(file.getPath());
        }

        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, fileList);
        dialog_ListView.setAdapter(directoryList);

    }

    ////////////////////////////К О П И Р О В А Н И Е  Ф А Й Л О В////////////////////



    void myCopyFunc(File selected, String dest, String fileName) {
        File sourceFile = selected;
        File destFile = new File(dest + fileName);

        try {
            FileInputStream var2 = new FileInputStream(sourceFile);
            FileOutputStream var3 = new FileOutputStream(destFile);
            byte[] var4 = new byte[1024];

            int var5;
            while ((var5 = var2.read(var4)) > 0) {
                var3.write(var4, 0, var5);
            }

            var2.close();
            var3.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /////////////////////////////////////////////////////////////////////////

}
