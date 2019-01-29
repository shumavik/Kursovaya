package com.android.fileexplorerdemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyFile {
    ////////////////////////////К О П И Р О В А Н И Е  Ф А Й Л О В////////////////////
    public static void myCopyFunc(File selected, String dest, String fileName) {
        File sourceFile = selected;
        File destFile = new File(dest + fileName);

        try {
            FileInputStream varInput = new FileInputStream(sourceFile);
            FileOutputStream varOutput = new FileOutputStream(destFile);
            byte[] var = new byte[1024];

            int var5;
            while ((var5 = varInput.read(var)) > 0) {
                varOutput.write(var, 0, var5);
            }

            varInput.close();
            varOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /////////////////////////////////////////////////////////////////////////
}
