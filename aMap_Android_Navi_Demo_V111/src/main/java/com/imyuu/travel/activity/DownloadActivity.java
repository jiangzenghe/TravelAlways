package com.imyuu.travel.activity;
        import java.io.BufferedOutputStream;
        import java.io.File;
        import java.io.*;
        import java.util.Enumeration;
        import java.util.List;
        import java.util.zip.ZipEntry;
        import java.util.zip.ZipException;
        import java.util.zip.ZipInputStream;
         import java.util.zip.ZipFile;
        import android.app.Activity;
        import android.content.Context;
        import android.os.Bundle;
        import android.os.Environment;
        import android.os.Handler;
        import android.os.Message;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.imyuu.travel.R;
        import com.imyuu.travel.api.ApiClient;
        import com.imyuu.travel.model.CityInfoJson;
        import com.imyuu.travel.model.RecommendLine;
        import  com.imyuu.travel.network.*;
        import com.imyuu.travel.util.Config;
        import com.imyuu.travel.util.FileUtils;
        import com.imyuu.travel.util.JSonUtil;

        import retrofit.Callback;
        import retrofit.RetrofitError;
        import retrofit.client.Response;

public class DownloadActivity extends Activity {
     private TextView resultView;
    private ProgressBar progressBar;


    private int reportSuccess;
    /**
     * 当Handler被创建会关联到创建它的当前线程的消息队列，该类用于往消息队列发送消息
     * 消息队列中的消息由当前线程内部进行处理
     * 使用Handler更新UI界面信息。
     */
    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    progressBar.setProgress(msg.getData().getInt("size"));
                    float num = (float)progressBar.getProgress()/(float)progressBar.getMax();
                    int result = (int)(num*100);
                    resultView.setText(result+ "%");
                    break;
                case 2:
                    //显示下载成功信息
                    reportSuccess++;
                    if(reportSuccess>= Config.THREAD_NUM){
                        resultView.setText("100%");
                        Toast.makeText(DownloadActivity.this, "finsh", Toast.LENGTH_SHORT).show();
                        parseAndSaveJson("334",resultView);
                    }

                    break;
                case -1:
                    //显示下载错误信息
                    Toast.makeText(DownloadActivity.this, "", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        reportSuccess = 0;

        progressBar = (ProgressBar) this.findViewById(R.id.downloadbar);
        resultView = (TextView) this.findViewById(R.id.resultView);
        Button button = (Button) this.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //String path = downloadpathText.getText().toString();
                String path="http://www.imyuu.com/trip/allScenicScenicAreaAction.action";
               //download("334",getBaseContext(),myhandler);
                Log.d("ApiClient", "111-" );
                ApiClient.getIuuApiClient().getCityList(new Callback<List<CityInfoJson>>() {
                    @Override
                    public void success(List<CityInfoJson> rlineList, Response response) {
                        try {
                            Log.d("ApiClient", "111-" + response.getStatus());
                           for(CityInfoJson cityInfoJson:rlineList)
                           {
                               Log.d("ApiClient", "111-" + cityInfoJson.getPinyin());
                           }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {

                        //   consumeApiData();
                    }
                });
            }
        });
    }

    public String download(final String scenicId, Handler handler,ProgressBar progressBar ) {
        //String url= Config.SERVER_ADDR+"map/download.do?scenicId="+scenicId;
        String url= Config.MAP_SERVER_ADDR+scenicId+".zip";
          //开始下载文件
        File savePath= new File(Config.Map_FILEPATH,scenicId+".zip");
        download(url, savePath,progressBar,handler);
        return savePath.getAbsolutePath();
    }
    /**
     * 主线程(UI线程)
     * 对于显示控件的界面更新只是由UI线程负责，如果是在非UI线程更新控件的属性值，更新后的显示界面不会反映到屏幕上
     * 如果想让更新后的显示界面反映到屏幕上，需要用Handler设置。
     * @param url
     * @param saveFile
     */
    private void download(final String url, final File saveFile,final ProgressBar progressBar, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //开启3个线程进行下载
                FileDownloader loader = new FileDownloader(url, saveFile, Config.THREAD_NUM);
                progressBar.setMax(loader.getFileSize());//设置进度条的最大刻度为文件的长度

                try {
                    loader.download(new DownloadProgressListener() {
                        @Override
                        public void onDownloadSize(int size) {//实时获知文件已经下载的数据长度
                            Message msg = new Message();
                            msg.what = 1;
                            msg.getData().putInt("size", size);
                            if(handler != null)
                                handler.sendMessage(msg);//发送消息
                        }

                        @Override
                        public void onFinish()
                        {
                            Message msg = new Message();
                            msg.what = 2;
                            msg.getData().putInt("1", 1);
                            if(handler != null)
                               handler.sendMessage(msg);//发送消息

                        }
                    });
                } catch (Exception e) {
                    if(handler != null)
                        handler.obtainMessage(-1).sendToTarget();
                    Log.e("MainActivity", e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void parseAndSaveJson(final String scenicId,final TextView resultView)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File saveFile= new File(Config.Map_FILEPATH,scenicId+".zip");
                    FileUtils.unzip(saveFile, Config.NEW_FILEPATH+scenicId);
                    JSonUtil.parseScenicInfo(scenicId,getBaseContext());
                    resultView.setText("地图已离线");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
