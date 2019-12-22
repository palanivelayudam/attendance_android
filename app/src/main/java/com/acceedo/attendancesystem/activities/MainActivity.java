package com.acceedo.attendancesystem.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.acceedo.attendancesystem.Pojo.RecentScanDetails;
import com.acceedo.attendancesystem.adapter.TimeSheetListAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.crashlytics.android.Crashlytics;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.acceedo.attendancesystem.adapter.ListMenuAdapter;
import com.acceedo.attendancesystem.Pojo.ListMenu;
import com.acceedo.attendancesystem.Pojo.TaskInfo;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.apiclient.ApiClient;
import com.acceedo.attendancesystem.databinding.ActivityMainBinding;
import com.acceedo.attendancesystem.fragments.AboutFragment;
import com.acceedo.attendancesystem.fragments.DashBoardFragment;
import com.acceedo.attendancesystem.fragments.LeaveRequest;
import com.acceedo.attendancesystem.fragments.SettingsFragment;
import com.acceedo.attendancesystem.fragments.TimeSheet;
import com.acceedo.attendancesystem.interfaces.APIService;
import com.acceedo.attendancesystem.interfaces.RefreshTokenCallbacks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
   ActivityMainBinding mBinding;

    ListMenuAdapter mMenuAdapter;
    ArrayList <ListMenu> mMenuList;
    int prePos = -1;
    String[] nameList;

    public boolean isClose = false;

    String timeSheetDate="", timeSheetDropDown="";
    ArrayList <TaskInfo> mTaskList;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        mBinding.setData(this);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mBinding.drawer, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                mBinding.drawer.openDrawer(Gravity.START);
            }
        });

        toggle.setHomeAsUpIndicator(R.drawable.ictick);

        mBinding.drawer.addDrawerListener(toggle);
        getSupportActionBar();
        toggle.syncState();


        mBinding.tvName.setText(sharedPreference.getfull_name());

        Glide.with(this)
                .asBitmap()
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .load(sharedPreference.getuser_image())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition)
                    {
                        //holder.progressBar.setVisibility(View.VISIBLE);
                        mBinding.civProfileimg.setImageBitmap(cf.scaleToFitHeight(resource,150));
                        mBinding.civProfileimg.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        mBinding.civProfileimg.setImageResource(R.drawable.avatar);
                    }
                });

        mBinding.navigationView.setNavigationItemSelectedListener(this);
        mMenuList = new ArrayList <>();

        nameList = getResources().getStringArray(R.array.MenuItemNames);
        // TypedArray imgs = getResources().obtainTypedArray(R.array.MenuItemIcons);

        mBinding.homeMenuNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // displaySelectedScreen(0);
                onNavMenuClick(v);
                /*if (prePos > -1) {
                    mMenuList.get(prePos).setSelected(false);
                    mMenuAdapter.setList(mMenuList);
                    mMenuAdapter.notifyItemChanged(prePos);
                }*/
            }
        });

        onPrepareNavMenus();

        mBinding.ivnotifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timeSheetDate.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Date is : " + timeSheetDate + "\n DropDown : " + timeSheetDropDown, Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBinding.llProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this,ProfileActvity.class));
            }
        });

        mBinding.llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();

            }
        });

        try {
            PackageInfo pInfo = MainActivity.this.getPackageManager().getPackageInfo(MainActivity.this.getPackageName(), 0);
            mBinding.tvVersion.setText("v"+" "+pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void onMenuChangeDesign(int position) {

        for (int i = 0; i < mMenuList.size(); i++)
            mMenuList.get(i).setSelected(i == position);

        mMenuAdapter.setList(mMenuList);
        mMenuAdapter.notifyDataSetChanged();
        prePos = position;
        changeScreen(position);

        if (mBinding.drawer.isDrawerOpen(GravityCompat.START))
            mBinding.drawer.closeDrawer(GravityCompat.START);
    }

    public void changeScreen(int itemId) {

        Fragment fragment = null;

        switch (itemId) {

            case 0:
                SelectedMenuSettingOrAbout("selectedlistmenu");
                mBinding.tvtitle.setText(getResources().getString(R.string.dashbord));
                fragment = new DashBoardFragment();
                ((DashBoardFragment) fragment).setonMenuFragmentClickListner(new DashBoardFragment.onMenuFragmentClickListener() {
                    @Override
                    public void onClick(int position) {
                        onMenuChangeDesign(position);
                    }
                });
                break;
            case 1:
                SelectedMenuSettingOrAbout("selectedlistmenu");
                startActivity(new Intent(MainActivity.this, QrScanActivity.class));
                break;


                /*((TimeSheet) fragment).setOnDataChangeListener(new TimeSheet.onDataChangeListener() {
                    @Override
                    public void onDataChange(String date, String dropdown, ArrayList <TaskInfo> list) {
                        onTimeSheetDataChange(date, dropdown, list);
                    }
                });*/


            case 2:
                SelectedMenuSettingOrAbout("selectedlistmenu");
                mBinding.tvtitle.setText(getResources().getString(R.string.useronline));
                fragment = new TimeSheet();
                break;
            case 3:
                SelectedMenuSettingOrAbout("selectedlistmenu");
                mBinding.tvtitle.setText(getResources().getString(R.string.setting));
                fragment = new LeaveRequest();
                break;

            case 4:
                SelectedMenuSettingOrAbout("selectedlistmenu");
                logoutDialog();
                break;
                /*
            case 3:

                SelectedMenuSettingOrAbout("selectedlistmenu");
                mBinding.tvtitle.setText(getResources().getString(R.string.leaverequest));
                fragment = new SettingsFragment();
                ((SettingsFragment) fragment).setmOnSettingsFragmentClickListener(new SettingsFragment.onSettingsFragmentClickListener() {
                    @Override
                    public void onClick(int position) {
                        onMenuChangeDesign(position);
                    }
                });
                break;
            case 4:
                SelectedMenuSettingOrAbout("selectedlistmenu");
                mBinding.tvtitle.setText(getResources().getString(R.string.referrals));
                fragment = new LeaveRequest();
                break;
            case 5:
                SelectedMenuSettingOrAbout("selectedlistmenu");
                mBinding.tvtitle.setText(getResources().getString(R.string.announcement));
                fragment = new LeaveRequest();
                break;
            case 6:
                SelectedMenuSettingOrAbout("selectedlistmenu");
                mBinding.tvtitle.setText(getResources().getString(R.string.gallery));
                fragment = new LeaveRequest();
                break;*/
            default://Default
                SelectedMenuSettingOrAbout("selectedlistmenu");
                fragment = new DashBoardFragment();
                break;

        }

        mBinding.rlAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedMenuSettingOrAbout("about");
                changeFragmentSettingOrAbout("about");

            }
        });
        mBinding.rlSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedMenuSettingOrAbout("setting");
                changeFragmentSettingOrAbout("setting");

            }
        });
        if (isClose) {
            MainActivity.this.finish();
            return;
        }

        // Replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.home_content_body, fragment);
            ft.commit();
        }

        mBinding.drawer.closeDrawer(GravityCompat.START);

    }

    private void changeFragmentSettingOrAbout(String selectedFragment) {
        Fragment fragment =null;
        if(selectedFragment.equals("setting")){
             fragment = new SettingsFragment();

        }else {
            fragment= new AboutFragment();
        }
        if (prePos > -1) {
            mMenuList.get(prePos).setSelected(false);
            mMenuAdapter.setList(mMenuList);
            mMenuAdapter.notifyItemChanged(prePos);
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.home_content_body, fragment);
            ft.commit();
        }

        mBinding.drawer.closeDrawer(GravityCompat.START);
    }

    private void SelectedMenuSettingOrAbout(String selectedType) {
        if(selectedType.equals("setting")){
            mBinding.tvtitle.setText(getResources().getString(R.string.setting));
            mBinding.tvSettings.setTextColor(getResources().getColor(R.color.PrimaryColor));
            mBinding.rlSetting.setBackgroundColor(getResources().getColor(R.color.Gray200));
            mBinding.rlAbout.setBackgroundColor(getResources().getColor(R.color.white));
            mBinding.tvabout.setTextColor(getResources().getColor(R.color.Gray600));
        }else if(selectedType.equals("about")) {
            mBinding.tvtitle.setText(getResources().getString(R.string.about));
            mBinding.tvabout.setTextColor(getResources().getColor(R.color.PrimaryColor));
            mBinding.rlAbout.setBackgroundColor(getResources().getColor(R.color.Gray200));
            mBinding.rlSetting.setBackgroundColor(getResources().getColor(R.color.white));
            mBinding.tvSettings.setTextColor(getResources().getColor(R.color.Gray600));
        }else {
            mBinding.tvabout.setTextColor(getResources().getColor(R.color.Gray600));
            mBinding.rlSetting.setBackgroundColor(getResources().getColor(R.color.white));
            mBinding.rlAbout.setBackgroundColor(getResources().getColor(R.color.white));
            mBinding.tvSettings.setTextColor(getResources().getColor(R.color.Gray600));
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onBackPressed() {

        if (mBinding.drawer.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (mMenuList.get(0).isSelected()) {
            isClose = true;
            super.onBackPressed();
        }
        if (!mMenuList.get(0).isSelected()) {
            onMenuChangeDesign(0);
            return;
        }
        //super.onBackPressed();
    }

    public void onNavMenuClick(View v) {
        if (!mBinding.drawer.isDrawerOpen(GravityCompat.START))
            mBinding.drawer.openDrawer(GravityCompat.START);
        else
            mBinding.drawer.closeDrawer(GravityCompat.START);
    }

    private void onTimeSheetDataChange(String date, String dropdown, ArrayList <TaskInfo> list) {
        timeSheetDate = date;
        timeSheetDropDown = dropdown;
        mTaskList = list;
    }

    private void onPrepareNavMenus() {
        for (int i = 0; i < nameList.length; i++) {
            ListMenu item = new ListMenu();
            item.setName(nameList[i]);
            item.setSelected(false);
            mMenuList.add(item);
        }


        if (mMenuList.size() > 0) {
            mMenuAdapter = new ListMenuAdapter(MainActivity.this, mMenuList);
            mBinding.rvMenu.setLayoutManager(new LinearLayoutManager(this));
            mBinding.rvMenu.setAdapter(mMenuAdapter);
            mMenuAdapter.notifyDataSetChanged();
            mMenuAdapter.setOnClickListener(new ListMenuAdapter.OnClickListener() {
                @Override
                public void onMenuClick(int position) {
                    onMenuChangeDesign(position);
                }
            });
        }

        onMenuChangeDesign(0);
    }

    private void logoutDialog() {
        Window mAlertDialogWindow;

        final AlertDialog dialog1 = new AlertDialog.Builder(MainActivity.this)
                .create();
        dialog1.show();

        dialog1.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog1.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog1.setCancelable(true);

        mAlertDialogWindow = dialog1.getWindow();
        View contv = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.dialog_logout, null);
        contv.setFocusable(true);
        contv.setFocusableInTouchMode(true);

        mAlertDialogWindow.setBackgroundDrawableResource(R.drawable.material_dialog_window);

        mAlertDialogWindow.setContentView(contv);


        TextView dialog_Txt = mAlertDialogWindow.findViewById(R.id.tvlogout);
        Button okBtn = mAlertDialogWindow.findViewById(R.id.btnok);
        Button cancelBtn = mAlertDialogWindow.findViewById(R.id.btncancel);
        dialog_Txt.setText(getResources().getString(R.string.logouttext));

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cf.isInternetOn()==true)
                {
                    LogoutApi();

                }
                else
                {

                  //  No_InternetDialog();
                }

                dialog1.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        countdownApi();
    }

    private void countdownApi() {
        cf.progressdialog.show("");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(sharedPreference.getapiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(cf.initOkHttp())
                .build();

        APIService service = retrofit.create(APIService.class);
        Call<JsonObject> call = service.countdownapi(sharedPreference.getuser_token());

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
                            if(response.has("data")){

                                if(countDownTimer!=null)
                                {
                                    countDownTimer.cancel();
                                }
                                JSONObject countdownObj=response.getJSONObject("data");
                                startTimer(countdownObj.getInt("count_down"));
                                if(countdownObj.has("loggedin")){
                                    String s= String.valueOf(countdownObj.getInt("loggedin"));
                                    sharedPreference.setloggedinCount(s);

                                }
                                if(countdownObj.has("required")){
                                    String required= String.valueOf(countdownObj.getInt("required"));

                                    sharedPreference.setrequired(required);
                                }
                                if(countdownObj.has("ratio")){
                                    String ratio= String.valueOf(countdownObj.getInt("ratio"));

                                    sharedPreference.setratio(ratio);
                                }

                            }

                        }

                    } catch (JSONException e) {
                        cf.show_toast(getResources().getString(R.string.lbl_jsonerror));
                        e.printStackTrace();
                    }

                } else {
                    cf.showErrorHangling( MainActivity.this,responseObject);
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

    private void startTimer(int noOfMinutes) {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                mBinding.tvcountdown.setText(hms);//set text
            }
            public void onFinish() {
                if(countDownTimer!=null)
                countDownTimer.cancel();
                mBinding.tvcountdown.setText("TIME'S UP!!"); //On finish change timer text
            }
        }.start();

    }
}
