package com.acceedo.attendancesystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.acceedo.attendancesystem.Pojo.SliderDashBoardPojo;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.databinding.AdapterdashsliderpagerBinding;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class SliderDashBoard extends RecyclerView.Adapter <SliderDashBoard.MyViewHolder> {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<SliderDashBoardPojo> mList;
    public SliderDashBoard(Context mContext,ArrayList<SliderDashBoardPojo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public SliderDashBoard.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (mLayoutInflater == null)
            mLayoutInflater = LayoutInflater.from(mContext);

        AdapterdashsliderpagerBinding mBinding= DataBindingUtil.inflate(mLayoutInflater, R.layout.adapterdashsliderpager, parent, false);
        return new MyViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderDashBoard.MyViewHolder holder, int position) {
        SliderDashBoardPojo item=mList.get(position);
        AdapterdashsliderpagerBinding mBinding=holder.binding;
        mBinding.tvHours.setText(item.getTimehours());
        mBinding.tvTitle.setText(item.getTitle());
        mBinding.tvhourstext.setText(item.getHourstext());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AdapterdashsliderpagerBinding binding;

        public MyViewHolder(AdapterdashsliderpagerBinding mBinding) {
            super(mBinding.getRoot());
            binding=mBinding;
        }
    }
}
