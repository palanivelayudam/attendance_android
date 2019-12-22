package com.acceedo.attendancesystem.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.activities.LoginAcitivity;
import com.acceedo.attendancesystem.apiclient.ApiClient;
import com.acceedo.attendancesystem.databinding.DialogNointernetBinding;
import com.acceedo.attendancesystem.databinding.FragmentSettingsBinding;
import com.acceedo.attendancesystem.interfaces.APIService;
import com.acceedo.attendancesystem.interfaces.RefreshTokenCallbacks;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends BaseFragment {


    private FragmentSettingsBinding mBinding;
    boolean loadsms = false, loadmail = false, loadpush = false;
    String type, notifyStr = "";
    AlertDialog dialog1;

    private onSettingsFragmentClickListener mOnSettingsFragmentClickListener;

    public interface onSettingsFragmentClickListener {
        void onClick(int position);
    }

    public void setmOnSettingsFragmentClickListener(onSettingsFragmentClickListener mOnSettingsFragmentClickListener) {
        this.mOnSettingsFragmentClickListener = mOnSettingsFragmentClickListener;
    }

    public SettingsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_settings, container, false);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        mBinding.setData(this);

        initView();

        if (!sharedPreference.getnotify_mail().equals("")) {
            if (sharedPreference.getnotify_mail().equals("1")) {
                mBinding.switchEmailNoti.setChecked(true);
            } else {
                mBinding.switchEmailNoti.setChecked(false);
            }
             }
        if (!sharedPreference.getnotify_push().equals("")) {
            if (sharedPreference.getnotify_push().equals("1")) {
                mBinding.switchPushNoti.setChecked(true);
            } else {
                mBinding.switchPushNoti.setChecked(false);
            }
             }

        if (!sharedPreference.getnotify_sms().equals("")) {
            if (sharedPreference.getnotify_sms().equals("1")) {
                mBinding.switchSmsNoti.setChecked(true);
            } else {
                mBinding.switchSmsNoti.setChecked(false);
            }
        }
        return mBinding.getRoot();
    }

    private void initView() {

        mBinding.switchEmailNoti.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                loadmail = true;
                return false;
            }
        });

        mBinding.switchEmailNoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                type = "mail";

                //  tvMail.setText(isChecked ? getString(R.string.notifaication_on) : getString(R.string.notifaication_off));
                if (loadmail) {

                    loadmail = false;

                    if (isChecked) {
                        notifyStr = "1";
                        if (cf.isInternetOn() == true) {
                            notificationService(type, notifyStr);
                        } else {
                            No_InternetDialog(type);
                        }
                    } else {
                        notifyStr = "0";
                        openAlertDialog(type, notifyStr);
                    }
                }
            }
        });

        mBinding.switchPushNoti.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                loadpush = true;
                return false;
            }
        });

        mBinding.switchPushNoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                type = "pushswitch";

                if (loadpush) {
                    loadpush = false;
                    if (isChecked) {
                        notifyStr = "1";
                        if (cf.isInternetOn() == true) {
                            notificationService(type, notifyStr);
                        } else {
                            No_InternetDialog(type);
                        }

                    } else {
                        notifyStr = "0";
                        openAlertDialog(type, notifyStr);
                    }
                }
            }
        });
        mBinding.switchSmsNoti.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                loadsms = true;
                return false;
            }
        });

        mBinding.switchSmsNoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                type = "message";

                if (loadsms) {
                    loadsms = false;
                    if (isChecked) {
                        notifyStr = "1";
                        if (cf.isInternetOn() == true) {
                            notificationService(type, notifyStr);
                        } else {
                            No_InternetDialog(type);
                        }

                    } else {
                        notifyStr = "0";
                        openAlertDialog(type, notifyStr);
                    }
                }
            }
        });

        /*mBinding.tvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_content_body, new TimeSheet());
                ft.commit();*//*
                if (mOnSettingsFragmentClickListener != null)
                    mOnSettingsFragmentClickListener.onClick(0);
            }
        });*/
    }
    private void openAlertDialog(final String type, final String notifyStr) {
        Window mAlertDialogWindow;

        dialog1 = new AlertDialog.Builder(requireContext())
                .create();
        dialog1.show();

        dialog1.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog1.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mAlertDialogWindow = dialog1.getWindow();
        DialogNointernetBinding alertNotifyBinding = DataBindingUtil.
                inflate(LayoutInflater.from(requireContext()), R.layout.dialog_nointernet, null, false);

        alertNotifyBinding.getRoot().getRootView().setFocusable(true);
        alertNotifyBinding.getRoot().getRootView().setFocusableInTouchMode(true);

        dialog1.setCancelable(false);

        mAlertDialogWindow
                .setBackgroundDrawableResource(R.drawable.material_dialog_window);

        mAlertDialogWindow.setContentView(alertNotifyBinding.getRoot());
        alertNotifyBinding.btnyes.setText(getString(R.string.ok));
        alertNotifyBinding.btnno.setText(getString(R.string.cancel));

        if (type.equals("message")) {
            alertNotifyBinding.tvdetails.setText(getString(R.string.notification_smsnotify) + " " + sharedPreference.getapp_name());
        } else if (type.equals("mail")) {
            alertNotifyBinding.tvdetails.setText(getString(R.string.notification_emailnotify) + " " + sharedPreference.getapp_name());

        } else if (type.equals("pushswitch")) {
            alertNotifyBinding.tvdetails.setText(getString(R.string.notification_pushnotification) + " " + sharedPreference.getapp_name());

        }


        alertNotifyBinding.btnyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cf.isInternetOn() == true) {
                    notificationService(type, notifyStr);
                } else {
                    //dialogtype="stoproute";
                    No_InternetDialog(type);
                }


                dialog1.dismiss();
            }
        });

        alertNotifyBinding.btnno.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (type.equals("message")) {
                    mBinding.switchSmsNoti.setChecked(true);
                } else if (type.equals("mail")) {
                    mBinding.switchEmailNoti.setChecked(true);

                } else if (type.equals("pushswitch")) {
                    mBinding.switchPushNoti.setChecked(true);
                }


                dialog1.dismiss();
            }
        });

    }
    private void No_InternetDialog(String type) {
        Window mAlertDialogWindow;

        dialog1 = new AlertDialog.Builder(requireContext())
                .create();
        dialog1.show();

        dialog1.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog1.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mAlertDialogWindow = dialog1.getWindow();
        DialogNointernetBinding nointernetBinding = DataBindingUtil.
                inflate(LayoutInflater.from(requireContext()), R.layout.dialog_nointernet, null, false);

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

                if (cf.isInternetOn() == true) {
                    dialog1.dismiss();
                    if (type.equals("message")) {
                        notificationService(type, notifyStr);
                    } else if (type.equals("mail")) {
                       notificationService(type, notifyStr);
                    } else if (type.equals("pushswitch")) {
                        notificationService(type, notifyStr);
                    }


                }
            }
        });

        nointernetBinding.btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();

            }
        });
    }

    void notificationService(final String receivetype, final String recenotifyStr) {


        cf.progressdialog.show("");

        APIService service = ApiClient.getClient(requireContext())
                .create(APIService.class);

        Call<JsonObject> call = null;
        if (receivetype.equals("message")) {
            call = service.messagepreference(
                    sharedPreference.getuser_token(), recenotifyStr);

        } else if (receivetype.equals("mail")) {
            call = service.mailpreference(
                    sharedPreference.getuser_token(), recenotifyStr);

        } else if (receivetype.equals("pushswitch")) {
            call = service.notifypreference(
                    sharedPreference.getuser_token(), recenotifyStr);

        }


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

                                if (receivetype.equals("message")) {
                                    sharedPreference.setnotify_sms(recenotifyStr);
                                    

                                } else if (receivetype.equals("mail"))

                                {
                                    sharedPreference.setnotify_mail(recenotifyStr);
                                    

                                } else if (receivetype.equals("pushswitch")) {
                                    sharedPreference.setnotify_push(recenotifyStr);
                                   
                                }


                                cf.show_toast(response.getString("message"));

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

                            cf.callRefreshToken(requireActivity(), new RefreshTokenCallbacks() {
                                @Override
                                public void onSuccess(@NonNull boolean value) {
                                    if (value) {
                                        Log.d("isCheckRefreshedToken", "false");
                                        isCheckRefreshedToken = false;
                                        Log.i("RefreshToken Response", "TRUE");
                                        notificationService(receivetype,recenotifyStr);
                                    } else {
                                        Log.d("isCheckRefreshedToken", "failure");

                                        cf.show_toast(getString(R.string.tockenfailederror));
                                        cf.progressdialog.dismiss("");
                                        Intent intent = new Intent(requireContext(), LoginAcitivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        sharedPreference.ClearPreferences();
                                        startActivity(intent);
                                        requireActivity().finish();
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
                                        Intent intent = new Intent(requireContext(), LoginAcitivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        sharedPreference.ClearPreferences();
                                        startActivity(intent);
                                        requireActivity().finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } else {
                            cf.progressdialog.dismiss("");
                            cf.showErrorHangling(requireActivity(),responseObject);
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
                        Toast.makeText(requireContext(), jObj.getString("message"),
                                Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
