package com.acceedo.attendancesystem.activities;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.common.CustomTextWatcher;
import com.acceedo.attendancesystem.databinding.ActivityFeedbackBinding;
import com.acceedo.attendancesystem.databinding.DialogImagePickerBinding;

import org.apache.http.HttpHeaders;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Feedback extends BaseActivity {
    private ActivityFeedbackBinding mBinding;
    String name="",email="",comments="", rating = "";
    private static final int PERMISSION_ALL = 104;
    static int GALLERY_PICTURE = 25, CAMERA_REQUEST = 26;
    BottomSheetDialog mImageDialog;
    String s_attachment = "";
    String filepath1="",filepath2="",filepath3="";
    String id="",key="",self="";
    String selectType = "";
    public Uri CapturedImageURI;
    JSONObject jobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_feedback);

        mBinding.etName.addTextChangedListener(new CustomTextWatcher(mBinding.etName));
        mBinding.etEmail.addTextChangedListener(new CustomTextWatcher(mBinding.etEmail));
        mBinding.etComments.addTextChangedListener(new CustomTextWatcher(mBinding.etComments));

        mBinding.etName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                if ((motionEvent.getAction() & MotionEvent.ACTION_UP) != 0 && (motionEvent.getActionMasked() & MotionEvent.ACTION_UP) != 0)
                {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });

        mBinding.etEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                view.getParent().requestDisallowInterceptTouchEvent(true);
                if ((motionEvent.getAction() & MotionEvent.ACTION_UP) != 0 && (motionEvent.getActionMasked() & MotionEvent.ACTION_UP) != 0)
                {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });

        mBinding.etComments.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                view.getParent().requestDisallowInterceptTouchEvent(true);
                if ((motionEvent.getAction() & MotionEvent.ACTION_UP) != 0 && (motionEvent.getActionMasked() & MotionEvent.ACTION_UP) != 0)
                {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });
        mBinding.etName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.etName.setFocusableInTouchMode(true);
                mBinding.etName.requestFocus();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(mBinding.etName, InputMethodManager.SHOW_FORCED);
            }
        });
        mBinding.etEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.etEmail.setFocusableInTouchMode(true);
                mBinding.etEmail.requestFocus();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(mBinding.etEmail, InputMethodManager.SHOW_FORCED);
            }
        });
        mBinding.etComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.etComments.setFocusableInTouchMode(true);
                mBinding.etComments.requestFocus();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(mBinding.etComments, InputMethodManager.SHOW_FORCED);
            }
        });





        mBinding.llRatingAwesome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                rating = "5";

                mBinding.ratingAwesome.setImageResource(R.drawable.smiley);
                mBinding.ratingBad.setImageResource(R.drawable.sadgrey);
                mBinding.ratingGood.setImageResource(R.drawable.happygrey);
                mBinding.ratingMeh.setImageResource(R.drawable.meh);
                mBinding.ratingHorrible.setImageResource(R.drawable.unhappygrey);


            }
        });
        mBinding.llRatingGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rating = "4";

                mBinding.ratingAwesome.setImageResource(R.drawable.smileygrey);
                mBinding.ratingBad.setImageResource(R.drawable.sadgrey);
                mBinding.ratingGood.setImageResource(R.drawable.happy);
                mBinding.ratingMeh.setImageResource(R.drawable.meh);
                mBinding.ratingHorrible.setImageResource(R.drawable.unhappygrey);


            }
        });
        mBinding.llRatingMeh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rating = "3";

                mBinding.ratingAwesome.setImageResource(R.drawable.smileygrey);
                mBinding.ratingBad.setImageResource(R.drawable.sadgrey);
                mBinding.ratingGood.setImageResource(R.drawable.happygrey);
                mBinding.ratingMeh.setImageResource(R.drawable.confused);
                mBinding.ratingHorrible.setImageResource(R.drawable.unhappygrey);


            }
        });
        mBinding.llRatingBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rating = "2";

                mBinding.ratingAwesome.setImageResource(R.drawable.smileygrey);
                mBinding.ratingBad.setImageResource(R.drawable.sad);
                mBinding.ratingGood.setImageResource(R.drawable.happygrey);
                mBinding.ratingMeh.setImageResource(R.drawable.meh);
                mBinding.ratingHorrible.setImageResource(R.drawable.unhappygrey);


            }
        });
        mBinding.llRatingHorrible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rating = "1";

                mBinding.ratingAwesome.setImageResource(R.drawable.smileygrey);
                mBinding.ratingBad.setImageResource(R.drawable.sadgrey);
                mBinding.ratingGood.setImageResource(R.drawable.happygrey);
                mBinding.ratingMeh.setImageResource(R.drawable.meh);
                mBinding.ratingHorrible.setImageResource(R.drawable.unhappy);


            }
        });

        mBinding.ivFeedAttachement1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="1";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

                    if (!hasPermissions(Feedback.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(Feedback.this, PERMISSIONS, PERMISSION_ALL);
                    }
                    else
                    {
                        showImagePickerDialog();
                    }

                } else {
                    showImagePickerDialog();
                }
            }
        });
        mBinding.ivFeedAttachement2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="2";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

                    if (!hasPermissions(Feedback.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(Feedback.this, PERMISSIONS, PERMISSION_ALL);
                    }
                    else
                    {
                        showImagePickerDialog();
                    }

                } else {
                    showImagePickerDialog();
                }
            }
        });
        mBinding.ivFeedAttachement3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType="3";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

                    if (!hasPermissions(Feedback.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(Feedback.this, PERMISSIONS, PERMISSION_ALL);
                    }
                    else
                    {
                        showImagePickerDialog();
                    }

                } else {
                    showImagePickerDialog();
                }
            }
        });

        mBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mBinding.etName.getText().toString().trim().equals("")){



                    if (!mBinding.etComments.getText().toString().trim().equals("")) {
                        if (!mBinding.etEmail.getText().toString().trim().equals("")) {

                            email = mBinding.etEmail.getText().toString().trim();
                            if (cf.eMailValidation(email)) {
                                if(!rating.equals("")) {
                                    name = mBinding.etName.getText().toString().trim();
                                    comments = mBinding.etComments.getText().toString().trim();

                                    String mySDK = android.os.Build.VERSION.SDK;
                                    String myDevice = android.os.Build.DEVICE;
                                    String myDeviceModel = android.os.Build.MODEL;
                                    String myProduct = android.os.Build.PRODUCT;

                                    Log.i("mySDK", mySDK);
                                    Log.i("myDevice", myDevice);
                                    Log.i("myDeviceModel", myDeviceModel);
                                    Log.i("myProduct", myProduct);

                                    try {
                                        String description = "";

                                        description = "Name : " + name + "\n";
                                        description = description + "Email : " + email + "\n";
                                        description = description + "Comments : " + comments + "\n";
                                        description = description + "Rating : " + rating + "\n";
                                        description = description + "User ID : " + sharedPreference.getuser_id() + "\n";
                                        description = description + "User Full Name : " + sharedPreference.getfirstname()+" "+sharedPreference.getlastname() + "\n";
                                        description = description + "User username : " + sharedPreference.getusername() + "\n";
                                        description = description + "User Role : " + sharedPreference.getuser_role()+ "\n";
                                        description = description + "Device OS Version : " + mySDK + "\n";
                                        description = description + "Device Model : " + myDeviceModel + "\n";
                                        description = description + "Device Product : " + myProduct + "\n";
                                        description = description + "OS Type : Android";

                                        JSONObject jobjFields = new JSONObject();
                                        jobjFields.put("summary", "NEW FEEDBACK FROM ANDROID");
                                        jobjFields.put("description", description);

                                        JSONObject jobjproject = new JSONObject();
                                        jobjproject.put("key", "CAMS");

                                        JSONObject jobjissuetype = new JSONObject();
                                        jobjissuetype.put("name", "Task");

                                        jobjFields.put("project", jobjproject);
                                        jobjFields.put("issuetype", jobjissuetype);

                                        jobj = new JSONObject();
                                        jobj.put("fields", jobjFields);

                                        Log.i("JSON STRING ", jobj.toString());

                                        new FeedbackServer().execute();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    cf.show_toast(getResources().getString(R.string.feedback_rating));
                                }
                            } else {
                                cf.show_toast(getResources().getString(R.string.entervalidemail));
                            }


                        } else {
                            cf.show_toast(getResources().getString(R.string.feedback_enteremail));
                        }


                    }else {

                        cf.show_toast(getResources().getString(R.string.feedback_feedbackcomments));
                    }
                }else {
                    cf.show_toast(getResources().getString(R.string.feedback_entername));
                }
            }
        });

    }

    private String getB64Auth (String login, String pass) {
        String source=login+":"+pass;
        String ret="Basic "+ Base64.encodeToString(source.getBytes(),Base64.URL_SAFE|Base64.NO_WRAP);
        Log.i("getB64Auth : ",ret);
        return ret;
    }

    class FeedbackServer extends AsyncTask<Void, String, String> {

        String response = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            cf.progressdialog.show("");
        }

        @Override
        protected String doInBackground(Void... arg0)
        {
            InputStream inputStream = null;

            try {

                System.out.println("In Background...");

                if (Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy1 = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy1);
                }

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("https://sisargroup.atlassian.net/rest/api/2/issue/");

                httpPost.setEntity(new StringEntity(jobj.toString()));

                httpPost.setHeader(HttpHeaders.AUTHORIZATION, getB64Auth("suresh.kumar@sagous.in","keep5miling"));
                httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

//				httpPost.setHeader("Accept", "application/json");
//				httpPost.setHeader("Content-type", "application/json");

                HttpResponse httpResponse = httpclient.execute(httpPost);

                int statusCode = httpResponse.getStatusLine().getStatusCode();

                Log.i("Status Code",String.valueOf(statusCode));

                inputStream = httpResponse.getEntity().getContent();

                if (inputStream != null) {
                    response = convertInputStreamToString(inputStream);
                    System.out.println("the result is " + response);
                } else {
                    response = "error";
                    System.out.println("The result is " + response);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            cf.progressdialog.dismiss("");

            if (response.equals("error"))
            {
                cf.show_toast(getResources().getString(R.string.lbl_serverdown));
            }
            else
            {
                try
                {
                    JSONObject responseJson = new JSONObject(response);
                    id = responseJson.getString("id");
                    key = responseJson.getString("key");
                    self = responseJson.getString("self");

                    if(!filepath1.equals("") || !filepath2.equals("") || !filepath3.equals(""))
                    {
                        new FeedbackAttachmentServer().execute();
                    }
                    else
                    {
                        cf.show_toast("Thank you for your submission!");
                        finish();
                    }

                }
                catch (JSONException e)
                {
                    // JSON error

                    cf.show_toast("Unable to precess due to server issues");
                    e.printStackTrace();
                }
            }
        }
    }

    class FeedbackAttachmentServer extends AsyncTask<Void, String, String> {

        String response = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            cf.progressdialog.show("");
        }

        @Override
        protected String doInBackground(Void... arg0)
        {
            InputStream inputStream = null;

            try {

                System.out.println("In Background...");

                if (Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy1 = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy1);
                }

//                HttpClient httpclient = new DefaultHttpClient();
                HttpClient httpclient = HttpClientBuilder.create().build();
                HttpPost httpPost = new HttpPost("https://sisargroup.atlassian.net/rest/api/2/issue/"+key+"/attachments");

                MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();

                reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                if(!filepath1.equals(""))
                {
                    File file1 = new File(filepath1);
                    FileBody bin1 = new FileBody(file1);
                    reqEntity.addPart("file", bin1);
                }

                if(!filepath2.equals(""))
                {
                    File file1 = new File(filepath2);
                    FileBody bin1 = new FileBody(file1);
                    reqEntity.addPart("file", bin1);
                }

                if(!filepath3.equals(""))
                {
                    File file1 = new File(filepath3);
                    FileBody bin1 = new FileBody(file1);
                    reqEntity.addPart("file", bin1);
                }

                HttpEntity entity = reqEntity.build();

                httpPost.setEntity(entity);

                httpPost.setHeader(HttpHeaders.AUTHORIZATION, getB64Auth("suresh.kumar@sagous.in","keep5miling"));
                httpPost.setHeader("X-Atlassian-Token","nocheck");
//                httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
//                httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

                HttpResponse httpResponse = httpclient.execute(httpPost);

                int statusCode = httpResponse.getStatusLine().getStatusCode();

                Log.i("Status Code",String.valueOf(statusCode));

                inputStream = httpResponse.getEntity().getContent();

                if (inputStream != null) {
                    response = convertInputStreamToString(inputStream);
                    System.out.println("the result is " + response);
                } else {
                    response = "error";
                    System.out.println("The result is " + response);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            cf.progressdialog.dismiss("");

            if (response.equals("error"))
            {
                cf.show_toast(getResources().getString(R.string.lbl_serverdown));
            }
            else
            {
                cf.show_toast("Thank you for your submission!");
                finish();
            }
        }
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

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

    void showImagePickerDialog()
    {
        LinearLayout vGallery, vCamera, vRemove;

        mImageDialog = new BottomSheetDialog(this);
        DialogImagePickerBinding pickerBinding=DataBindingUtil.inflate(LayoutInflater.from(Feedback.this),
                R.layout.dialog_image_picker, null,false);
        //View sheetView = getLayoutInflater().inflate(R.layout.dialog_image_picker, null);

        //vGallery = (LinearLayout) sheetView.findViewById(R.id.gallery_image);
        //vCamera = (LinearLayout) sheetView.findViewById(R.id.camera_image);

        pickerBinding.galleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gallery_Open();

                mImageDialog.dismiss();

            }
        });

        pickerBinding.cameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Camera_Open();

                mImageDialog.dismiss();

            }
        });

        mImageDialog.setContentView(pickerBinding.getRoot());
        mImageDialog.show();
    }

    private void Gallery_Open() {
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, GALLERY_PICTURE);
    }

    private void Camera_Open() {
        String fileName = "temp.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        CapturedImageURI = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, CapturedImageURI);
        startActivityForResult(intent, CAMERA_REQUEST);
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

        }

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

                        if(selectType.equals("1"))
                        {
                            filepath1 = filePath;
                            mBinding.ivFeedAttachement1.setImageBitmap(bitmapdb);
                        }
                        else if(selectType.equals("2"))
                        {
                            filepath2 = filePath;
                            mBinding.ivFeedAttachement2.setImageBitmap(bitmapdb);
                        }
                        else if(selectType.equals("3"))
                        {
                            filepath3 = filePath;
                            mBinding.ivFeedAttachement3.setImageBitmap(bitmapdb);
                        }

                        s_attachment = filePath;

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
                    + "/GMIS-CAMS/Files/Compressed");

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()){
                mediaStorageDir.mkdirs();
            }

            String mImageName="IMG_"+ String.valueOf(System.currentTimeMillis()) +".jpg";
            String uriString = (mediaStorageDir.getAbsolutePath() + "/"+ mImageName);;
            return uriString;

        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }
}