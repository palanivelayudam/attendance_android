package com.acceedo.attendancesystem.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.WindowManager;

import com.acceedo.attendancesystem.interfaces.APIService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.common.CommonFunction;
import com.acceedo.attendancesystem.common.Shared_Preference;
import com.google.gson.JsonObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseActivity extends AppCompatActivity {

    public CommonFunction cf;
    public Shared_Preference sharedPreference=null;
    public String refreshedToken = "";

    //for this to call progressbar is enable once until referesh tocken call to same service
    public boolean isCheckRefreshedToken=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cf = new CommonFunction(this);
        sharedPreference=new Shared_Preference(this);
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.DarkPrimaryColor));
        }
    }
    public void LogoutApi() {

        cf.progressdialog.show("");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(sharedPreference.getapiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(cf.initOkHttp())
                .build();

        APIService service = retrofit.create(APIService.class);
        Call<JsonObject> call = service.logout(sharedPreference.getuser_token());

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

                       /* if (response.has("success"))
                        {



                        }*/
                        if(response.has("message")){
                            startActivity(new Intent(BaseActivity.this,SplashScreen.class));
                            sharedPreference.ClearPreferences();
                            finish();
                            cf.show_toast(response.getString("message"));
                        }


                    } catch (JSONException e) {
                        cf.show_toast(getResources().getString(R.string.lbl_jsonerror));
                        e.printStackTrace();
                    }

                } else {
                    cf.showErrorHangling( BaseActivity.this,responseObject);
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
}
