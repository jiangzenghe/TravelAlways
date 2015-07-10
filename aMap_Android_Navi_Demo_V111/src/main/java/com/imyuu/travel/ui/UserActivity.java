package com.imyuu.travel.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.api.StreamApiClient;
import com.imyuu.travel.model.Photo;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.model.UserInfoJson;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.FileUtils;
import com.imyuu.travel.util.ImageUtil;
import com.imyuu.travel.util.StringUtils;
import com.imyuu.travel.util.ToastUtil;
import com.imyuu.travel.view.RoundImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public abstract class UserActivity extends AppCompatActivity {


    private Activity mActivity;
    private String fileCreateTimeFlag;
    protected boolean uploadRequired =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
    }

    public abstract void loadPortrait(Bitmap bitmap);
    protected abstract void setDate(String dateStr);

    protected void uploadPortrait(UserInfoJson userInfoJson)
    {

        Photo headPhoto = new Photo();
        headPhoto.setUserId(userInfoJson.getUserId());
        final String image = Config.FACE_FILE_PATH + Config.FACE_FILE_NAME;
        TypedFile photo = new TypedFile("multipart/octet-stream", new File(image));
        headPhoto.setPath("head");
        Log.d("upload",image+"--"+photo.length());
        ByteArrayOutputStream bufout = new ByteArrayOutputStream((int) photo.length());
        try {
            photo.writeTo(bufout);
            headPhoto.setBuf(bufout.toByteArray());

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        final String imageUrl =userInfoJson.getUserId()+".jpg";
        headPhoto.setPath("image");
        StreamApiClient.getStreamInterfaceService().sendPhoto(headPhoto, new Callback<ServiceState>() {
            @Override
            public void success(ServiceState state, Response response) {

                ToastUtil.show(mActivity, "上传头像成功" );
                FileUtils.copyFile(image, Config.UU_FILEPATH, imageUrl);
                Intent intent = new Intent(getBaseContext(), UserHomeActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                ToastUtil.show(mActivity, "上传头像失败！");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(!FileUtils.isExist(Config.FACE_FILE_PATH))
            FileUtils.createDirectory(Config.FACE_FILE_PATH);
        if (resultCode != RESULT_CANCELED) {// 结果码不等于取消时候
            ContentResolver cr = this.getContentResolver();
            switch (requestCode) {
                case Config.IMAGE_REQUEST_CODE:
                    //todo 上传选择手机相机的拍摄的照片时，照片会有旋转，目前猜测与不同设备相关
                    Uri uri = data.getData();
                    try {
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.img_sign_photo);
                        int h = bmp.getHeight();
                        Bitmap bitmapUserLogo = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(cr.openInputStream(uri)), h, h, true);
                        if (FileUtils.existSDCard()) {
                            try {

                                //将选择的头像先写到临时目录下面，每次退出注册界面的时候都会清理掉这个临时目录
                                FileUtils.storeImageToSDCard(bitmapUserLogo,  Config.FACE_FILE_NAME,
                                        Config.FACE_FILE_PATH);
                                loadPortrait(bitmapUserLogo);
                            } catch (Exception e) {
                                e.printStackTrace();
                                break;
                            }

                        } else {
                            ToastUtil.show(mActivity,"图片选择错误");
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        break;
                    }
                    break;
                case Config.CAMERA_REQUEST_CODE:
                    if (FileUtils.existSDCard()) {
                        Bundle bundle = data.getExtras();
                        Bitmap bitmapUserLogo;
                        bitmapUserLogo = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.img_sign_photo);
                        int h = bmp.getHeight();
                        // bitmapUserLogo =  Bitmap.createScaledBitmap(bitmapUserLogo, h, h, true);
                        if (bitmapUserLogo != null) {
                            Log.d("bitmap","---"+bitmapUserLogo.getRowBytes());
                            File tempFile = new File(Config.FACE_FILE_PATH+ fileCreateTimeFlag + "_" + Config.FACE_FILE_NAME);
                            FileUtils.storeImageToSDCard(bitmapUserLogo,
                                    fileCreateTimeFlag + "_" + Config.FACE_FILE_NAME,Config.FACE_FILE_PATH);
                            /**
                             * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
                             */
                            int degree = ImageUtil.readPictureDegree(tempFile.getAbsolutePath());
                            BitmapFactory.Options opts = new BitmapFactory.Options();//获取缩略图显示到屏幕上
                            opts.inSampleSize = 2;
                            Bitmap cbitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), opts);
                            /**
                             * 把图片旋转为正的方向
                             */
                            bitmapUserLogo = Bitmap.createScaledBitmap(ImageUtil.rotaingImageView(degree, cbitmap), h, h, true);
                            FileUtils.storeImageToSDCard(bitmapUserLogo,  Config.FACE_FILE_NAME,
                                    Config.FACE_FILE_PATH);
                            loadPortrait(bitmapUserLogo);
                        }

                    } else {
                        ToastUtil.show(mActivity,"SD卡错误");
                    }
                    break;
                case Config.DEFAULT_REQUEST_CODE:
                    //选取卡通头像返回的图片资源ID
//                    int id = data.getIntExtra(Constants.DEFAULT_LOGO_SELECTED_KEY, -1);
//                    Bitmap bitmapUserLogo = BitmapFactory.decodeResource(getResources(), R.drawable.img_sign_photo);
//                    if (!(id == -1)) {
//                        Resources r = this.getResources();
//                        InputStream is = r.openRawResource(id);
//                        BitmapDrawable bmpDraw = new BitmapDrawable(r, is);
//                        bitmapUserLogo = bmpDraw.getBitmap();
//                    }
//                    if (FileUtil.existSDCard()) {
//                        try {
//                            saveNameUserLogo = fileCreateTimeFlag + "_" + Constants.IMAGE_FILE_NAME;
//                            savePathUserLogo = Environment.getExternalStorageDirectory() + Constants.IMAGE_FILE_PATH + fileCreateTimeFlag + "_" + Constants.IMAGE_FILE_NAME;
//                            FileUtil.storeImageToSDCard(bitmapUserLogo, fileCreateTimeFlag + "_" + Constants.IMAGE_FILE_NAME,
//                                    Environment.getExternalStorageDirectory() + Constants.IMAGE_FILE_PATH_TEMP);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            break;
//                        }
//                        imageSignupPhoto.setImageBitmap(bitmapUserLogo);
//                    } else {
//                        Toast.makeText(SignUpActivity.this, R.string.error_msg_no_sdcard, Toast.LENGTH_SHORT).show();
//                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    protected void selectBirthClick() {

        final Calendar c = Calendar.getInstance();
        int   year = c.get(Calendar.YEAR);
        int   month = c.get(Calendar.MONTH);
        int    day = c.get(Calendar.DAY_OF_MONTH);
        View view = View.inflate(getApplicationContext(), R.layout.layout_datepicker, null);
        final DatePicker datePicker = (DatePicker)view.findViewById(R.id.new_act_date_picker);
        datePicker.init(year, month, day, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(view);
        builder.setTitle("日期选择");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int arrive_year = datePicker.getYear();
                int arrive_month = datePicker.getMonth();
                int arrive_day = datePicker.getDayOfMonth();
                String dateStr = new StringBuilder()// Month is 0 based so add 1
                        .append(arrive_year).append("-").append(arrive_month+1).append("-").append(arrive_day).toString();
                setDate(dateStr);
            }
        });
        builder.show();

    }

    protected void takePhoto()
    {
        fileCreateTimeFlag = String.valueOf(System.currentTimeMillis());
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentFromCapture, Config.CAMERA_REQUEST_CODE);
        uploadRequired = true;
    }
    protected void setlectPhoto()
    {
        fileCreateTimeFlag = String.valueOf(System.currentTimeMillis());
        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*"); // 设置文件类型
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, Config.IMAGE_REQUEST_CODE);
        uploadRequired = true;
    }
}
