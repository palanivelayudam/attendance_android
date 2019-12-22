package com.acceedo.attendancesystem.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acceedo.attendancesystem.Pojo.ListMenu;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.databinding.AdaptermenuBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ListMenuAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList <ListMenu> mList;
    OnClickListener onClickListener;
    private LayoutInflater layoutInflater;

    public interface OnClickListener {
        void onMenuClick(int position);
    }

    public ListMenuAdapter(Context mContext, ArrayList <ListMenu> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public void setList(ArrayList <ListMenu> mList) {
        this.mList = mList;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());
        AdaptermenuBinding mBinding= DataBindingUtil.inflate(layoutInflater,R.layout.adaptermenu, parent, false);
        return new MyViewHolder(mBinding);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListMenu item = mList.get(position);
        AdaptermenuBinding mBinding=((MyViewHolder) holder).binding;

        mBinding.tvName.setText(item.getName());

        if(mBinding.tvName.getText().toString().trim().equalsIgnoreCase("Timesheet"))
        {
            mBinding.ivmoduleimage.setImageResource(R.drawable.menutimesheet);
            //Glide.with(mContext).load(R.drawable.menutimesheet).into(mBinding.ivmoduleimage);
        }else if(mBinding.tvName.getText().toString().trim().equalsIgnoreCase("task"))
        {
            mBinding.ivmoduleimage.setImageResource(R.drawable.menutask);
            //Glide.with(mContext).load(R.drawable.menutask).into(mBinding.ivmoduleimage);

        }else if(mBinding.tvName.getText().toString().trim().equalsIgnoreCase("leave request"))
        {
            mBinding.ivmoduleimage.setImageResource(R.drawable.menuleaverequest);
            //Glide.with(mContext).load(R.drawable.menuleaverequest).into(mBinding.ivmoduleimage);

        }else if(mBinding.tvName.getText().toString().trim().equalsIgnoreCase("Referrals"))
        {
            mBinding.ivmoduleimage.setImageResource(R.drawable.menureferals);
           // Glide.with(mContext).load(R.drawable.menureferals).into(mBinding.ivmoduleimage);

        }else if(mBinding.tvName.getText().toString().trim().equalsIgnoreCase("Announcement"))
        {
            mBinding.ivmoduleimage.setImageResource(R.drawable.menuannouncement);
            //Glide.with(mContext).load(R.drawable.menuannouncement).into(mBinding.ivmoduleimage);

        }else if(mBinding.tvName.getText().toString().trim().equalsIgnoreCase("Gallery"))
        {
            mBinding.ivmoduleimage.setImageResource(R.drawable.menugallery);
            //Glide.with(mContext).load(R.drawable.menugallery).into(mBinding.ivmoduleimage);
        }else {
            mBinding.ivmoduleimage.setImageResource(R.drawable.menutimesheet);
            //Glide.with(mContext).load(R.drawable.menutimesheet).into(mBinding.ivmoduleimage);
        }
        if (item.isSelected()){
            mBinding.tvName.setTextColor(mContext.getResources().getColor(R.color.PrimaryColor));
            mBinding.llMenuSelectLayout.setBackgroundColor(mContext.getResources().getColor(R.color.Gray200));
            mBinding.ivmoduleimage.setColorFilter(mContext.getResources().getColor(R.color.PrimaryColor));

        } else{
            mBinding.tvName.setTextColor(mContext.getResources().getColor(R.color.Gray600));
            mBinding.ivmoduleimage.setColorFilter(mContext.getResources().getColor(R.color.PrimaryColor));
             mBinding.llMenuSelectLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        }

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AdaptermenuBinding binding;

        public MyViewHolder(AdaptermenuBinding mBinding) {
            super(mBinding.getRoot());
            binding=mBinding;
            binding.llMenuSelectLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onMenuClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}

