package com.acceedo.attendancesystem.activities;

import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.acceedo.attendancesystem.Pojo.TaskInfo;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.adapter.MySpinnerAdapter;
import com.acceedo.attendancesystem.adapter.TimeSheetSlideAdapter;
import com.acceedo.attendancesystem.databinding.ActivityTimeSheetBinding;

import java.util.ArrayList;

public class TimeSheetActivity extends BaseActivity {

    ActivityTimeSheetBinding mBinding;
    private TimeSheetSlideAdapter mSlideAdapter;
    private ArrayList <String> mDateList;
    private ArrayList <String> spnList;

    onDataChangeListener mOnDataChangeListener;
    onRecylerViewScrollListner mOnRecylerViewScrollListner;

    boolean spinnertouch = false;


    public interface onRecylerViewScrollListner{
        void onRecyclerScroll(String scrollValue);
    }
    public  void setOnRecylerViewScrollListner(onRecylerViewScrollListner mOnRecylerViewScrollListner)
    {
        this.mOnRecylerViewScrollListner=mOnRecylerViewScrollListner;
    }
    public interface onDataChangeListener {
        void onDataChange(String date, String dropdown, ArrayList <TaskInfo> list);
    }

    public void setOnDataChangeListener(onDataChangeListener mOnDataChangeListener) {
        this.mOnDataChangeListener = mOnDataChangeListener;
    }

    public TimeSheetActivity(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_time_sheet);
        mBinding.setData(this);

        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });

        spnList = new ArrayList <>();
        String s="";
        for (int i = 0; i < 5; i++) {

            if(i==2||i==4){
               s ="21 jun-27 jun (open)";
            }else if(i==3||i==1) {
                s ="27 jun-30jun (close)";
            }else {
                s ="30 jun-1 jun (Approved)";
            }

            spnList.add(s);
        }
        MySpinnerAdapter ma = new MySpinnerAdapter(this,spnList,false, "true","date");
        mBinding.spnWeekChanges.setAdapter(ma);

        //mBinding.spnWeekChanges.setSelection(spnList.indexOf(sharedPreference.getschool_year()));


        mBinding.spnWeekChanges.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                spinnertouch = true;
                return false;
            }
        });

        mBinding.spnWeekChanges.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                Log.i("POSITION : ", String.valueOf(i));
                Log.i("SPINNERTOUCH : ", String.valueOf(spinnertouch));

                if(spinnertouch)
                {
                    spinnertouch = false;
                    String selectitem=spnList.get(mBinding.spnWeekChanges.getSelectedItemPosition());
                    cf.show_toast(selectitem);
//                    selectClasGrp = idYrList.get(spinnerYear.getSelectedItemPosition());
//                    userSchoolYr = mYearList.get(spinnerYear.getSelectedItemPosition());
//                    Log.i("selectpositiospn", selectClasGrp);
//                    shardPreference.setschool_year(userSchoolYr);
//                    Log.i("testingyear",userSchoolYr);
//
//                    loadSpnFronService("2");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mBinding.fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDialog();
            }
        });

        prepareTabs();

    }

    private void CreateDialog() {
            Window mAlertDialogWindow;

            final AlertDialog dialog1 = new AlertDialog.Builder(TimeSheetActivity.this)
                    .create();
            dialog1.show();

            dialog1.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog1.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            dialog1.setCancelable(true);

            mAlertDialogWindow = dialog1.getWindow();
            View contv = LayoutInflater.from(TimeSheetActivity.this).inflate(
                    R.layout.dialog_createtask, null);
            contv.setFocusable(true);
            contv.setFocusableInTouchMode(true);

            mAlertDialogWindow.setBackgroundDrawableResource(R.drawable.material_dialog_window);

            mAlertDialogWindow.setContentView(contv);
            dialog1.show();

            EditText edtTask=mAlertDialogWindow.findViewById(R.id.et_Task);
            EditText edtComments=mAlertDialogWindow.findViewById(R.id.et_Comments);
            Button btnCancel=mAlertDialogWindow.findViewById(R.id.btncancel);
            Button btnCreate=mAlertDialogWindow.findViewById(R.id.btncreate);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog1.dismiss();
                }
            });
            btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });


    }


    private void prepareTabs() {

        mDateList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            mDateList.add("" + (i * 2));
            //mTabLayout.addTab(mTabLayout.newTab().setCustomView(getCustomView("SUN", (i * 10) + "")));
        }

        mSlideAdapter = new TimeSheetSlideAdapter(getSupportFragmentManager(),this, mDateList);
        mBinding.viewpager.setAdapter(mSlideAdapter);
        mBinding.tabTimes.setupWithViewPager(mBinding.viewpager);

        /*((TimeSheet) fragment).setOnDataChangeListener(new TimeSheet.onDataChangeListener() {
                    @Override
                    public void onDataChange(String date, String dropdown, ArrayList <TaskInfo> list) {
                        onTimeSheetDataChange(date, dropdown, list);
                    }
                });*/


       mSlideAdapter.setOnDataChangeListener(new TimeSheetSlideAdapter.onDataChangeListener() {
           @Override
           public void onDataChange(String date, String dropdown, ArrayList <TaskInfo> list) {
               mOnDataChangeListener.onDataChange(date, dropdown, list);
           }
       });
       mSlideAdapter.setOnRecylerViewScrollListner(new TimeSheetSlideAdapter.onRecylerViewScrollListner() {
           @Override
           public void onRecyclerScroll(String scrollValue) {
               if(scrollValue.equals("1")){
                   mBinding.fabCreate.show();
               }else {
                   mBinding.fabCreate.hide();
               }
           }
       });

        for (int i = 0; i < mDateList.size(); i++) {
            mBinding.tabTimes.getTabAt(i).setCustomView(getCustomView(cf.getWeekNames(i), mDateList.get(i)));
        }
    }

    private View getCustomView(String day, String date) {
        View v = LayoutInflater.from(this).inflate(R.layout.list_custom_tab, null);
        TextView tvDay = v.findViewById(R.id.tvDayName);
        TextView tvDate = v.findViewById(R.id.tvDate);
        tvDay.setText(day);
        tvDate.setText(date);
        return v;
    }

}
