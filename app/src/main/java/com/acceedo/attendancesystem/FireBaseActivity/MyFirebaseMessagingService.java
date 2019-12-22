package com.acceedo.attendancesystem.FireBaseActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    Map<String, String> getData;
    String screenType="",id="",page="",schoolId="",schoolYearid="";
    int id1=0;
    String refreshedToken="";

    //private static final String TAG = "TestFCMMsg";

    //notification object

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional

       // refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Toast.makeText(getApplicationContext(),refreshedToken,Toast.LENGTH_SHORT).show();
        Log.i(TAG, "From: " + remoteMessage.getFrom());
        Log.i(TAG, "Notification Message Title: " + remoteMessage.getNotification().getTitle());
        Log.i(TAG, "Notification Message:" + remoteMessage.getData().toString());
        Log.i(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        getData=remoteMessage.getData();
        Log.i(TAG, "Notification Message Data: " + remoteMessage.getData());


        if (getData.containsKey("screen")) {
            screenType = getData.get("screen");
        }else {
            screenType = "";
        }
        if (getData.containsKey("page")) {
            page = getData.get("page");
        }else {
            page = "";
        }
        if (getData.containsKey("school_id")) {
            schoolId = getData.get("school_id");
        }else {
            schoolId = "";
        }
        if (getData.containsKey("school_year_id")) {
            schoolYearid = getData.get("school_year_id");
        }else {
            schoolYearid = "";
        }
        if (getData.containsKey("id")) {
            id = getData.get("id");
        }else {
            id = "";
        }

        try {

            if (!remoteMessage.getNotification().getTitle().isEmpty()) {

                if (remoteMessage.getData().size()>0) {

                    sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData(), "");

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts

    private void sendNotification(String title, String body, Map<String,String> message, String type) {

        String notifyType="",notifyCategory="",classifyID="",chatNavigate="";

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        /*PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);*/
        Log.i("notify",type);
        Log.i("notify1",title);
        Log.i("notify2", String.valueOf(message));

        Intent intent=null;

        /* if(screenType.equals("announcements")){
            intent=new Intent(this, AnnouncementActivity.class);
            intent.putExtra("ClassName","MyFirebaseMessagingService");
            intent.putExtra("page",page);
            intent.putExtra("school_id",schoolId);
            intent.putExtra("announcemntid",id);
            intent.putExtra("school_year_id",schoolYearid);
            intent.putExtra("entry","1");
        }
        else if(screenType.equals("gallery")){
            intent=new Intent(this, GalleryActivity.class);
            intent.putExtra("page",page);
            intent.putExtra("school_id",schoolId);
            intent.putExtra("galleryid",id);
            intent.putExtra("school_year_id",schoolYearid);
            intent.putExtra("entry","1");
        }
        else if(screenType.equals("events")){
            intent=new Intent(this, EventActivity.class);
            intent.putExtra("ClassName","MyFirebaseMessagingService");
            intent.putExtra("etId",id);
            intent.putExtra("page",page);
            intent.putExtra("school_id",schoolId);
            intent.putExtra("school_year_id",schoolYearid);
            intent.putExtra("entry","1");
        }*//*
        else if(screenType.equals("direct_message")){
            intent=new Intent(this, DMActivity.class);
            intent.putExtra("ClassName","MyFirebaseMessagingService");
            intent.putExtra("etId",id);
            intent.putExtra("page",page);
            intent.putExtra("school_id",schoolId);
            intent.putExtra("school_year_id",schoolYearid);
            intent.putExtra("entry","1");
        }else if(screenType.equals("leave_requests")){
            intent=new Intent(this, LeaveRequestList.class);
            intent.putExtra("etId",id);
            intent.putExtra("page",page);
            intent.putExtra("school_id",schoolId);
            intent.putExtra("school_year_id",schoolYearid);
        }*//*else if(screenType.equals("forms")){
            intent=new Intent(this, FormsListActivity.class);
            intent.putExtra("etId",id);
            intent.putExtra("page",page);
            intent.putExtra("school_id",schoolId);
            intent.putExtra("school_year_id",schoolYearid);
        }
        else if(screenType.equals("invoice")){
             intent=new Intent(this, FormsListActivity.class);
             intent.putExtra("etId",id);
             intent.putExtra("page",page);
             intent.putExtra("school_id",schoolId);
             intent.putExtra("school_year_id",schoolYearid);
         }
         else if(screenType.equals("aarthi")){
             intent=new Intent(this, MainActivity3.class);
             intent.putExtra("type",getData.get("type"));
         }*/
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);

//            //This is the default without sound and vibrate dsettings
//            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(title)
//                    .setContentText(messageBody)
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

      /*  NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_stat_icon)
                .setContentTitle(title)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification_stat_icon))
                .setBadgeIconType(R.drawable.notification_stat_icon)
                .setColor(getResources().getColor(R.color.notificstioniconcolor))
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(alarmSound);

        notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(getRandomId(), notificationBuilder.build());*/

        }


    public int getRandomId() {
        int min = 0;
        int max = 999;
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;

    }


}
