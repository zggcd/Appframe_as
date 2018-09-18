package com.asiainfo.appframe.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public class BitmapUtil {
	/*
     * 添加文字到Drawable中
     */
    public static Drawable addStringToDrawable(Context context,
            Drawable imgMarker, String paramString, int paramX, int paramY) {

        Bitmap bitmap = ((BitmapDrawable) imgMarker).getBitmap();
        int width, height;
        width = bitmap.getWidth();
        height = bitmap.getHeight();

        Bitmap imgTemp = Bitmap.createBitmap(width, height,
                Config.ARGB_8888);

        Canvas canvas = new Canvas(imgTemp);
        Paint paint = new Paint(); // 建立画笔
        paint.setDither(true);
        paint.setFilterBitmap(true);
        Rect src = new Rect(0, 0, width, height);
        Rect dst = new Rect(0, 0, width, height);
        canvas.drawBitmap(bitmap, src, dst, paint);

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DEV_KERN_TEXT_FLAG);
        textPaint.setTextSize(20.0f);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD); // 采用默认的宽度
        textPaint.setColor(Color.WHITE);

        canvas.drawText(paramString, width / 2 - paramX, height / 2 + paramY,
                textPaint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return (Drawable) new BitmapDrawable(context.getResources(), imgTemp);

    }

    /**
     * 图片灰化
     * 
     * @param bmpOriginal
     *            原彩图
     * @return 灰化后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /**
     * 图片合成
     * 
     * @param bitmap
     * @return
     */
    public Bitmap createBitmap(Bitmap src, Bitmap watermark) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        // draw watermark into
        cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;
    }

    /**
     * 图片圆角
     * 
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 缩放、翻转和旋转图片
     * 
     * @param bmpOrg
     * @param rotate
     * @return
     */
    public Bitmap gerZoomRotateBitmap(
            Bitmap bmpOrg, int rotate) {
        // 获取图片的原始的大小
        int width = bmpOrg.getWidth();
        int height = bmpOrg.getHeight();

        int newWidth = 300;
        int newheight = 300;
        // 定义缩放的高和宽的比例
        float sw = ((float) newWidth) / width;
        float sh = ((float) newheight) / height;
        // 创建操作图片的用的Matrix对象
        Matrix matrix = new Matrix();
        // 缩放翻转图片的动作
        // sw sh的绝对值为绽放宽高的比例，sw为负数表示X方向翻转，sh为负数表示Y方向翻转
        matrix.postScale(sw, sh);
        // 旋转30*
        matrix.postRotate(rotate);
        // 创建一个新的图片
        Bitmap resizeBitmap = Bitmap
                .createBitmap(bmpOrg, 0, 0, width, height, matrix, true);
        return resizeBitmap;
    }

    /**
     * Draw the view into a bitmap.
     */
    public static Bitmap getViewBitmap(View v) {
        if (null == v) {
            return null;
        }
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    /**
     * Bitmap对象转化byte数组
     * 
     * @param bitmap
     *            Bitmap对象
     * @return byte数组
     */
    public static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * drawable对象转化byte数组
     * 
     * @param drawable
     *            drawable对象
     * @return byte数组
     */
    public static byte[] drawableToBytes(Drawable drawable) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ((BitmapDrawable) drawable).getBitmap().compress(
                Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byte数组转化drawable对象
     * 
     * @param byte byte数组
     * @return drawable对象
     */
    public static Drawable bytesToDrawable(byte[] data) {
        if (data.length != 0) {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(
                    BitmapFactory.decodeByteArray(data, 0, data.length));
            Drawable drawable = (Drawable) bitmapDrawable;
            return drawable;
        } else {
            return null;
        }
    }

    /**
     * 将bitmap转换成Drawable
     * 
     * @param bitmap
     *            bitmap对象
     * @return drawable对象
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        if (bitmap != null) {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            Drawable drawable = (Drawable) bitmapDrawable;
            return drawable;
        }
        return null;
    }

    /**
     * 将Drawable转换成bitmap对象
     * 
     * @param Drawable
     *            Drawable对象
     * @return bitmap对象
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            return bitmap;
        }
        return null;
    }

    /**
     * 从工程资源或者SD卡中加载图片资源
     * 
     * @param fileName
     *            SD卡中存放的资源路径
     */
    public static Bitmap loadImageResource(int fileName) {
        Bitmap bitmap = null;
        InputStream is = null;
        FileInputStream fis = null;
        try {
            // bitmap = BitmapFactory.decodeResource(
            // FusionField.currentActivity.getResources(), fileName);
        } catch (Exception e) {
            // LogX.getInstance().e(TAG,"::loadImageResource: loadImageResource error "
            // + e.toString());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                // LogX.getInstance().e(TAG,
                // "::loadImageResource: is close Exception. " + e.toString());
            } finally {
                is = null;
                fis = null;
            }
        }
        return bitmap;
    }

    /**
     * 加载本地图片
     * 
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 方法描述:<br>
     * 创建人:001651<br>
     * 创建日期:2012-6-12<br>
     * 
     * @param bitmap
     * @param width
     * @param height
     * @return<br>
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float) width / w, (float) height / h);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    public static byte[] zoomInBitmap(String path) {
        // "/sdcard/dcim/Camera/hello.jpg"

        Bitmap bitmap = resize(path);
        byte[] bp = null;
        if (bitmap != null) {
            bp = bitmapToBytes(bitmap);
        }
        return bp;

    }

    public static String getFileExt(String filepath) {
        String fileType = null;

        try {
            // 从SDCARD下读取一个文件
            FileInputStream inputStream = new FileInputStream(filepath);
            byte[] buffer = new byte[2];
            // 文件类型代码
            String filecode = "";

            // 通过读取出来的前两个字节来判断文件类型
            if (inputStream.read(buffer) != -1) {
                for (int i = 0; i < buffer.length; i++) {
                    // 获取每个字节与0xFF进行与运算来获取高位，这个读取出来的数据不是出现负数
                    // 并转换成字符串
                    filecode += Integer.toString((buffer[i] & 0xFF));
                }
                // 把字符串再转换成Integer进行类型判断
                switch (Integer.parseInt(filecode)) {
                case 255216:
                    fileType = "jpg";
                    break;
                case 7173:
                    fileType = "gif";
                    break;
                case 6677:
                    fileType = "bmp";
                    break;
                case 13780:
                    fileType = "png";
                    break;
                default:
                    fileType = "unknown type: " + filecode;
                }

            }
            Log.d(fileType);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return fileType;
    }

    public static int getBitmapWidth(String path) {
        // 对图片进行压缩
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);// 此时返回bitmap为空
        options.inJustDecodeBounds = false;
        int width = options.outWidth;
        return width;
    }

    public static Bitmap resize(String path) {
        // 对图片进行压缩
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);// 此时返回bitmap为空
        options.inJustDecodeBounds = false;

        int width = options.outWidth;
        int height = options.outHeight;
        // Log.d("BitmapUtil", "orignal width--->"+width+" height--->"+height);

        int be = 0;
        double ratio = 0.0;
        int deswidth = 0;
        int desheight = 0;

        // int longLen = width > height ? width : height;

        if (width <= 500) {
            File file = new File(path);

            if (((float) file.length() / (1024 * 1024)) < 2.0) {
                Bitmap bp = (Bitmap) BitmapFactory.decodeFile(path);
                return bp;
            } else {
                return null;
            }

        } else if (width > 500) {
            if (width != height) {
                // ratio = Math.ceil(width / 500);
                ratio = (double) width / (Math.round(500 * 100) / 100);
                deswidth = 500;
                desheight = (int) (height / ratio);

                // if (width >= 500)
                // {
                // int newHeight = 500 * height / width;
                // LogX.trace("BitmapUtil", "height:" + newHeight + "width:" +
                // width
                // + "---");
                //
                // be = getOptionsSampleSize(options, 500, newHeight);
                // }

            } else if (width == height) {
                deswidth = 500;
                desheight = 500;
            }
        }

        // 计算缩放比
        // int be = (int) (options.outHeight / (float) 200);
        be = (int) ratio;
        if (be <= 0) {
            be = 1;
        }
        // Log.d("BitmapUtil",
        // "new width--->"+deswidth+" height--->"+desheight);
        // Log.d("BitmapUtil", "inSampleSize--->"+ be);
        BitmapFactory.Options noptions = new BitmapFactory.Options();

        noptions.inSampleSize = be;
        noptions.outWidth = deswidth;
        noptions.outHeight = desheight;

        // 重新读入图片，注意这次要把options.inJustDecodeBounds设为false哦
        bitmap = BitmapFactory.decodeFile(path, noptions);
        // int w = bitmap.getWidth();
        // int h=bitmap.getHeight();
        // System.out.println(w+" "+h);
        bitmap = zoomBitmap(bitmap, deswidth, desheight);

        return bitmap;
    }

    private static int getOptionsSampleSize(BitmapFactory.Options options,
            int newWidth, int newHeight) {
        int radioWidth = (int) Math.ceil(options.outWidth / newWidth);
        int radioHeight = (int) Math.ceil(options.outHeight / newHeight);
        if (radioWidth > 1 || radioHeight > 1) {
            return radioWidth > radioHeight ? radioWidth : radioHeight;
        } else {
            return 1;
        }
    }

    /**
     * 缩小文件尺寸 (压缩文件大小)
     * 
     * @param filePath
     * @param hight
     * @param width
     */
    public static Bitmap zoomBitmap(String fromPath, int newhight,
            int newwidth, String topath) {
        // int new_hight = 1024;
        // int new_width = new_hight * width / hight;
        Bitmap bm = null;
        try {
            bm = getBitmapFromFile(fromPath, newwidth, newhight, topath);
        } catch (OutOfMemoryError e) {
            Log.d("BitmapUtil", "警告:手机内存不足");
            Log.d("BitmapUtil", "警告:手机内存不足");
        } catch (IOException e) {
            Log.d("BitmapUtil", "删除文件失败");
        }
        return bm;
    }

    public static Bitmap getBitmapFromFile(String frompath, int width,
            int height, String topath) throws IOException {
        File dst = new File(frompath);
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,
                        width * height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            Bitmap bitmap = BitmapFactory.decodeFile(frompath, opts);
            FileHelper.creatFile2SDCard(topath);
            FileOutputStream out = new FileOutputStream(topath);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)) {
                out.flush();
                out.close();
            }
            return bitmap;
        }
        return null;
    }

    public static Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(bitmap);
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

    public static int computeSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static int computeSampleSize(BitmapFactory.Options options,
            int maxNumOfPixels) {

        int initialSize = computeInitialSampleSize(options, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
            int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        System.out.println("原图size：" + w * h);
        if (w * h > maxNumOfPixels) {
            return (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        } else {
            return 1;
        }
    }
}
