package com.imyuu.travel.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.api.StreamApiClient;
import com.imyuu.travel.model.Photo;
import com.imyuu.travel.model.ReportJson;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.model.UserInfoJson;
import com.imyuu.travel.util.Config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class TestActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        upload();
//        UserInfoJson model = new UserInfoJson();
//        model.setLoginName("jack jos");
//        model.setNickName("jack");
//        model.setGender("man");
//        model.setUserId("11111");
//        model.setPassword("394");
//        model.setPlatform(0);
//        ReportJson messageJson = new ReportJson();
//        messageJson.setUserId("111");
//        messageJson.setTelno("111111");
//        messageJson.setMessage("sdfasdfasdfasdfasd");
//        messageJson.setReportType("非法");
//        ApiClient.getUserService().userRegister(model, new Callback<ServiceState>() {
//            @Override
//            public void success(ServiceState ss, Response response) {
//
//                Log.d("ApiClient", "111-" + response.getStatus());
//                Log.d("ApiClient", "111-" + ss);
//
//            }
//
//            @Override
//            public void failure(RetrofitError retrofitError) {
//                retrofitError.printStackTrace();
//                //consumeApiData();
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void upload() {
        // FileUpload Service service = ServiceGenerator.createService(FileUpload.class, FileUpload.BASE_URL);
        String image = Config.FACE_FILE_PATH + Config.FACE_FILE_NAME;
        TypedFile photo = new TypedFile("multipart/octet-stream", new File(image));
        String userId = "600";
        Photo dfile = new Photo();
        dfile.setPath("crash");
        dfile.setUserId(userId);
        ByteArrayOutputStream bufout = new ByteArrayOutputStream((int) photo.length());
        try {
            photo.writeTo(bufout);
            dfile.setBuf(bufout.toByteArray());

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        StreamApiClient.getStreamInterfaceService().sendCrash(dfile, new Callback<ServiceState>() {
            @Override
            public void success(ServiceState s, Response response) {
                Log.d("ApiClient", "success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ApiClient", "error");
                error.printStackTrace();
            }
        });

    }


}
