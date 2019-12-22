package com.acceedo.attendancesystem.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acceedo.attendancesystem.activities.MainActivity;
import com.acceedo.attendancesystem.activities.ProfileActvity;
import com.acceedo.attendancesystem.activities.QrScanActivity;
import com.blikoon.qrcodescanner.QrCodeActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.acceedo.attendancesystem.Pojo.SliderDashBoardPojo;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.adapter.AdapterRecentTimesheet;
import com.acceedo.attendancesystem.adapter.SliderDashBoard;
import com.acceedo.attendancesystem.databinding.FragmentDashboardBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

public class DashBoardFragment extends BaseFragment {


    private FragmentDashboardBinding mBinding;

    private SliderDashBoard mSliderDashBoard;
    ArrayList<SliderDashBoardPojo> SliderDashItem;
    private onMenuFragmentClickListener mOnMenuFragmentClickListener;

    public interface onMenuFragmentClickListener {
        void onClick(int position);

    }

    public void setonMenuFragmentClickListner(onMenuFragmentClickListener mOnMenuFragmentClickListener) {
        this.mOnMenuFragmentClickListener = mOnMenuFragmentClickListener;
    }

    public DashBoardFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        mBinding.setData(this);

        mBinding.tvname.setText(sharedPreference.getfull_name()+",");
        mBinding.tvemail.setText(sharedPreference.getuser_email());
        mBinding.tvJobtitle.setText(sharedPreference.getDesignation());
        Glide.with(requireContext())
                .asBitmap()
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .load(sharedPreference.getuser_image())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition)
                    {
                        //holder.progressBar.setVisibility(View.VISIBLE);
                        mBinding.civProfileimg.setImageBitmap(cf.scaleToFitHeight(resource,150));
                        mBinding.civProfileimg.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        mBinding.civProfileimg.setImageResource(R.drawable.avatar);
                    }
                });


        mBinding.rlModuleOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cf.show_toast("test");
            }
        });
        SliderDashItem= new ArrayList <>();
        for (int i = 0; i < 3; i++) {
            SliderDashBoardPojo item = new SliderDashBoardPojo();
            if (i == 0) {
                item.setTimehours(sharedPreference.getloggedinCount());
                item.setHourstext("Logged In");
            }else if (i == 1) {
                item.setTimehours(sharedPreference.getrequired());
                item.setHourstext("Required");
            } else {
                item.setTimehours(sharedPreference.getsratio());
                item.setHourstext("Ratio");
            }
           /* if (i == 0) {
                item.setTitle("This week");
                item.setTimehours("34:50");
                item.setHourstext("Hours");
            }else if (i == 1) {
                item.setTitle("Time Off");
                item.setTimehours("16:00");
                item.setHourstext("Remaining Hours");
            } else {
                item.setTitle("Time Off");
                item.setTimehours("16:00");
                item.setHourstext("Remaining Hours");
            }*/

            SliderDashItem.add(item);


        }
        mSliderDashBoard = new SliderDashBoard(requireContext(), SliderDashItem);
        mBinding.rvTaskCalList.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));
        mBinding.rvTaskCalList.setAdapter(mSliderDashBoard);
        mSliderDashBoard.notifyDataSetChanged();

        ArrayList<SliderDashBoardPojo> recentList= new ArrayList <>();
        for (int i = 0; i < 3; i++) {
            SliderDashBoardPojo recentItem = new SliderDashBoardPojo();
            if (i == 0) {
                recentItem.setTimehours(sharedPreference.getloggedinCount());
                recentItem.setHourstext("Logged In");
            }else if (i == 1) {
                recentItem.setTimehours(sharedPreference.getrequired());
                recentItem.setHourstext("Required");
            } else {
                recentItem.setTimehours(sharedPreference.getsratio());
                recentItem.setHourstext("Ratio");
            }

            recentList.add(recentItem);


        }
        AdapterRecentTimesheet mrecentAdapter= new AdapterRecentTimesheet(requireContext(), recentList);
        mBinding.rvRecentSubmission.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.rvRecentSubmission.setAdapter(mrecentAdapter);
        mrecentAdapter.notifyDataSetChanged();

        topFourModuleListner();

       // initView();
        return mBinding.getRoot();
    }

    private void topFourModuleListner() {
        mBinding.rlModuleOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), QrScanActivity.class));
                //listnerPostion(1);

            }
        });
        mBinding.rlModuleTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listnerPostion(1);
            }
        });
        mBinding.rlModuleThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //listnerPostion(3);
            }
        });
        mBinding.rlModuleFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //listnerPostion(4);
            }
        });
    }

    private void listnerPostion(int position){
        if (mOnMenuFragmentClickListener!= null)
            mOnMenuFragmentClickListener.onClick(position);
    }
   /* private void initView() {

        mBinding.tvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_content_body, new TimeSheet());
                ft.commit();*//*
                if (mOnSettingsFragmentClickListener != null)
                    mOnSettingsFragmentClickListener.onClick(0);
            }
        });
    }
*/

   }
