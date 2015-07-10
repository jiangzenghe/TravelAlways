package com.imyuu.travel.ui;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;
import android.widget.ExpandableListView;

import com.imyuu.travel.R;
import com.imyuu.travel.adapters.MyExpandableAdapter;

import com.imyuu.travel.adapters.SearchResult;
import com.imyuu.travel.adapters.treeview.*;
import com.imyuu.travel.api.ApiClient;

import com.imyuu.travel.model.CityScenesJson;
import com.imyuu.travel.model.MapInfoModel;
import com.imyuu.travel.model.ProvinceCityJson;
import com.imyuu.travel.model.ProvinceInfoJson;
import com.imyuu.travel.model.ScenicAreaJson;
import com.imyuu.travel.util.LogUtil;
import com.imyuu.travel.util.PreferencesUtils;
import com.imyuu.travel.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapAddActivity  extends Activity {

   @InjectView(R.id.bt_add_map_search)
    EditText bt_search;

    private  List<ProvinceInfoJson>  provinceCityList;
    private Activity mActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_add);
        ButterKnife.inject(this);
        mActivity =this;

        provinceCityList = new ArrayList<ProvinceInfoJson>();
        loadData();
      //  initExpandableView();
       // initTreeView();
        EventBus.getDefault().register(this);
        closeInputMethod();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(String msg) {

    }
    private void closeInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
            imm.hideSoftInputFromWindow(bt_search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /** 树中的元素集合 */
    private ArrayList<Element> elements;
    /** 数据源元素集合 */
    private ArrayList<Element> elementsData;


    protected void initTreeView() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        init();

        ListView treeview = (ListView) findViewById(R.id.treeview);
        TreeViewAdapter treeViewAdapter = new TreeViewAdapter(this,
                elements, elementsData, inflater);
        TreeViewItemClickListener treeViewItemClickListener = new TreeViewItemClickListener(treeViewAdapter,this);
        treeview.setAdapter(treeViewAdapter);
        treeview.setOnItemClickListener(treeViewItemClickListener);
    }

    private void init() {

        elements = new ArrayList<Element>();
        elementsData = new ArrayList<Element>();
        List<MapInfoModel> mapList = MapInfoModel.load();
        //添加节点  -- 节点名称，节点level，节点id，父节点id，是否有子节点，是否展开
        int seq = 0;
        //添加最外层节点   //添加第一层节点

        for (ProvinceInfoJson provinceInfoJson:provinceCityList) {

            String province = provinceInfoJson.getProvince();
            Element e1 = new Element(province, Element.TOP_LEVEL, seq++, Element.NO_PARENT, true, false);
            elements.add(e1);
            elementsData.add(e1);
            List<ProvinceCityJson> cityList= provinceInfoJson.getCityList();
          //  Log.d("MapAdd", "city num " + province+cityList.size());
            int scenicNum = 0;
            for (ProvinceCityJson cityJson : cityList) {
                String cityName = cityJson.getCityName();
                //添加第二层节点
                Element e2 = new Element(cityName, Element.TOP_LEVEL + 1, seq++, e1.getId(), true, false);
                elementsData.add(e2);
                List<CityScenesJson> scenicList = cityJson.getSceneList();
                e2.setMapSize(scenicList.size() + "个景点");
                scenicNum += scenicList.size();
                for (CityScenesJson areaJson : scenicList) {
                    Element e3 = new Element(areaJson.getScenicName(), Element.TOP_LEVEL + 2, seq++, e2.getId(), false, false);
                    e3.setMapSize(areaJson.getMapSize());
                    e3.setScenicId(areaJson.getScenicId());

                    e3.setCanNav(areaJson.isCanNav());
                    areaJson.setCityName(cityName);
                    areaJson.save();
                    for (MapInfoModel mapInfoModel : mapList) {
                        if (areaJson.getScenicId().equals(mapInfoModel.getScenicId())) {
                            e3.setHasDownload(true);
                            break;
                        }
                    }
                    elementsData.add(e3);
                }
            }
            e1.setMapSize(scenicNum+"个景点");
        }
    }



    @OnClick(R.id.app_addmaptitle_cancel)
    public void onClick()
    {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
       // this.finish();
    }
    @OnClick(R.id.bt_add_map_search)
    public void searchClick() {
        if (bt_search.getText().toString().length() < 1)
            return;
        String keyword = bt_search.getText().toString();
        Log.d("bt_search", keyword);
        List<CityScenesJson> scenicAreaJsons = CityScenesJson.find(keyword);
        bt_search.setText("");
        if(null == scenicAreaJsons || scenicAreaJsons.size()<1) {
            ToastUtil.show(MapAddActivity.this, "未能查询到景区");
            return;
        }

        LogUtil.d(scenicAreaJsons.toString());
        SearchResult result = new SearchResult(MapAddActivity.this, scenicAreaJsons);
        result.show();
    }

    private void loadData()
    {
        ApiClient.getMapService().queryCityMap(new Callback<List<ProvinceInfoJson>>() {
            @Override
            public void success(List<ProvinceInfoJson> cityList, Response response) {
                if (!PreferencesUtils.getBoolean(mActivity, "MapLoaded")) {
                    PreferencesUtils.putBoolean(mActivity, "MapLoaded", true);
                } else {
                  //  scenicAreaList = ScenicAreaJson.load();
                }
                LogUtil.d("queryCityMap",""+cityList.size());
                provinceCityList = cityList;
                initTreeView();
            }

            @Override
            public void failure(RetrofitError error) {

                ToastUtil.show(mActivity, "景区加载失败，请检查网络" );
            }
        });
    }

}
