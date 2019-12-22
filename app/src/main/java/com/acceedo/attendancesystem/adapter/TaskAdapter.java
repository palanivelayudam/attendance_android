package com.acceedo.attendancesystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acceedo.attendancesystem.Pojo.TaskInfo;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.databinding.AdaptertasklistBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter <TaskAdapter.MyViewHolder> {

    Context mContext;
    ArrayList <TaskInfo> mList;
    OnClickListener onClickListener;
    private LayoutInflater layoutInflater;

    public interface OnClickListener {
        void onhousclick(int position);
        void onLayoutclick(int position);
        void onTaskNameclick(int position);
        void onTaskDesclick(int position);
    }

    public TaskAdapter(Context mContext, ArrayList <TaskInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public void setList(ArrayList <TaskInfo> mList) {
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
        AdaptertasklistBinding mBinding= DataBindingUtil.inflate(layoutInflater,R.layout.adaptertasklist,parent,false);
        return new MyViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        AdaptertasklistBinding mBinding=holder.binding;
        TaskInfo item = mList.get(position);
        mBinding.tvTaskName.setText(item.getTaskName());
        mBinding.tvTaskDesc.setText(item.getTaskDesc());
        mBinding.tvTaskHrs.setText(item.getTaskHours());
        mBinding.tvTaskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onTaskNameclick(position);
                }
            }
        });
        mBinding.tvTaskDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onTaskDesclick(position);
                }
            }
        });
        mBinding.tvTaskHrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onhousclick(position);
                }
            }
        });
        mBinding.rrTotalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onLayoutclick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AdaptertasklistBinding binding;

        public MyViewHolder(AdaptertasklistBinding mBinding) {
            super(mBinding.getRoot());
            binding=mBinding;
        }
    }
}

