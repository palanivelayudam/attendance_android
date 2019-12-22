package com.acceedo.attendancesystem.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.apiclient.ApiClient;
import com.acceedo.attendancesystem.common.CustomTextWatcher;
import com.acceedo.attendancesystem.databinding.ActivitychangepasswordBinding;
import com.acceedo.attendancesystem.databinding.DialogNointernetBinding;
import com.acceedo.attendancesystem.interfaces.APIService;
import com.acceedo.attendancesystem.interfaces.RefreshTokenCallbacks;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends BaseActivity {

    ActivitychangepasswordBinding mBinding;

    String ScreenType="",strCode="",strId="";
    AlertDialog dialog1;
    String stroldpassword = "", strnewpassword = "", strconfirmpassword = "",changeLang="";
    boolean passwordvisible=true,confirmPasswordVisible=true,confirmnewpassword=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activitychangepassword);

        Intent intent=getIntent();
        if(intent!=null){
            if(intent.hasExtra("screenType")){
                ScreenType=intent.getStringExtra("screenType");
            }
            if(intent.hasExtra("id")){
                strId=intent.getStringExtra("id");
            }
            if(intent.hasExtra("code")){
                strCode=intent.getStringExtra("code");
            }

            Log.d("testingcoed",strCode+"\n"+strId);

        }

        if(ScreenType.equals("deepLink")){
            mBinding.tvToolbarHead.setText(getResources().getString(R.string.reset_password));
            mBinding.rrOldpassword.setVisibility(View.GONE);
        }else {
            mBinding.rrOldpassword.setVisibility(View.VISIBLE);
            mBinding.tvToolbarHead.setText(getResources().getString(R.string.change_password));
        }

        mBinding.etChangeOldPass.addTextChangedListener(new CustomTextWatcher(mBinding.etChangeOldPass));
        mBinding.etChangeConfirmPass.addTextChangedListener(new CustomTextWatcher(mBinding.etChangeConfirmPass));
        mBinding.etChangeNewPass.addTextChangedListener(new CustomTextWatcher(mBinding.etChangeNewPass));

        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cf.hidekeyboard(mBinding.etChangeOldPass);
                cf.hidekeyboard(mBinding.etChangeConfirmPass);
                cf.hidekeyboard(mBinding.etChangeNewPass);

                onBackPressed();
            }
        });
        mBinding.ivPasswordvisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!passwordvisible){
                    mBinding.ivPasswordvisible.setImageResource(R.drawable.loginpassword);
                    mBinding.ivPasswordvisible.setColorFilter(R.color.PrimaryColor);

                    mBinding.etChangeOldPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordvisible=true;
                }else {
                    passwordvisible=false;
                    mBinding.ivPasswordvisible.setImageResource(R.drawable.loginpasswordcross);
                    mBinding.ivPasswordvisible.setColorFilter(R.color.PrimaryColor);
                    mBinding.etChangeOldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        mBinding.ivConfirmnewvisiablility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!confirmPasswordVisible){
                    mBinding.ivConfirmnewvisiablility.setImageResource(R.drawable.loginpassword);
                    mBinding.etChangeConfirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmPasswordVisible=true;
                }else {
                    confirmPasswordVisible=false;
                    mBinding.ivConfirmnewvisiablility.setImageResource(R.drawable.loginpasswordcross);
                    mBinding.etChangeConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        mBinding.ivConfirmprofilevisiablility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!confirmnewpassword){
                    mBinding.ivConfirmprofilevisiablility.setImageResource(R.drawable.loginpassword);
                    mBinding.etChangeNewPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmnewpassword=true;
                }else {
                    confirmnewpassword=false;
                    mBinding.ivConfirmprofilevisiablility.setImageResource(R.drawable.loginpasswordcross);
                    mBinding.etChangeNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        mBinding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ScreenType.equals("deepLink")) {
                    if (!validate()) {
                        strnewpassword = mBinding.etChangeNewPass.getText().toString().trim();
                        strconfirmpassword = mBinding.etChangeConfirmPass.getText().toString().trim();
                        cf.hidekeyboard(mBinding.etChangeConfirmPass);
                        cf.hidekeyboard(mBinding.etChangeNewPass);

                        if (cf.isInternetOn() == true) {
                            resetPasswordService();
                        } else {
                            No_InternetDialog("reset");
                        }
                    }

                }else {
                    if (!validate()) {
                        stroldpassword = mBinding.etChangeOldPass.getText().toString().trim();
                        strnewpassword = mBinding.etChangeNewPass.getText().toString().trim();
                        strconfirmpassword = mBinding.etChangeConfirmPass.getText().toString().trim();
                        cf.hidekeyboard(mBinding.etChangeConfirmPass);
                        cf.hidekeyboard(mBinding.etChangeNewPass);
                        cf.hidekeyboard(mBinding.etChangeOldPass);

                        if (cf.isInternetOn() == true) {
                            changePasswordService();
                        } else {
                            No_InternetDialog("changepassword");
                        }
                    }
                }
            }
        });
        mBinding.etChangeConfirmPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){

                    if(ScreenType.equals("deepLink")) {

                        if (!validate()) {

                            strnewpassword = mBinding.etChangeNewPass.getText().toString().trim();
                            strconfirmpassword = mBinding.etChangeConfirmPass.getText().toString().trim();
                            cf.hidekeyboard(mBinding.etChangeConfirmPass);
                            cf.hidekeyboard(mBinding.etChangeNewPass);

                            if (cf.isInternetOn() == true) {
                                resetPasswordService();
                            } else {
                                No_InternetDialog("reset");
                            }
                        }

                    }else {
                        if (!validate()) {

                            stroldpassword = mBinding.etChangeOldPass.getText().toString().trim();
                            strnewpassword = mBinding.etChangeNewPass.getText().toString().trim();
                            strconfirmpassword = mBinding.etChangeConfirmPass.getText().toString().trim();
                            cf.hidekeyboard(mBinding.etChangeConfirmPass);
                            cf.hidekeyboard(mBinding.etChangeNewPass);
                            cf.hidekeyboard(mBinding.etChangeOldPass);

                            if(cf.isInternetOn()==true)
                            {
                                changePasswordService();
                            }
                            else
                            {
                                No_InternetDialog("changepassword");
                            }

                        }
                    }
                    return true;
                }

                return false;
            }
        });

    }

    private void No_InternetDialog(final String typestr) {
        Window mAlertDialogWindow;

        dialog1 = new AlertDialog.Builder(ChangePassword.this)
                .create();
        dialog1.show();

        dialog1.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog1.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mAlertDialogWindow = dialog1.getWindow();
        DialogNointernetBinding nointernetBinding = DataBindingUtil.
                inflate(LayoutInflater.from(ChangePassword.this), R.layout.dialog_nointernet, null, false);

        nointernetBinding.getRoot().getRootView().setFocusable(true);
        nointernetBinding.getRoot().getRootView().setFocusableInTouchMode(true);

        dialog1.setCancelable(false);

        mAlertDialogWindow
                .setBackgroundDrawableResource(R.drawable.material_dialog_window);

        mAlertDialogWindow.setContentView(nointernetBinding.getRoot());


        nointernetBinding.btnyes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(cf.isInternetOn()==true)
                {
                    dialog1.dismiss();
                    // callWebService();
                    if(typestr.equals("reset"))
                    {
                        resetPasswordService();
                    }else {
                        changePasswordService();
                    }

                }
            }
        });

        nointernetBinding.btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();

                finish();

            }
        });
    }


    private boolean validate() {
        boolean failflag = false;
        /*if (!passwordEdt.getText().toString().equals(passvalue)) {
            passwordEdt.setError("Your current password is incorrect");
            flag = false;
        }*/
        if(!ScreenType.equals("deepLink")){
            if (mBinding.etChangeOldPass.getText().toString().trim().isEmpty()) {
                cf.show_toast(getResources().getString(R.string.changepassword_oldpassword));
                failflag = true;
            }
        }

        if (mBinding.etChangeNewPass.getText().toString().trim().isEmpty()) {
            cf.show_toast(getResources().getString(R.string.changepassword_newpassword));

            failflag = true;
        }

        if (mBinding.etChangeNewPass.getText().toString().trim().length()<5) {
            cf.show_toast(getResources().getString(R.string.changepassword_passwordshouldbe));
            failflag = true;
            return failflag;
        }

        if (mBinding.etChangeConfirmPass.getText().toString().trim().isEmpty()) {
            cf.show_toast(getResources().getString(R.string.changepassword_enterconfirmpassword));
            failflag = true;
        }

        if (!mBinding.etChangeNewPass.getText().toString().trim().equals(mBinding.etChangeConfirmPass.getText().toString().trim())) {
            cf.show_toast(getResources().getString(R.string.changepassword_mismatch));
            failflag = true;
        }

        return failflag;
    }

    private void changePasswordService() {

        cf.progressdialog.show("");


        APIService service = ApiClient.getClient(getApplicationContext())
                .create(APIService.class);

        Call<JsonObject> call = service.changePassword(
                sharedPreference.getuser_token(), stroldpassword, strnewpassword, strconfirmpassword);

        //calling the api
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> responseObject) {
                //hiding progress dialog
                cf.progressdialog.dismiss("");

                if (responseObject.isSuccessful() && responseObject.body() != null) {
                    Log.i("Retrofit Response", responseObject.toString());
                    Log.i("Retrofit Response", responseObject.body().toString());

                    try {
                        JSONObject response = new JSONObject(responseObject.body().toString());

                        try {
                            if (response.has("message"))
                            {
                                mBinding.etChangeOldPass.setText("");
                                mBinding.etChangeNewPass.setText("");
                                mBinding.etChangeConfirmPass.setText("");
                                cf.show_toast(response.getString("message"));
                                mBinding.etChangeOldPass.requestFocus();


                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        cf.show_toast(getResources().getString(R.string.lbl_jsonerror));
                        e.printStackTrace();
                    }

                } else {
                    Log.i("Message", responseObject.message().toString());
                    Log.i("Retrofit Response", "Error in getGenericJson:" + responseObject.code() + " " + responseObject.message());

                    if (responseObject.code() == 401) {

                        cf.callRefreshToken(ChangePassword.this, new RefreshTokenCallbacks() {
                            @Override
                            public void onSuccess(@NonNull boolean value) {
                                if (value) {
                                    Log.d("isCheckRefreshedToken", "false");
                                    isCheckRefreshedToken = false;
                                    Log.i("RefreshToken Response", "TRUE");
                                    changePasswordService();
                                } else {
                                    Log.d("isCheckRefreshedToken", "failure");

                                    cf.show_toast(getString(R.string.tockenfailederror));
                                    cf.progressdialog.dismiss("");
                                    Intent intent = new Intent(ChangePassword.this, LoginAcitivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    sharedPreference.ClearPreferences();
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable throwable) {
                                cf.progressdialog.dismiss("");
                                try {

                                    String smJSON = throwable.getMessage().toString();
                                    JSONObject jObj = new JSONObject(smJSON);
                                    if (jObj.has("message")) ;
                                    cf.show_toast(jObj.getString("message"));
                                    Intent intent = new Intent(ChangePassword.this, LoginAcitivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    sharedPreference.ClearPreferences();
                                    startActivity(intent);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else {
                        cf.progressdialog.dismiss("");
                        cf.showErrorHangling(ChangePassword.this, responseObject);
                    }

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
                        Toast.makeText(ChangePassword.this, jObj.getString("message"),
                                Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    void resetPasswordService() {

        cf.progressdialog.show("");

        APIService service = ApiClient.getClient(getApplicationContext())
                .create(APIService.class);

        Call<JsonObject> call = service.resetPassword(strCode,strId,strconfirmpassword);

        //calling the api
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> responseObject) {
                //hiding progress dialog
                cf.progressdialog.dismiss("");

                if (responseObject.isSuccessful() && responseObject.body() != null) {
                    Log.i("Retrofit Response", responseObject.toString());
                    Log.i("Retrofit Response", responseObject.body().toString());

                    try {
                        JSONObject response = new JSONObject(responseObject.body().toString());

                        try {
                            if (response.has("message"))
                            {
                                mBinding.etChangeConfirmPass.setText("");
                                mBinding.etChangeNewPass.setText("");

                                cf.show_toast(response.getString("message"));

                                sharedPreference.ClearPreferences();
                                Intent intent=new Intent(ChangePassword.this,LoginAcitivity.class);
                                startActivity(intent);
                                finish();

                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        cf.show_toast(getResources().getString(R.string.lbl_jsonerror));
                        e.printStackTrace();
                    }

                } else {
                    Log.i("Message", responseObject.message().toString());
                    Log.i("Retrofit Response", "Error in getGenericJson:" + responseObject.code() + " " + responseObject.message());

                    if (responseObject.code() == 401) {

                        cf.callRefreshToken(ChangePassword.this, new RefreshTokenCallbacks() {
                            @Override
                            public void onSuccess(@NonNull boolean value) {
                                if (value) {
                                    Log.d("isCheckRefreshedToken", "false");
                                    isCheckRefreshedToken = false;
                                    Log.i("RefreshToken Response", "TRUE");
                                    resetPasswordService();
                                } else {
                                    Log.d("isCheckRefreshedToken", "failure");

                                    cf.show_toast(getString(R.string.tockenfailederror));
                                    cf.progressdialog.dismiss("");
                                    Intent intent = new Intent(ChangePassword.this, LoginAcitivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    sharedPreference.ClearPreferences();
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable throwable) {
                                cf.progressdialog.dismiss("");
                                try {

                                    String smJSON = throwable.getMessage().toString();
                                    JSONObject jObj = new JSONObject(smJSON);
                                    if (jObj.has("message")) ;
                                    cf.show_toast(jObj.getString("message"));
                                    Intent intent = new Intent(ChangePassword.this, LoginAcitivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    sharedPreference.ClearPreferences();
                                    startActivity(intent);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else {
                        cf.progressdialog.dismiss("");
                        cf.showErrorHangling(ChangePassword.this, responseObject);
                    }

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
                        Toast.makeText(ChangePassword.this, jObj.getString("message"),
                                Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


}
