package com.acceedo.attendancesystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acceedo.attendancesystem.Pojo.StudentAttendance;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.databinding.AdapterstudentaddenceBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class StudentAdapter extends RecyclerView.Adapter <StudentAdapter.MyViewHolder> {

    Context mContext;
    ArrayList <StudentAttendance> mList;
    OnClickListener onClickListener;
    private LayoutInflater layoutInflater;

    public interface OnClickListener {
        void onAddenanceclick(int position, boolean b);
    }

    public StudentAdapter(Context mContext, ArrayList <StudentAttendance> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public void setList(ArrayList <StudentAttendance> mList) {
        this.mList = mList;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());
        AdapterstudentaddenceBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.adapterstudentaddence, parent, false);
        return new MyViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        AdapterstudentaddenceBinding mBinding = holder.binding;
        StudentAttendance item = mList.get(position);
        mBinding.tvtime.setText(item.getTimeadde()+" Hours");
        if (item.isIsselectable()) {
            mBinding.rdselectdata.setChecked(true);
        } else {
            mBinding.rdselectdata.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AdapterstudentaddenceBinding binding;

        public MyViewHolder(AdapterstudentaddenceBinding mBinding) {
            super(mBinding.getRoot());
            binding = mBinding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null)
                        onClickListener.onAddenanceclick(getAdapterPosition(), !mBinding.rdselectdata.isChecked());
                }
            });
        }
    }
}


