package com.acceedo.attendancesystem.activities;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.acceedo.attendancesystem.Pojo.QrEmployeDetails;
import com.acceedo.attendancesystem.Pojo.StudentAttendance;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.adapter.StudentAdapter;
import com.acceedo.attendancesystem.databinding.ActivityTaskDetailsBinding;
import com.acceedo.attendancesystem.interfaces.APIService;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.gson.JsonObject;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskDetails extends BaseActivity {
    ActivityTaskDetailsBinding mBinding;
    StudentAdapter mAdapter;
    ArrayList<StudentAttendance> mList;
    QrEmployeDetails item;
    Window mAlertDialogWindow;
    String selecthours="";
    androidx.appcompat.app.AlertDialog dialog1 ;
    MaterialStyledDialog.Builder dialogHeader_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_task_details);
        mBinding.setData(this);
        Intent intent=getIntent();
        if(intent.hasExtra("details"))
        {
            item=intent.getParcelableExtra("details");
            mBinding.tvname.setText(item.getEmp_first_name()+" "+item.getEmp_last_name());
            mBinding.tvadress.setText(item.getContractor());
            mBinding.tvdepartment.setText(item.getDepartment());
            mBinding.tvintime.setText(item.getIntime());
            mBinding.tvouttime.setText(item.getOuttime());
           // mBinding.tvemail.setText(item.getEmp_email());
            mBinding.tvsupervisor.setText(item.getSupervisor());
            mBinding.tvphone.setText(item.getEmp_phone1());
            mBinding.tvempcode.setText(item.getEmp_code());
            if(!item.getUserpic().isEmpty()){
                Glide.with(TaskDetails.this).load(item.getUserpic()).into(mBinding.civprofile);
            }else {
                Glide.with(TaskDetails.this).load(getResources().getDrawable(R.drawable.avatar)).into(mBinding.civprofile);
            }
        }

        //RecentUserApi();

        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(TaskDetails.this).create();
                alertDialog.setTitle("Alert!");
                alertDialog.setMessage("Sorry once you need click ok button then scan will be completed");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

    }

    private void RecentUserApi() {

        cf.progressdialog.show("");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(sharedPreference.getapiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(cf.initOkHttp())
                .build();

        APIService service = retrofit.create(APIService.class);

        Call<JsonObject> call = service.getsettings(sharedPreference.getuser_token());

        //calling the api
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> responseObject)
            {
                //hiding progress dialog
                cf.progressdialog.dismiss("");

                if (responseObject.isSuccessful() && responseObject.body() != null)
                {
                    Log.i("Retrofit Response", responseObject.toString());
                    Log.i("Retrofit Response", responseObject.body().toString());

                    try {
                        JSONObject response = new JSONObject(responseObject.body().toString());

                        if (response.has("success"))
                        {
                            String type=response.getString("success");
                            if(response.getString("success").equals("true")) {
                                if (response.has("data")) {
                                    mList = new ArrayList<>();
                                    JSONArray itemarray = response.getJSONArray("data");
                                    for (int i = 0; i < itemarray.length(); i++) {
                                        JSONObject itemobj = itemarray.getJSONObject(i);
                                        StudentAttendance mItem=new StudentAttendance();
                                        mItem.setTimeadde(itemobj.getString("shift_hrs"));
                                        mItem.setIsselectable(false);
                                        mList.add(mItem);
                                    }
                                    CreateDialog();
                                }
                            }
                            if(response.has("message")){
                                cf.show_toast(response.getString("message"));
                            }
                        }

                    } catch (JSONException e) {
                        cf.show_toast(getResources().getString(R.string.lbl_jsonerror));
                        e.printStackTrace();
                    }

                } else {
                    cf.showErrorHangling( TaskDetails.this,responseObject);
                    //String value= String.valueOf(responseObject.code());

                   /* if(responseObject.code()==401)
                    {
                        String smJSON = null;
                        try {
                            smJSON = responseObject.errorBody().string();
                            JSONObject jObj = new JSONObject(smJSON);
                            cf.show_toast(jObj.getString("message"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                   *//* Log.i("Message", responseObject.message().toString());
                    Log.i("Retrofit Response", "Error in getGenericJson:" + responseObject.code() + " " + responseObject.message());
//                    cf.show_toast(getResources().getString(R.string.lbl_serverdown));

                    try {
                        String smJSON = responseObject.errorBody().string();
                        JSONObject jObj = new JSONObject(smJSON);
                        sharedPreference.setIsLoggedIn(false);
                        if (jObj.has("status_code")) {
                            if (jObj.getString("status_code").equals("500")) {

                                cf.show_toast(getResources().getString(R.string.error500));
                            }
                            if (jObj.getString("status_code").equals("401")) {

                                cf.show_toast(getResources().getString(R.string.error500));
                            }
                        } else if (jObj.has("message"))
                            cf.show_toast(jObj.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                cf.progressdialog.dismiss("");
                t.printStackTrace();

//                cf.show_toast(t.getMessage());
                sharedPreference.setIsLoggedIn(false);
                cf.show_toast(getResources().getString(R.string.lbl_serverdown));

                try {
                    String smJSON = t.getMessage().toString();
                    JSONObject jObj = new JSONObject(smJSON);
                    if (jObj.has("message"))
                        cf.show_toast(jObj.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
   /* private void preparedData() {
        mList=new ArrayList <>();
        for (int i = 0; i <15 ; i++) {
            StudentAttendance mItem=new StudentAttendance();
            mItem.setTimeadde("4 Hours");
            mItem.setIsselectable(false);
            mList.add(mItem);
        }
    }*/

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        AlertDialog alertDialog = new AlertDialog.Builder(TaskDetails.this).create();
        alertDialog.setTitle("Alert!");
        alertDialog.setMessage("Sorry once you need click ok button then scan will be completed");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void submitclick(View view) {
        if(sharedPreference.getnotify_mail().equals("1"))
        {
            RecentUserApi();
           // CreateDialog();


        }else {
            attendanceApi("normal");
        }
    }
    private void CreateDialog() {
        dialog1 = new androidx.appcompat.app.AlertDialog.Builder(TaskDetails.this)
                .create();
        dialog1.show();

        dialog1.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog1.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog1.setCancelable(false);

        mAlertDialogWindow = dialog1.getWindow();
        View contv = LayoutInflater.from(TaskDetails.this).inflate(
                R.layout.dialog_attendanceselect, null);
        contv.setFocusable(true);
        contv.setFocusableInTouchMode(true);

        mAlertDialogWindow.setBackgroundDrawableResource(R.drawable.material_dialog_window);

        mAlertDialogWindow.setContentView(contv);
        dialog1.show();

        RecyclerView rvTimeList=mAlertDialogWindow.findViewById(R.id.rvTimeSheetList);
        Button btnCancel=mAlertDialogWindow.findViewById(R.id.btncancel);
        Button btnCreate=mAlertDialogWindow.findViewById(R.id.btncreate);

        mAdapter=new StudentAdapter(this,mList);
        rvTimeList.setLayoutManager(new LinearLayoutManager(this));
        rvTimeList.setAdapter(mAdapter);
        mAdapter.setOnClickListener(new StudentAdapter.OnClickListener() {
            @Override
            public void onAddenanceclick(int position, boolean b) {

                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setIsselectable(i==position);
                }

                mAdapter.setList(mList);
                mAdapter.notifyDataSetChanged();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();

            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<StudentAttendance> templist=new ArrayList <>();
                for (int i = 0; i <mList.size() ; i++) {

                    if(mList.get(i).isIsselectable())
                    {
                        StudentAttendance studentAttendance=new StudentAttendance();
                        studentAttendance.setIsselectable(mList.get(i).isIsselectable());
                        studentAttendance.setTimeadde(mList.get(i).getTimeadde());
                        templist.add(studentAttendance);

                    }
                }
                if(templist.size()>0)
                {
                    for (int i = 0; i <templist.size() ; i++) {
                        selecthours=templist.get(i).getTimeadde();
                    }

                    Log.d("testingselecthours",selecthours);
                    attendanceApi("dailog");
                }else {
                    cf.show_toast("Select time");
                }
            }
        });

    }

    void attendanceApi(String type1) {
        cf.progressdialog.show("");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(sharedPreference.getapiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(cf.initOkHttp())
                .build();

        APIService service = retrofit.create(APIService.class);

        JsonObject postParam = new JsonObject();
        try {
            postParam.addProperty("emp_code", sharedPreference.getempcodefromscan());
            postParam.addProperty("supervisor", sharedPreference.getsupervisor());
            postParam.addProperty("scan_type", sharedPreference.getnotify_mail());
            postParam.addProperty("work_hrs", selecthours);
        }catch (Exception e){
            e.printStackTrace();
        }
        Call<JsonObject> call = service.attendancein(postParam,sharedPreference.getuser_token());

        //calling the api
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> responseObject)
            {
                //hiding progress dialog
                cf.progressdialog.dismiss("");

                if (responseObject.isSuccessful() && responseObject.body() != null)
                {
                    Log.i("Retrofit Response", responseObject.toString());
                    Log.i("Retrofit Response", responseObject.body().toString());

                    try {
                        JSONObject response = new JSONObject(responseObject.body().toString());

                        if (response.has("success")) {
                            String type = response.getString("success");

                            if(response.getString("success").equals("true")){
                                if (type1.equals("dailog")) {
                                    dialog1.dismiss();

                                }
                                String s=response.getString("message");
                                scanDialog(s,"1");

                            }

                            cf.show_toast(response.getString("message"));

                        }

                    } catch (JSONException e) {
                        cf.show_toast(getResources().getString(R.string.lbl_jsonerror));
                        e.printStackTrace();
                    }

                } else {
                    String smJSON = null;
                    String errorMessage1="";
                    try {
                        smJSON = responseObject.errorBody().string();
                        JSONObject jObj = new JSONObject(smJSON);
                        if (jObj.has("error")) {
                            errorMessage1 = jObj.getString("error");

                        } else if (jObj.has("message")) {
                            errorMessage1 = jObj.getString("message");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    cf.showErrorHangling( TaskDetails.this,responseObject);

                    if (responseObject.code() == 422) {

                        if (!errorMessage1.equals("") && !errorMessage1.equals("null")) {
                            scanDialog(errorMessage1,"2");
                            //show_toast_long(errorMessage);
                        }
                    }
                    //String value= String.valueOf(responseObject.code());

                   /* if(responseObject.code()==401)
                    {
                        String smJSON = null;
                        try {
                            smJSON = responseObject.errorBody().string();
                            JSONObject jObj = new JSONObject(smJSON);
                            cf.show_toast(jObj.getString("message"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                   *//* Log.i("Message", responseObject.message().toString());
                    Log.i("Retrofit Response", "Error in getGenericJson:" + responseObject.code() + " " + responseObject.message());
//                    cf.show_toast(getResources().getString(R.string.lbl_serverdown));

                    try {
                        String smJSON = responseObject.errorBody().string();
                        JSONObject jObj = new JSONObject(smJSON);
                        sharedPreference.setIsLoggedIn(false);
                        if (jObj.has("status_code")) {
                            if (jObj.getString("status_code").equals("500")) {

                                cf.show_toast(getResources().getString(R.string.error500));
                            }
                            if (jObj.getString("status_code").equals("401")) {

                                cf.show_toast(getResources().getString(R.string.error500));
                            }
                        } else if (jObj.has("message"))
                            cf.show_toast(jObj.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                cf.progressdialog.dismiss("");
                t.printStackTrace();

//                cf.show_toast(t.getMessage());
                sharedPreference.setIsLoggedIn(false);
                cf.show_toast(getResources().getString(R.string.lbl_serverdown));

                try {
                    String smJSON = t.getMessage().toString();
                    JSONObject jObj = new JSONObject(smJSON);
                    if (jObj.has("message"))
                        cf.show_toast(jObj.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void scanDialog(String messgae, String s) {
       /* Window mAlertDialogWindow;

        dialog1 = new androidx.appcompat.app.AlertDialog.Builder(TaskDetails.this)
                .create();
        dialog1.show();

        dialog1.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog1.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mAlertDialogWindow = dialog1.getWindow();
        ScandialogBinding nointernetBinding = DataBindingUtil.
                inflate(LayoutInflater.from(TaskDetails.this), R.layout.scandialog, null, false);

        nointernetBinding.getRoot().getRootView().setFocusable(true);
        nointernetBinding.getRoot().getRootView().setFocusableInTouchMode(true);

        dialog1.setCancelable(false);

        mAlertDialogWindow
                .setBackgroundDrawableResource(R.drawable.material_dialog_window);

        mAlertDialogWindow.setContentView(nointernetBinding.getRoot());
        nointernetBinding.tvdetails.setText(messgae);

        nointernetBinding.llok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(cf.isInternetOn()==true)
                {
                    dialog1.dismiss();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();

                }
            }
        });
*/
       if(s.equals("1"))
       {
           new MaterialStyledDialog.Builder(TaskDetails.this)
                   .setIcon(new IconicsDrawable(TaskDetails.this).icon(MaterialDesignIconic.Icon.gmi_check_circle).color(Color.WHITE))
                   .withDialogAnimation(true)
                   .setTitle("Success!")
                   .setDescription(messgae)
                   .setHeaderColor(R.color.colorPrimary)
                   .setPositiveText("Ok")
                   .setCancelable(false)
                   .onPositive(new MaterialDialog.SingleButtonCallback() {
                       @Override
                       public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                           // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
                           if(cf.isInternetOn()==true)
                           {
                               // dialog1.dismiss();
                               Intent intent = new Intent();
                               setResult(RESULT_OK, intent);
                               finish();

                           }
                       }
                   }).show();

       }else {

           new MaterialStyledDialog.Builder(TaskDetails.this)
                   .setIcon(new IconicsDrawable(TaskDetails.this).icon(MaterialDesignIconic.Icon.gmi_close_circle).color(Color.WHITE))
                   .withDialogAnimation(true)
                   .setTitle("Error!")
                   .setDescription(messgae)
                   .setHeaderColor(R.color.red)
                   .setPositiveText("Ok")
                   .setCancelable(false)
                   .onPositive(new MaterialDialog.SingleButtonCallback() {
                       @Override
                       public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                           // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
                           if(cf.isInternetOn()==true)
                           {
                               // dialog1.dismiss();
                               Intent intent = new Intent();
                               setResult(RESULT_OK, intent);
                               finish();

                           }
                       }
                   }).show();

       }

    }

    public void cancelclick(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
