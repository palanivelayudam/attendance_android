package com.acceedo.attendancesystem.activities;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.acceedo.attendancesystem.Pojo.PlacesResult;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.adapter.CustomListAdapter;
import com.acceedo.attendancesystem.apiclient.ApiClient;
import com.acceedo.attendancesystem.databinding.ActivityProfileActvityBinding;
import com.acceedo.attendancesystem.databinding.DialogNointernetBinding;
import com.acceedo.attendancesystem.interfaces.APIService;
import com.acceedo.attendancesystem.interfaces.RefreshTokenCallbacks;
import com.acceedo.attendancesystem.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProfileActvity extends BaseActivity {
    ActivityProfileActvityBinding mBinding;
    ArrayList<PlacesResult> mList;
    String BASE_URL = "https://maps.googleapis.com";
    //String apiKey = "AIzaSyAv7jYF9coH1Im7GWcHtKXK865GkZLDu3k";
    String apiKey = "AIzaSyAXMQQRvItAQBZPwkLLSMjowyyJYzzDVac";

    String s_attachment = "",passwordStr="";
    public Uri CapturedImageURI;
    BottomSheetDialog mImageDialog;

    private static final int PERMISSION_ALL = 104;
    static int GALLERY_PICTURE = 25, CAMERA_REQUEST = 26, IMAGE_CROP_REQUEST = 27;

    int selectGenderId = 1;
    AlertDialog dialog1,dialog2;
    String strselectGender = "",percentage = "",dialogtype = "",dob = "";
    String firstname = "", lastname = "", email = "", phonenumber = "", phonenumber1 = "", address = "", birthcity = "", name = "",
            image = "", countrycode = "", gender = "", strCity = "", strZipcode = "", strState = "", strCountry = "";

    boolean selectable = true,loginpasswordvisible=true;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mBinding= DataBindingUtil.setContentView(this,R.layout.activity_profile_actvity);

        mBinding.ccp.hideNameCode(true);

        mBinding.seekBarPercen.getThumb().mutate().setAlpha(0);
//        mSeekBar.setEnabled(false);

        mBinding.seekBarPercen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });



       mBinding.tvMale.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
              keyBoardHide();

                if (selectable) {
                    selectGenderId = 1;
                   maleSelectColorChange();
                }


            }
        });
        mBinding.tvfemale.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                keyBoardHide();

                if (selectable) {
                    selectGenderId = 0;

                   femaleSelectColorChange();

                }

            }
        });


        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate1()) {
                    finish();
                } else {
                    showMobileVerificationDialog("backListner");
                    // cf.show_toast("Are you sure want to delete ");
                }

            }
        });

        mBinding.llchangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActvity.this,ChangePassword.class));
            }
        });

        mBinding.civprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.flImagePicker.performClick();
            }
        });

        mBinding.flImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

                    if (!hasPermissions(ProfileActvity.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(ProfileActvity.this, PERMISSIONS, PERMISSION_ALL);
                    } else {
                        showImagePickerDialog();
                    }

                } else {
                    showImagePickerDialog();
                }

//                showImagePickerDialog();
            }
        });

        //Autocomplete places
        // CustomAutoCompleteAdapter adapter = new CustomAutoCompleteAdapter(ProfileNewActivity.this);

        // autoTxtFullAddress.setAdapter(adapter);

        mBinding.etFulladdress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1= String.valueOf(s);
                placesApiCall(s1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBinding.etFulladdress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {

                mBinding.etCountry.setText("");
                mBinding.etState.setText("");
                mBinding.etCity.setText("");
                mBinding.etZipcode.setText("");

                String addressstr = String.valueOf(parent.getItemAtPosition(position));
                // Toast.makeText(ProfileNewActivity.this, addressstr, Toast.LENGTH_SHORT).show();
                getLocationFromAddress(addressstr);
                // List<Place> mgetdate=
            }
        });

        mBinding.etFulladdress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.et_fulladdress) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mBinding.etFulladdress, InputMethodManager.SHOW_IMPLICIT);
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

        mBinding.llediticon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                selectable = true;
                mBinding.btnUpdate.setVisibility(View.VISIBLE);
                mBinding.lltotallayout.setAlpha(1);
                mBinding.llmobile.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gray400)));
                mBinding.ccp.setTextColor(getResources().getColor(R.color.DarkPrimaryColor));

                mBinding.rlfirstname.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gray400)));
                mBinding.rlemail.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gray400)));
                mBinding.rldob.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gray400)));
                mBinding.rladdress.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gray400)));
                mBinding.rlcity.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gray400)));
                mBinding.rlstate.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gray400)));

                mBinding.etFirstname.setEnabled(true);
                mBinding.etLastname.setEnabled(true);
                mBinding.etEmail.setEnabled(true);
                mBinding.etFulladdress.setEnabled(true);
                mBinding.etDateofbirth.setEnabled(true);
                mBinding.etMobileno.setEnabled(true);
                mBinding.etCity.setEnabled(true);
                mBinding.etCountry.setEnabled(true);
                mBinding.etState.setEnabled(true);
                mBinding.etZipcode.setEnabled(true);

                mBinding.etFirstname.setFocusable(true);
                mBinding.etFirstname.setFocusableInTouchMode(true);

                mBinding.etLastname.setFocusable(true);
                mBinding.etLastname.setFocusableInTouchMode(true);
                mBinding.etEmail.setFocusable(true);
                mBinding.etFulladdress.setFocusable(true);
                mBinding.etCity.setFocusable(true);
                mBinding.etCountry.setFocusable(true);
                mBinding.etState.setFocusable(true);
                mBinding.etZipcode.setFocusable(true);

                
                mBinding.etEmail.setFocusableInTouchMode(true);
                mBinding.etFulladdress.setFocusableInTouchMode(true);

                mBinding.etCity.setFocusableInTouchMode(true);
                mBinding.etCountry.setFocusableInTouchMode(true);
                mBinding.etState.setFocusableInTouchMode(true);
                mBinding.etZipcode.setFocusableInTouchMode(true);


            }
        });

        mBinding.ivinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMobileVerificationDialog("percentageInfoListner");
            }
        });

        mBinding.rrpercentagelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.ivinfo.performClick();
            }
        });
        mBinding.etMobileno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cf.hidekeyboard(mBinding.etMobileno);
                if (selectable) {
                    showMobileVerificationDialog("mobileListner");
                }


            }
        });

        mBinding.etDateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectable) {
                    BottomDatePicker();
                }
            }
        });

        mBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    if (cf.isInternetOn() == true) {
                        if (cf.eMailValidation(mBinding.etEmail.getText().toString())) {
                            profileUpdateService();
                        } else {
                            cf.show_toast(getResources().getString(R.string.entervalidemail));
                        }
                    } else {
                        dialogtype = "updateserver";
                        No_InternetDialog(dialogtype);
                    }

                }
            }
        });


        String code = sharedPreference.getcountry_code().replaceAll("\\D+", "");
        Log.i("CODE : ", code);

        mBinding.ccp.setCountryForPhoneCode(Integer.parseInt(code));

        mBinding.etFirstname.setText(sharedPreference.getfirstname());
        mBinding.etLastname.setText(sharedPreference.getlastname());
        mBinding.etEmail.setText(sharedPreference.getuser_email());
        mBinding.etMobileno.setText(sharedPreference.getmobile());
        mBinding.tvprofilename.setText(sharedPreference.getfull_name());
        // etAddress.setText(address);
//                            etCode.setText(countrycode);
        if (!sharedPreference.getbirth_date().isEmpty() && !sharedPreference.getbirth_date().equals("null")) {
            dob = sharedPreference.getbirth_date();

            mBinding.etDateofbirth.setText(cf.FormatDate(dob));
        } else {
            mBinding.etDateofbirth.setText("");
            // birthcity = "";
            //dob = "";
        }
        if (!sharedPreference.getaddress().isEmpty() && !sharedPreference.getaddress().equals("null")) {
            mBinding.etFulladdress.setText(sharedPreference.getaddress());
        } else {
            mBinding.etFulladdress.setText("");
        }

        if (!sharedPreference.getcity().isEmpty() && !sharedPreference.getcity().equals("null")) {
            mBinding.etCity.setText(sharedPreference.getcity());
        } else {
            mBinding.etCity.setText("");
            //strCity = "";
        }

        if (!sharedPreference.getzipcode().isEmpty() && !sharedPreference.getzipcode().equals("null")) {
            mBinding.etZipcode.setText(sharedPreference.getzipcode());
        } else {
            mBinding.etZipcode.setText("");
        }

        if (!sharedPreference.getcountry_code().isEmpty() && !sharedPreference.getcountry_code().equals("null")) {
            mBinding.etCountry.setText(sharedPreference.getcountry_code());
        } else {
            mBinding.etCountry.setText("");
        }


        if (!sharedPreference.getstate().isEmpty() && !sharedPreference.getstate().equals("null")) {
            mBinding.etState.setText(sharedPreference.getstate());
        } else {
            mBinding.etState.setText("");
            //strState = "";
        }


        if (!sharedPreference.getgender().equals("") && !sharedPreference.getgender().equals("null")) {
            if (sharedPreference.getgender().equals("1")) {
               maleSelectColorChange();

            } else if (sharedPreference.getgender().equals("0")){
                femaleSelectColorChange();
            }else {

                noneSelectMaleorFemale();

            }
        } else {
            noneSelectMaleorFemale();

        }

        if (sharedPreference.getuser_image().isEmpty()) {
            Glide.with(ProfileActvity.this).load(R.drawable.avatar).into(mBinding.civprofile);
        } else {
            Glide.with(ProfileActvity.this).load(sharedPreference.getuser_image()).into(mBinding.civprofile);
        }
        setEdditablefalse();
        if(cf.isInternetOn()==true)
        {
            getUserDetailsService();
        }
        else
        {
            dialogtype="getprofile";
            No_InternetDialog(dialogtype);
        }



    }

    @SuppressLint("NewApi")
    private void noneSelectMaleorFemale() {
        mBinding.tvMale.setBackground(getResources().getDrawable(R.drawable.bgleftcurve));
        mBinding.tvMale.setBackgroundTintList(getResources().getColorStateList(R.color.transparent));
        mBinding.tvfemale.setBackground(getResources().getDrawable(R.drawable.bgrightcurve));
        mBinding.tvfemale.setBackgroundTintList(getResources().getColorStateList(R.color.transparent));

        mBinding.tvfemale.setTextColor(getResources().getColor(R.color.PrimaryColor));
        mBinding.tvMale.setTextColor(getResources().getColor(R.color.PrimaryColor));
    }

    @SuppressLint("NewApi")
    private void maleSelectColorChange() {
        mBinding.tvMale.setBackground(getResources().getDrawable(R.drawable.bgleftcurve));
        mBinding.tvMale.setBackgroundTintList(getResources().getColorStateList(R.color.PrimaryColor));
        mBinding.tvfemale.setBackground(getResources().getDrawable(R.drawable.bgrightcurve));
        mBinding.tvfemale.setBackgroundTintList(getResources().getColorStateList(R.color.transparent));

        mBinding.tvfemale.setTextColor(getResources().getColor(R.color.PrimaryColor));
        mBinding.tvMale.setTextColor(getResources().getColor(R.color.white));
    }

    @SuppressLint("NewApi")
    private void femaleSelectColorChange() {
        mBinding.tvMale.setBackground(getResources().getDrawable(R.drawable.bgleftcurve));
        mBinding.tvMale.setBackgroundTintList(getResources().getColorStateList(R.color.transparent));
        mBinding.tvfemale.setBackground(getResources().getDrawable(R.drawable.bgrightcurve));
        mBinding.tvfemale.setBackgroundTintList(getResources().getColorStateList(R.color.PrimaryColor));

        mBinding.tvMale.setTextColor(getResources().getColor(R.color.PrimaryColor));
        mBinding.tvfemale.setTextColor(getResources().getColor(R.color.white));
    }

    private void keyBoardHide() {
        cf.hidekeyboard(mBinding.etFirstname);
        cf.hidekeyboard(mBinding.etLastname);
        cf.hidekeyboard(mBinding.etMobileno);
        cf.hidekeyboard(mBinding.etFulladdress);
        cf.hidekeyboard(mBinding.etCity);
        cf.hidekeyboard(mBinding.etState);
        cf.hidekeyboard(mBinding.etDateofbirth);
//                cf.hidekeyboard(etCode);
        cf.hidekeyboard(mBinding.etCountry);
    }



    private void showMobileVerificationDialog(final String lisnerType) {

        Window mAlertDialogWindow;

        dialog1 = new AlertDialog.Builder(ProfileActvity.this)
                .create();
        dialog1.show();

        dialog1.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog1.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mAlertDialogWindow = dialog1.getWindow();
        DialogNointernetBinding mdialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(ProfileActvity.this), R.layout.dialog_nointernet, null, false);

        mdialogBinding.getRoot().getRootView().setFocusable(true);
        mdialogBinding.getRoot().getRootView().setFocusableInTouchMode(true);

        dialog1.setCancelable(false);

        mAlertDialogWindow
                .setBackgroundDrawableResource(R.drawable.material_dialog_window);

        mAlertDialogWindow.setContentView(mdialogBinding.getRoot());

        mdialogBinding.btnyes.setText(getString(R.string.ok));
        mdialogBinding.btnno.setText(getString(R.string.cancel));

        if(lisnerType.equals("backListner")){
            mdialogBinding.tvdetails.setText(getString(R.string.unsavedchagesprofile));
            mdialogBinding.btnyes.setVisibility(View.VISIBLE);
            mdialogBinding.btnno.setVisibility(View.VISIBLE);
        }else {
            String s=getString(R.string.filldialogtextpartfirst)+" "+percentage+getString(R.string.filldilogtextparttwo);
            mdialogBinding.tvdetails.setText(s);
            mdialogBinding.btnno.setVisibility(View.GONE);
        }


        mdialogBinding.btnyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(lisnerType.equals("backListner")){
                    finish();
                }else {
                    dialog1.dismiss();
                }


            }
        });

        mdialogBinding.btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog1.dismiss();
            }
        });

    }



    private void BottomDatePicker()
    {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ProfileActvity.this);
       /* BottomSheetDatepickerBinding mdatePickBinding = DataBindingUtil.
                inflate(LayoutInflater.from(ProfileActvity.this), R.layout.dialog_nointernet, null, false);
*/
        View sheetView = ProfileActvity.this.getLayoutInflater().inflate(R.layout.bottom_sheet_datepicker, null);
        mBottomSheetDialog.setContentView(sheetView);

        TextView tvdone = sheetView.findViewById(R.id.tvdone);
        final DatePicker picker=(DatePicker)sheetView.findViewById(R.id.datePicker1);

        Calendar c1 = Calendar.getInstance();
        picker.setMaxDate(c1.getTimeInMillis()-10000);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) sheetView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        View parent = (View) sheetView.getParent();
        parent.setFitsSystemWindows(true);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(parent);
        int screenHeight = getViewHeight(sheetView);
        bottomSheetBehavior.setPeekHeight(screenHeight);

        params.height = screenHeight;
        parent.setLayoutParams(params);

        tvdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();

                dob = cf.checkDigit(picker.getYear()) + "-" + cf.checkDigit((picker.getMonth() + 1)) + "-" + cf.checkDigit(picker.getDayOfMonth());

                mBinding.etDateofbirth.setText(cf.FormatDate(dob));
            }
        });

        mBottomSheetDialog.show();
    }
    public int getViewHeight(View view) {
        WindowManager wm =
                (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int deviceWidth;

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            Point size = new Point();
            display.getSize(size);
            deviceWidth = size.x;
        } else {
            deviceWidth = display.getWidth();
        }

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
        return view.getMeasuredHeight(); //        view.getMeasuredWidth();
    }
    boolean validate() {

        boolean failflag = false;

        if (mBinding.etFirstname.getText().toString().trim().isEmpty()) {
            cf.show_toast(getString(R.string.profile_error_enter_firstname));
            failflag = true;
            return failflag;
        }
        if (mBinding.etLastname.getText().toString().trim().isEmpty()) {
            //etLastName.setError(getString(R.string.profile_error_enter_lastname));
            cf.show_toast(getString(R.string.profile_error_enter_lastname));
            failflag = true;
            return failflag;
        }
        if (mBinding.etEmail.getText().toString().trim().isEmpty()) {
            cf.show_toast(getString(R.string.feedback_enteremail));
            failflag = true;
            return failflag;
        }

        return failflag;
    }

    boolean validate1() {

        boolean failflag = false;

        if (!firstname.equals(mBinding.etFirstname.getText().toString().trim())) {
            failflag = true;
            return failflag;
        }
        if (!lastname.equals(mBinding.etLastname.getText().toString().trim())) {
            failflag = true;
            return failflag;
        }
        if (!email.equals(mBinding.etEmail.getText().toString().trim())) {
            failflag = true;
            return failflag;
        }
        if (!phonenumber1.equals(mBinding.etMobileno.getText().toString().trim())) {
            failflag = true;
            return failflag;
        }
        if (!address.equals(mBinding.etFulladdress.getText().toString().trim())) {
            failflag = true;
            return failflag;
        }
        if (!birthcity.equals(dob)) {
            failflag = true;
            return failflag;
        }
        if (!strCity.equals(mBinding.etCity.getText().toString().trim())) {
            failflag = true;
            return failflag;
        }
        if (!strCountry.equals(mBinding.etCountry.getText().toString().trim())) {
            failflag = true;
            return failflag;
        }
        if (!strState.equals(mBinding.etState.getText().toString().trim())) {
            failflag = true;
            return failflag;
        }
        if (!strZipcode.equals(mBinding.etZipcode.getText().toString().trim())) {
            failflag = true;
            return failflag;
        }
        strselectGender= String.valueOf(selectGenderId);

        if (!gender.equals(strselectGender)) {
            failflag = true;
            return failflag;
        }

        return failflag;
    }

    @SuppressLint("NewApi")
    void setEdditablefalse() {

        selectable=false;

        mBinding.btnUpdate.setVisibility(View.GONE);
        mBinding.rlfirstname.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gray400)));
        mBinding.rlemail.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gray400)));
        mBinding.rldob.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gray400)));
        mBinding.rladdress.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gray400)));
        mBinding.rlcity.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gray400)));
        mBinding.rlstate.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gray400)));
        mBinding.llmobile.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gray400)));
        // ccp.setTextColor(getResources().getColor(R.color.Gray500));
        mBinding.lltotallayout.setAlpha(0.5f);


        mBinding.etFirstname.setFocusable(false);
        mBinding.etLastname.setFocusable(false);
        mBinding.etEmail.setFocusable(false);
//        etCode.setFocusable(false);
        mBinding.etFulladdress.setFocusable(false);
        mBinding.etDateofbirth.setFocusable(false);
        mBinding.etMobileno.setFocusable(false);


        mBinding.etCity.setFocusableInTouchMode(false);
        mBinding.etCountry.setFocusableInTouchMode(false);
        mBinding.etState.setFocusableInTouchMode(false);
        mBinding.etZipcode.setFocusableInTouchMode(false);
        mBinding.etDateofbirth.setFocusableInTouchMode(false);
        mBinding.etMobileno.setFocusableInTouchMode(false);

   }

    private void No_InternetDialog(final String gettype) {
        Window mAlertDialogWindow;

        dialog1 = new AlertDialog.Builder(ProfileActvity.this)
                .create();
        dialog1.show();

        dialog1.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog1.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mAlertDialogWindow = dialog1.getWindow();
        DialogNointernetBinding nointernetBinding = DataBindingUtil.
                inflate(LayoutInflater.from(ProfileActvity.this), R.layout.dialog_nointernet, null, false);

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
                    if(gettype.equals("getprofile")){
                        getUserDetailsService();
                    }else if(gettype.equals("updateserver")){
                        profileUpdateService();
                    }else if(gettype.equals("imageupload")){
                        uploadImageToServer();
                    }else if(gettype.equals("imageupload1")){
                        uploadImageToServer();
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

    void getUserDetailsService() {

        cf.progressdialog.show("");

        APIService service = ApiClient.getClient(getApplicationContext())
                .create(APIService.class);

        Call<JsonObject> call = service.getprofile(
                sharedPreference.getuser_token());


        //calling the api
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> responseObject) {
                //hiding progress dialog
                cf.progressdialog.dismiss("");

                if (responseObject.isSuccessful() && responseObject.body() != null) {
                    Log.i("Count Retrofit Response", responseObject.toString());
                    Log.i("Count Retrofit Response", responseObject.body().toString());

                    try {
                        final JSONObject response = new JSONObject(responseObject.body().toString());

                        if (response.has("user")) {
                            JSONObject jsonObject = response.getJSONObject("user");
                            firstname = jsonObject.getString("first_name");
                            lastname = jsonObject.getString("last_name");
                            email = jsonObject.getString("email");
                            phonenumber1 = jsonObject.getString("mobile_no");
                            phonenumber = jsonObject.getString("mobile_no");
                            address = jsonObject.getString("street");
                            birthcity = jsonObject.getString("birth_date");
                            strCity = jsonObject.getString("city");
                            strCountry = jsonObject.getString("country");
                            strState = jsonObject.getString("state");
                            strZipcode = jsonObject.getString("zipcode");
                            name = jsonObject.getString("first_name")+" "+jsonObject.getString("last_name");
                            image = jsonObject.getString("image");
                            countrycode=jsonObject.getString("country_code");
                            gender= String.valueOf(jsonObject.getInt("gender"));

                            String code = countrycode.replaceAll("\\D+","");
                            Log.i("CODE : ",code);

                            mBinding.ccp.setCountryForPhoneCode(Integer.parseInt(code));

                            mBinding.etFirstname.setText(firstname);
                            mBinding.etLastname.setText(lastname);
                            mBinding.etEmail.setText(email);
                            mBinding.etMobileno.setText(phonenumber);
                            mBinding.tvprofilename.setText(name);
                            // etAddress.setText(address);
//                            etCode.setText(countrycode);
                            if(!birthcity.isEmpty()&&!birthcity.equals("null")) {
                                dob =birthcity;

                                mBinding.etDateofbirth.setText(cf.FormatDate(dob));
                            }else {
                                mBinding.etDateofbirth.setText("");
                                birthcity="";
                                dob="";
                            }
                            if(!address.isEmpty()&&!address.equals("null")) {
                                mBinding.etFulladdress.setText(address);
                            }else {
                                mBinding.etFulladdress.setText("");
                                address="";
                            }

                            if(!strCity.isEmpty()&&!strCity.equals("null")) {
                                mBinding.etCity.setText(strCity);
                            }else {
                                mBinding.etCity.setText("");
                                strCity="";
                            }

                            if(!strZipcode.isEmpty()&&!strZipcode.equals("null")) {
                                mBinding.etZipcode.setText(strZipcode);
                            }else {
                                mBinding.etZipcode.setText("");
                                strZipcode="";
                            }

                            if(!strCountry.isEmpty()&&!strCountry.equals("null")) {
                                mBinding.etCountry.setText(strCountry);
                            }else {
                                mBinding.etCountry.setText("");
                                strCountry="";
                            }


                            if(!strState.isEmpty()&&!strState.equals("null")) {
                                mBinding.etState.setText(strState);
                            }else {
                                mBinding.etState.setText("");
                                strState="";
                            }



                            selectGenderId= Integer.parseInt(gender);
                            if(!gender.equals("")&&!gender.equals("null")) {
                                if (gender.equals("1")) {
                                   maleSelectColorChange();

                                } else if (gender.equals("0")){
                                    femaleSelectColorChange();
                                }else {
                                   noneSelectMaleorFemale();

                                }
                            }else {
                               noneSelectMaleorFemale();

                            }


                            sharedPreference.setfull_name(name);
                            sharedPreference.setuser_image(Constants.image+image);
                            sharedPreference.setapp_name(jsonObject.getString("app_name"));

                            if(!image.isEmpty())
                            {
                                Glide.with(ProfileActvity.this).load(sharedPreference.getuser_image()).into(mBinding.civprofile);

                            }else {
                                Glide.with(ProfileActvity.this).load(R.drawable.avatar).into(mBinding.civprofile);

                            }

                        }
                        if(response.has("completeness"))
                        {
                            percentage= String.valueOf(response.getInt("completeness"));
                            mBinding.tvvpercentage.setText("PROFILE"+" "+percentage+"%");
                            if(percentage.equals("100")){
                                mBinding.rrpercentagelayout.setVisibility(View.GONE);
                                mBinding.tvvpercentage.setVisibility(View.GONE);
                            }else {
                                mBinding.rrpercentagelayout.setVisibility(View.VISIBLE);
                                mBinding.tvvpercentage.setVisibility(View.VISIBLE);
                            }
                            mBinding.seekBarPercen.setProgress(response.getInt("completeness"));
                        }else {
                            mBinding.seekBarPercen.setProgress(response.getInt("completeness"));
                        }



                    } catch (JSONException e) {
                        cf.show_toast(getResources().getString(R.string.lbl_jsonerror));
                        e.printStackTrace();
                    }

                } else {
                    Log.i("Message", responseObject.message().toString());
                    Log.i("Retrofit Response", "Error in getGenericJson:" + responseObject.code() + " " + responseObject.message());
//                    cf.show_toast(getResources().getString(R.string.lbl_serverdown));

                    if (responseObject.code() == 401) {

                        cf.callRefreshToken(ProfileActvity.this, new RefreshTokenCallbacks() {
                            @Override
                            public void onSuccess(@NonNull boolean value) {
                                if (value) {
                                    Log.d("isCheckRefreshedToken", "false");
                                    isCheckRefreshedToken = false;
                                    Log.i("RefreshToken Response", "TRUE");
                                    getUserDetailsService();
                                } else {
                                    Log.d("isCheckRefreshedToken", "failure");

                                    cf.show_toast(getString(R.string.tockenfailederror));
                                    cf.progressdialog.dismiss("");
                                    Intent intent = new Intent(ProfileActvity.this, LoginAcitivity.class);
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
                                    Intent intent = new Intent(ProfileActvity.this, LoginAcitivity.class);
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
                        cf.showErrorHangling(ProfileActvity.this, responseObject);
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
                    if (jObj.has("message")){

                    }
                    // Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    void profileUpdateService() {

        cf.progressdialog.show("");

        APIService service = ApiClient.getClient(getApplicationContext())
                .create(APIService.class);

        Call<JsonObject> call = service.profileupdate(
                sharedPreference.getuser_token(),
                mBinding.etFirstname.getText().toString(),
                mBinding.etLastname.getText().toString(),
                mBinding.etFulladdress.getText().toString(),
                mBinding.etEmail.getText().toString(),
                selectGenderId,
                mBinding.etCountry.getText().toString(),
                mBinding.etCity.getText().toString(),
                mBinding.etState.getText().toString(),
                mBinding.etZipcode.getText().toString(),
                countrycode,
                phonenumber,
                dob);

        Log.d("testing11",mBinding.etFirstname.getText().toString()+" "+
                mBinding.etLastname.getText().toString()+" "+
                mBinding.etFulladdress.getText().toString()+" "+
                mBinding.etEmail.getText().toString()+" "+
                dob+" "+
                sharedPreference.getchange_lang());

        //calling the api
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> responseObject) {
                //hiding progress dialog
                cf.progressdialog.dismiss("");

                if (responseObject.isSuccessful() && responseObject.body() != null) {
                    Log.i("Count Retrofit Response", responseObject.toString());
                    Log.i("Count Retrofit Response", responseObject.body().toString());

                    try {
                        JSONObject response = new JSONObject(responseObject.body().toString());

                        if (response.has("message")) {
                            setEdditablefalse();
                            cf.show_toast(response.getString("message"));
                            getUserDetailsService();
                        }

                    } catch (JSONException e) {
                        cf.show_toast(getResources().getString(R.string.lbl_jsonerror));
                        e.printStackTrace();
                    }

                } else {
                    Log.i("Message", responseObject.message().toString());
                    Log.i("Retrofit Response", "Error in getGenericJson:" + responseObject.code() + " " + responseObject.message());
//                    cf.show_toast(getResources().getString(R.string.lbl_serverdown));

                    if (responseObject.code() == 401) {

                        cf.callRefreshToken(ProfileActvity.this, new RefreshTokenCallbacks() {
                            @Override
                            public void onSuccess(@NonNull boolean value)
                            {
                                if(value){
                                    Log.d("isCheckRefreshedToken","false");
                                    isCheckRefreshedToken=false;
                                    Log.i("RefreshToken Response","TRUE");
                                    profileUpdateService();
                                }else {
                                    Log.d("isCheckRefreshedToken","failure");

                                    cf.show_toast(getString(R.string.tockenfailederror));
                                    cf.progressdialog.dismiss("");
                                    Intent intent = new Intent(ProfileActvity.this, LoginAcitivity.class);
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
                                    Intent intent = new Intent(ProfileActvity.this, LoginAcitivity.class);
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
                        cf.showErrorHangling( ProfileActvity.this,responseObject);
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
                    if (jObj.has("message")){

                    }
                    // Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void getLocationFromAddress(String strAddress) {
        String postalCode="",country="",state="",city="";
        Geocoder coder = new Geocoder(this);
        //List<Address> address;
        // GeoPoint p1 = null;

        if (strAddress != null && !strAddress.isEmpty()) {
            try {
                List<Address> addressList = coder.getFromLocationName(strAddress, 1);
                if (addressList != null && addressList.size() > 0) {
                    double lat = addressList.get(0).getLatitude();
                    double lng = addressList.get(0).getLongitude();
                    // getseparateaddress(lat,lng);
                    String address = addressList.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    city = addressList.get(0).getLocality();
                    state = addressList.get(0).getAdminArea();
                    country = addressList.get(0).getCountryName();
                    postalCode = addressList.get(0).getPostalCode();
                    String knownName = addressList.get(0).getFeatureName();
                    String knownName1 = addressList.get(0).getSubLocality();
                    Log.d("testinglocation","address: "+address+"\n"+" "
                            +"city: "+city+"\n"+" "+"state: "+state+"\n"+" "+
                            "country: "+country+"\n"+" "+
                            "postalCode: "+postalCode+"\n"+" "+
                            "knownName1: "+knownName1+"\n"+" "+
                            "knownName: "+knownName+" ");
                    /*autoTxtFullAddress.setText(city
                    );*/

                    if(!city.isEmpty()&&city!=null){
                        mBinding.etCity.setText(city);
                    }else {
                        mBinding.etCity.setText("");
                    }

                    if(!country.isEmpty()&&country!=null){
                        mBinding.etCountry.setText(country);
                    }else {
                        mBinding.etCountry.setText("");
                    }

                    if(!state.isEmpty()&&state!=null){
                        mBinding.etState.setText(state);
                    }else {
                        mBinding.etState.setText("");
                    }

                    if(!postalCode.isEmpty()&&!postalCode.equals("null")){
                        mBinding.etZipcode.setText(postalCode);
                    }else {
                        mBinding.etZipcode.setText("");
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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
                    showImagePickerDialog();
                } else {
                    // Permission Denied
                    cf.show_toast(getString(R.string.llpermissionde));

                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void showImagePickerDialog() {

        LinearLayout vGallery, vCamera, vRemove;

        mImageDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.dialog_image_picker, null);

        vGallery = (LinearLayout) sheetView.findViewById(R.id.gallery_image);
        vCamera = (LinearLayout) sheetView.findViewById(R.id.camera_image);

        vGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                i.setType("image/*");
                startActivityForResult(i, GALLERY_PICTURE);

                mImageDialog.dismiss();

            }
        });

        vCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fileName = "temp.jpg";
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                CapturedImageURI = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, CapturedImageURI);
                startActivityForResult(intent, CAMERA_REQUEST);

                mImageDialog.dismiss();


            }
        });

        mImageDialog.setContentView(sheetView);
        mImageDialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (CapturedImageURI != null) {
            outState.putString("cameraImageUri", CapturedImageURI.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("cameraImageUri")) {
            CapturedImageURI = Uri.parse(savedInstanceState.getString("cameraImageUri"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK) {
            /*Uri selectedImage = data.getData();
            Log.d("testingdata",selectedImage.toString());
            Intent intent = new Intent(ProfileNewActivity.this, CropActivity.class);
            intent.putExtra("imageUri", selectedImage.toString());
            intent.putExtra("imageType", "profile");
            startActivityForResult(intent, IMAGE_CROP_REQUEST);*/
            Bitmap bitmapdb = null;
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);

            new ImageCompression().execute(filePath);

        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap bitmapdb = null;
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(CapturedImageURI, projection, null,
                    null, null);
            int column_index_data = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String capturedImageFilePath = cursor.getString(column_index_data);
            String filePath = capturedImageFilePath;

            new ImageCompression().execute(filePath);

        }else if (requestCode == IMAGE_CROP_REQUEST) {
            if (resultCode == RESULT_OK) {

                String imgPath = sharedPreference.getcrop_img();
                Log.d("TAGZZ", "Path:" + imgPath);

                s_attachment = imgPath;

                if(cf.isInternetOn()==true)
                {
                    uploadImageToServer();
                }
                else
                {
                    dialogtype="imageupload1";
                    No_InternetDialog(dialogtype);
                }


            }
        }else if(requestCode==555){
            if(resultCode == RESULT_OK) {
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        }
        /*else  if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
//                showErrorActivity(loginResult.getError());
                cf.show_toast(toastMessage);
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();

                    Log.i("TAG-getAccountId",loginResult.getAccessToken().getAccountId());
                    Log.i("TAG-getToken",loginResult.getAccessToken().getToken());
                    Log.i("TAG-getApplicationId",loginResult.getAccessToken().getApplicationId());

                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0,10));
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
//                goToMyLoggedInActivity();

                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
                        account_kit_id = account.getId();
                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        String phonenumber = phoneNumber.toString();
                        String countrycode = "";

                        Log.i("TAG-accountKitId",account_kit_id);
                        Log.i("TAG-phoneNumberString",phonenumber);

                        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                        try {
                            // phone must begin with '+'
                            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phonenumber, "");
                            countrycode = String.valueOf(numberProto.getCountryCode());
                            System.out.println("The country code is "+countrycode);

                            phonenumber = phonenumber.replace("+"+countrycode,"");
                            Log.i("Phone : ",phonenumber);

                            countrycode = "+"+countrycode;
                            Log.i("Country Code : ",countrycode);

                        } catch (NumberParseException e) {
                            System.out.println("NumberParseException was thrown: " + e.toString());
                        }

                        if(Arrays.asList(countryCodes).contains(countrycode))
                        {
                            CheckMobileRegistered(phonenumber,countrycode);
                        }
                        else
                        {
                            cf.show_toast("Th app is not available for your coountry to signup to the app. Until then you can enjoy listening to the Aartis");
                        }

                    }

                    @Override
                    public void onError(final AccountKitError error) {
                        // Handle Error
                        Log.i("AccountKitLogin",error.getErrorType().getMessage());
                    }
                });

            }*/

    }

    public Bitmap rotateBmp(Bitmap bmp, int orientation) {
        try {
            Matrix matrix = new Matrix();
            //set image rotation value to 90 degrees in matrix.
            matrix.postRotate(orientation);
            //supply the original width and height, if you don't want to change the height and width of bitmap.
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
            return bmp;
        }

    }

    private static Bitmap decodeFile(String file) {
        try {

            File f = new File(file);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 150;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE
                    && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            o.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }

        return null;
    }

    public class ImageCompression extends AsyncTask<String, Void, String> {

        //        private Context context;
        private static final float maxHeight = 1280.0f;
        private static final float maxWidth = 1280.0f;


//        public ImageCompression(Context context){
//            this.context=context;
//        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cf.progressdialog.show("");
        }

        @Override
        protected String doInBackground(String... strings) {

            System.out.println("The strings[0] is "+strings[0]);


            if(strings.length == 0 || strings[0] == null)
                return null;

            return compressImage(strings[0]);
        }

        protected void onPostExecute(String filePath){
            // imagePath is path of new compressed image.

            System.out.println("The image path is "+filePath);

            try
            {
                cf.progressdialog.dismiss("");
                Bitmap bitmapdb = decodeFile(filePath);

                if (bitmapdb == null) {
                    cf.show_toast(getResources().getString(R.string.lbl_filecorrupted));
                } else {
                    File imageFile = new File(filePath);

                    try {
                        ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());

                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                orientation = 270;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                orientation = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                orientation = 90;
                                break;
                        }

                        bitmapdb = rotateBmp(bitmapdb, orientation);

                        //civ_profileImg.setImageBitmap(bitmapdb);
                        Uri image= Uri.parse(filePath);

                        String s_attachment1 = String.valueOf(bitmapdb);
                        File f = new File(filePath);
                        Uri yourUri = Uri.fromFile(f);
                        Log.d("teestingdata",yourUri.toString());
                        Log.d("teestingdata",image.toString());
                        Log.d("teestingdata", String.valueOf(bitmapdb));

                        Intent intent = new Intent(ProfileActvity.this, CropActivity.class);
                        intent.putExtra("imageUri", yourUri.toString());
                        intent.putExtra("imageType", "profile");
                        startActivityForResult(intent, IMAGE_CROP_REQUEST);



                        //uploadImageToServer();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        public String compressImage(String imagePath) {
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

            float imgRatio = (float) actualWidth / (float) actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(imagePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

            if(bmp!=null)
            {
                bmp.recycle();
            }

            ExifInterface exif;
            try {
                exif = new ExifInterface(imagePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out = null;
            String filepath = getFilename();
            try {
                out = new FileOutputStream(filepath);

                //write the compressed bitmap at the destination specified by filename.
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return filepath;
        }

        public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }

            return inSampleSize;
        }

        public String getFilename() {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                    + "/WMS/Files/Compressed");

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()){
                mediaStorageDir.mkdirs();
            }

            String mImageName="IMG_"+ String.valueOf(System.currentTimeMillis()) +".jpg";
            String uriString = (mediaStorageDir.getAbsolutePath() + "/"+ mImageName);;
            return uriString;

        }

    }



    void uploadImageToServer() {

        cf.progressdialog.show("");

        File file1 = new File(s_attachment);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("picture", file1.getName(), RequestBody.create(MediaType.parse("image/*"), file1));

        RequestBody userToken = RequestBody.create(MediaType.parse("text/plain"), sharedPreference.getuser_token());

        APIService service = ApiClient.getClient(getApplicationContext())
                .create(APIService.class);


        Call<JsonObject> call = service.updateprofileimage(
                sharedPreference.getuser_token(), filePart);

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
                            if (response.has("message")) {
                                cf.show_toast(response.getString("message"));
                                getUserDetailsService();
                            }

                        } catch (JSONException e) {
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

                        cf.callRefreshToken(ProfileActvity.this, new RefreshTokenCallbacks() {
                            @Override
                            public void onSuccess(@NonNull boolean value)
                            {
                                if(value){
                                    Log.d("isCheckRefreshedToken","false");
                                    isCheckRefreshedToken=false;
                                    Log.i("RefreshToken Response","TRUE");
                                    profileUpdateService();
                                }else {
                                    Log.d("isCheckRefreshedToken","failure");

                                    cf.show_toast(getString(R.string.tockenfailederror));
                                    cf.progressdialog.dismiss("");
                                    Intent intent = new Intent(ProfileActvity.this, LoginAcitivity.class);
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
                                    Intent intent = new Intent(ProfileActvity.this, LoginAcitivity.class);
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
                        cf.showErrorHangling( ProfileActvity.this,responseObject);
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



    public void placesApiCall(String searchtext) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);


        Call <JsonObject> call = service.getplacesapi(
                searchtext,"true",
                apiKey);

        //calling the api
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call <JsonObject> call, Response<JsonObject> responseObject) {
                //hiding progress dialog

                if (responseObject.isSuccessful() && responseObject.body() != null) {
                    Log.i("Retrofit Response", responseObject.toString());
                    Log.i("Retrofit Response", responseObject.body().toString());

                    try {
                        JSONObject response = new JSONObject(responseObject.body().toString());

                        try {
                            if(response.has("status")){
                                if(response.getString("status").equals("OK")){
                                    if (response.has("predictions")) {
                                        JSONArray jsonArray=response.getJSONArray("predictions");
                                        mList=new ArrayList<>();
                                        ArrayList<String> addressList=new ArrayList <>();
                                        for (int i=0;i<jsonArray.length();i++)
                                        {
                                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                                            PlacesResult item=new PlacesResult();
                                            item.setDescription(jsonObject.getString("description"));
                                            item.setId(jsonObject.getString("place_id"));
                                            mList.add(item);
                                            addressList.add(jsonObject.getString("description"));

                                        }
                                        CustomListAdapter adapter = new CustomListAdapter(ProfileActvity.this,
                                                R.layout.customlayout, addressList);
                                        mBinding.etFulladdress.setAdapter(adapter);

                                        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                                (ProfileNewActivity.this,android.R.layout.select_dialog_item,addressList);
                                        autoTxtFullAddress.setAdapter(adapter);*/
                                        Log.d("addressList", String.valueOf(mList));
                                    }
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();



                        }

                    } catch (JSONException e) {
                        e.printStackTrace();



                    }

                } else {
                    Log.i("Message", responseObject.message().toString());
                    Log.i("Retrofit Response", "Error in getGenericJson:" + responseObject.code() + " " + responseObject.message());
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//				progressdialog.dismiss("");
                t.printStackTrace();



            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(!validate1()){
            finish();
        }else {
            showMobileVerificationDialog("backListner");
            //  cf.show_toast("Are you sure want to delete ");
        }
    }
}
