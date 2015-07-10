package com.imyuu.travel.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.imyuu.travel.R;
import com.imyuu.travel.activity.DownloadActivity;
import com.imyuu.travel.activity.DownloadOldActivity;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.model.Food;
import com.imyuu.travel.model.MapInfoModel;
import com.imyuu.travel.model.Recommend;
import com.imyuu.travel.model.ScenicAreaJson;
import com.imyuu.travel.model.ScenicDetailJson;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.ui.CommentsWallActivity;

import com.imyuu.travel.ui.MapOfflineActivity;
import com.imyuu.travel.ui.MapOldActivity2;
import com.imyuu.travel.ui.MapOnlineActivity;
import com.imyuu.travel.ui.ScenicAreaActivity;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.ConstantsOld;
import com.imyuu.travel.util.FileUtils;
import com.imyuu.travel.util.FrescoFactory;
import com.imyuu.travel.util.LogUtil;
import com.imyuu.travel.util.ToastUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailFragments extends Fragment {

    @InjectView(R.id.download_progress)
    ProgressBar progressBar;
    @InjectView(R.id.text_download)
    TextView resultView;
    @InjectView(R.id.bt_detailfavor)
    Button bt_favor;
    @InjectView(R.id.bt_comments)
    Button bt_comments;
    @InjectView(R.id.bt_enter)
    Button bt_enter;
    String scenicId;
    DownloadActivity   downloadActivity = null;
    private String TAG = "DetailFragment";
    private ScenicSmallAdaptor adaptor;

    private RecyclerView recyclerView;
    private ScenicAreaJson scenicAreaJson;
    private int reportSuccess;
    private Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    progressBar.setProgress(msg.getData().getInt("size"));
                    resultView.setText(msg.getData().getInt("size") + "%");
                    break;
                case 2:
                    //显示下载成功信息
                    reportSuccess++;
                    if (reportSuccess >= Config.THREAD_NUM) {
                        resultView.setText("100%");
                        resultView.setText("数据解析中");
                        downloadActivity.parseAndSaveJson(scenicAreaJson.getScenicId());
                        resultView.setText("已离线");
                        onEventMainThread("");
                    }

                    break;
                case -1:
                    //显示下载错误信息
                    Toast.makeText(getActivity(), "下载错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment, container, false);
        ButterKnife.inject(this, view);

        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.sdv_image);
        Intent intent = getActivity().getIntent();
        String url = intent.getStringExtra("URL");
        scenicId = intent.getStringExtra("scenicId");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_scincsmall);
        initData(scenicId);

        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url))
                .setProgressiveRenderingEnabled(true)
                .build();
        simpleDraweeView.setDrawingCacheEnabled(true);
        PipelineDraweeController controller =  (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(simpleDraweeView.getController())
                .build();

        simpleDraweeView.setController(controller);
        //FrescoFactory.setDraweewithController(simpleDraweeView, url);
        return view;
    }

    private void initData(final String scenicId) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        adaptor = new ScenicSmallAdaptor(new ArrayList<Recommend>());
        recyclerView.setAdapter(adaptor);

        ApiClient.getIuuApiClient().queryScenicDetail(scenicId, new Callback<ScenicDetailJson>() {
            @Override
            public void success(ScenicDetailJson scenicDetailJson, Response response) {
                if( scenicDetailJson == null)
                    return;
                initScenicDetail(scenicDetailJson, scenicId);
                adaptor.data.addAll(scenicDetailJson.getRecommendScenicList());
                for(Recommend recommend:scenicDetailJson.getRecommendScenicList())
                {
                    recommend.setScenicId(scenicId);
                    recommend.save();
                    Log.d("Recommend",recommend.toString());
                }
                adaptor.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                scenicAreaJson = ScenicAreaJson.load(scenicId);
                resultView.setText("未知");
                bt_favor.setText(""+scenicAreaJson.getFavourNum());
                bt_comments.setText("\t留言墙("+scenicAreaJson.getCommentsNum()+")");
                adaptor.data.clear();
                adaptor.data.addAll(Recommend.load(scenicId));
                adaptor.notifyDataSetChanged();
                Toast.makeText(getActivity(), "加载景区详情失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initScenicDetail(ScenicDetailJson scenicDetailJson, String scenicId) {
        scenicAreaJson = new ScenicAreaJson();
        scenicAreaJson.setScenicId(scenicId);
        scenicAreaJson.setScenicName(scenicDetailJson.getScenicName());
        scenicAreaJson.setImageUrl(scenicDetailJson.getImageUrl());
        scenicAreaJson.setLat(scenicDetailJson.getLat());
        scenicAreaJson.setLng(scenicDetailJson.getLng());
        scenicAreaJson.setRight_lat(scenicDetailJson.getRight_lat());
        scenicAreaJson.setRight_lng(scenicDetailJson.getRight_lng());
        scenicAreaJson.setCanNavi(scenicDetailJson.getCanNavi());
        scenicAreaJson.setMapSize(scenicDetailJson.getMapSize());
        // to be continued
        if (!MapInfoModel.isDownload(scenicId))
            resultView.setText(scenicDetailJson.getMapSize());
        else {
            resultView.setText("已下载");
        }
        bt_favor.setText(""+scenicDetailJson.getFavourNum());
        bt_comments.setText("\t留言墙("+scenicDetailJson.getCommentsNum()+")");
        if(scenicAreaJson.getCanNavi().equals("0")) {
            if (!FileUtils.isExist(Config.UU_FILEPATH + "scenic" + scenicId + "/scenic" + scenicId + "_files"))
            {
                bt_enter.setText("下载导游地图("+scenicAreaJson.getMapSize()+")");
                bt_enter.setBackground(getResources().getDrawable(R.drawable.round_button_enter_blue));
            }
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(String msg) {

      LogUtil.d(TAG,msg.toString());
        if(scenicAreaJson.getCanNavi().equals("0")) {
            if (FileUtils.isExist(Config.UU_FILEPATH + "scenic" + scenicId + "/scenic" + scenicId + "_files"))
            {
                bt_enter.setText("进入导游地图");
                bt_enter.setBackground(getResources().getDrawable(R.drawable.round_button_enter));
                resultView.setText("已下载");
            }
        }
    }
    private void showDownloadDialog(final Activity ctx)
    {
        new AlertDialog.Builder(ctx).setTitle("提示")
                .setMessage("尚未下载景区地图，确定开始下载吗?")
                .setIcon(R.drawable.icon_sys_warning)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        downloadActivity = new DownloadOldActivity(ctx);
                        downloadActivity.download(scenicAreaJson.getScenicId(), myhandler);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //取消按钮事件

                    }
                })
                .show();
    }

    @OnClick(R.id.bt_enter)
    public void enterClick() {
        Log.d(TAG, "enterClick"+scenicAreaJson);
        if(scenicAreaJson == null) return;
         if(scenicAreaJson.getCanNavi().equals("0")) {
            if(!FileUtils.isExist(Config.UU_FILEPATH+"scenic"+scenicId+"/scenic"+scenicId+"_files")) {
               // ToastUtil.show(getActivity(), "请先下载地图~");
               //
                showDownloadDialog(getActivity());
                return;
            }
            Intent intent = new Intent(getActivity(), MapOldActivity2.class);

             intent.putExtra("scenicInfo",scenicAreaJson);
            startActivity(intent);
        } else if (scenicAreaJson.getCanNavi().equals("1")
                && FileUtils.isExist(Config.UU_FILEPATH + scenicId + "/tiles")){
            Intent intent = new Intent(getActivity(),MapOfflineActivity.class);
            intent.putExtra("scenicInfo",scenicAreaJson);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(),MapOnlineActivity.class);
            intent.putExtra("scenicInfo",scenicAreaJson);
            startActivity(intent);
        }
    }

    @OnClick(R.id.bt_comments)
    public void enterCommentsWall() {
        Log.d(TAG, "enterClick");
        Intent intent = new Intent(getActivity(), CommentsWallActivity.class);
        intent.putExtra("scenicInfo", scenicAreaJson);
        startActivity(intent);
    }


    //
    @OnClick(R.id.bt_detailfavor)
    public void bt_favorClick() {
        final String favor_num = bt_favor.getText().toString();
        if(null == ApplicationHelper.getInstance().getLoginUser())
        {
            Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
            return ;
        }
        String userId = ApplicationHelper.getInstance().getLoginUser().getUserId();
        LogUtil.d("1111", "bt_favorClick");

        ApiClient.getSocialService().sendFavors(scenicId, userId, new Callback<ServiceState>() {
            @Override
            public void success(ServiceState state, Response response) {
                if (state.getStateCode() == 0)
                    bt_favor.setText("" + (Integer.parseInt(favor_num) + 1));
                else
                    Toast.makeText(getActivity(), "不能重复点赞", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "点赞失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.button_download)
    public void downloadClick() {
//        downloadActivity = new DownloadActivity();
//        progressBar.setMax(100);
//        downloadActivity.download(scenicAreaJson.getScenicId(), myhandler );


            if(scenicAreaJson.getCanNavi().equals("1")) {
                downloadActivity = new DownloadActivity();
                downloadActivity.download(scenicAreaJson.getScenicId(), myhandler);

            } else {
                downloadActivity = new DownloadOldActivity(getActivity());
                downloadActivity.download(scenicAreaJson.getScenicId(), myhandler);

            }


    }

    /**
     * 推荐等ViewHolder
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_ecommend_name;
        private SimpleDraweeView simpleDraweeView_recommend_pic;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_ecommend_name = (TextView) itemView.findViewById(R.id.item_tv_scenicdetailname);

            simpleDraweeView_recommend_pic = (SimpleDraweeView) itemView.findViewById(R.id.item_smp_scenicdetail);
        }
    }

    class ScenicSmallAdaptor extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<Recommend> data;

        public ScenicSmallAdaptor(ArrayList<Recommend> data) {
            this.data = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_scenicdetail_small, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Recommend recommend = data.get(position);
            holder.tv_ecommend_name.setText(data.get(position).getName());
            //holder.simpleDraweeView_recommend_pic.setImageURI(Uri.parse(Config.IMAGE_SERVER_ADDR + recommend.getImageUrl()));
            FrescoFactory.setDraweewithController( holder.simpleDraweeView_recommend_pic,Config.IMAGE_SERVER_ADDR + recommend.getImageUrl());

            holder.simpleDraweeView_recommend_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //     Log.v("----tule----", scenicAreaJson.getId());
                    Intent intent = new Intent(getActivity(), ScenicAreaActivity.class);
                    intent.putExtra("URL", Config.IMAGE_SERVER_ADDR + recommend.getImageUrl());
                    intent.putExtra("scenicId", recommend.getIntentLink());
                    intent.putExtra("scenicName", recommend.getName());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (data != null)
                return data.size();
            return 0;
        }
    }
}
