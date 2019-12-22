package com.acceedo.attendancesystem.interfaces;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIService {

    @GET("/maps/api/place/autocomplete/json")
    Call<JsonObject>
    getplacesapi(
            @Query("input") String location,
            @Query("sensor") String sensor,
            @Query("key") String key
    );
    @GET("/api/showemployee/{id}")
    Call<JsonObject>
    getempleydetails(@Path("id") String productID,@Header("authorization") String token);

    @GET("/api/recentscans")
    Call<JsonObject>
    getrecentscan(@Header("authorization") String token);

    @GET("/api/settings")
    Call<JsonObject>
    getsettings(@Header("authorization") String token);

    @GET("/api/allscans")
    Call<JsonObject>
    allrecentscan(@Header("authorization") String token);

    @GET("/api/logout")
    Call<JsonObject>
    logout(@Header("authorization") String token);

    @GET("/api/countdown")
    Call<JsonObject>
    countdownapi(@Header("authorization") String token);

    @GET("refresh")
    Call<JsonObject>
    callRefreshToken(
                    @Header("Authorization") String token
            );
    /*@POST("logout")
    Call<JsonObject> logout(
            @Header("Authorization") String accesstoken);*/
    @FormUrlEncoded
    @POST("get-user-records")
    Call<JsonObject> getUserRecords(
            @Header("Authorization") String token,
            @Field("organization_id") String orgid);
    @GET
    Call<String> getDirectionAPI(@Url String url);

    /*@FormUrlEncoded
    @POST("login")
    Call<JsonObject> login(
            @Field("email") String username,
            @Field("password") String password,
            @Field("fcm_token") String fcmid,
            @Field("device") String device,
            @Field("os") String os,
            @Field("model") String model);*/
    @POST("/api/login")
    Call<JsonObject> login(@Body JsonObject jsonObject);
    @POST("/api/attendancein")
    Call<JsonObject> attendancein(@Body JsonObject jsonObject,@Header("Authorization") String token);
   @FormUrlEncoded
    @POST("forgot-password")
    Call<JsonObject> forgetpassword(
           @Field("email") String username);

    @FormUrlEncoded
    @POST("profile/change-password")
    Call<JsonObject> changePassword(
            @Header("Authorization") String accesstoken,
            @Field("old_password") String oldpassword,
            @Field("new_password") String newpassword,
            @Field("confirm_password") String confirmpassword);

    @FormUrlEncoded
    @POST("reset-password")
    Call<JsonObject> resetPassword(
            @Field("code") String code,
            @Field("id") String id,
            @Field("password") String password);
    @FormUrlEncoded
    @POST("profile/preferences")
    Call<JsonObject> messagepreference(
            @Header("Authorization") String accesstoken,
            @Field("get_sms") String mail);

    @FormUrlEncoded
    @POST("profile/preferences")
    Call<JsonObject> mailpreference(
            @Header("Authorization") String accesstoken,
            @Field("get_mail") String mail);

    @FormUrlEncoded
    @POST("profile/preferences")
    Call<JsonObject> notifypreference(
            @Header("Authorization") String accesstoken,
            @Field("get_notify") String mail);

    @FormUrlEncoded
    @POST("announcements/list")
    Call<JsonObject> announcementslist(
            @Field("token") String token,
            @Field("school_id") String schoolid,
            @Field("school_year_id") String yearid,
            @Field("role") String role,
            @Field("page") String page,
            @Field("q") String q,
            @Field("post") String post,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("announcements/hidden-list")
    Call<JsonObject> announcementsHiddenlist(
            @Field("token") String token,
            @Field("school_id") String schoolid,
            @Field("school_year_id") String yearid,
            @Field("role") String role,
            @Field("q") String q,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("announcements/like-post")
    Call<JsonObject> announcementslikepost(
            @Field("token") String token,
            @Field("grpId") String schoolid,
            @Field("type") String yearid,
            @Field("lang") String lang);

  @FormUrlEncoded
    @POST("announcements/add-confirmation")
    Call<JsonObject> announcementsAddconfirmation(
          @Field("token") String token,
          @Field("grpId") String grpid,
          @Field("type") String type,
          @Field("lang") String lang);

    @FormUrlEncoded
    @POST("announcements/get-users-list")
    Call<JsonObject> announcementsgetuserlist(
            @Field("token") String token,
            @Field("grpId") String grpid,
            @Field("type") String type,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("announcements/hide-post")
    Call<JsonObject> announcementshidepost(
            @Field("token") String token,
            @Field("grpId") String grpid,
            @Field("type") String type,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("announcements/cancel-event")
    Call<JsonObject> announcementscancelevent(
            @Field("token") String token,
            @Field("calId") int grpid,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("announcements/delete")
    Call<JsonObject> announcementsdeletepost(
            @Field("token") String token,
            @Field("grpId") String grpid,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("announcements/get-class-details")
    Call<JsonObject> announcementsgetclass(
            @Field("token") String token,
            @Field("school_id") String schoolid,
            @Field("school_year_id") String schoolyearid,
            @Field("lang") String lang);
    @FormUrlEncoded
    @POST("announcements/get-class-subjects")
    Call<JsonObject> announcementsgetclasssubject(
            @Field("token") String token,
            @Field("list_id") String schoolid,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("get-counts")
    Call<JsonObject> announcementsgetcount(
            @Field("token") String token,
            @Field("school_id") String schoolid,
            @Field("school_year_id") String schoolyearid,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("profile/update")
    Call<JsonObject> profileupdate(
            @Header("Authorization") String token,
            @Field("first_name") String firstname,
            @Field("last_name") String lastname,
            @Field("address") String address,
            @Field("email") String email,
            @Field("gender") int gender,
            @Field("country") String country,
            @Field("city") String city,
            @Field("state") String state,
            @Field("zipcode") String zipcode,
            @Field("country_code") String code,
            @Field("mobile") String mobile,
            @Field("birth_date") String birthday);

    @Multipart
    @POST("profile/change-picture")
    Call<JsonObject> updateprofileimage(
            @Header("Authorization") String accesstoken,
            @Part MultipartBody.Part picture);

    @POST("profile/get-profile")
    Call<JsonObject> getprofile(
            @Header("Authorization") String token);
    @FormUrlEncoded
    @POST("account/delete")
    Call<JsonObject> accountDelete(
            @Field("token") String token,
            @Field("password") String firstname,
            @Field("lang") String lang);



    @Multipart
    @POST("announcements/create")
    Call<JsonObject> createannouncement(
            @Part("token") RequestBody token,
            @Part("grpSubject") RequestBody subject,
            @Part("grpContent") RequestBody description,
            @Part("school_id") RequestBody school_id,
            @Part("school_year_id") RequestBody school_year_id,
            @Part("type") RequestBody type,
            @Part("postType") RequestBody radiotype,
            @Part("grpTags") RequestBody selecttypespinner,
            @Part("fromDate") RequestBody fromdate,
            @Part("toDate") RequestBody todate,
            @Part("lang") RequestBody lang,

            @Part List <MultipartBody.Part> attachments,
            //@Part List<MultipartBody.Part> grpListId,
            @PartMap Map <String, String> grpListId,
            //@Query("grpSpecific[]") ArrayList<String> lostProjectReasons,
            //@Part("grpSpecific[]") LinkedHashMap<String, RequestBody> countries);
            @PartMap Map <String, Integer> fields);

  @Multipart
    @POST("announcements/update")
    Call<JsonObject> updateannouncement(
          @Part("token") RequestBody token,
          @Part("grpSubject") RequestBody subject,
          @Part("grpContent") RequestBody description,
          @Part("school_id") RequestBody school_id,
          @Part("school_year_id") RequestBody school_year_id,
          @Part("postType") RequestBody radiotype,
          @Part("grpTags") RequestBody selecttypespinner,
          @Part("fromDate") RequestBody fromdate,
          @Part("toDate") RequestBody todate,
          @Part("grpId") RequestBody grpid,
          @Part("lang") RequestBody lang,

          @PartMap Map <String, String> grpListId,

          @PartMap Map <String, Integer> fields);

    @Multipart
    @POST("announcements/add-attachment")
    Call<JsonObject> Addattachement(
            @Part("token") RequestBody token,
            @Part("grpId") RequestBody subject,
            @Part("type") RequestBody description,
            @Part("lang") RequestBody lang,
            @Part List <MultipartBody.Part> attachments);
    @Multipart
    @POST("announcements/remove-attachment")
    Call<JsonObject> removeattachement(
            @Part("token") RequestBody token,
            @Part("grpId") RequestBody subject,
            @Part("lang") RequestBody lang,
            @PartMap Map <String, String> grpListId);
    @Multipart
    @POST("gallery/remove-attachment")
    Call<JsonObject> removegalleryattachement(
            @Part("token") RequestBody token,
            @Part("galId") RequestBody subject,
            @Part("lang") RequestBody lang,
            @PartMap Map <String, String> grpListId);
    @Multipart
    @POST("gallery/add-attachment")
    Call<JsonObject> AddGalleryattachement(
            @Part("token") RequestBody token,
            @Part("galId") RequestBody subject,
            @Part("type") RequestBody description,
            @Part("lang") RequestBody lang,
            @Part List <MultipartBody.Part> attachments);

    @FormUrlEncoded
    @POST("get-events")
    Call<JsonObject> getEvents(
            @Field("token") String accesstoken,
            @Field("school_id") String school_id,
            @Field("school_year_id") String school_year_id,
            @Field("role") String role,
            @Field("section_id") String sectionId,
            @Field("class_id") String classid,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("event-types/get-requests")
    Call<JsonObject> get_requests(
            @Field("token") String accesstoken,
            @Field("school_id") String school_id,
            @Field("school_year_id") String school_year_id,
            @Field("show_expired") String show_expired,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("add-info")
    Call<JsonObject> createEvents(
            @Field("token") String accesstoken,
            @Field("school_id") String school_id,
            @Field("school_year_id") String school_year_id,
            @Field("title") String title,
            @Field("start") String start,
            @Field("end") String end,
            @Field("color") String color,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("delete-info")
    Call<JsonObject> deleteEvents(
            @Field("token") String accesstoken,
            @Field("id") String eventid,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("announcements/get-class-details")
    Call<JsonObject> get_class_details(
            @Field("token") String token,
            @Field("school_id") String school_id,
            @Field("school_year_id") String school_year_id,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("event-types/get-slots")
    Call<JsonObject> get_slots(
            @Field("token") String token,
            @Field("start") String start,
            @Field("end") String end,
            @Field("duration") String duration,
            @Field("lang") String lang);

    //token, ids[], school_id, school_year_id
    @Multipart
    @POST("event-types/get-students")
    Call<JsonObject> get_students(
            @PartMap Map <String, RequestBody> params);

    //token, etClassSection[], etTitle,etType,etDesc,etStart,etEnd,
    // etDuration,etStdId[],etExemption[],attachments[],etMaxInvite,
    // etSpecific,school_id,school_year_id
    @Multipart
    @POST("event-types/create")
    Call<JsonObject> create_event(
            @PartMap Map <String, RequestBody> params);

    //token, etId, etTitle, etDesc, attachments[]
    @Multipart
    @POST("event-types/update")
    Call<JsonObject> update_event(
            @PartMap Map <String, RequestBody> params);

    @FormUrlEncoded
    @POST("event-types/delete")
    Call<JsonObject> delete_event(
            @Field("token") String token,
            @Field("etId") String etId,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("event-types/reports")
    Call<JsonObject> reports(
            @Field("token") String token,
            @Field("etId") String etId,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("event-types/available-slots")
    Call<JsonObject> available_slots(
            @Field("token") String token,
            @Field("etId") String etId,
            @Field("lang") String lang);

    @Multipart
    @POST("event-types/confirm-slot")
    Call<JsonObject> confirm_slot(
            @PartMap Map <String, RequestBody> params);
    @FormUrlEncoded
    @POST("event-types/download")
    Call<JsonObject> event_types_forms(
            @Field("token") String token,
            @Field("etId") String formid,
            @Field("lang") String lang);

    @Multipart
    @POST("gallery/create")
    Call<JsonObject> creategallery(
            @Part("token") RequestBody token,
            @Part("galSubject") RequestBody subject,
            @Part("galDesc") RequestBody description,
            @Part("school_id") RequestBody school_id,
            @Part("school_year_id") RequestBody school_year_id,
            @Part("type") RequestBody type,
            @Part("lang") RequestBody lang,
            @Part("postType") RequestBody radiotype,
            @Part List <MultipartBody.Part> attachments,
            @PartMap Map <String, String> grpListId,
            @PartMap Map <String, Integer> fields,
            @PartMap Map <String, String> galTags);

    @Multipart
    @POST("gallery/update")
    Call<JsonObject> updategallery(
            @Part("token") RequestBody token,
            @Part("galId") RequestBody galid,
            @Part("galSubject") RequestBody subject,
            @Part("galDesc") RequestBody description,
            @Part("school_id") RequestBody school_id,
            @Part("school_year_id") RequestBody school_year_id,
            @Part("lang") RequestBody lang,
            @Part("postType") RequestBody radiotype,
            @PartMap Map <String, Integer> fields,
            @PartMap Map <String, String> grpListId,
            @PartMap Map <String, String> galTags);

    @FormUrlEncoded
    @POST("gallery/lists")
    Call<JsonObject> galleryList(
            @Field("token") String token,
            @Field("school_id") String schoolid,
            @Field("page") String page,
            @Field("q") String q,
            @Field("school_year_id") String yearid,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("gallery/hided-posts")
    Call<JsonObject> galleryHiddenList(
            @Field("token") String token,
            @Field("school_id") String schoolid,
            @Field("q") String q,
            @Field("school_year_id") String yearid,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("gallery/like-post")
    Call<JsonObject> gallerylikepost(
            @Field("token") String token,
            @Field("galId") String schoolid,
            @Field("type") String yearid,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("gallery/hide-post")
    Call<JsonObject> galleryhideposts(
            @Field("token") String token,
            @Field("galId") String grpid,
            @Field("type") String type,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("gallery/delete")
    Call<JsonObject> gallerydeleteposts(
            @Field("token") String token,
            @Field("galId") String grpids,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("gallery/get-users-list")
    Call<JsonObject> gallerygetuserlist(
            @Field("token") String token,
            @Field("galId") String grpid,
            @Field("lang") String lang);



    @FormUrlEncoded
    @POST("direct-message/lists")
    Call<JsonObject> direct_message_lists(
            @Field("token") String token,
            @Field("school_id") String schoolid,
            @Field("school_year_id") String schoolyearid,
            @Field("q") String q,
            @Field("page") String paging_date,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("direct-message/get-class-details")
    Call<JsonObject> direct_message_get_class_details(
            @Field("token") String token,
            @Field("school_id") String schoolid,
            @Field(" school_year_id") String schoolyearid,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("direct-message/get-students")
    Call<JsonObject> direct_message_get_students(
            @Field("token") String token,
            @Field("school_id") String schoolid,
            @Field("school_year_id") String schoolyearid,
            @Field("id") String id,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("direct-message/get-conversations")
    Call<JsonObject> direct_message_get_conversations(
            @Field("token") String token,
            @Field("dmId") String dmId,
            @Field("lang") String lang);

    @Multipart
    @POST("direct-message/create")
    Call<JsonObject> direct_message_create(
            @PartMap Map <String, RequestBody> params);

    @Multipart
    @POST("direct-message/create-conversation")
    Call<JsonObject> direct_message_create_conversation(
            @PartMap Map <String, RequestBody> params);

    @FormUrlEncoded
    @POST("direct-message/get-participants")
    Call<JsonObject> direct_message_get_participants(
            @Field("token") String token,
            @Field("school_id") String schoolid,
            @Field("school_year_id") String schoolyearid,
            @Field("dmId") String dmId,
            @Field("lang") String lang);

    @Multipart
    @POST("direct-message/add-participants")
    Call<JsonObject> direct_message_add_participants(
            @PartMap Map <String, RequestBody> params);

    @FormUrlEncoded
    @POST("direct-message/update")
    Call<JsonObject> direct_message_update(
            @Field("token") String token,
            @Field("dmId") String dmId,
            @Field("subject") String subject,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("direct-message/delete-conversation")
    Call<JsonObject> direct_message_delete_conversation(
            @Field("token") String token,
            @Field("convId") String convId,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("direct-message/get-read-users")
    Call<JsonObject> direct_message_get_read_users(
            @Field("token") String token,
            @Field("id") String id,
            @Field("school_id") String schoolid);

    @FormUrlEncoded
    @POST("direct-message/mark-read")
    Call<JsonObject> direct_message_mark_read(
            @Field("token") String token,
            @Field("school_id") String schoolid,
            @Field("school_year_id") String schoolyearid);


   /* @FormUrlEncoded
    @POST("forms/lists")
    Call<JsonObject> formslist(
            @Field("token") String accesstoken,
            @Field("school_id") String school_id,
            @Field("school_year_id") String school_year_id,
            @Field("current_student_id") String currentStudentId,
            @Field("class_id") String class_id,
            @Field("section_id") String sectin_id,
            @Field("lang") String lang);*/
    @FormUrlEncoded
    @POST("forms/lists")
    Call<JsonObject> formslist(
            @Field("token") String accesstoken,
            @Field("school_id") String school_id,
            @Field("school_year_id") String school_year_id,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("forms/get-reports")
    Call<JsonObject> formsreport(
            @Field("token") String accesstoken,
            @Field("slug") String slug,
            @Field("sf_id") String sfid,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("forms/delete")
    Call<JsonObject> delete_forms(
            @Field("token") String token,
            @Field("id") String formid,
            @Field("school_id") String school_id,
            @Field("school_year_id") String school_yearid,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("forms/download")
    Call<JsonObject> download_forms(
            @Field("token") String token,
            @Field("id") String formid,
            @Field("slug") String school_id,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("leaves/get-requests")
    Call<JsonObject> leavegetrequest(
            @Field("token") String accesstoken,
            @Field("school_id") String school_id,
            @Field("school_year_id") String school_year_id,
            @Field("current_student_id") String currentStudentId,
            @Field("class_id") String class_id,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("leaves/delete")
    Call<JsonObject> leavedelete(
            @Field("token") String accesstoken,
            @Field("id") int id,
            @Field("school_id") String school_id,
            @Field("school_year_id") String school_year_id,
            @Field("lang") String lang);
    @FormUrlEncoded
    @POST("leaves/approve")
    Call<JsonObject> leaveapprove(
            @Field("token") String accesstoken,
            @Field("id") int id,
            @Field("school_id") String school_id,
            @Field("school_year_id") String school_year_id,
            @Field("type") int type,
            @Field("comments") String reason,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("leaves/create")
    Call<JsonObject> leavescreate(
            @Field("token") String accesstoken,
            @Field("school_id") String school_id,
            @Field("school_year_id") String school_year_id,
            @Field("current_student_id") String studentid,
            @Field("from_date") String fromdate,
            @Field("to_date") String todate,
            @Field("title") String title,
            @Field("description") String description,
            @Field("type") int type,
            @Field("lang") String lang);
  @FormUrlEncoded
    @POST("leaves/update")
    Call<JsonObject> leavesupdate(
          @Field("token") String accesstoken,
          @Field("id") int id,
          @Field("from_date") String fromdate,
          @Field("to_date") String todate,
          @Field("title") String title,
          @Field("description") String description,
          @Field("type") int type,
          @Field("lang") String lang);

    @FormUrlEncoded
    @POST("print-calendar")
    Call<JsonObject> download_calendar(
            @Field("token") String token,
            @Field("school_id") String formid,
            @Field("school_year_id") String school_id,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("get-info")
    Call<JsonObject> getinfo(
            @Field("school_id") String firstname,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("get-songs")
    Call<JsonObject> get_songs(
            @Field("school_id") String school_id,
            @Field("filter") String filter,
            @Field("search") String search);

    @FormUrlEncoded
    @POST("invoice/list")
    Call<JsonObject> invoicelist(
            @Field("token") String accesstoken,
            @Field("school_id") String school_id,
            @Field("school_year_id") String school_year_id,
            @Field("no") String no,
            @Field("status") String status,
            @Field("name") String name,
            @Field("amount") String amount,
            @Field("layout") String layout,
            @Field("from_date") String fromdate,
            @Field("to_date") String todate,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("invoice/view")
    Call<JsonObject> invoiceview(
            @Field("token") String accesstoken,
            @Field("id") String id,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("invoice/change-status")
    Call<JsonObject> invoicechangesstatus(
            @Field("token") String accesstoken,
            @Field("status") String status,
            @Field("id") String id,
            @Field("reason") String reason,
            @Field("lang") String lang);
    @FormUrlEncoded
    @POST("invoice/remove")
    Call<JsonObject> invoiceremove(
            @Field("token") String accesstoken,
            @Field("id") String id,
            @Field("reason") String reason,
            @Field("lang") String lang);
    @FormUrlEncoded
    @POST("invoice/get-layouts")
    Call<JsonObject> invoicegetlayout(
            @Field("token") String accesstoken,
            @Field("school_id") String school_id,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("invoice/download")
    Call<JsonObject> download_invoice(
            @Field("token") String token,
            @Field("id") String formid,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("send-notification")
    Call<JsonObject> send_notification(
            @Field("token") String token,
            @Field("type") String type);

}
