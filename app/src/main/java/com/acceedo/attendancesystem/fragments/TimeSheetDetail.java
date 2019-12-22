package com.acceedo.attendancesystem.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.acceedo.attendancesystem.activities.TaskDetails;
import com.acceedo.attendancesystem.adapter.MySpinnerAdapter;
import com.acceedo.attendancesystem.adapter.TaskAdapter;
import com.acceedo.attendancesystem.Pojo.TaskInfo;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.databinding.FragmentTimeDetailBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TimeSheetDetail extends BaseFragment {

    FragmentTimeDetailBinding mBinding;

    TaskAdapter mTaskAdapter;
    ArrayList <TaskInfo> mList;
    ArrayList<String> spnList;
    onDataChangeListener mOnDataChangeListener;
    onRecylerViewScrollListner mOnRecylerViewScrollListner;
    String strDate="";
    boolean spinnertouch = false;
    String strHours="0";
    String strMins="0";

    public interface onDataChangeListener {
        void onDataChange(String date, String dropdown, ArrayList <TaskInfo> list);
    }
    public interface onRecylerViewScrollListner{
        void onRecyclerScroll(String scrollValue);
    }
    public  void setOnRecylerViewScrollListner(onRecylerViewScrollListner mOnRecylerViewScrollListner)
    {
        this.mOnRecylerViewScrollListner=mOnRecylerViewScrollListner;
    }

    public void setOnDataChangeListener(onDataChangeListener mOnDataChangeListener) {
        this.mOnDataChangeListener = mOnDataChangeListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_time_detail,container,false);
        mBinding.setData(this);
        if (getArguments().containsKey("date"))
            strDate = getArguments().getString("date");

        mBinding.tvTotalHrs.setText(strDate);

        if(getUserVisibleHint()){
            cf.show_toast(strDate);
        }


        prepareDetails();

        mBinding.rvTasks.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy >0) {
                        // Scroll Down
                    if(mOnRecylerViewScrollListner!=null)
                        mOnRecylerViewScrollListner.onRecyclerScroll("0");

                }
                else if (dy <0) {
                    // Scroll Up
                    if(mOnRecylerViewScrollListner!=null)
                        mOnRecylerViewScrollListner.onRecyclerScroll("1");
                }
            }
        });
        return mBinding.getRoot();
    }


    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser && isResumed()){
            cf.show_toast(strDate);

        }
    }*/

    private void prepareDetails() {

        mList = new ArrayList <>();
        for (int i = 0; i < 10; i++) {
            TaskInfo item = new TaskInfo();
            if(i>0&&i<=4){
                item.setTaskName("Lorem ipsum dolor sit amet consectetur");
            }else {
                item.setTaskName("Holiday / Sick Leave");
            }

            item.setTaskDesc("Task Description and Test Values");
            //item.setTaskHours((i + 8)+"");
            item.setTaskHours((strDate));
            mList.add(item);
        }

        mTaskAdapter = new TaskAdapter(requireContext(), mList);
        mBinding.rvTasks.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.rvTasks.setAdapter(mTaskAdapter);
        mTaskAdapter.setOnClickListener(new TaskAdapter.OnClickListener() {
            @Override
            public void onhousclick(int position) {
                BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity());
                View sheetView = getActivity().getLayoutInflater().inflate(R.layout.hourpicker_bottomsheet, null);
                mBottomSheetDialog.setContentView(sheetView);
                TextView tvSelectHoursmins=sheetView.findViewById(R.id.tvHoursmins);
                TextView tvSubmit=sheetView.findViewById(R.id.tvSubmit);
                NumberPicker npHours=sheetView.findViewById(R.id.npHours);
                NumberPicker npMins=sheetView.findViewById(R.id.npMins);



                npHours.setMinValue(0);
                npHours.setMaxValue(12);
                npHours.setWrapSelectorWheel(true);
                npMins.setMinValue(0);
                npMins.setMaxValue(60);
                npMins.setWrapSelectorWheel(true);

                tvSelectHoursmins.setText(strHours+" "+":"+" "+strMins);
                npHours.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                        //Display the newly selected number from picker
                        //cf.show_toast("Selected Hours : " + newVal);
                        strHours=newVal+"";
                        tvSelectHoursmins.setText(strHours+" "+":"+" "+strMins);



                    }
                });
                npMins.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                        //Display the newly selected number from picker
                        //cf.show_toast("Selected Hours : " + newVal);
                        strMins=newVal+"";
                        tvSelectHoursmins.setText(strHours+" "+":"+" "+strMins);

                    }
                });
               tvSubmit.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       mBottomSheetDialog.dismiss();
                       cf.show_toast(strHours+" "+strMins);
                   }
               });

                mBottomSheetDialog.show();
                //  updateValues();
                //Bottomsheet
            }

            @Override
            public void onLayoutclick(int position) {
                startActivity(new Intent(requireContext(), TaskDetails.class));
            }

            @Override
            public void onTaskNameclick(int position) {
                CreateDialog(mList.get(position).getTaskName(),mList.get(position).getTaskDesc());
                cf.show_toast(strDate);
            }

            @Override
            public void onTaskDesclick(int position) {
                CreateDialog(mList.get(position).getTaskName(),mList.get(position).getTaskDesc());
                cf.show_toast(strDate);
            }
        });


        /*final String[] country = {"India", "USA", "China", "Japan", "Other"};
        ArrayAdapter aa = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnProjects.setAdapter(aa);*/

        String s="";
        spnList = new ArrayList <>();
        for (int i = 0; i < 5; i++) {

            if(i==2||i==4){
                s ="Workforce Mangement system";
            }else if(i==3||i==1) {
                s ="Gmis cams";
            }else {
                s ="SSSNL";
            }

            spnList.add(s);
        }

        MySpinnerAdapter ma = new MySpinnerAdapter(requireContext(),spnList,false, "true","Project");
        mBinding.spnProjects.setAdapter(ma);

        //mBinding.spnWeekChanges.setSelection(spnList.indexOf(sharedPreference.getschool_year()));


        mBinding.spnProjects.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                spinnertouch = true;
                return false;
            }
        });

        mBinding.spnProjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                Log.i("POSITION : ", String.valueOf(i));
                Log.i("SPINNERTOUCH : ", String.valueOf(spinnertouch));

                if(spinnertouch)
                {
                    spinnertouch = false;
                    String selectitem=spnList.get(mBinding.spnProjects.getSelectedItemPosition());
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

       /* spnProjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                Toast.makeText(requireContext(), country[position], Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {

            }
        });*/


    }

    private void updateValues() {
        if(mOnDataChangeListener!=null)
            mOnDataChangeListener.onDataChange(strDate,mBinding.spnProjects.getSelectedItem().toString(),mList);
    }

    private void CreateDialog(String taskname,String taskdesc) {
        Window mAlertDialogWindow;

        final AlertDialog dialog1 = new AlertDialog.Builder(requireContext())
                .create();
        dialog1.show();

        dialog1.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog1.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog1.setCancelable(true);

        mAlertDialogWindow = dialog1.getWindow();
        View contv = LayoutInflater.from(requireContext()).inflate(
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
        btnCreate.setText(getString(R.string.update));

        edtTask.setText(taskname);
        edtComments.setText(taskdesc);

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


}
