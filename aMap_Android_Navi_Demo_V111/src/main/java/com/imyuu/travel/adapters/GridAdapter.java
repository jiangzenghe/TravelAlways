package com.imyuu.travel.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imyuu.travel.R;

import java.util.ArrayList;

/**
 * @author <p>Modification History:</p>
 *         <p>Date             Author      Description</p>
 *         <p>--------------------------------------------------------------</p>
 *         <p>             new</p>
 *         <p>  </p>
 */
public class GridAdapter extends BaseAdapter {

    private ArrayList<String> titles;
    private ArrayList<String> keys;
    private ArrayList<Drawable> images;
    private LayoutInflater inflater = null;
    private Activity activity;

    public GridAdapter(Activity activity, ArrayList<String> titles, ArrayList<String> keys, ArrayList<Drawable> images) {
        this.images = images;
        this.titles = titles;
        this.keys = keys;
        inflater = LayoutInflater.from(activity);
    }

    public GridAdapter(Activity activity) {
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return titles == null ? 0 : titles.size();
    }

    @Override
    public String getItem(int position) {
        if (titles != null && titles.size() != 0) {
            return keys.get(position);
        }
        return null;
    }

    public String getItemText(int position) {
        if (titles != null && titles.size() != 0) {
            return titles.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SdCardPath")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;
        View view = convertView;
        if (view == null) {
            //一张图片的布局
            view = inflater.inflate(R.layout.help_item, null);
            mHolder = new ViewHolder();
            mHolder.center_image = (ImageView) view.findViewById(R.id.help_center_image);
            mHolder.center_title = (TextView) view.findViewById(R.id.help_center_title);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        //获取position对应的数据
        String title = getItemText(position);
        mHolder.center_image.setBackgroundDrawable(images.get(position));
        mHolder.center_title.setText(title);

        return view;

    }

    static class ViewHolder {
        ImageView center_image;
        TextView center_title;
    }

}
