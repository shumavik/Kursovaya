/**
 * Класс: addTop
 * Цель: добавление выбранной пользователем книги в список избранного
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class addTop {

    public static void funcAddTop(View v, HashMap<Button, Boolean> starFlag, HashMap<Button, Button> topAdd,
                                  File rootFolder ,Context act) {
        if ((v.getClass() == Button.class) && (starFlag.get(((Button) v)) == false)) {
            ((Button) v).setBackgroundResource(R.drawable.staron);
            starFlag.put((Button) v, true);

            Toast.makeText(act, "Книга добавлена в раздел 'Избранное'",
                    Toast.LENGTH_SHORT).show();

            String selected = Environment.getExternalStorageDirectory().getAbsolutePath() + "/World of books/" +
                    topAdd.get((Button) v).getText().toString();


            File fileSelected = new File(selected);

            String pattern = "([^/]+)$";
            String filePath = selected.toString();
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(filePath);
            String fileName = new String();
            while(m.find()) {
                fileName += filePath.substring(m.start(), m.end());
            }

            String dest = rootFolder.getAbsolutePath() + "/Top/";
            CopyFile.myCopyFunc(fileSelected, dest, fileName); // Копирование файла

        } else if ((v.getClass() == Button.class) && (starFlag.get(((Button) v)) == true)) {
            String selected = Environment.getExternalStorageDirectory().getAbsolutePath() + "/World of books/Top/" +
                    topAdd.get((Button) v).getText().toString();


            File delFile = new File(selected);
            if (delFile.delete()) {
                ((Button) v).setBackgroundResource(R.drawable.staroff);
                starFlag.put((Button) v,false);

                Toast.makeText(act, "Книга удалена из Избранного",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(act, "Ошибка! Не получилось удалить книгу из Избранного",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }
}
