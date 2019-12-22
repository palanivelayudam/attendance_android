package com.acceedo.attendancesystem.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Shared_Preference {
    private static final String USER_PREFS = "USER_PREFS";
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    public Shared_Preference(Context context){
        this.appSharedPrefs = context.getSharedPreferences(USER_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public boolean getIsSplashIntro() {
        return appSharedPrefs.getBoolean("IsSplashIntro", false);
    }

    public void setIsSplashIntro(boolean IsSplashIntro) {
        prefsEditor.putBoolean("IsSplashIntro", IsSplashIntro).commit();
    }
     public boolean getIsurldialogOpen() {
        return appSharedPrefs.getBoolean("IsurldialogOpen", false);
    }

    public void setIsurldialogOpen(boolean IsSplashIntro) {
        prefsEditor.putBoolean("IsurldialogOpen", IsSplashIntro).commit();
    }

    public String getuser_id() {
        return appSharedPrefs.getString("grpID", "");
    }

    public void setuser_id(String user_id) {
        prefsEditor.putString("grpID", user_id).commit();
    }

    public String getloggedinCount() {
        return appSharedPrefs.getString("loggedinCount", "");
    }

    public void setloggedinCount(String loggedinCount) {
        prefsEditor.putString("loggedinCount", loggedinCount).commit();
    }
    public String getrequired() {
        return appSharedPrefs.getString("required", "");
    }

    public void setrequired(String required) {
        prefsEditor.putString("required", required).commit();
    }
    public String getsratio() {
        return appSharedPrefs.getString("ratio", "");
    }

    public void setratio(String ratio) {
        prefsEditor.putString("ratio", ratio).commit();
    }

    public String getgender() {
        return appSharedPrefs.getString("gender", "");
    }

    public void setgender(String gender) {
        prefsEditor.putString("gender", gender).commit();
    }

    public String getbirth_date() {
        return appSharedPrefs.getString("birth_date", "");
    }

    public void setbirth_date(String birth_date) {
        prefsEditor.putString("birth_date", birth_date).commit();
    }

    public String getmobile() {
        return appSharedPrefs.getString("mobile", "");
    }

    public void setmobile(String mobile) {
        prefsEditor.putString("mobile", mobile).commit();
    }

    public String getcountry_code() {
        return appSharedPrefs.getString("country_code", "");
    }

    public void setcountry_code(String country_code) {
        prefsEditor.putString("country_code", country_code).commit();
    }

    public String getzipcode() {
        return appSharedPrefs.getString("zipcode", "");
    }

    public void setzipcode(String zipcode) {
        prefsEditor.putString("zipcode", zipcode).commit();
    }

    public String getstate() {
        return appSharedPrefs.getString("state", "");
    }

    public void setstate(String state) {
        prefsEditor.putString("state", state).commit();
    }

    public String getcity() {
        return appSharedPrefs.getString("city", "");
    }

    public void setcity(String city) {
        prefsEditor.putString("city", city).commit();
    }

    public String getaddress() {
        return appSharedPrefs.getString("address", "");
    }

    public void setaddress(String address) {
        prefsEditor.putString("address", address).commit();
    }

    public String getgrpID() {
        return appSharedPrefs.getString("user_id", "");
    }

    public void setgrpID(String setgrpID) {
        prefsEditor.putString("user_id", setgrpID).commit();
    }

    public String getuser_image() {
        return appSharedPrefs.getString("user_image", "");
    }

    public void setuser_image(String user_image) {
        prefsEditor.putString("user_image", user_image).commit();
    }

    public String getuser_email() {
        return appSharedPrefs.getString("user_email", "");
    }

    public void setuser_email(String user_email) {
        prefsEditor.putString("user_email", user_email).commit();
    }

    public String getnotify_sms() {
        return appSharedPrefs.getString("notify_sms", "");
    }

    public void setnotify_sms(String notify_sms) {
        prefsEditor.putString("notify_sms", notify_sms).commit();
    }

    public String getnotify_mail() {
        return appSharedPrefs.getString("notify_mail", "");
    }

    public void setnotify_mail(String notify_mail) {
        prefsEditor.putString("notify_mail", notify_mail).commit();
    }

    public String getnotify_push() {
        return appSharedPrefs.getString("notify_push", "");
    }

    public void setnotify_push(String notify_push) {
        prefsEditor.putString("notify_push", notify_push).commit();
    }

    public String getusername() {
        return appSharedPrefs.getString("username", "");
    }

    public void setusername(String username) {
        prefsEditor.putString("username", username).commit();
    }

    public String getfirstname() {
        return appSharedPrefs.getString("firstname", "");
    }

    public void setfirstname(String firstname) {
        prefsEditor.putString("firstname", firstname).commit();
    }

    public String getlastname() {
        return appSharedPrefs.getString("lastname", "");
    }
    public void setlastname(String lastname) {
        prefsEditor.putString("lastname", lastname).commit();
    }
    public void setLastLogin(String last_login) {
        prefsEditor.putString("last_login", last_login).commit();
    }
    public String getLastLogin() {
        return appSharedPrefs.getString("last_login", "");
    }

    public void setDesignation(String designation) {
        prefsEditor.putString("designation", designation).commit();
    }
    public String getDesignation() {
        return appSharedPrefs.getString("designation", "");
    }
    public void setShow_role(String show_role) {
        prefsEditor.putString("show_role", show_role).commit();
    }
    public String getShow_role() {
        return appSharedPrefs.getString("show_role", "");
    }


    public String getfull_name() {
        return appSharedPrefs.getString("full_name", "");
    }

    public void setfull_name(String full_name) {
        prefsEditor.putString("full_name", full_name).commit();
    }
    public String getcrop_img() {
        return appSharedPrefs.getString("crop_image", "");
    }

    public void setcrop_img(String image) {
        prefsEditor.putString("crop_image", image).commit();
    }


    public boolean getIsLoggedIn() {
        return appSharedPrefs.getBoolean("IsLoggedIn", false);
    }

    public void setIsLoggedIn(boolean IsLoggedIn) {
        prefsEditor.putBoolean("IsLoggedIn", IsLoggedIn).commit();
    }

    public String getuser_token() {
        return appSharedPrefs.getString("user_token", "");
    }

    public void setuser_token(String user_token) {
        prefsEditor.putString("user_token", user_token).commit();
    }


    public String getorganization_title() {
        return appSharedPrefs.getString("org_title", "");
    }

    public void setorganization_title(String org_title) {
        prefsEditor.putString("org_title", org_title).commit();
    }

    public String getorg_id() {
        return appSharedPrefs.getString("org_id", "");
    }

    public void setorg_id(String org_id) {
        prefsEditor.putString("org_id", org_id).commit();
    }

    public String getapp_name() {
        return appSharedPrefs.getString("app_name", "");
    }
    public void setapp_name(String app_name) {
        prefsEditor.putString("app_name", app_name).commit();
    }

    public String getuser_role() {
        return appSharedPrefs.getString("user_role", "");
    }

    public void setuser_role(String user_role) {
        prefsEditor.putString("user_role", user_role).commit();
    }

    public  void settranslated_role(String translated_role){
        prefsEditor.putString("translated_role", translated_role).commit();
    }

    public void settime_zone(String timezone) {
        prefsEditor.putString("timezone", timezone).commit();
    }
    public String gettime_zone() {
        return appSharedPrefs.getString("timezone", "");
    }
    public void settime_zoneabbr(String timezone_abbr) {
        prefsEditor.putString("timezone_abbr", timezone_abbr).commit();
    }
    public String gettime_zoneabbr() {
        return appSharedPrefs.getString("timezone_abbr", "");
    }

    public String gettranslated_role() {
        return appSharedPrefs.getString("translated_role", "");
    }

    public  void setchange_lang(String change_lang){
        prefsEditor.putString("langCode", change_lang).commit();
    }

    public String getchange_lang() {
        return appSharedPrefs.getString("langCode", "");
    }

    public  void setapiUrl(String apiUrl){
        prefsEditor.putString("apiUrl", apiUrl).commit();
    }

    public String getapiUrl() {
        return appSharedPrefs.getString("apiUrl", "");
    }
    public  void setempcodefromscan(String empcodefromscan){
        prefsEditor.putString("empcodefromscan", empcodefromscan).commit();
    }

    public String getempcodefromscan() {
        return appSharedPrefs.getString("empcodefromscan", "");
    }
    public  void setsupervisor(String supervisor){
        prefsEditor.putString("supervisor", supervisor).commit();
    }

    public String getsupervisor() {
        return appSharedPrefs.getString("supervisor", "");
    }

    public void ClearPreferences()
    {
       /* this.prefsEditor.clear();
        this.prefsEditor.commit();*/
        setuser_id("");
        setgender("");
        setmobile("");
        setbirth_date("");
        setgender("");
        setstate("");
        setzipcode("");
        setcountry_code("");
        setgrpID("");
        setaddress("");
        setcity("");
        setuser_image("");
        setuser_email("");
        setnotify_sms("");
        setnotify_mail("");
        setnotify_push("");
        setusername("");
        setfirstname("");
        setlastname("");
        setLastLogin("");
        setDesignation("");
        setShow_role("");
        setfull_name("");
        setuser_token("");
        setcrop_img("");
        setorganization_title("");
        setorg_id("");
        setapp_name("");
        setuser_role("");
        settranslated_role("");
        settime_zone("");
        settime_zoneabbr("");
        setchange_lang("");
        setIsLoggedIn(false);
        setIsSplashIntro(false);
        setempcodefromscan("");
        setsupervisor("");
        setratio("");
        setrequired("");
        setloggedinCount("");
    }

}
