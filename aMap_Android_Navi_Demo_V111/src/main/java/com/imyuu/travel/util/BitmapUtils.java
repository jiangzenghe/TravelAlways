package com.imyuu.travel.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

import com.imyuu.travel.R;
import com.imyuu.travel.model.UserInfoJson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

@SuppressLint("NewApi")
public class BitmapUtils {
    public static final int IO_BUFFER_SIZE = 1024;


    public BitmapUtils() {
    }

    private static int computeSampleSize(android.graphics.BitmapFactory.Options options, int i) {
        return (options.outWidth * options.outHeight) / i;
    }

    public static byte[] convertBitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 10, bytearrayoutputstream);
        return bytearrayoutputstream.toByteArray();
    }

    private static void copy(InputStream inputstream, OutputStream outputstream)
            throws IOException {
        byte abyte0[] = new byte[1024];
        do {
            int i = inputstream.read(abyte0);
            if (i == -1) {
                return;
            }
            outputstream.write(abyte0, 0, i);
        } while (true);
    }

    public static Bitmap getAutoSizeBitmap(int idx, String s) {
        ParcelFileDescriptor parcelfiledescriptor;
        android.graphics.BitmapFactory.Options options;
        double d;
        Bitmap bitmap = null;
        try {
            //268435456
            parcelfiledescriptor = ParcelFileDescriptor.open(new File(s), 0x10000000);
            options = new android.graphics.BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(parcelfiledescriptor.getFileDescriptor(), null, options);
            d = Math.sqrt(computeSampleSize(options, idx));
            System.out.println((new StringBuilder("getAutoSizeBitmap sampleSize=")).append(d).toString());
            if (d <= 1.0D) {
                return null;
            }
            options.inSampleSize = (int) d;


            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFileDescriptor(parcelfiledescriptor.getFileDescriptor(), null, options);
            parcelfiledescriptor.close();
            options.inSampleSize = 1;
        } catch (FileNotFoundException filenotfoundexception) {

            filenotfoundexception.printStackTrace();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

        return bitmap;

    }

    public static BitmapParam getBitmapParamFromSDcard(Context context, String s) {
        ParcelFileDescriptor parcelfiledescriptor;
        BitmapParam bitmapparam1;
        bitmapparam1 = new BitmapParam();
        android.graphics.BitmapFactory.Options options;
        double d;
        options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            parcelfiledescriptor = ParcelFileDescriptor.open(new File((new StringBuilder()).append(Environment.getExternalStorageDirectory()).append("/").append(s).toString()), 0x10000000);

            BitmapFactory.decodeFileDescriptor(parcelfiledescriptor.getFileDescriptor(), null, options);
            bitmapparam1.width = options.outWidth;
            bitmapparam1.height = options.outHeight;
            d = Math.sqrt(computeSampleSize(options, 0x100000));
            if (d <= 1.0D) {
                return null;
            }
            options.inSampleSize = (int) d;

            bitmapparam1.scale = 1.0F / (float) d;
            options.inJustDecodeBounds = false;
            options.inScaled = false;
            options.inPreferredConfig = android.graphics.Bitmap.Config.RGB_565;
            bitmapparam1.mBitmap = BitmapFactory.decodeFileDescriptor(parcelfiledescriptor.getFileDescriptor(), new Rect(0, 0, 100, 100), options);
            parcelfiledescriptor.close();

            options.inSampleSize = 1;
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
        return bitmapparam1;

    }

    public static Bitmap getNetworkBitmap(int i, String s) {


        ByteArrayOutputStream bytearrayoutputstream;
        BufferedOutputStream bufferedoutputstream = null;
        BufferedInputStream bufferedinputstream = null;
        byte abyte0[];
        Bitmap bitmap = null;
        try {
            bufferedinputstream = new BufferedInputStream((new URL(s)).openStream(), 1024);

            bytearrayoutputstream = new ByteArrayOutputStream();
            bufferedoutputstream = new BufferedOutputStream(bytearrayoutputstream, 1024);
            copy(bufferedinputstream, bufferedoutputstream);
            bufferedoutputstream.flush();
            abyte0 = bytearrayoutputstream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(abyte0, 0, abyte0.length);

            bufferedoutputstream.close();
            bufferedoutputstream.close();

        } catch (IOException ioexception) {

            bufferedoutputstream = null;

        } finally {
            if (bufferedinputstream != null) {
                try {
                    bufferedinputstream.close();
                    if (bufferedoutputstream != null)
                        bufferedoutputstream.close();
                } catch (IOException ioexception1) {
                    ioexception1.printStackTrace();
                }
            }
        }
        return bitmap;

    }

    public static void releaseBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public static void releaseBitmap(ArrayList arraylist) {
        Iterator iterator = arraylist.iterator();
        do {
            Bitmap bitmap;
            do {
                if (!iterator.hasNext()) {
                    return;
                }
                bitmap = (Bitmap) iterator.next();
            } while (bitmap == null || bitmap.isRecycled());
            bitmap.recycle();
        } while (true);
    }

    public static boolean saveBitmap2file(Bitmap bitmap, String s, int i) {
        android.graphics.Bitmap.CompressFormat compressformat = android.graphics.Bitmap.CompressFormat.PNG;
        File file = new File(s);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fileoutputstream;
        try {
            fileoutputstream = new FileOutputStream(s);
        } catch (FileNotFoundException filenotfoundexception) {
            filenotfoundexception.printStackTrace();
            return false;
        }
        return bitmap.compress(compressformat, i, fileoutputstream);
    }

    public Bitmap returnBitMap(String s) {
        Bitmap bitmap = null;
        if (s == null || "".equals(s)) {
            return null;
        }


        try {
            URL url = new URL(s);
            HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setConnectTimeout(2000);
            httpurlconnection.setDoInput(true);
            httpurlconnection.connect();
            InputStream inputstream = httpurlconnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputstream);
            inputstream.close();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();

        }

        return bitmap;
    }

    public static class BitmapParam {

        public int height;
        public Bitmap mBitmap;
        public float scale;
        public int width;

        public BitmapParam() {
            scale = 1.0F;
        }
    }

    public static Bitmap loadHeadPortrait(Context mActivity) {
        Bitmap mBitmap = null;
        try {
            Bitmap bmp_t = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.img_sign_photo);
            //if user had not login or has logout,set default head portrait

            if(ApplicationHelper.getInstance().getLoginUser() == null)
                return bmp_t;
            UserInfoJson userInfoJson = ApplicationHelper.getInstance().getLoginUser();
            LogUtil.d("loadHeadPortrait",userInfoJson.toString());
            if(!FileUtils.isExist(Config.FACE_FILE_PATH+Config.FACE_FILE_NAME))
              return bmp_t;
            String url = Config.FACE_FILE_PATH+Config.FACE_FILE_NAME;

            if(!"0".equals(userInfoJson.getSsoSource()))
                url = userInfoJson.getImageUrl();
            Uri uri01 = Uri.fromFile(new File(url));

            ContentResolver contentProvider = mActivity.getContentResolver();

            int h = bmp_t.getHeight();
            mBitmap = BitmapFactory.decodeStream(contentProvider.openInputStream(uri01));
            mBitmap = Bitmap.createScaledBitmap(mBitmap, h, h, true);

            return mBitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
