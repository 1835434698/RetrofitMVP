package com.cnepay.android.swiper.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageUtils {

    /**
     * 获取压缩之后的Bitmap 对象
     *
     * @param filePath  源文件路径
     * @param desHeight 目标压缩高度
     * @param desWidth  目标压缩宽度
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, double desHeight, double desWidth) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        Boolean scaleByHeight = Math.abs(options.outHeight - desHeight) >= Math.abs(options.outWidth - desWidth);
        if (options.outHeight * options.outWidth * 2 >= 200 * 100 * 2) {
            // Load, scaling to smallest power of 2 that'll get it < = desired dimensions
            double sampleSize = scaleByHeight ? options.outHeight
                    / desHeight : options.outWidth / desWidth;
            options.inSampleSize = (int) Math.pow(2d,
                    Math.floor(Math.log(sampleSize) / Math.log(2d)));
        }

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 图片压缩到目标大小并且转换成为灰色图片存储到目标路径
     *
     * @param srcfilePath 源文件路径
     * @param desfilePath 目标文件路径
     * @param desHeight   目标压缩高度
     * @param desWidth    目标压缩宽度
     * @throws IOException
     */
    public static void compress2GrayFile(String srcfilePath, String desfilePath,
                                         double desHeight, double desWidth) {

        Bitmap bitmap = getSmallBitmap(srcfilePath, desHeight, desWidth);
        Bitmap graybmp = toGrayscale(bitmap);
        bitmap.recycle();
        bitmap = null;
        compress2File(graybmp, desfilePath);
    }

    public static void compress2File(Bitmap srcBitmap, String desfilePath) {
        Log.e("ImageUtils",desfilePath);
        if (srcBitmap == null)
            return;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(desfilePath);
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        srcBitmap.recycle();
        srcBitmap = null;
    }

    /**
     * bitmap to gray bitmap
     *
     * @param bmpOriginal
     * @return
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {

        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix(new float[]{0.5f, 0.5f, 0.5f, 0, 0,
                0.5f, 0.5f, 0.5f, 0, 0, 0.5f, 0.5f, 0.5f, 0, 0, 0, 0, 0, 1, 0,
                0, 0, 0, 0, 0, 1, 0});
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

}
