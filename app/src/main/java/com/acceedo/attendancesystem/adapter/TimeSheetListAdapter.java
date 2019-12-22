package com.acceedo.attendancesystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acceedo.attendancesystem.Pojo.RecentScanDetails;
import com.acceedo.attendancesystem.Pojo.SliderDashBoardPojo;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.databinding.AdapterlisttimesheetBinding;
import com.acceedo.attendancesystem.databinding.LayoutLoadingItemBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class TimeSheetListAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList <RecentScanDetails> mList;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private LayoutInflater layoutInflater;

    private OnClickListener mOnClickListener;

    public interface OnClickListener {
        void onDataClick(int position);
    }

    public TimeSheetListAdapter(Context requireContext, ArrayList <RecentScanDetails> recentList) {
        this.mContext = requireContext;
        this.mList = recentList;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_ITEM) {
            AdapterlisttimesheetBinding binding = DataBindingUtil.
                    inflate(layoutInflater, R.layout.adapterlisttimesheet, parent, false);
            return new MyViewHolder(binding);
        } else if (viewType == VIEW_TYPE_LOADING) {
            LayoutLoadingItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(binding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        RecentScanDetails mitem=mList.get(position);
        if (holder instanceof MyViewHolder) {
            AdapterlisttimesheetBinding binding = ((MyViewHolder) holder).mBinding;
            binding.tvcode.setText(mitem.getEmpcode());
            binding.tvcontractor.setText(mitem.getContractor());
            binding.tvdepartment.setText(mitem.getDepartment());
            binding.tvsupervisor.setText(mitem.getSupervisor());
            binding.tvempintime.setText(mitem.getEmpintime());
            binding.tvfullname.setText(mitem.getEmpfirstname()+" "+mitem.getEmplastname());
            binding.tvempouttime.setText(mitem.getEmpouttime());
            if(mitem.getUserimg().isEmpty()||mitem.getUserimg().equals("null"))
            {
                Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.avatar)).into(binding.civProfileImg);
            }else {
                Glide.with(mContext).load(mitem.getUserimg()).into(binding.civProfileImg);
            }





        } else if (holder instanceof LoadingViewHolder) {
            LayoutLoadingItemBinding binding = ((LoadingViewHolder) holder).mBinding;
            binding.progress.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AdapterlisttimesheetBinding mBinding;

        public MyViewHolder(AdapterlisttimesheetBinding binding) {
            super(binding.getRoot());
            mBinding = binding;

            mBinding.rrDataLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnClickListener!=null)
                    {
                        mOnClickListener.onDataClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {

        LayoutLoadingItemBinding mBinding;

        LoadingViewHolder(LayoutLoadingItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
