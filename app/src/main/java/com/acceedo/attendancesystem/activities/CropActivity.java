package com.acceedo.attendancesystem.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.common.Shared_Preference;
import com.acceedo.attendancesystem.utils.CommonFuns;
import com.acceedo.attendancesystem.utils.CropImageViewOptions;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class CropActivity extends AppCompatActivity
        implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener {



    private CropImageView mCropImageView;
    CropImageViewOptions mCropImageViewOptions=new CropImageViewOptions();
    Shared_Preference shardPreference;
    Toolbar mToolbar;

    //GlobalValues mGlobalValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        shardPreference=new Shared_Preference(this);

        Intent intent=getIntent();
        String s=intent.getStringExtra("imageUri");
        Uri imageUri = Uri.parse(s);
        String imgType=intent.getStringExtra("imageType");
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(getApplicationContext().getResources().getColor(R.color.white));
        mToolbar.setNavigationIcon(R.drawable.backarrow);
        ImageView imageView = mToolbar.findViewById(R.id.anoce_editoolmenu);
        imageView.setVisibility(View.GONE);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.DarkPrimaryColor));
        }

//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
      //  getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_backarrow);

        /*Spannable text = new SpannableString(getResources().getString(R.string.select_image));
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
        getSupportActionBar().setLogo (getResources().getDrawable(R.drawable.ic_backarrow));*/

        mCropImageView = (CropImageView) findViewById(R.id.cropImageView);
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);

        if (imgType.equals("profile")) {
            mCropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
            mCropImageView.setAspectRatio(5, 5);
        }

        mCropImageView.setImageUriAsync(imageUri);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.crop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else*/ if (item.getItemId() == R.id.menu_image_rotate) {
            mCropImageView.rotateImage(90);
            return true;
        } else if (item.getItemId() == R.id.menu_image_crop) {
            mCropImageView.getCroppedImageAsync();

            Bitmap photo = mCropImageView.getCroppedImage();
            photo = Bitmap.createScaledBitmap(photo,photo.getWidth(),photo.getHeight(),false);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG,50, bytes);

            try {

                File folder = new File(CommonFuns.UploadImagePath);
                boolean success = false;
                if (!folder.exists()) {
                    success = folder.mkdirs();
                }
                //else if (!folder.isDirectory() && folder.canWrite()) {
                else if (folder.exists()) {
                    folder.delete();
                    folder.mkdirs();
                }
                System.out.println(success + "folder");

                File file = new File(CommonFuns.UploadImagePath
                        + File.separator + System.currentTimeMillis() + ".jpg");

                if (!file.exists()) {
                    try {
                        file.createNewFile();
                        FileOutputStream fo = new FileOutputStream(file);
                        fo.write(bytes.toByteArray());
                        fo.close();
                        //mGlobalValues.put("upload_image",file.getPath());

                        shardPreference.setcrop_img(file.getPath());


                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        finish();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (file.exists()) {
                    file.delete();
                    file.createNewFile();
                    FileOutputStream fo = new FileOutputStream(file);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    shardPreference.setcrop_img(file.getPath());

                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCropImageViewOptions(CropImageViewOptions options) {
        mCropImageView.setScaleType(options.scaleType);
        mCropImageView.setCropShape(options.cropShape);
        mCropImageView.setGuidelines(options.guidelines);
        mCropImageView.setAspectRatio(options.aspectRatio.first, options.aspectRatio.second);
        mCropImageView.setFixedAspectRatio(options.fixAspectRatio);
        mCropImageView.setMultiTouchEnabled(options.multitouch);
        mCropImageView.setShowCropOverlay(options.showCropOverlay);
        mCropImageView.setShowProgressBar(options.showProgressBar);
        mCropImageView.setAutoZoomEnabled(options.autoZoomEnabled);
        mCropImageView.setMaxZoom(options.maxZoomLevel);
    }

    /**
     * Set the initial rectangle to use.
     */
    public void setInitialCropRect() {
        mCropImageView.setCropRect(new Rect(100, 300, 500, 1200));
    }

    /**
     * Reset crop window to initial rectangle.
     */
    public void resetCropRect() {
        mCropImageView.resetCropRect();
    }

    public void updateCurrentCropViewOptions() {
        CropImageViewOptions options = new CropImageViewOptions();
        options.scaleType = mCropImageView.getScaleType();
        options.cropShape = mCropImageView.getCropShape();
        options.guidelines = mCropImageView.getGuidelines();
        options.aspectRatio = mCropImageView.getAspectRatio();
        options.fixAspectRatio = mCropImageView.isFixAspectRatio();
        options.showCropOverlay = mCropImageView.isShowCropOverlay();
        options.showProgressBar = mCropImageView.isShowProgressBar();
        options.autoZoomEnabled = mCropImageView.isAutoZoomEnabled();
        options.maxZoomLevel = mCropImageView.getMaxZoom();
        setCurrentOptions(options);
    }

    public void setCurrentOptions(CropImageViewOptions options) {
        mCropImageViewOptions = options;
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {

    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {

    }
}


