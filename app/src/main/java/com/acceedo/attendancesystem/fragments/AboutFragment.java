package com.acceedo.attendancesystem.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.activities.Feedback;
import com.acceedo.attendancesystem.databinding.FragmentAboutBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {
    private FragmentAboutBinding mBinding;
    String version = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);
        mBinding.setData(this);

        SpannableStringBuilder ssBuilder = new SpannableStringBuilder("At SISAR, We have worked hard to ensure our users interest are prioritised.");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sisarcams.com/")));

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(R.color.greenfeed_selected));
                ds.setFakeBoldText(true);

            }
        };

        ssBuilder.setSpan(clickableSpan, 3, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        mBinding.tvSisar.setText(ssBuilder);
        mBinding.tvSisar.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.tvSisar.setHighlightColor(Color.TRANSPARENT);

        mBinding.btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), Feedback.class);
                startActivity(intent);
            }
        });


        try {
            PackageInfo pInfo = requireContext().getPackageManager().getPackageInfo(requireContext().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mBinding.tvVersionNameSupport.setBackgroundResource(R.color.ctransparent);
        mBinding.tvVersionNameSupport.setText(getString(R.string.faq_app_version) + " " + ":" + " " + version);
        return mBinding.getRoot();
    }
}
