package com.acceedo.attendancesystem.utils;

import com.acceedo.attendancesystem.BuildConfig;

public class Constants {

    public static String BASE_URL = BuildConfig.BASE_URL;

    public static String MAIN = BASE_URL + "api/";
    public static String image = BASE_URL + "uploads/avatar/";
    public static String logo = BASE_URL + "uploads/logo/";
    public static String MEDIA_URL = BASE_URL + "uploads/announcement_files/";
    public static String EVENT_URL = BASE_URL + "uploads/event_files/";
    public static String DM_URL = BASE_URL + "uploads/directmsg_files/";
    public static String DM_THUMBNAIL_URL = BASE_URL + "uploads/video_thumbnail/";
    public static String MEDIAGAL_URL = BASE_URL + "uploads/gallery_files/";
    public static String BASEURL = BASE_URL + "api/";
    public  static String DeeplinkHost= "sms.sisar.nl";

//    public static String MAIN = BASE_URL + "api/";
//    public static String image = BASE_URL + "uploads/avatar/";

    public static String Login=MAIN+"login";
    public static String ForgetPassword=MAIN+"forgot-password";

    public static String TransportList=MAIN+"transport/list";
    public static String Changestatus=MAIN+"transport/change-status";
    public static String RefreshTocken=MAIN+"refresh?token=";
    public static String UserList=MAIN+"transport/user-list";
    public static String CREATEMESSAGE=MAIN+"transport/create-messages";
    public static String LoadMessages=MAIN+"transport/messages";
    public static String ChangePicture=MAIN+"profile/change-picture";
    public static String UpdateProfile=MAIN+"profile/update";
    public static String GetUserDetailsProfile=MAIN+"profile/get-profile";

   // public static String GET_DETAILS="http://www.nandhamotors.com/test/kiggrass/signin.php";

}
