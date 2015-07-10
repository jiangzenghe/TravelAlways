package com.imyuu.travel.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.model.CityInfoJson;
import com.imyuu.travel.model.MapInfoModel;
import com.imyuu.travel.network.DownloadProgressListener;
import com.imyuu.travel.network.FileDownloader;
import com.imyuu.travel.util.AndroidUtils;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.FileUtils;
import com.imyuu.travel.util.JSonUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DownloadActivity extends Activity {

    public String download(final String scenicId, Handler handler) {
        //String url= Config.SERVER_ADDR+"map/download.do?scenicId="+scenicId;
        String url = Config.MAP_SERVER_ADDR + scenicId + ".zip";
        //开始下载文件
        File saveFile = new File(Config.Map_FILEPATH, scenicId + ".zip");
        if(saveFile.exists())
            saveFile.delete();
        //  Log.d("download",url);

        download(url,saveFile, handler);
        return saveFile.getAbsolutePath();
    }

    public void download(String canNav,final String scenicId, Handler handler) {
       if("1".equals(canNav))
       {
         download(scenicId,handler);
       }
       else
       {
           String url = Config.OLDMAP_SERVER_ADDR + "scenic"+scenicId + ".zip";
           //开始下载文件
           File saveFile = new File(Config.Map_FILEPATH, scenicId + ".zip");
           if(saveFile.exists())
               saveFile.delete();
           //  Log.d("download",url);
           download(url,saveFile, handler);
       }
    }

    /**
     * 主线程(UI线程)
     * 对于显示控件的界面更新只是由UI线程负责，如果是在非UI线程更新控件的属性值，更新后的显示界面不会反映到屏幕上
     * 如果想让更新后的显示界面反映到屏幕上，需要用Handler设置。
     *

     */
    protected void download(final String url,final File saveFile,final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //开启3个线程进行下载
                final FileDownloader loader = new FileDownloader(url, saveFile, Config.THREAD_NUM);
                //progressBar.setMax(loader.getFileSize());//设置进度条的最大刻度为文件的长度
                try {
                    loader.download(new DownloadProgressListener() {
                        @Override
                        public void onDownloadSize(int size) {//实时获知文件已经下载的数据长度
                            Message msg = new Message();
                            msg.what = 1;
                            int num =  (int) (size*1.0/loader.getFileSize() * 100);
                            msg.getData().putInt("size", num);

                            if (handler != null)
                                handler.sendMessage(msg);//发送消息
                        }

                        @Override
                        public void onFinish() {
                            Message msg = new Message();
                            msg.what = 2;
                            msg.getData().putInt("1", 1);
                            if (handler != null)
                                handler.sendMessage(msg);//发送消息

                        }
                    });
                } catch (Exception e) {
                    if (handler != null)
                        handler.obtainMessage(-1).sendToTarget();
                    Log.e("MainActivity", e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void parseAndSaveJson(final String scenicId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long start = System.currentTimeMillis();
                    File saveFile = new File(Config.Map_FILEPATH, scenicId + ".zip");
                    FileUtils.unzip(saveFile, Config.UU_FILEPATH + scenicId);
                    MapInfoModel mapInfoModel = new MapInfoModel();
                    mapInfoModel.setScenicId(scenicId);
                    JSonUtil.parseScenicInfo(scenicId, mapInfoModel,getBaseContext());
                    double fileSize = 1.0*saveFile.length()/1000000;
                    mapInfoModel.setFileSize(new DecimalFormat("0.00").format(fileSize)+"M");
                    mapInfoModel.setFilePath(scenicId + ".zip");
                    mapInfoModel.setCanNav("1");
                    mapInfoModel.setDownloadTime(AndroidUtils.getShortDate());
                    mapInfoModel.save();
                    saveFile.delete();
                    EventBus.getDefault().post("finshed");
                    long end = System.currentTimeMillis();
                    Log.d("parseAndSaveJson",mapInfoModel+ "time elpased:" + (end - start));
                    // resultView.setText("地图已离线");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
