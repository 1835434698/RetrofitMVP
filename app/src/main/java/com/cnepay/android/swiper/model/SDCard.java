package com.cnepay.android.swiper.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.internal.Util;

/**
 * Created by Administrator on 2017/5/20.
 */

public class SDCard {
    public static String FILE_CATALOGUE = Environment.getExternalStorageState();
    public static String IMG_CATALOGUE;

    public static void init(Context context) {
        IMG_CATALOGUE = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static String saveBitmap2Local(Context context, Bitmap bitmap) {
        if (bitmap == null) return null;
        FileOutputStream fos = null;
        String filepath = null;
        String name = System.currentTimeMillis() + ".png";
        try {
            fos = context.openFileOutput(name, Context.MODE_PRIVATE);
            if (null != fos) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                filepath = context.getFileStreamPath(name).getAbsolutePath();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filepath;
    }

    public static File saveStream2Local(InputStream inputStream, OnProgress onProgress, long contentLength, String path, boolean isResume, String fileName) throws IOException {
        File targetFile = new File(path, fileName);
        if (!targetFile.exists()) {
            File dir = targetFile.getParentFile();
            if ((dir.exists()) || (dir.mkdirs())) {
                targetFile.createNewFile();
            }
        }
        long current = 0L;
        InputStream bis = null;
        FileOutputStream fileOutputStream = null;
        try {
            if (isResume) {
                current = targetFile.length();
                fileOutputStream = new FileOutputStream(targetFile, true);
            } else {
                fileOutputStream = new FileOutputStream(targetFile);
            }
            long total = contentLength + current;
            bis = inputStream;
            if ((onProgress != null) && (!onProgress.onProgress(total, current, true))) {
                return targetFile;
            }

            byte[] tmp = new byte[4 * 1024];
            int len;
            while ((len = bis.read(tmp)) != -1) {
                fileOutputStream.write(tmp, 0, len);
                current += len;
                if ((onProgress != null) &&
                        (!onProgress.onProgress(total, current, false))) {
                    return targetFile;
                }
            }

            fileOutputStream.flush();
            if (onProgress != null)
                onProgress.onProgress(total, current, true);
        } finally {
            Util.closeQuietly(bis);
            Util.closeQuietly(fileOutputStream);
        }

        return targetFile;
    }

    public interface OnProgress {
        boolean onProgress(long total, long current, boolean forceUpdateUI);
    }
}
