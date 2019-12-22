package com.acceedo.attendancesystem.adapter;

import android.content.Context;
import android.os.Bundle;

import com.acceedo.attendancesystem.fragments.TimeSheetDetail;
import com.acceedo.attendancesystem.Pojo.TaskInfo;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class TimeSheetSlideAdapter extends FragmentPagerAdapter {

    private Context mContext;
    onDataChangeListener mOnDataChangeListener;
    onRecylerViewScrollListner mOnRecylerViewScrollListner;
    ArrayList <String> mDateList;

    public interface onRecylerViewScrollListner{
        void onRecyclerScroll(String scrollValue);
    }
    public  void setOnRecylerViewScrollListner(onRecylerViewScrollListner mOnRecylerViewScrollListner)
    {
        this.mOnRecylerViewScrollListner=mOnRecylerViewScrollListner;
    }

    public interface onDataChangeListener {
        void onDataChange(String date, String dropdown, ArrayList <TaskInfo> list);
    }

    public void setOnDataChangeListener(onDataChangeListener mOnDataChangeListener) {
        this.mOnDataChangeListener = mOnDataChangeListener;
    }

    public TimeSheetSlideAdapter(FragmentManager fm, Context context, ArrayList <String> mDateList) {
        super(fm);
        mContext = context;
        this.mDateList = mDateList;
    }

    @Override
    public Fragment getItem(int position) {
        TimeSheetDetail fragment = new TimeSheetDetail();
        Bundle bundle = new Bundle();
        bundle.putString("date", mDateList.get(position));
        fragment.setArguments(bundle);
        fragment.setOnDataChangeListener(new TimeSheetDetail.onDataChangeListener() {
            @Override
            public void onDataChange(String date, String dropdown, ArrayList <TaskInfo> list) {
                mOnDataChangeListener.onDataChange(date, dropdown, list);
            }
        });
        fragment.setOnRecylerViewScrollListner(new TimeSheetDetail.onRecylerViewScrollListner() {
            @Override
            public void onRecyclerScroll(String scrollValue) {
                mOnRecylerViewScrollListner.onRecyclerScroll(scrollValue);
            }
        });
        return fragment;
    }

    @Override
    public int getCount() {
        return mDateList == null ? 0 : mDateList.size();
    }
}
