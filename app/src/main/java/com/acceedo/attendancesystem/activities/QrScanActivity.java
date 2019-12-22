package com.acceedo.attendancesystem.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.acceedo.attendancesystem.Pojo.QrEmployeDetails;
import com.acceedo.attendancesystem.Pojo.RecentScanDetails;
import com.acceedo.attendancesystem.Pojo.SliderDashBoardPojo;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.adapter.TimeSheetListAdapter;
import com.acceedo.attendancesystem.databinding.ActivityQrScanBinding;
import com.acceedo.attendancesystem.interfaces.APIService;
import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QrScanActivity extends BaseActivity {
    ActivityQrScanBinding mbinding;
    ArrayList <RecentScanDetails> timeList;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int PERMISSION_ALL = 104;
    private static final int RESULTCODE = 105;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mbinding= DataBindingUtil.setContentView(this,R.layout.activity_qr_scan);
        RecentUserApi();

        
    }

    private void RecentUserApi() {

        cf.progressdialog.show("");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(sharedPreference.getapiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(cf.initOkHttp())
                .build();

        APIService service = retrofit.create(APIService.class);

        Call<JsonObject> call = service.getrecentscan(sharedPreference.getuser_token());

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
                                    timeList = new ArrayList<>();
                                    JSONArray itemarray = response.getJSONArray("data");
                                    for (int i = 0; i < itemarray.length(); i++) {
                                        JSONObject itemobj = itemarray.getJSONObject(i);
                                        RecentScanDetails recentItem = new RecentScanDetails();
                                        recentItem.setEmpcode(itemobj.getString("emp_code"));
                                        recentItem.setEmpfirstname(itemobj.getString("emp_first_name"));
                                        recentItem.setEmplastname(itemobj.getString("emp_last_name"));
                                        recentItem.setSupervisor(itemobj.getString("supervisor"));
                                        recentItem.setContractor(itemobj.getString("contractor"));
                                        recentItem.setDepartment(itemobj.getString("department"));
                                        recentItem.setEmpintime(itemobj.getString("emp_in_time"));
                                        recentItem.setEmpouttime(itemobj.getString("emp_out_time"));
                                        recentItem.setUserimg(sharedPreference.getapiUrl()+itemobj.getString("emp_profile_pic"));
                                        timeList.add(recentItem);
                                    }
                                    TimeSheetListAdapter mrecentAdapter = new TimeSheetListAdapter(QrScanActivity.this, timeList);
                                    mbinding.rvTimeSheetList.setLayoutManager(new LinearLayoutManager(QrScanActivity.this));
                                    mbinding.rvTimeSheetList.setAdapter(mrecentAdapter);
                                    mrecentAdapter.notifyDataSetChanged();
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
                    cf.showErrorHangling( QrScanActivity.this,responseObject);
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

    public void fabscannerClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

            if (!hasPermissions(QrScanActivity.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(QrScanActivity.this, PERMISSIONS, PERMISSION_ALL);
            } else {
                Intent i = new Intent(QrScanActivity.this, QrCodeActivity.class);
                startActivityForResult( i,REQUEST_CODE_QR_SCAN);

            }

        } else {
            Intent i = new Intent(QrScanActivity.this, QrCodeActivity.class);
            startActivityForResult( i,REQUEST_CODE_QR_SCAN);

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Log.d("testing", "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(QrScanActivity.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d("testing", "Have scan result in your app activity :" + result);
            sharedPreference.setempcodefromscan(result);
            getqrRecordDetails(result);



        }
        if(requestCode==RESULTCODE){
            RecentUserApi();
           // cf.show_toast("successfully scan");
        }
    }

    private void getqrRecordDetails(String result) {
        cf.progressdialog.show("");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(sharedPreference.getapiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(cf.initOkHttp())
                .build();

        APIService service = retrofit.create(APIService.class);
        Call<JsonObject> call = service.getempleydetails(result,sharedPreference.getuser_token());

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
                            if(response.has("data")){
                                JSONObject itemobj=response.getJSONObject("data");
                                QrEmployeDetails mItem=new QrEmployeDetails();
                                //mItem.setEmp_id(itemobj.getString("emp_id"));
                                mItem.setEmp_code(itemobj.getString("emp_code"));
                                mItem.setEmp_first_name(itemobj.getString("emp_first_name"));
                                mItem.setEmp_last_name(itemobj.getString("emp_last_name"));
                                //mItem.setEmp_address(itemobj.getString("emp_address"));
                               // mItem.setEmp_city(itemobj.getString("emp_city"));
                               // mItem.setEmp_state(itemobj.getString("emp_state"));
                                mItem.setEmp_phone1(itemobj.getString("emp_phone1"));
                                mItem.setIntime(itemobj.getString("in_time"));
                               mItem.setOuttime(itemobj.getString("entry_type"));
                                //mItem.setEmp_email(itemobj.getString("emp_email"));
                               // mItem.setEmp_salary(itemobj.getString("emp_salary"));
                               //mItem.setEmp_role_id(itemobj.getString("emp_role_id"));
                               //mItem.setEmp_role_id(itemobj.getString("emp_role_id"));
                                mItem.setEmp_supervisor(itemobj.getString("emp_supervisor"));
                                mItem.setEmp_contractor(itemobj.getString("emp_contractor"));
                                mItem.setEmp_dept_id(itemobj.getString("emp_dept_id"));
                                //mItem.setEmp_date_of_join(itemobj.getString("emp_date_of_join"));
                               // mItem.setEmp_active_status(itemobj.getString("emp_active_status"));
                              //  mItem.setCreated_by(itemobj.getString("created_by"));
                               // mItem.setu("updated_at");
                                mItem.setSupervisor(itemobj.getString("supervisor"));
                                mItem.setContractor(itemobj.getString("contractor"));
                                mItem.setDepartment(itemobj.getString("department"));
                                mItem.setUserpic(sharedPreference.getapiUrl()+itemobj.getString("emp_profile_pic"));
                                startActivityForResult(new Intent(QrScanActivity.this,TaskDetails.class)
                                        .putExtra("details",mItem),RESULTCODE);
                            }


                            cf.show_toast(response.getString("message"));


                        }

                    } catch (JSONException e) {
                        cf.show_toast(getResources().getString(R.string.lbl_jsonerror));
                        e.printStackTrace();
                    }

                } else {
                    cf.showErrorHangling( QrScanActivity.this,responseObject);
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


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Intent i = new Intent(QrScanActivity.this, QrCodeActivity.class);
                    startActivityForResult( i,REQUEST_CODE_QR_SCAN);

                } else {
                    // Permission Denied
                    cf.show_toast(getString(R.string.llpermissionde));

                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
   }


    public void backiconclick(View view) {
        finish();
    }
}
