/**
 * Исключение: IsNotCorrectFormatException
 * Цель: выбросить исключение в случаи выбора файла формата, неподдерживаемого приложением
 * Автор: Шумаков А.А.
 */

package com.android.fileexplorerdemo;

import java.io.File;

public class IsNotCorrectFormatException extends Exception {
    private File selectFile;

    IsNotCorrectFormatException(File selectFile) {
        this.selectFile = selectFile;
    }

    public File getSelectFile() {
        return selectFile;
    }
}
