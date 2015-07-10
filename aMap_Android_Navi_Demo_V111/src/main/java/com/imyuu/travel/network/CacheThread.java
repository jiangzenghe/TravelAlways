package com.imyuu.travel.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.util.Log;

import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.FileUtil;
import com.imyuu.travel.util.FileUtils;
import com.imyuu.travel.util.LogUtil;

/**
 * Download module, supports breakpoints download
 * 
 */
public class CacheThread extends Thread {
    static private final String TAG = "CacheThread";

    private String mPath;
    private long mDownloadSize;
    private int mTargetSize;
    private boolean mStop;
    private boolean mDownloading;
    private boolean mStarted;
    private boolean mError;
    private List<String> urls;
    public CacheThread(List<String> urls,String scenicId)
    {
        this.urls = urls;
        mPath = Config.CACHE_DIR+"/"+scenicId;
        if(!FileUtils.isExist(mPath))
            FileUtils.createDirectory(mPath);
    }
    @Override
    public void run() {
        mDownloading = true;
        for(String url:urls) {
            String filename = url.substring(url.lastIndexOf("/")+1);
            File file = new File(mPath+"/"+filename);

            if (file.exists()) {
               continue;
            }
            url = Config.IMAGE_SERVER_ADDR+url;
           // LogUtil.d("CacheThread",file.getAbsolutePath()+"url:"+url);
            download(url,file);
        }
    }

    /**
     * Start downloading thread
     */
    public void startThread() {
        if (!mStarted) {
            this.start();

            // Only start once
            mStarted = true;
        }
    }

    /**
     * Stop downloading thread
     */
    public void stopThread() {
        mStop = true;
    }

    /**
     * Are Downloading
     */
    public boolean isDownloading() {
        return mDownloading;
    }

    /**
     * Whether to download an exception
     *
     * @return
     */
    public boolean isError() {
        return mError;
    }

    public long getDownloadedSize() {
        return mDownloadSize;
    }

    /**
     * Whether the download was successful
     */
    public boolean isDownloadSuccessed() {
        return (mDownloadSize != 0 && mDownloadSize >= mTargetSize);
    }

    private void download(String mUrl,File destFile) {
        //Download success is closed

        InputStream is = null;
        FileOutputStream os = null;
        if (mStop) {
            return;
        }
        try {
            URL url = new URL(mUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setInstanceFollowRedirects(true);//Allows the redirection
            is = urlConnection.getInputStream();
            os = new FileOutputStream(destFile);
            Log.i(TAG, "download file:" + destFile);

            int len = 0;
            byte[] bs = new byte[4*1024];
            if (mStop) {
                return;
            }
            while (!mStop
                    && ((len = is.read(bs)) != -1)) {
                os.write(bs, 0, len);
                mDownloadSize += len;
            }
            os.flush();
        } catch (Exception e) {
            mError = true;
            Log.i(TAG, "download error:" + e.toString() + "");
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            Log.i(TAG, "mDownloadSize:" + mDownloadSize + ",mTargetSize:" + mTargetSize);
        }
    }
}