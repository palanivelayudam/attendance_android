package com.acceedo.attendancesystem.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.apiclient.ApiClient;
import com.acceedo.attendancesystem.databinding.DialogNointernetBinding;
import com.acceedo.attendancesystem.interfaces.APIService;
import com.acceedo.attendancesystem.interfaces.RefreshTokenCallbacks;
import com.acceedo.attendancesystem.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends BaseActivity {

    AlertDialog dialog1;
    TextView tvcopyright;

    private static final String TAG = "SplashScreen";

    int strServiceAdVersion;
    String strforcedUpdate = "";
    String androidversion = "";
    int versionst;
    String type1 = "";
    String versionName = "";
    int versionCode = -1;
    String playStoreLink = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);


        if (getIntent().getExtras() != null) {


            Log.d("TAGZ", getIntent().getExtras().keySet().toString());
            Log.d("suresh", "sdasda");

            String screen = "", page = "", schoolid = "", schoolyearId = "", id = "", type = "";

            screen = getIntent().getExtras().containsKey("screen") == true ? getIntent().getExtras().getString("screen") : "";
            page = getIntent().getExtras().containsKey("page") == true ? getIntent().getExtras().getString("page") : "";
            schoolid = getIntent().getExtras().containsKey("school_id") == true ? getIntent().getExtras().getString("school_id") : "";
            schoolyearId = getIntent().getExtras().containsKey("school_year_id") == true ? getIntent().getExtras().getString("school_year_id") : "";
            id = getIntent().getExtras().containsKey("id") == true ? getIntent().getExtras().getString("id") : "";
            type = getIntent().getExtras().containsKey("type") == true ? getIntent().getExtras().getString("type") : "";

            Intent intent = null;

            if(sharedPreference.getIsLoggedIn())
            {
                if(screen.isEmpty())
                {
                    openNormalScreen();
                    Log.d("notificationTesting","1");
                }
            }
            else
            {
                openNormalScreen();
                Log.d("notificationTesting","2");
            }

        }
        else
            {

            openNormalScreen();
            Log.d("notificationTesting","3");

            }

    }

    void openNormalScreen() {
        getWindow().setWindowAnimations(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.DarkPrimaryColor));
        }


        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
            versionst = Integer.parseInt(versionName.replace(".", ""));
            Log.d("versionName", String.valueOf(versionst));
            Log.d("versioncode", versionCode + "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tvcopyright = (TextView) findViewById(R.id.tvcopyright);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String formattedDate = df.format(c);

        tvcopyright.setText("©" + formattedDate + " " + "App developed and maintained by PVR Tech Studio");

        Thread thread = new Thread(Splash_Runnable);
        thread.start();
    }

    Runnable Splash_Runnable = new Runnable() {

        @Override
        public void run() {
            try {
                Thread.sleep(3000);

                SplashScreen.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (cf.isInternetOn() == true) {
                            Intent intent1 = getIntent();
                            String action = intent1.getAction();
                            Uri data = intent1.getData();
                            String urldata = String.valueOf(data);
                            String screenAction = "";

                            if (data != null)
                            {
                                if (data.getQueryParameter("action") != null)
                                {
                                    data.getQueryParameter("action");
                                    screenAction = data.getQueryParameter("action");

                                } else {
                                    screenAction = "";
                                }
                            } else {
                                screenAction = "";

                            }

                            if(screenAction.equals("reset")){

                                String[] segments = data.getPath().split("/");
                                String id = segments[segments.length-2];
                                String code = segments[segments.length-1];
                                String s= String.valueOf(data.getPathSegments());
                                Log.d("testingurl1",id+"\n"+code+"\n"+s);

                                Intent intent=new Intent(SplashScreen.this, ChangePassword.class);
                                intent.putExtra("screenType","deepLink");
                                intent.putExtra("id",id);
                                intent.putExtra("code",code);
                                startActivity(intent);
                                finish();

                            }else
                            {
                                //OpenActivity();
                                if(sharedPreference.getIsurldialogOpen()){
                                    OpenActivity();
                                }else {

                                    CreateDialog();
                                }
                            }
                        } else {
                            No_InternetDialog();
                        }
                        //OpenActivity();
                    }
                });

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    };

    @SuppressLint("ClickableViewAccessibility")
    private void CreateDialog() {
        Window mAlertDialogWindow;
        final androidx.appcompat.app.AlertDialog dialog1 = new androidx.appcompat.app.AlertDialog.Builder(SplashScreen.this)
                .create();
        dialog1.show();

        dialog1.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog1.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog1.setCancelable(true);

        mAlertDialogWindow = dialog1.getWindow();
        View contv = LayoutInflater.from(SplashScreen.this).inflate(
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

        edtTask.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.et_Task) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(edtTask, InputMethodManager.SHOW_IMPLICIT);
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

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog1.dismiss();
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtTask.getText().toString().isEmpty())
                {
                    cf.hidekeyboard(edtTask);
                    sharedPreference.setapiUrl(edtTask.getText().toString().trim());
                    sharedPreference.setIsurldialogOpen(true);

                    OpenActivity();
                    dialog1.dismiss();
                }else {
                    cf.show_toast("Enter Url");
                }
            }
        });

    }
    private void OpenActivity() {

        if(sharedPreference.getIsSplashIntro()){
            if(sharedPreference.getIsLoggedIn()){
               // GetUserRecordApi();
                startActivity(new Intent(SplashScreen.this,MainActivity.class));
                finish();

            }else {
                startActivity(new Intent(this,LoginAcitivity.class));
                finish();
            }

        }else {
            Intent intent=new Intent(SplashScreen.this,SplashIntoActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void No_InternetDialog() {
        Window mAlertDialogWindow;

        dialog1 = new AlertDialog.Builder(SplashScreen.this)
                .create();
        dialog1.show();

        dialog1.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog1.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mAlertDialogWindow = dialog1.getWindow();
        DialogNointernetBinding nointernetBinding = DataBindingUtil.
                inflate(LayoutInflater.from(SplashScreen.this), R.layout.dialog_nointernet, null, false);

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

                    Intent intent1 = getIntent();
                    String action = intent1.getAction();
                    Uri data = intent1.getData();
                    String urldata= String.valueOf(data);
                    String screenAction="";
                    if(data!=null) {
                        if (data.getQueryParameter("action") != null) {
                            data.getQueryParameter("action");
                            screenAction = data.getQueryParameter("action");
                        } else {
                            screenAction = "";
                        }
                    }
                    Log.d("testingurldeeplink",action+" "+screenAction+" "+urldata);

                    if(screenAction.equals("reset")){

                        String[] segments = data.getPath().split("/");
                        String id = segments[segments.length-2];
                        String code = segments[segments.length-1];
                        String s= String.valueOf(data.getPathSegments());
                        Log.d("testingurl1",id+"\n"+code+"\n"+s);

                        Intent intent=new Intent(SplashScreen.this, ChangePassword.class);
                        intent.putExtra("screenType","deepLink");
                        intent.putExtra("id",id);
                        intent.putExtra("code",code);
                        startActivity(intent);
                        finish();

                    }else {
                        //OpenActivity();
                        if(sharedPreference.getIsurldialogOpen()){
                            OpenActivity();
                        }else {
                            CreateDialog();
                        }
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

    void GetUserRecordApi() {
        if(isCheckRefreshedToken){
            Log.d("isCheckRefreshedToken","progreshshown");
            cf.progressdialog.show("");
        }


        APIService service = ApiClient.getClient(getApplicationContext())
                .create(APIService.class);

        Call<JsonObject> call = service.getUserRecords(
                sharedPreference.getuser_token(),sharedPreference.getorg_id());

        //calling the api
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> responseObject)
            {
                //hiding progress dialog


                if (responseObject.isSuccessful() && responseObject.body() != null)
                {
                    isCheckRefreshedToken=true;
                    Log.d("isCheckRefreshedToken","true");
                    cf.progressdialog.dismiss("");
                    Log.i("Retrofit Response", responseObject.toString());
                    Log.i("Retrofit Response", responseObject.body().toString());

                    try {
                        JSONObject response = new JSONObject(responseObject.body().toString());



                        if (response.has("user"))

                        {
                            JSONObject jsonObj = new JSONObject();
                            jsonObj = response.getJSONObject("user");

                            //sharedPreference.setaddress(address);
                            sharedPreference.setcity(jsonObj.getString("city"));
                            sharedPreference.setstate(jsonObj.getString("state"));
                            sharedPreference.setzipcode(jsonObj.getString("zipcode"));
                            sharedPreference.setcountry_code(jsonObj.getString("country_code"));
                            sharedPreference.setmobile(jsonObj.getString("mobile_no"));
                            sharedPreference.setbirth_date(jsonObj.getString("birth_date"));
                            //sharedPreference.setgender(gender);



                            sharedPreference.setuser_id(jsonObj.getInt("id") + "");
                            //sharedPreference.setusername(username);
                            sharedPreference.setfirstname(jsonObj.getString("first_name"));
                            sharedPreference.setlastname(jsonObj.getString("last_name"));
                            sharedPreference.setLastLogin(jsonObj.getString("last_login"));
                            //sharedPreference.setfull_name(full_name);
                            sharedPreference.setuser_image(Constants.image+jsonObj.getString("image"));
                            sharedPreference.setuser_email(jsonObj.getString("email"));
                            sharedPreference.setnotify_sms(jsonObj.getInt("get_sms") + "");
                            sharedPreference.setnotify_mail(jsonObj.getInt("get_mail") + "");
                            sharedPreference.setnotify_push(jsonObj.getInt("get_notify") + "");

                        }

                        if (response.has("organization_data")) {
                            JSONObject organizationData = new JSONObject();
                            organizationData = response.getJSONObject("organization_data");

                            sharedPreference.setorganization_title(organizationData.getString("org_title"));
                            sharedPreference.setorg_id(String.valueOf(organizationData.getInt("org_id")));
                            sharedPreference.setapp_name(organizationData.getString("app_name"));

                        }

                        if(response.has("timezone")){
                            sharedPreference.settime_zone(response.getString("timezone"));
                        }
                        if(response.has("timezone_abbr")){
                            sharedPreference.settime_zoneabbr(response.getString("timezone_abbr"));
                        }

                        sharedPreference.setuser_role(response.getString("role"));
                        sharedPreference.setShow_role(response.getString("show_role"));


                        startActivity(new Intent(SplashScreen.this,MainActivity.class));
                        finish();


                    } catch (JSONException e) {
                        cf.show_toast(getResources().getString(R.string.lbl_jsonerror));
                        e.printStackTrace();
                    }

                } else {
                    Log.i("Message", responseObject.message().toString());
                    Log.i("Retrofit Response", "Error in getGenericJson:" + responseObject.code() + " " + responseObject.message());
//                    cf.show_toast(getResources().getString(R.string.lbl_serverdown));
                   // cf.showErrorHangling(SplashScreen.this,responseObject);



                    if (responseObject.code() == 401) {

                        cf.callRefreshToken(SplashScreen.this, new RefreshTokenCallbacks() {
                            @Override
                            public void onSuccess(@NonNull boolean value)
                            {
                                if(value){
                                    Log.d("isCheckRefreshedToken","false");
                                    isCheckRefreshedToken=false;
                                    Log.i("RefreshToken Response","TRUE");
                                    GetUserRecordApi();
                                }else {
                                    Log.d("isCheckRefreshedToken","failure");

                                    cf.show_toast(getString(R.string.tockenfailederror));
                                    cf.progressdialog.dismiss("");
                                    Intent intent = new Intent(SplashScreen.this, LoginAcitivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    sharedPreference.ClearPreferences();
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            @Override
                            public void onError(@NonNull Throwable throwable) {
                                cf.progressdialog.dismiss("");
                                try
                                {

                                    String smJSON = throwable.getMessage().toString();
                                    JSONObject jObj = new JSONObject(smJSON);
                                    if (jObj.has("message"));
                                    cf.show_toast(jObj.getString("message"));
                                    Intent intent = new Intent(SplashScreen.this, LoginAcitivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    sharedPreference.ClearPreferences();
                                    startActivity(intent);
                                    finish();
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }else {
                        cf.progressdialog.dismiss("");
                        cf.showErrorHangling( SplashScreen.this,responseObject);
                    }

                }



        }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                cf.progressdialog.dismiss("");
                t.printStackTrace();

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


}
