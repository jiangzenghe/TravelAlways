package com.imyuu.travel.adapters;

import android.os.Parcelable;
import android.view.View;

import com.imyuu.travel.view.VerticalViewPager;

import java.util.List;

public class VerticalPagerAdapter extends PagerAdapter
{

    public List mListViews;

    public VerticalPagerAdapter(List list)
    {
        mListViews = list;
    }

    public void destroyItem(View view, int i, Object obj)
    {
        ((VerticalViewPager)view).removeView((View)mListViews.get(i));
    }

    public void finishUpdate(View view)
    {
    }

    public int getCount()
    {
        return mListViews.size();
    }

    public Object instantiateItem(View view, int i)
    {
        ((VerticalViewPager)view).addView((View)mListViews.get(i), 0);
        return mListViews.get(i);
    }

    public boolean isViewFromObject(View view, Object obj)
    {
        return view == obj;
    }

    public void restoreState(Parcelable parcelable, ClassLoader classloader)
    {
    }

    public Parcelable saveState()
    {
        return null;
    }

    public void startUpdate(View view)
    {
    }
}