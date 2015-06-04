package com.imyuu.travel.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import android.app.Activity;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import  com.imyuu.travel.bean.City;
import com.imyuu.travel.adapters.PinnedHeaderAdapter;
import com.imyuu.travel.bean.ScenicModel;
import com.imyuu.travel.model.CityInfoJson;
import com.imyuu.travel.model.ScenicPointJson;
import com.imyuu.travel.util.CityScenicUtils;
import com.imyuu.travel.util.MarkerUtilsFor2D;
import com.imyuu.travel.view.GridView;
import com.imyuu.travel.view.IndexBarView;
import com.imyuu.travel.view.PinnedHeaderListView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 搜索功能
 *
 * @author 
 * 
 * <p>Modification History:</p>
 * <p>Date         Author      Description</p>
 * <p>      New  </p>
 * <p>  </p>
 */
public class QueryCityActivity extends Activity{
	
		ArrayList<City> original_items;
		
		// unsorted list items pinyin
		ArrayList<String> mItems;

		// array list to store section positions
		ArrayList<Integer> mListSectionPos;

		// array list to store listView data
		ArrayList<String> mListItems;
		
		// array list to store listView data pinyin
		ArrayList<String> mListShowItems;

		// custom list view with pinned header
		PinnedHeaderListView mListView;

		// custom adapter
		PinnedHeaderAdapter mAdaptor;

		// search box
		EditText mSearchView;

		GridView mGridView;

		// loading view
		ProgressBar mLoadingView;

		// empty view
		TextView mEmptyView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);// 显示返回箭头
//		actionBar.setDisplayShowHomeEnabled(false);
//		actionBar.setTitle("                        "+"选择城市");
		// UI elements
		setupViews();

		getCitys(savedInstanceState);
//		original_items = CityScenicUtils.createCities();
		// Array to ArrayList
//		mItems = new ArrayList<String>(Arrays.asList(ITEMS));

	}

	private void getCitys(final Bundle savedInstanceState) {
		ApiClient.getIuuApiClient().getCityList(new Callback<List<CityInfoJson>>() {
			@Override
			public void success(List<CityInfoJson> resultJson, Response response) {
				Toast.makeText(QueryCityActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
				if (resultJson == null) {
					Toast.makeText(QueryCityActivity.this, "结果为空", Toast.LENGTH_SHORT).show();
				}

				original_items = new ArrayList<City>();
				for(CityInfoJson each:resultJson) {
					City object0 = new City(each.getCityname(), each.getPinyin());
					original_items.add(object0);
				}

				mItems = new ArrayList<String>();
				for(City each:original_items) {
					mItems.add(each.getCityPY());
				}
				mListSectionPos = new ArrayList<Integer>();
				mListItems = new ArrayList<String>();
				mListShowItems = new ArrayList<String>();
				// for handling configuration change
				if (savedInstanceState != null) {
					mListItems = savedInstanceState.getStringArrayList("mListItems");
					mListSectionPos = savedInstanceState.getIntegerArrayList("mListSectionPos");
					mListShowItems = savedInstanceState.getStringArrayList("mListShowItems");
					if (mListItems != null && mListItems.size() > 0 && mListSectionPos != null && mListSectionPos.size() > 0) {
						setListAdaptor();
					}

					String constraint = savedInstanceState.getString("constraint");
					if (constraint != null && constraint.length() > 0) {
						mSearchView.setText(constraint);
						setIndexBarViewVisibility(constraint);
					}
				} else {
					new Poplulate().execute(mItems);
				}
			}

			@Override
			public void failure(RetrofitError error) {
				Toast.makeText(QueryCityActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void getHotCitys() {
		ApiClient.getIuuApiClient().getHotCityList(new Callback<List<CityInfoJson>>() {
			@Override
			public void success(List<CityInfoJson> resultJson, Response response) {
				Toast.makeText(QueryCityActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
				if (resultJson == null) {
					Toast.makeText(QueryCityActivity.this, "结果为空", Toast.LENGTH_SHORT).show();
				}
				ArrayList<CityInfoJson> hotCitys = new ArrayList<CityInfoJson>();
				hotCitys = (ArrayList<CityInfoJson>)resultJson;
				final CityAdapter adapter = new CityAdapter(QueryCityActivity.this, hotCitys);
				mGridView.setAdapter(adapter);
				mGridView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent intent = new Intent();
						CityInfoJson bean = (CityInfoJson)mGridView.getAdapter().getItem(position);
						City city = new City(bean.getCityname(), bean.getPinyin());

						intent.putExtra("cityResult", city);
						setResult(-1, intent);
						finish();
					}
				});
			}

			@Override
			public void failure(RetrofitError error) {
				Toast.makeText(QueryCityActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void setupViews() {
		setContentView(R.layout.main_act);
		mSearchView = (EditText) findViewById(R.id.search_view);
		mGridView = (GridView) findViewById(R.id.hot_citys);
		mLoadingView = (ProgressBar) findViewById(R.id.loading_view);
		mListView = (PinnedHeaderListView) findViewById(R.id.list_view);
		mEmptyView = (TextView) findViewById(R.id.empty_view);
		getHotCitys();
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("cityResult", getCity((String)mListView.getAdapter().getItem(arg2)));
				setResult(-1, intent);
				finish();
			}
			
		});
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		mSearchView.addTextChangedListener(filterTextWatcher);
		super.onPostCreate(savedInstanceState);
	}
	
	private TextWatcher filterTextWatcher = new TextWatcher() {
		public void afterTextChanged(Editable s) {
			String str = s.toString();
			if (mAdaptor != null && str != null)
				mAdaptor.getFilter().filter(str);
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}
	};
	
	public class ListFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			// NOTE: this function is *always* called from a background thread,
			// and
			// not the UI thread.
			String constraintStr = constraint.toString().toLowerCase(Locale.getDefault());
			FilterResults result = new FilterResults();

			if (constraint != null && constraint.toString().length() > 0) {
				ArrayList<String> filterItems = new ArrayList<String>();

				synchronized (this) {
					for (String item : mItems) {
						if (item.toLowerCase(Locale.getDefault()).startsWith(constraintStr)) {
							filterItems.add(item);
						}
						if(getCityName(item).contains(constraintStr)) {
							filterItems.add(item);
						}
					}
					result.count = filterItems.size();
					result.values = filterItems;
				}
			} else {
				synchronized (this) {
					result.count = mItems.size();
					result.values = mItems;
				}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			ArrayList<String> filtered = (ArrayList<String>) results.values;
			setIndexBarViewVisibility(constraint.toString());
			// sort array and extract sections in background Thread
			new Poplulate().execute(filtered);
		}

	}
	
	private void setIndexBarViewVisibility(String constraint) {
		// hide index bar for search results
		if (constraint != null && constraint.length() > 0) {
			mListView.setIndexBarVisibility(false);
		} else {
			mListView.setIndexBarVisibility(true);
		}
	}
	
	// sort array and extract sections in background Thread here we use
	// AsyncTask
	private class Poplulate extends AsyncTask<ArrayList<String>, Void, Void> {

		private void showLoading(View contentView, View loadingView, View emptyView) {
			contentView.setVisibility(View.GONE);
			loadingView.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
		}

		private void showContent(View contentView, View loadingView, View emptyView) {
			contentView.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
			emptyView.setVisibility(View.GONE);
		}

		private void showEmptyText(View contentView, View loadingView, View emptyView) {
			contentView.setVisibility(View.GONE);
			loadingView.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPreExecute() {
			// show loading indicator
			showLoading(mListView, mLoadingView, mEmptyView);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(ArrayList<String>... params) {
			mListItems.clear();
			mListShowItems.clear();
			mListSectionPos.clear();
			ArrayList<String> items = params[0];
			if (mItems.size() > 0) {

				// NOT forget to sort array
				Collections.sort(items, new SortIgnoreCase());

				String prev_section = "";
				for (String current_item : items) {
//					String current_item = current_city.getPName();
					String current_section = current_item.substring(0, 1).toUpperCase(Locale.getDefault());

					if (!prev_section.equals(current_section)) {
						mListItems.add(current_section);
						mListItems.add(current_item);
						mListShowItems.add(current_section);
						mListShowItems.add(getCityName(current_item));
						// array list of section positions
						mListSectionPos.add(mListItems.indexOf(current_section));
						prev_section = current_section;
					} else {
						mListItems.add(current_item);
						mListShowItems.add(getCityName(current_item));
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (!isCancelled()) {
				if (mListItems.size() <= 0) {
					showEmptyText(mListView, mLoadingView, mEmptyView);
				} else {
					setListAdaptor();
					showContent(mListView, mLoadingView, mEmptyView);
				}
			}
			super.onPostExecute(result);
		}
		
	}
	
	private String getCityName(String pinyin) {
		String result = "";
		for(City each: original_items) {
			if(each.getCityPY().equals(pinyin)) {
				result = each.getCityName();
				break;
			}
		}
		return result;
	}
	
	private City getCity(String pinyin) {
		City result = new City();
		for(City each: original_items) {
			if(each.getCityPY().equals(pinyin)) {
				result = each;
				break;
			}
		}
		return result;
	}
	
	private void setListAdaptor() {
		// create instance of PinnedHeaderAdapter and set adapter to list view
		mAdaptor = new PinnedHeaderAdapter(this, mListItems, mListShowItems, mListSectionPos);
		mListView.setAdapter(mAdaptor);

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		// set header view
		View pinnedHeaderView = inflater.inflate(R.layout.section_row_view, mListView, false);
		mListView.setPinnedHeaderView(pinnedHeaderView);

		// set index bar view
		IndexBarView indexBarView = (IndexBarView) inflater.inflate(R.layout.index_bar_view, mListView, false);
		indexBarView.setData(mListView, mListItems, mListSectionPos);
		mListView.setIndexBarView(indexBarView);

		// set preview text view
		View previewTextView = inflater.inflate(R.layout.preview_view, mListView, false);
		mListView.setPreviewView(previewTextView);

		// for configure pinned header view on scroll change
		mListView.setOnScrollListener(mAdaptor);
	}
	
	public class SortIgnoreCase implements Comparator<String> {
		public int compare(String s1, String s2) {
			return s1.compareToIgnoreCase(s2);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		 case android.R.id.home:
		        finish(); 
		        return true; 
		default:
			return super.onOptionsItemSelected(item);
		}
	}	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (mListItems != null && mListItems.size() > 0) {
			outState.putStringArrayList("mListItems", mListItems);
		}
		if (mListShowItems != null && mListShowItems.size() > 0) {
			outState.putStringArrayList("mListShowItems", mListShowItems);
		}
		if (mListSectionPos != null && mListSectionPos.size() > 0) {
			outState.putIntegerArrayList("mListSectionPos", mListSectionPos);
		}
		String searchText = mSearchView.getText().toString();
		if (searchText != null && searchText.length() > 0) {
			outState.putString("constraint", searchText);
		}
		super.onSaveInstanceState(outState);
	}
}

class CityAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	public CityAdapter(Activity activity, ArrayList<CityInfoJson> data) {
		this.data = data;
		mInflater = LayoutInflater.from(activity);
	}

	public CityAdapter(Context context){
		this.mInflater = LayoutInflater.from(context);
	}
	private ArrayList<CityInfoJson> data;

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	 @Override
	 public long getItemId(int arg0) {
	 // TODO Auto-generated method stub
		 return arg0;
	 }

	@SuppressLint("SdCardPath")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			//一张图片的布局
			view = mInflater.inflate(R.layout.item_hot_city, null);
			mHolder = new ViewHolder();
			mHolder.center_title = (TextView)view.findViewById(R.id.city_name);

			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		//获取position对应的数据
		CityInfoJson bean = (CityInfoJson)getItem(position);
		mHolder.center_title.setText(bean.getCityname());

		return view;

	}

	static class ViewHolder {
		TextView center_title;
	}

}