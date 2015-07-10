package com.imyuu.travel.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.imyuu.travel.R;
import com.imyuu.travel.adapters.CommentAdapter;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.callback.LoadFinishCallBack;
import com.imyuu.travel.model.ChatMessageJson;
import com.imyuu.travel.model.CommentsInfoJson;
import com.imyuu.travel.model.ScenicAreaJson;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.model.UserInfoJson;
import com.imyuu.travel.util.AndroidUtils;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.ToastUtil;
import com.imyuu.travel.view.AutoLoadRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CommentsWallActivity extends AppCompatActivity {
    @InjectView(R.id.recycler_view)
    AutoLoadRecyclerView mRecyclerView;
    @InjectView(R.id.ed_commentinfo)
    EditText  commentText;
    private CommentAdapter mAdapter;
   private ArrayList<CommentsInfoJson> data;
    private LoadFinishCallBack mLoadFinisCallBack;
    private String scenicId;
    private String scenicName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new ArrayList<CommentsInfoJson>();
        ScenicAreaJson scenic = (ScenicAreaJson) getIntent().getExtras().getSerializable("scenicInfo");
        if (scenic != null) {
            scenicId = scenic.getScenicId();
            scenicName = scenic.getScenicName();
        }
        setContentView(R.layout.activity_comments_wall);
        ButterKnife.inject(this);

        initView();
    }


    public void initView( ) {

     //   View view = getLayoutInflater().inflate(R.layout.activity_comments_wall, null, false);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLoadFinisCallBack = mRecyclerView;
        mAdapter = new CommentAdapter(getBaseContext(),scenicId,data,mLoadFinisCallBack);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.loadNextPage();
        mRecyclerView.setLoadMoreListener(new AutoLoadRecyclerView.onLoadMoreListener() {
            @Override
            public void loadMore() {
                mAdapter.loadNextPage();
            }
        });

    }



    @OnClick(R.id.image_comenmt_cancel)
    public void adviceFeedbackClick(View v) {

       this.finish();
    }

    @OnClick(R.id.ed_commentinfo)
    public void edcommentinfokClick(View v) {
      //check if the user had register,if not,jump to the regsiter page
        UserInfoJson userInfoJson =    ApplicationHelper.getInstance().getLoginUser() ;
        if(userInfoJson == null)
        {
            //show a dialog to tips user forward to register page
            Intent intent = new Intent(getBaseContext(), GreetingActivity.class);
            startActivity(intent);
        }
    }

   @OnClick(R.id.bt_savecomment)
    public void saveComments(View v)
   {
       UserInfoJson userInfoJson = ApplicationHelper.getInstance().getLoginUser();
       if(null == userInfoJson)
       {
           ToastUtil.show(this,"请先登录，才能发表留言。");
           return;
       }
      final CommentsInfoJson messageJson = new CommentsInfoJson();
       messageJson.setContent(commentText.getText().toString());
       messageJson.setScenicId(scenicId);
       messageJson.setUserId(userInfoJson.getUserId());
       messageJson.setUserName(userInfoJson.getNickName());
       messageJson.setGender(userInfoJson.getGender());
       messageJson.setAge(userInfoJson.getAge());
       messageJson.setRemark(scenicName);
       Log.d("sendSysMessage", "111-" + messageJson.toString());
       ApiClient.getSocialService().sendCommentsMessage(messageJson,new Callback<ServiceState>() {
           @Override
           public void success(ServiceState ss, Response response) {

               Log.d("sendSysMessage", "111-" + response.getStatus());
               messageJson.setCommentTime(AndroidUtils.getLongDate());
               messageJson.save();
               mAdapter.notifyUpdate(messageJson);
               commentText.setText("");
               //commentText.setInputType(InputType.TYPE_NULL); // 关闭软键盘
               ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                       CommentsWallActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

               ToastUtil.show(getBaseContext(), "留言成功" );
           }

           @Override
           public void failure(RetrofitError retrofitError) {
               retrofitError.printStackTrace();
               ToastUtil.show(getBaseContext(), "留言失败" );
           }
       });
   }

}
