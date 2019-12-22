package com.acceedo.attendancesystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.acceedo.attendancesystem.Pojo.SplashIntro;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.databinding.AdapterintropagerBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

public class IntroPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<SplashIntro> mList;

    @Override
    public int getCount() {
        return mList.size();
    }
    public IntroPagerAdapter(Context mContext,ArrayList<SplashIntro> mList) {
        this.mContext=mContext;
        this.mList=mList;

        mLayoutInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(ConstraintLayout)object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mLayoutInflater == null)
            mLayoutInflater = LayoutInflater.from(mContext);

        AdapterintropagerBinding mBinding= DataBindingUtil.inflate(mLayoutInflater,R.layout.adapterintropager, container, false);


        SplashIntro mitem=mList.get(position);

        if(mList.get(mList.size()-1)==mList.get(position)){

            Glide.with(mContext).load(R.drawable.walkthroughthree).into(mBinding.imageView);
        }else if(mList.get(0)==mList.get(position)){
            Glide.with(mContext).load(R.drawable.walkthroughone).into(mBinding.imageView);
        }else {
            Glide.with(mContext).load(R.drawable.walkthroughtwo).into(mBinding.imageView);
        }

        mBinding.textView.setText(mitem.getTitle());
        mBinding.tvcontent.setText(mitem.getName());

        container.addView(mBinding.getRoot());
        return mBinding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
