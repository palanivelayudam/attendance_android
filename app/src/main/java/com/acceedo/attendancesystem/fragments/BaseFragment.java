package com.acceedo.attendancesystem.fragments;

import android.os.Bundle;

import com.acceedo.attendancesystem.common.CommonFunction;
import com.acceedo.attendancesystem.common.Shared_Preference;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    public CommonFunction cf;
    public Shared_Preference sharedPreference;
    public boolean isCheckRefreshedToken=true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cf = new CommonFunction(requireContext());
        sharedPreference=new Shared_Preference(requireContext());
    }
}
