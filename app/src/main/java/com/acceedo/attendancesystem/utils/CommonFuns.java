package com.acceedo.attendancesystem.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.acceedo.attendancesystem.R;

import java.io.File;

import androidx.appcompat.app.AlertDialog;


/**
   * Created by suresh on 09/5/18.
  */


public class CommonFuns {

      public static final String UploadImagePath= Environment.getExternalStorageDirectory().toString() + File.separator + "SSSNL";
      //public static final String UploadImagePath= Environment.getExternalStorageDirectory().toString() + "/"+ "SisarCams/";

    /*public static final String UploadImagePath= Environment.getExternalStorageDirectory().toString() + File.separator + ".ICanHelp";
    public static final File newFile=new File(UploadImagePath);

    public static final int LOCATION_UPDATE_TIMER=1000*60*15;

    static ProgressBar progress;

    static ProgressDialog mProgressDialog;

    public static boolean isNetworkConnected(Activity context) {

        try {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni == null) {
                // There are no active networks.
                ShowAlertMessageFinish(context);
                return false;
            } else {
                return true;
            }

        } catch (Exception exp) {

            return false;
        }
    }

    public static boolean isNetworkConnection(Activity context) {

        try {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni == null) {
                ShowAlertMessageFinish(context);
                return false;
            } else {
                return true;
            }

        } catch (Exception exp) {

            return false;
        }
    }

    public static boolean isNetworkConnectedcontext(Activity context) {

        try {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni == null) {
                //Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                ShowAlertMessageFinish(context);
                return false;
            } else {
                return true;
            }

        } catch (Exception exp) {

            return false;
        }
    }


      public static void ShowAlertMessageFinish(final Activity activity) {

          AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
          alertDialogBuilder.setMessage("Please check your internet connection");
          alertDialogBuilder.setTitle(activity.getString(R.string.app_name));

          alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface arg0, int arg1) {
                  activity.finish();
              }
          });

          AlertDialog alertDialog = alertDialogBuilder.create();
          alertDialog.show();
      }


    public static String GetDeviceId(Activity activity) {

        return Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void showProgressDialog(Activity context) {
        mProgressDialog= ProgressDialog.show(context, context.getResources().getString(R.string.app_name), "Loading Please wait . . ");
    }

    public static void stopProgressDialog(Activity context) {
        if(mProgressDialog!=null)
            if(mProgressDialog.isShowing())
                mProgressDialog.dismiss();
    }


*/

      Context con;

      public CommonFuns(Context cont) {
          con = cont;
      }

      public static final File newFile=new File(UploadImagePath);

      public static final int LOCATION_UPDATE_TIMER=1000*60*15;

      static ProgressBar progress;

      static ProgressDialog mProgressDialog;

      public static boolean isNetworkConnected(Activity context) {

          try {

              ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
              NetworkInfo ni = cm.getActiveNetworkInfo();
              if (ni == null) {
                  // There are no active networks.
                  ShowAlertMessageFinish(context);
                  return false;
              } else {
                  return true;
              }

          } catch (Exception exp) {

              return false;
          }
      }

      public static void ShowAlertMessageFinish(final Activity activity) {

          AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
          alertDialogBuilder.setMessage("Please check your internet connection");
          alertDialogBuilder.setTitle(activity.getString(R.string.app_name));

          alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface arg0, int arg1) {
                  activity.finish();
              }
          });

          AlertDialog alertDialog = alertDialogBuilder.create();
          alertDialog.show();
      }


      public static String GetDeviceId(Activity activity) {

          return Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
      }

      public static void showProgressDialog(Activity context) {
          mProgressDialog= ProgressDialog.show(context, context.getResources().getString(R.string.app_name), "Loading Please wait . . ");
      }

      public static void stopProgressDialog(Activity context) {
          if(mProgressDialog!=null)
              if(mProgressDialog.isShowing())
                  mProgressDialog.dismiss();
      }

      public void show_toast(String msg) {
//		Toast.makeText(con, msg, Toast.LENGTH_SHORT).show();

          Toast toast = Toast.makeText(con, msg, Toast.LENGTH_SHORT);
          LinearLayout toastLayout = (LinearLayout) toast.getView();
//          TextView toastTV = (TextView) toastLayout.getChildAt(0);
//          toastTV.setTypeface(tfregular);
          toast.show();

      }







  }
