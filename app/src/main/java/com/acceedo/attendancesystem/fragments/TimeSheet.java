package com.acceedo.attendancesystem.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acceedo.attendancesystem.Pojo.RecentScanDetails;
import com.acceedo.attendancesystem.adapter.TimeSheetListAdapter;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.databinding.FragmentTimesheetBinding;
import com.acceedo.attendancesystem.interfaces.APIService;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TimeSheet extends BaseFragment {

    FragmentTimesheetBinding mBinding;

    ArrayList <RecentScanDetails> timeList;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_timesheet, container, false);
        mBinding.setData(this);

        RecentUserApi();
        return mBinding.getRoot();
    }



    private void RecentUserApi() {

        cf.progressdialog.show("");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(sharedPreference.getapiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(cf.initOkHttp())
                .build();

        APIService service = retrofit.create(APIService.class);
        Call<JsonObject> call = service.allrecentscan(sharedPreference.getuser_token());

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
                                    TimeSheetListAdapter mrecentAdapter = new TimeSheetListAdapter(requireContext(), timeList);
                                    mBinding.rvTimeSheetList.setLayoutManager(new LinearLayoutManager(requireContext()));
                                    mBinding.rvTimeSheetList.setAdapter(mrecentAdapter);
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
                    cf.showFragmentErrorHangling( responseObject);
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
