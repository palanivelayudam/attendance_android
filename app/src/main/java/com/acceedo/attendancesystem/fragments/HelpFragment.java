package com.acceedo.attendancesystem.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.acceedo.attendancesystem.R;
import com.acceedo.attendancesystem.databinding.HelpBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class HelpFragment extends BaseFragment {

    private HelpBinding mBinding;
    String HtmlDataSupport = "";
    String HtmlDataAbout = "https://sms.sisar.nl/report-issues";
    String version = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.help, container, false);
        mBinding.setData(this);

        mBinding.llHelp.setVisibility(View.VISIBLE);
        mBinding.llSupport.setVisibility(View.GONE);
        mBinding.llAbout.setVisibility(View.GONE);
        try {
            PackageInfo pInfo = requireContext().getPackageManager().getPackageInfo(requireContext().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        LoadSupport();
        LoadAbout();

        mBinding.tvSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.llHelp.setVisibility(View.GONE);
                mBinding.llSupport.setVisibility(View.VISIBLE);
                mBinding.llAbout.setVisibility(View.GONE);

            }
        });

        mBinding.tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                currenttab = "about";
//
//                toolbar.setTitle("AboutFragment");
//
//                llhelp.setVisibility(View.GONE);
//                llsupport.setVisibility(View.GONE);
//                llabout.setVisibility(View.VISIBLE);

//                Intent intent = new Intent(requireContext(),AboutFragment.class);
//                startActivity(intent);
            }
        });

        mBinding.tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(requireContext(),TermsofUse.class);
//                startActivity(intent);
            }
        });

        mBinding.tvPrivacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(requireContext(),PrivacyPolicy.class);
//                startActivity(intent);
            }
        });

        return mBinding.getRoot();
    }

    private void LoadSupport()
    {

        HtmlDataSupport="https://kb.sisarcams.com/wp-login.php?autologin="+sharedPreference.getuser_role();
        mBinding.webViewSupport.setWebViewClient(new MyBrowser());

        mBinding.tvVersionNameSupport.setBackgroundResource(R.color.ctransparent);
        mBinding.tvVersionNameSupport.setText(getString(R.string.faq_app_version) + " " + ":" + " " + version);

        mBinding.webViewSupport.setWebViewClient(new WebViewClient());
        mBinding.webViewSupport.getSettings().setLoadsImagesAutomatically(true);
        mBinding.webViewSupport.getSettings().setJavaScriptEnabled(true);

        mBinding.webViewSupport.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //mWebView.getSettings().setUserAgentString("Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            //Log.d(AppConstants.TAG,"SDk version above android L so forcibaly enabling ThirdPartyCookies");
            CookieManager.getInstance().setAcceptThirdPartyCookies(mBinding.webViewSupport, true);
        }



        mBinding.webViewSupport.setWebChromeClient(new WebChromeClient());

        mBinding.webViewSupport.loadUrl(HtmlDataSupport);

    }

    private void LoadAbout()
    {
        mBinding.webViewAbout.setWebViewClient(new MyBrowser());

        mBinding.tvVersionNameAbout.setBackgroundResource(R.color.ctransparent);
        mBinding.tvVersionNameAbout.setText(getString(R.string.faq_app_version) + " " + ":" + " " + version);

        mBinding.webViewAbout.setWebViewClient(new WebViewClient());
        mBinding.webViewAbout.getSettings().setLoadsImagesAutomatically(true);
        mBinding.webViewAbout.getSettings().setJavaScriptEnabled(true);

        mBinding.webViewAbout.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        //mWebView.getSettings().setUserAgentString("Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)");
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            //Log.d(AppConstants.TAG,"SDk version above android L so forcibaly enabling ThirdPartyCookies");
            CookieManager.getInstance().setAcceptThirdPartyCookies(mBinding.webViewAbout,true);
        }
        mBinding.webViewAbout.loadData(HtmlDataAbout, "text/html", null);
        mBinding.webViewAbout.setWebChromeClient(new WebChromeClient());

        mBinding.webViewAbout.loadUrl(HtmlDataAbout);

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
