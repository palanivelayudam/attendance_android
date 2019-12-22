package com.acceedo.attendancesystem.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mateialprogressbar.CustomProgressDialog;
import com.google.gson.JsonObject;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.activities.LoginAcitivity;
import com.acceedo.attendancesystem.apiclient.ApiClient;
import com.acceedo.attendancesystem.interfaces.APIService;
import com.acceedo.attendancesystem.interfaces.RefreshTokenCallbacks;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonFunction {
    Context con;
    public Typeface tfregular, tfmedium, tflight, tfbold, tfmonstusemibold, tfmonstumedium, tfmonsturegular, tfrobotosemibol, tfrobotomedium, tfrobotoregular;
    public CustomProgressDialog progressdialog;
    Shared_Preference sharedPreference;
    public static boolean confirmationCheck = false;

    public static String clickedspinner = "";
    String valuReturnMessage = "";

    public CommonFunction(Context cont) {
        con = cont;

        progressdialog = new CustomProgressDialog(con);
        sharedPreference = new Shared_Preference(con);
        tfrobotosemibol = ResourcesCompat.getFont(con, R.font.robotobold);
        tfrobotomedium = ResourcesCompat.getFont(con, R.font.robotomedium);
        tfrobotoregular = ResourcesCompat.getFont(con, R.font.robotoregular);

    }

    public final boolean isInternetOn() {
        boolean chk = false;
        ConnectivityManager conMgr = (ConnectivityManager) this.con
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMgr.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            chk = true;
        } else {
            chk = false;
        }

        return chk;
    }

    public boolean getConnectedNetwork() {
        String connectednetwork = "";
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        boolean status = false;

        ConnectivityManager cm = (ConnectivityManager) this.con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            connectednetwork = "WIFI";
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
            connectednetwork = "MOBILE";
        }

        if (haveConnectedWifi) {
            status = true;
        } else if (haveConnectedWifi && haveConnectedMobile) {
            status = true;
        } else {
            status = false;
        }

        Log.i("haveConnectedWifi", String.valueOf(haveConnectedWifi));
        Log.i("haveConnectedMobile", String.valueOf(haveConnectedMobile));

        return status;
    }

    public void hidekeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) con
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public boolean eMailValidation(CharSequence stringemailaddress1) {
        // TODO Auto-generated method stub
//		String EMAIL_PATTERN="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";//for not accepting more than 1 dot after @
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(stringemailaddress1);

        return matcher.matches();

    }

    public void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(con.getAssets(), "montserratsemibold.ttf");
        //Typeface font = Typeface.createFromAsset(con.getAssets(), "montserratregular.ttf");
        //Typeface font = Typeface.createFromAsset(con.getAssets(), "nova-bold.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //mNewTitle.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, mNewTitle.length(), 0); Use this if you want to center the items
        mi.setTitle(mNewTitle);
    }

    public void applyFontToMenuItemregular(MenuItem mi) {
        //Typeface font = Typeface.createFromAsset(con.getAssets(), "montserratsemibold.ttf");
        Typeface font = Typeface.createFromAsset(con.getAssets(), "robotoregular.ttf");
        //Typeface font = Typeface.createFromAsset(con.getAssets(), "nova-bold.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //mNewTitle.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, mNewTitle.length(), 0); Use this if you want to center the items
        mi.setTitle(mNewTitle);
    }

    public boolean passwordValidation(CharSequence stringpassword) {
        //Minimum eight characters, at least one letter and one number:
        //"^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$"

        //Minimum eight characters, at least one letter, one number and one special character:
        //"^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$"

        //Minimum eight characters, at least one uppercase letter, one lowercase letter and one number:
        //"^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$"

        //Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character:
        //"^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}"

        //Minimum eight and maximum 10 characters, at least one uppercase letter, one lowercase letter, one number and one special character:
        //"^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,10}"

        final String PASSWORD_PATTERN = "^.*(?=.{6,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(stringpassword);

        return matcher.matches();

    }

    public boolean isGlobalPhoneNumber(String phoneNumber) {

        Pattern pattern = Pattern.compile("\\d{10}");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();

    }

    public void show_toast(String msg) {
//		Toast.makeText(con, msg, Toast.LENGTH_SHORT).show();

        Toast toast = Toast.makeText(con, msg, Toast.LENGTH_SHORT);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTypeface(tfrobotoregular);
        toast.show();

    }

    public void show_toast_long(String msg) {
//		Toast.makeText(con, msg, Toast.LENGTH_LONG).show();

        Toast toast = Toast.makeText(con, msg, Toast.LENGTH_LONG);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTypeface(tfrobotoregular);
        toast.show();
    }

    public void SetToolBarTypeface(Toolbar toolbar) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);

            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setTypeface(tfrobotoregular);
            }
        }
    }

    public int DP(int dp) {
        final float scale = con.getResources().getDisplayMetrics().density;
        int dp1 = (int) (dp * scale + 0.5f);

        return dp1;
    }

    public static String fmt(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }

    public String decimalformat(float d) {
        NumberFormat formatter = new DecimalFormat("#0.00");

        System.out.println(formatter.format(d));

        return formatter.format(d);
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public String checkDigit(long number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public String checkDigitDecimal(int number) {
        return number <= 9 ? number + "0" : String.valueOf(number);
    }

    public String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    /*public  void  collapsingtoolbar(CollapsingToolbarLayout collapsingToolbarLayout){
        Typeface typeface = ResourcesCompat.getFont(con, R.font.robotoregular);
        collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);
    }
*/
    public static Bitmap scaleToFitHeight(Bitmap b, int height) {
        float factor = height / (float) b.getHeight();
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor), height, true);
    }

    public String getMonthName(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
        return monthName;
    }

    public String getDate(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String strdate = new SimpleDateFormat("dd").format(cal.getTime());
        return strdate;
    }

    public String FormatDateyyyyMMdd_ddMMMyyyy(String strdate) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
//			SimpleDateFormat format2 = new SimpleDateFormat("MMMM-dd-yyyy");

            Date date = format1.parse(strdate);

            String showdate = format2.format(date).toString();

            return showdate;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String FormatDate(String strdate) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy");
//			SimpleDateFormat format2 = new SimpleDateFormat("MMMM-dd-yyyy");

            Date date = format1.parse(strdate);

            String showdate = format2.format(date).toString();

            return showdate;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String FormatTime1(String strtime) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("hh:mm a");

            Date date = format1.parse(strtime);

            String showtime = format2.format(date).toString();

            return showtime;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String showDate(String strdate) {
        String inputPattern = "yyyy/MM/dd";
        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(strdate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String showDate1(String strdate) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(strdate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public Date convertDate1(String strdate) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);

            Date date = format1.parse(strdate);

            return date;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*public Date convertDate(String strdate)
    {
        try
        {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.ENGLISH);
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format3 = new SimpleDateFormat("MMM-dd-yyyy hh:mm a");
            SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

            Date date = format1.parse(strdate);

            return date;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }*/
    public Date convertDate(String strdate, String fromTimeZone, String type) {
        try {
            String toTimeZone = Calendar.getInstance().getTimeZone().getID();
            SimpleDateFormat format1;
            String format = "";
            if (type.equals("1")) {
                format1 = new SimpleDateFormat("yyyy-MM-dd");
                format = "yyyy-MM-dd";
            } else {
                format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                format = "yyyy-MM-dd hh:mm a";
            }
            //SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm a",Locale.ENGLISH);

            SimpleDateFormat format3 = new SimpleDateFormat("MMM-dd-yyyy hh:mm a");
            SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

            //String format = "yyyy-MM-dd hh:mm a";
            SimpleDateFormat estFormatter = new SimpleDateFormat(format);
            estFormatter.setTimeZone(TimeZone.getTimeZone(fromTimeZone));
            Date date = estFormatter.parse(strdate);

            SimpleDateFormat utcFormatter = new SimpleDateFormat(format);
            utcFormatter.setTimeZone(TimeZone.getTimeZone(toTimeZone));

            String strdate1 = utcFormatter.format(date);

            Date date1 = format1.parse(strdate1);
            return date1;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date convertDateWithTimeZone(String strdate, String fromTimeZone) {
        try {
            /*TimeZone tz = TimeZone.getDefault();
            String gmt=TimeZone.getTimeZone(tz.getID())
                    .getDisplayName(false,TimeZone.LONG);
            System.out.println("timeZone : " + gmt);
            String toTimeZone = "";
            for (String s : gmt.split(" ")) {
                toTimeZone+=s.charAt(0);
            }
            System.out.println("The to time zone is "+toTimeZone);


//			String toTimeZone = Calendar.getInstance().getTimeZone().getID();
//			toTimeZone = "Asia/Calcutta";

			SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.ENGLISH);
			String format = "yyyy/MM/dd HH:mm:ss";
			SimpleDateFormat estFormatter = new SimpleDateFormat(format);
			estFormatter.setTimeZone(TimeZone.getTimeZone(fromTimeZone));
			Date date = estFormatter.parse(strdate);

			SimpleDateFormat utcFormatter = new SimpleDateFormat(format);
			utcFormatter.setTimeZone(TimeZone.getTimeZone(toTimeZone));

			String strdate1 = utcFormatter.format(date);

			Date date1 = format1.parse(strdate1);

			return date1;
*/
            String toTimeZone = Calendar.getInstance().getTimeZone().getID();
            //			toTimeZone = "Asia/Calcutta";
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
            String format = "yyyy/MM/dd HH:mm:ss";
            SimpleDateFormat estFormatter = new SimpleDateFormat(format);
            estFormatter.setTimeZone(TimeZone.getTimeZone(fromTimeZone));
            Date date = estFormatter.parse(strdate);
            SimpleDateFormat utcFormatter = new SimpleDateFormat(format);
            utcFormatter.setTimeZone(TimeZone.getTimeZone(toTimeZone));
            String strdate1 = utcFormatter.format(date);
            Date date1 = format1.parse(strdate1);
            return date1;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public long getDuration() {
        // get todays date
        Calendar cal = Calendar.getInstance();
        // get current month
        int currentMonth = cal.get(Calendar.MONTH);

        // move month ahead
        currentMonth++;
        // check if has not exceeded threshold of december

        if (currentMonth > Calendar.DECEMBER) {
            // alright, reset month to jan and forward year by 1 e.g fro 2013 to 2014
            currentMonth = Calendar.JANUARY;
            // Move year ahead as well
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
        }

        // reset calendar to next month
        cal.set(Calendar.MONTH, currentMonth);
        // get the maximum possible days in this month
        int maximumDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // set the calendar to maximum day (e.g in case of fEB 28th, or leap 29th)
        cal.set(Calendar.DAY_OF_MONTH, maximumDay);
        long thenTime = cal.getTimeInMillis(); // this is time one month ahead


        return (thenTime); // this is what you set as trigger point time i.e one month after

    }

    public String GetVersionInfo() {
        try {
            PackageInfo pInfo = con.getPackageManager().getPackageInfo(con.getPackageName(), 0);
            String versionName = pInfo.versionName;
            String versionCode = String.valueOf(pInfo.versionCode);

            Log.i("versionName", versionName);
            Log.i("versionCode", versionCode);

            return versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1";
        }
    }

    public void callRefreshToken(final Activity activity, @Nullable final RefreshTokenCallbacks callbacks) {
        final Shared_Preference sharedPreference = new Shared_Preference(activity);


        APIService service = ApiClient.getClient(activity)
                .create(APIService.class);

        // APIService service = retrofit.create(APIService.class);

        Call <JsonObject> call = service.callRefreshToken(
               sharedPreference.getuser_token());

        //calling the api
        call.enqueue(new Callback <JsonObject>() {
            @Override
            public void onResponse(Call <JsonObject> call, Response <JsonObject> responseObject) {
                //hiding progress dialog

//				progressdialog.dismiss("");

                if (responseObject.isSuccessful() && responseObject.body() != null) {
                    Log.i("Retrofit Response", responseObject.toString());
                    Log.i("Retrofit Response", responseObject.body().toString());

                    try {
                        JSONObject response = new JSONObject(responseObject.body().toString());

                        try {
                            if (response.has("token")) {

                                sharedPreference.setuser_token("Bearer" + " " + response.getString("token"));

                                if (callbacks != null)
                                    callbacks.onSuccess(true);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            if (callbacks != null)
                                callbacks.onSuccess(false);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        if (callbacks != null)
                            callbacks.onSuccess(false);

                    }

                } else {
                    Log.i("Message", responseObject.message().toString());
                    Log.i("Retrofit Response", "Error in getGenericJson:" + responseObject.code() + " " + responseObject.message());
                    if (callbacks != null)
                        callbacks.onSuccess(false);
                    String smJSON = null;
                    /*try {

                        smJSON = responseObject.errorBody().string();
                        JSONObject jObj = new JSONObject(smJSON);
                        if (jObj.has("message")) {
                            show_toast_long(jObj.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }*/

                  /*  try {
                        String smJSON = responseObject.errorBody().string();
                        JSONObject jObj = new JSONObject(smJSON);
                        if (jObj.has("status_code")) {
                            if (jObj.getString("status_code").equals("500")) {
                                if (callbacks != null)
                                    callbacks.onSuccess(false);

								Intent intent = new Intent(activity, LoginAcitivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
								sharedPreference.ClearPreferences();
								activity.startActivity(intent);
                            }

                            if (jObj.getString("status_code").equals("401")) {
                                if (callbacks != null)
                                    callbacks.onSuccess(false);

								Intent intent = new Intent(activity, LoginAcitivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
								sharedPreference.ClearPreferences();
								activity.startActivity(intent);

                            }
                            if (callbacks != null)
                                callbacks.onSuccess(false);

                            Intent intent = new Intent(activity, LoginAcitivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            sharedPreference.ClearPreferences();
                            activity.startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        if (callbacks != null)
                            callbacks.onSuccess(false);

                    } catch (IOException e) {
                        e.printStackTrace();

                        if (callbacks != null)
                            callbacks.onSuccess(false);

                    }*/

                }

            }

            @Override
            public void onFailure(Call <JsonObject> call, Throwable t) {
//				progressdialog.dismiss("");
                t.printStackTrace();

                if (callbacks != null)
                    callbacks.onError(t);

            }
        });

    }

   /* int REQUEST_TIMEOUT = 60;

    public OkHttpClient initOkHttp() {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json");

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        return httpClient.build();
    }*/

    public String getWeekNames(int week) {
        switch (week) {
            case 0:
                return "SUN";
            case 1:
                return "MON";
            case 2:
                return "TUE";
            case 3:
                return "WED";
            case 4:
                return "THU";
            case 5:
                return "FRI";
            case 6:
                return "SAT";
            default:
        }
        return "";
    }

    String errorMessage = "";

    public String showErrorHangling(Activity activity, Response <JsonObject> responseObject) {

            String smJSON = null;
            try {
                smJSON = responseObject.errorBody().string();
                JSONObject jObj = new JSONObject(smJSON);
                if (jObj.has("error")) {
                    errorMessage = jObj.getString("error");

                } else if (jObj.has("message")) {
                    errorMessage = jObj.getString("message");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        if (responseObject.code() == 500) {

            show_toast_long(activity.getResources().getString(R.string.error500));

        } else if (responseObject.code() == 401) {
            if (!errorMessage.equals("") && !errorMessage.equals("null")) {
                show_toast_long(errorMessage);
            }
            Intent intent = new Intent(activity, LoginAcitivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            sharedPreference.ClearPreferences();
            activity.startActivity(intent);
            activity.finish();
        }   else if (responseObject.code() == 404) {

            if (!errorMessage.equals("") && !errorMessage.equals("null")) {
                show_toast_long(errorMessage);
            }

        }else if (responseObject.code() == 422) {

            if (!errorMessage.equals("") && !errorMessage.equals("null")) {
                show_toast_long(errorMessage);
            }
        } else {
            if (!errorMessage.equals("") && !errorMessage.equals("null")) {
                show_toast_long(errorMessage);
            }
            Intent intent = new Intent(con, LoginAcitivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            sharedPreference.ClearPreferences();
            activity.startActivity(intent);
            activity.finish();
        }
        return valuReturnMessage;
    }

    public String showFragmentErrorHangling(Response <JsonObject> responseObject) {
        if (responseObject.isSuccessful() && responseObject.body() != null) {
            String smJSON = null;
            try {
                smJSON = responseObject.errorBody().string();
                JSONObject jObj = new JSONObject(smJSON);
                if (jObj.has("error")) {
                    errorMessage = jObj.getString("error");

                } else if (jObj.has("message")) {
                    errorMessage = jObj.getString("message");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (responseObject.code() == 500) {

            show_toast_long(con.getResources().getString(R.string.error500));

        } else if (responseObject.code() == 400) {
            Intent intent = new Intent(con, LoginAcitivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            sharedPreference.ClearPreferences();
            con.startActivity(intent);
        }   else if (responseObject.code() == 404) {

            if (!errorMessage.equals("") && !errorMessage.equals("null")) {
                show_toast_long(errorMessage);
            }

        }else if (responseObject.code() == 422) {

            if (!errorMessage.equals("") && !errorMessage.equals("null")) {
                show_toast_long(errorMessage);
            }
        } else {
            if (!errorMessage.equals("") && !errorMessage.equals("null")) {
                show_toast_long(errorMessage);
            }
            Intent intent = new Intent(con, LoginAcitivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            sharedPreference.ClearPreferences();
            con.startActivity(intent);
        }
        return valuReturnMessage;
    }
    public String refereshTokenErrorHangling(Activity activity, Response <JsonObject> responseObject, String ischeck) {


            callRefreshToken(activity, new RefreshTokenCallbacks() {
                @Override
                public void onSuccess(@NonNull boolean value) {
                    progressdialog.dismiss("");

                    Log.i("RefreshToken Response", "TRUE");
                    if(!value){
                        Log.d("errorhandling","false");
                        Intent intent = new Intent(activity, LoginAcitivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        sharedPreference.ClearPreferences();
                        activity.startActivity(intent);
                        activity.finish();
                    }else {
                        Log.d("errorhandling","true");
                        valuReturnMessage = String.valueOf(value);
                    }
                    //GetUserRecordApi();
                }

                @Override
                public void onError(@NonNull Throwable throwable) {
                    try {
                        Log.d("errorhandling","throwable");
                        String smJSON = throwable.getMessage().toString();
                        JSONObject jObj = new JSONObject(smJSON);
                        if (jObj.has("message")) ;
                        show_toast(jObj.getString("message"));
                        Intent intent = new Intent(con, LoginAcitivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        sharedPreference.ClearPreferences();
                        activity.startActivity(intent);
                        activity.finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return valuReturnMessage;
        }

    int REQUEST_TIMEOUT = 60;
    public OkHttpClient initOkHttp() {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

       /* httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        //.addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json");

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });*/

        return httpClient.build();
    }



}
