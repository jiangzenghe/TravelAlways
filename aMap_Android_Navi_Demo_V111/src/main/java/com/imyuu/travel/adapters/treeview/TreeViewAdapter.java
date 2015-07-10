package com.imyuu.travel.adapters.treeview;
   import java.util.ArrayList;

import com.imyuu.travel.R;
   import com.imyuu.travel.activity.DownloadActivity;
   import com.imyuu.travel.activity.DownloadOldActivity;
   import com.imyuu.travel.util.Config;
   import com.imyuu.travel.util.LogUtil;
   import com.imyuu.travel.view.HorizontalProgressBarWithNumber;

   import android.app.Activity;
   import android.content.Context;
   import android.os.Handler;
   import android.os.Message;
   import android.util.Log;
   import android.util.TypedValue;
   import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
   import android.widget.LinearLayout;
   import android.widget.ProgressBar;
   import android.widget.RelativeLayout;
   import android.widget.TextView;
   import android.widget.Toast;

   import org.w3c.dom.Text;

/**
 * TreeViewAdapter
 * @author carrey
 *
 */
public class TreeViewAdapter extends BaseAdapter {
    /** 元素数据源 */
    private ArrayList<Element> elementsData;
    /** 树中元素 */
    private ArrayList<Element> elements;
    /** LayoutInflater */
    private LayoutInflater inflater;
    /** item的行首缩进基数 */
    private int indentionBase;
    private static final int MSG_PROGRESS_UPDATE = 0x110;
    int reportSuccess =0;
    private DownloadActivity downloader  = new DownloadActivity();
    private Activity context;
    public TreeViewAdapter(Activity ctx,ArrayList<Element> elements, ArrayList<Element> elementsData, LayoutInflater inflater) {
        this.elements = elements;
        this.elementsData = elementsData;
        this.inflater = inflater;
        indentionBase = 15;
        context = ctx;
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public ArrayList<Element> getElementsData() {
        return elementsData;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       final  ViewHolder holder =   new ViewHolder();
       final Element element = elements.get(position);
        int level = element.getLevel();
        //if (convertView == null)
        {

            if(element.isHasChildren())
            {
                convertView = inflater.inflate(R.layout.treeview_item, null);
                if(level==Element.TOP_LEVEL) {
                    convertView.setBackgroundResource(R.color.grey);
//                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) convertView.getLayoutParams();
//                    params.height = 60;
//                    convertView.setLayoutParams(params);

                }
                else
                    convertView.setBackgroundResource(R.color.white);
                holder.disclosureImg = (ImageView) convertView.findViewById(R.id.disclosureImg);
                holder.contentText = (TextView) convertView.findViewById(R.id.contentText);
                holder.mapsize = (TextView) convertView.findViewById(R.id.map_num);
             }
             else
            {
                convertView = inflater.inflate(R.layout.child, null);
                holder.contentText = (TextView) convertView.findViewById(R.id.map_mapname);
                holder.mapsize=(TextView) convertView.findViewById(R.id.map_mapsize);
                holder.download=(TextView) convertView.findViewById(R.id.map_download);

                holder.progressBar = (ProgressBar)convertView.findViewById(R.id.map_progress);

            }
            convertView.setTag(holder);
        }
//        else {
//            holder = (ViewHolder) convertView.getTag();
//        }


        if(element.isHasChildren()) {
            holder.disclosureImg.setPadding(
                    indentionBase * (level + 1),
                    holder.disclosureImg.getPaddingTop(),
                    holder.disclosureImg.getPaddingRight(),
                    holder.disclosureImg.getPaddingBottom());
        }else
        {
            if(element.isHasDownload())
            {
                holder.download.setText("已下载");
            }
            else
            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Log.d("element is called",element.getScenicId());
                    holder.progressBar.setVisibility(View.VISIBLE);
                    holder.progressBar.setMax(100);
                    final  Handler myhandler  = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case 1:
                                    holder.progressBar.setProgress(msg.getData().getInt("size"));
                                    holder.download.setText(msg.getData().getInt("size") + "%");
                                    break;
                                case 2:
                                    //显示下载成功信息
                                    reportSuccess++;

                                     if (reportSuccess >= Config.THREAD_NUM) {
                                        holder.download.setText("100%");
                                        Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
                                        downloader.parseAndSaveJson(element.getScenicId());

                                        holder.download.setText("解析完成");
                                    }
                                   // myhandler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, 100);

                                    element.setHasDownload(true);
                                    break;
                                case -1:
                                    //显示下载错误信息
                                    //  Toast.makeText(DownloadActivity.this, "", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    };

                    LogUtil.d("TreeViewAdapter", "cannav"+element.getCanNav());
                   if(element.getCanNav()) {

                       downloader.download(element.getScenicId(), myhandler);
                   }
                    else
                   {
                       downloader = new DownloadOldActivity(context);
                       downloader.download(element.getScenicId(),myhandler);
                   }

                }
            });
        }
        holder.contentText.setText(element.getContentText());
             holder.mapsize.setText(element.getMapSize());
        if (element.isHasChildren() && !element.isExpanded()) {
            holder.disclosureImg.setImageResource(R.drawable.close);
            //这里要主动设置一下icon可见，因为convertView有可能是重用了"设置了不可见"的view，下同。
            holder.disclosureImg.setVisibility(View.VISIBLE);
        } else if (element.isHasChildren() && element.isExpanded()) {
            holder.disclosureImg.setImageResource(R.drawable.open);
            holder.disclosureImg.setVisibility(View.VISIBLE);
       }
//        else if (!element.isHasChildren()) {
//            holder.disclosureImg.setImageResource(R.drawable.close);
//            holder.disclosureImg.setVisibility(View.INVISIBLE);
//        }
        return convertView;
    }

    /**
     * 优化Holder
     * @author carrey
     *
     */
    static class ViewHolder{
        ImageView disclosureImg;
        TextView contentText;
       ProgressBar progressBar;
        TextView  mapsize;
        TextView  download;

    }
}
