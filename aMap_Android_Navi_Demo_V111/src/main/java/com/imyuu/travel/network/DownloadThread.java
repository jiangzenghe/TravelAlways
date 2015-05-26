package com.imyuu.travel.network;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载线程类
 */
public class DownloadThread extends Thread {
    private static final String TAG = "DownloadThread";
    private File saveFile;
    private URL downUrl;
    private int block;

    /* 下载开始位置  */
    private int threadId = -1;
    private int downLength;
    private boolean finish = false;
    private FileDownloader downloader = null;

    /**
     * @param downloader:下载器
     * @param downUrl:下载地址
     * @param saveFile:下载路径
     */
    public DownloadThread(FileDownloader downloader, URL downUrl, File saveFile, int block, int downLength, int threadId) {
        this.downUrl = downUrl;
        this.saveFile = saveFile;
        this.block = block;
        this.downloader = downloader;
        this.threadId = threadId;
        this.downLength = downLength;
        this.finish = false;
    }

    /**
     * 打印日志信息
     *
     * @param msg
     */
    private static void print(String msg) {
        Log.i(TAG, msg);
    }

    @Override
    public void run() {

        try {
            //使用Get方式下载
            HttpURLConnection http = (HttpURLConnection) downUrl.openConnection();
            http.setConnectTimeout(3 * 1000);
            http.setRequestMethod("GET");
            http.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            http.setRequestProperty("Accept-Language", "zh-CN");
            http.setRequestProperty("Referer", downUrl.toString());
            http.setRequestProperty("Charset", "UTF-8");

            int startPos = block * (threadId - 1) + downLength;//开始位置
            int endPos = block * threadId - 1;//结束位置
            http.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);//设置获取实体数据的范围
            http.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            http.setRequestProperty("Connection", "Keep-Alive");

            InputStream inStream = http.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inStream);

            byte buffer[] = new byte[8 * 1024];
            long startTime = System.currentTimeMillis();
            //	File temp = new File(saveFile.getAbsolutePath()+ saveFile.getName()+threadId);

            int offset = 0;
            print("Thread " + this.threadId + " start download from position " + startPos + "--" + saveFile.getCanonicalPath());
            // ByteArrayOutputStream out  = new ByteArrayOutputStream();
            RandomAccessFile threadfile = new RandomAccessFile(saveFile, "rwd");
            threadfile.seek(startPos);
            while (downLength < block && (offset = bis.read(buffer, 0, buffer.length)) != -1) {
                threadfile.write(buffer, 0, offset);
                //out.write(buffer,0,offset);
                downloader.append(offset);
            }
            //threadfile.write(out.toByteArray());
            threadfile.close();
            //  out.close();
            bis.close();
            http.disconnect();

            downloader.notifyOnFinish();
            long endTime = System.currentTimeMillis();
            print("Thread " + this.threadId + " download finish" + (endTime - startTime));
            this.finish = true;
        } catch (Exception e) {
            this.downLength = -1;
            print("Thread " + this.threadId + ":" + e);
        }

    }

    /**
     * 下载是否完成
     *
     * @return
     */
    public boolean isFinish() {
        return finish;
    }

    /**
     * 已经下载的内容大小
     *
     * @return 如果返回值为-1,代表下载失败
     */
    public long getDownLength() {
        return downLength;
    }
}
