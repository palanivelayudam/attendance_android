package com.acceedo.attendancesystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.acceedo.attendancesystem.Pojo.SliderDashBoardPojo;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.databinding.AdapterrecenttimesheetBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterRecentTimesheet extends RecyclerView.Adapter<AdapterRecentTimesheet.MyViewHolders> {
    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<SliderDashBoardPojo> mList;

    public AdapterRecentTimesheet(Context context, ArrayList<SliderDashBoardPojo> recentList) {
        this.mContext=context;
        this.mList=recentList;
    }

    @NonNull
    @Override
    public MyViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mLayoutInflater == null)
            mLayoutInflater = LayoutInflater.from(mContext);

        AdapterrecenttimesheetBinding mBinding= DataBindingUtil.inflate(mLayoutInflater, R.layout.adapterrecenttimesheet, parent, false);
        return new MyViewHolders(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecentTimesheet.MyViewHolders holder, int position) {

        SliderDashBoardPojo item=mList.get(position);
        AdapterrecenttimesheetBinding mBinding=holder.mbinding;

        mBinding.tvDate.setText(item.getTimehours());
        mBinding.tvTags.setText(item.getHourstext());

        if(mBinding.tvTags.getText().toString().equalsIgnoreCase("submitted"))
        {
            mBinding.tvTags.setTextColor(mContext.getResources().getColor(R.color.PrimaryYellowColor));
        }else if(mBinding.tvTags.getText().toString().equalsIgnoreCase("Approved")) {
            mBinding.tvTags.setTextColor(mContext.getResources().getColor(R.color.PrimaryGreenwColor));
        }else{
            mBinding.tvTags.setTextColor(mContext.getResources().getColor(R.color.litered));

        }
    }

    @Override
    public int getItemCount() {
         return mList == null ? 0 : mList.size();
    }


    public class MyViewHolders extends RecyclerView.ViewHolder {

        AdapterrecenttimesheetBinding mbinding;

        public MyViewHolders(AdapterrecenttimesheetBinding mBinding) {
            super(mBinding.getRoot());
            mbinding=mBinding;
        }
    }
}
