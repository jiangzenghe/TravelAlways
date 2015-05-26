package com.imyuu.travel.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.app.Activity;

import com.imyuu.travel.R;
import  com.imyuu.travel.bean.City;
import com.imyuu.travel.adapters.PinnedHeaderAdapter;
import com.imyuu.travel.util.CityScenicUtils;
import com.imyuu.travel.view.IndexBarView;
import com.imyuu.travel.view.PinnedHeaderListView;

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

		original_items = CityScenicUtils.createCities();
		// Array to ArrayList
//		mItems = new ArrayList<String>(Arrays.asList(ITEMS));
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
	
	private void setupViews() {
		setContentView(R.layout.main_act);
		mSearchView = (EditText) findViewById(R.id.search_view);
		mLoadingView = (ProgressBar) findViewById(R.id.loading_view);
		mListView = (PinnedHeaderListView) findViewById(R.id.list_view);
		mEmptyView = (TextView) findViewById(R.id.empty_view);
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
