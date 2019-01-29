/**
 * Класс: delBook
 * Цель: удаление выбранной пользователем книги из списка книг и из списка избранного
 * Автор: Шумаков А.А.
 */

package com.android.fileexplorerdemo;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;

public class delBook {

    public static void funcDelBook(View v, HashMap<Button, Button> bookDel, Context act) {
        String selected;
        File delFile;


            selected = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/World of books/" + bookDel.get((Button) v).getText().toString();

            delFile = new File(selected);
            if (delFile.delete()) {
                Toast.makeText(act, "Книга удалена из списка",
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(act, "Ошибка! Не получилось удалить книгу из списка",
                        Toast.LENGTH_SHORT).show();
            }


        selected = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/World of books/Top/" + bookDel.get((Button) v).getText().toString();

        delFile = new File(selected);
        if (delFile.delete()) {
            Toast.makeText(act, "Книга удалена из Избранного",
                    Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(act, "Ошибка! Не получилось удалить книгу из Избранного",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
