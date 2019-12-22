package com.acceedo.attendancesystem.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonObject;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.apiclient.ApiClient;
import com.acceedo.attendancesystem.common.CustomTextWatcher;
import com.acceedo.attendancesystem.databinding.ActivityLoginAcitivityBinding;
import com.acceedo.attendancesystem.interfaces.APIService;
import com.acceedo.attendancesystem.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginAcitivity extends BaseActivity {

    ActivityLoginAcitivityBinding mBinding;
    private boolean passwordvisible=false;
    private boolean isSelectable=true;
    private boolean isLogin = true;
    String myDeviceModel="";
    int currentVeros=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_login_acitivity);
        mBinding.setData(this);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);
       // refreshedToken = FirebaseInstanceId.getInstance().getToken();

        myDeviceModel = Build.MODEL;
        currentVeros = android.os.Build.VERSION.SDK_INT;
        mBinding.etEmail.addTextChangedListener(new CustomTextWatcher(mBinding.etEmail));
        mBinding.etPassword.addTextChangedListener(new CustomTextWatcher(mBinding.etPassword));


        mBinding.ivPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!passwordvisible){
                    mBinding.ivPassword.setImageResource(R.drawable.loginpassword);
                    mBinding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordvisible=true;
                }else {
                    passwordvisible=false;
                    mBinding.ivPassword.setImageResource(R.drawable.loginpasswordcross);
                    mBinding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        mBinding.etEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.et_email) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mBinding.etEmail, InputMethodManager.SHOW_IMPLICIT);
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        mBinding.etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.etPassword) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mBinding.etPassword, InputMethodManager.SHOW_IMPLICIT);
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }

                return false;
            }
        });

        mBinding.etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if(!Validate()){
                        mBinding.etEmail.setText("");
                        transferLogincondition();
                        cf.show_toast_long("Password will send to your mail or phone no");
                       // ForgotPasswordApi();
                        //cf.show_toast("success");
                    }

                    return true;
                }
                return false;
            }
        });

        mBinding.etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if(!Validate()){
                        //loginApi();
                        loginApi();

                    }

                    return true;
                }
                return false;
            }
        });

        mBinding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {

                    if(!Validate()){
                        isSelectable=true;
                        loginApi();
                        //sharedPreference.setIsLoggedIn(true);
                        /*Intent intent=new Intent(LoginAcitivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();*/
                    }
                }else {
                    if(!Validate()){
                        //ForgotPasswordApi();
                        mBinding.etEmail.setText("");
                        transferLogincondition();
                        cf.show_toast_long("Password will send to your mail or phone no");
                    }
                }

            }
        });

        mBinding.tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLogin) {
                    mBinding.etEmail.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    mBinding.etEmail.setText("");
                    mBinding.tvLogin.setText(getResources().getString(R.string.submit));
                    mBinding.tvLoginhead.setText(getResources().getString(R.string.forgetpasswordhead));
                    isLogin = false;
                    isSelectable=false;
                    mBinding.tvForgetPassword.setVisibility(View.GONE);
                    mBinding.rlPassword.setVisibility(View.GONE);

                } else {
                    transferLogincondition();
                }
            }
        });

    }


    private void ForgotPasswordApi() {

        cf.progressdialog.show("");

        APIService service = ApiClient.getClient(getApplicationContext())
                .create(APIService.class);

        Call<JsonObject> call = service.forgetpassword(
                mBinding.etEmail.getText().toString().trim());

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

                            if (response.has("message")) {
                                mBinding.etEmail.setText("");
                                transferLogincondition();
                                cf.show_toast_long(response.getString("message"));

                            }


                    } catch (JSONException e) {
                        cf.show_toast(getResources().getString(R.string.lbl_jsonerror));
                        e.printStackTrace();
                    }

                } else {
                    Log.i("Message", responseObject.message().toString());
                    Log.i("Retrofit Response", "Error in getGenericJson:" + responseObject.code() + " " + responseObject.message());
//                    cf.show_toast(getResources().getString(R.string.lbl_serverdown));
                    cf.showErrorHangling( LoginAcitivity.this,responseObject);




                  /*  try {
                        String smJSON = responseObject.errorBody().string();
                        JSONObject jObj = new JSONObject(smJSON);
                        sharedPreference.setIsLoggedIn(false);
                        if (jObj.has("status_code")) {
                            if (jObj.getString("status_code").equals("500")) {

                                cf.show_toast(getResources().getString(R.string.error500));
                            }
                            if (jObj.getString("status_code").equals("401")) {

                                cf.show_toast(jObj.getString("message"));
                            }
                        } *//*else if (jObj.has("message"))
                            cf.show_toast(jObj.getString("message"));*//*
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

    void loginApi() {
        cf.progressdialog.show("");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(sharedPreference.getapiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(cf.initOkHttp())
                .build();

        APIService service = retrofit.create(APIService.class);
/*

        APIService service = ApiClient.getClient(getApplicationContext())
                .create(APIService.class);
*/

        //"3o-QpPWZag:APA91bGrBkwIuoLlQsU7YYEXIAn57tr0_WDyU-V8BXlpgeXWp7dcGD3yERX7MbxGNL6L92nUehzdSxSp2lF1wsPCct0FnPTegPemMSyrMM_nei4fJQL9IynRUwVMKWhe9xLosFtZD8ZA"

        JsonObject postParam = new JsonObject();
        try {
            postParam.addProperty("email", mBinding.etEmail.getText().toString().trim());
            postParam.addProperty("password", mBinding.etPassword.getText().toString().trim());
            postParam.addProperty("remember_me", true);
        }catch (Exception e){
            e.printStackTrace();
        }
        Call<JsonObject> call = service.login(postParam);

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

                        if (response.has("access_token"))
                        {
                            String type=response.getString("token_type");
                            sharedPreference.setuser_token(type+" "+response.getString("access_token"));
                            sharedPreference.setIsLoggedIn(true);
                            sharedPreference.setnotify_mail("2");
                            sharedPreference.setfull_name(response.getString("first_name")+" "+
                                    response.getString("last_name"));
                            sharedPreference.setsupervisor(response.getString("supervisor_id"));
                            sharedPreference.setuser_image(sharedPreference.getapiUrl()+response.getString("emp_profile_pic"));
                            sharedPreference.setDesignation(response.getString("designation"));
                            sharedPreference.setuser_email(response.getString("email"));
                            cf.show_toast(type);

                            Intent intent=new Intent(LoginAcitivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } catch (JSONException e) {
                        cf.show_toast(getResources().getString(R.string.lbl_jsonerror));
                        e.printStackTrace();
                    }

                } else {
                    cf.showErrorHangling( LoginAcitivity.this,responseObject);
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

    boolean Validate() {

        boolean failflag = false;

        if (mBinding.etEmail.getText().toString().trim().isEmpty()) {
            cf.show_toast(getString(R.string.enteremail));
            failflag = true;
            return failflag;
        }else {
            if (!cf.eMailValidation(mBinding.etEmail.getText().toString().trim()))
            {
                cf.show_toast(getString(R.string.entervalidemail));
                failflag = true;
                return failflag;
            }
        }
        if(isSelectable){
            if (mBinding.etPassword.getText().toString().trim().isEmpty()) {
                cf.show_toast(getString(R.string.enterpassowrd));
                failflag = true;
                return failflag;
            }
        }


        return failflag;
    }

    void transferLogincondition(){
        mBinding.etEmail.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mBinding.etEmail.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mBinding.etEmail.setText("");
        mBinding.etPassword.setText("");
        mBinding.tvLogin.setText(getResources().getString(R.string.login));
        mBinding.tvLoginhead.setText(getResources().getString(R.string.login));
        isLogin = true;
        isSelectable=true;
        mBinding.tvForgetPassword.setVisibility(View.VISIBLE);
        mBinding.rlPassword.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (isLogin) {
            finish();
        } else {
            transferLogincondition();
        }
    }


}
