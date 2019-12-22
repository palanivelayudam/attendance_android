package com.acceedo.attendancesystem.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.databinding.FragmentLeaverequestBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class LeaveRequest extends BaseFragment {
    private FragmentLeaverequestBinding mBinding;
    boolean loadsms = false;


    public LeaveRequest() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //View view = inflater.inflate(R.layout.fragment_settings, container, false);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_leaverequest, container, false);
        mBinding.setDate(this);



        mBinding.onetimecheckin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                loadsms = true;
                return false;
            }
        });

        mBinding.onetimecheckin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //type = "message";

                //tvMsg.setText(isChecked ? getString(R.string.notifaication_on) : getString(R.string.notifaication_off));

                if (loadsms) {

                    loadsms = false;

                    if (isChecked) {
                        sharedPreference.setnotify_mail("1");
                        //notifyStr = "1";
                        /*if (cf.isInternetOn() == true) {
                            notificationService(type, notifyStr);
                        } else {
                            //dialogtype="stoproute";
                            No_InternetDialog(type);
                        }

                        tvMsg.setTextColor(getResources().getColor(R.color.liteGreen));
*/
                        //cf.show_toast(type + "ison");
                    } else {
                        sharedPreference.setnotify_mail("2");

                        //openAlertDialog(type, notifyStr);

                        //cf.show_toast("isoff");
                    }
                }
            }
        });

        if (!sharedPreference.getnotify_mail().isEmpty()) {
            if (sharedPreference.getnotify_mail().equals("1")) {
                mBinding.onetimecheckin.setChecked(true);
                //   tvMsg.setTextColor(getResources().getColor(R.color.liteGreen));
            } else {
                //     tvMsg.setTextColor(getResources().getColor(R.color.Gray60));
                mBinding.onetimecheckin.setChecked(false);
            }

            //   tvMsg.setText(msgSwitch.isChecked() ? getString(R.string.notifaication_on) : getString(R.string.notifaication_off));
        }


        return mBinding.getRoot();
    }

}
