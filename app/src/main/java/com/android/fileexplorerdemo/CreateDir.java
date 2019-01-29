/**
 * Класс: CreateDir
 * Цель: Создание директорий мобильного приложения
 * Автор: Шумаков А.А.
 */
package com.android.fileexplorerdemo;

import android.content.Context;
import android.widget.Toast;

import java.io.File;

public class CreateDir {
    public static void funcCreateDir(File rootFolder, Context act) {
        File theDir = new File(rootFolder.getAbsolutePath() + "/World of books");
        File topDir = new File(rootFolder.getAbsolutePath() + "/World of books"+"/Top");
        if (!theDir.exists() || !topDir.exists()) {
            try {
                theDir.mkdir();
                topDir.mkdir();
            } catch (SecurityException se) {
                Toast.makeText(act, "Ошибка! Не удалось создать папку",
                        Toast.LENGTH_SHORT).show();
                se.printStackTrace();
            }
        }
    }
}
