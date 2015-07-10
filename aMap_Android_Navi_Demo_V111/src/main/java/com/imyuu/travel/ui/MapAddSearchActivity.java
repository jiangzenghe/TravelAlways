package com.imyuu.travel.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.imyuu.travel.R;

import com.imyuu.travel.adapters.treeview.Element;
import com.imyuu.travel.adapters.treeview.TreeViewAdapter;
import com.imyuu.travel.adapters.treeview.TreeViewItemClickListener;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.model.MapInfoModel;
import com.imyuu.travel.model.ScenicAreaJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapAddSearchActivity extends MapAddActivity {


    private Activity mActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        ButterKnife.inject(this);
        mActivity =this;
        String keyword = getIntent().getStringExtra("keyword");
        this.searchClick(keyword);
    }

    @OnClick(R.id.app_addmaptitle_cancel)
    public void onClick()
    {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
       // this.finish();
    }

    public void searchClick(String keyword) {

                 List<ScenicAreaJson> scenicAreaJsons =   ScenicAreaJson.find(keyword);
              //   parse(scenicAreaJsons);
                 initTreeView();


    }





}
