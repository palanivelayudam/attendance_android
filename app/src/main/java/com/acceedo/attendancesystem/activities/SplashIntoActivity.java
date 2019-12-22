package com.acceedo.attendancesystem.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.acceedo.attendancesystem.adapter.IntroPagerAdapter;
import com.acceedo.attendancesystem.Pojo.SplashIntro;
import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.common.OnboardingPageTransformer;
import com.acceedo.attendancesystem.databinding.ActivitySplashIntoBinding;

import java.util.ArrayList;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

public class SplashIntoActivity extends BaseActivity {

    ActivitySplashIntoBinding mBinding;
    ArrayList <SplashIntro> mList;
    IntroPagerAdapter mPagerAdapter;
    private LinearLayout pagerIndicator;

    private int dotsCount;
    private ImageView[] dots;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_into);
        mBinding.setData(this);

        mBinding.floatingActionButton.setVisibility(View.GONE);
        mBinding.button.setVisibility(View.GONE);

        mList = new ArrayList <>();
        for (int i = 0; i < 3; i++) {
            SplashIntro item = new SplashIntro();
            if (i == 0) {
                item.setTitle("What is Lorem Ipsum?");
                item.setName("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s");
            } else {
                item.setTitle("Where does it come from?");
                item.setName("Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old.");
            }

            mList.add(item);


        }
        mPagerAdapter = new IntroPagerAdapter(SplashIntoActivity.this, mList);
        mBinding.slideViewpager.setAdapter(mPagerAdapter);
        mPagerAdapter.notifyDataSetChanged();
        setUiPageViewController();
        mBinding.slideViewpager.setPageTransformer(false, new OnboardingPageTransformer());


        mBinding.slideViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.inactive_indicator));
                }
                dots[position].setImageDrawable(getResources().getDrawable(R.drawable.active_indicator));

                if (position == mList.size() - 1) {
                    sharedPreference.setIsSplashIntro(true);
                    mBinding.floatingActionButton.setVisibility(View.VISIBLE);
                    mBinding.button.setVisibility(View.GONE);
                } else {
                    sharedPreference.setIsSplashIntro(false);
                    mBinding.floatingActionButton.setVisibility(View.GONE);
                    mBinding.button.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SplashIntoActivity.this,LoginAcitivity.class);
                startActivity(intent);
                finish();

            }
        });
       /* mBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.slideViewpager.setCurrentItem(mBinding.slideViewpager.getCurrentItem() + 1, true);
            }
        });
*/

    }

    private void setUiPageViewController() {

        dotsCount = mPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.inactive_indicator));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            mBinding.viewPagerCountDots.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.active_indicator));
    }


}
