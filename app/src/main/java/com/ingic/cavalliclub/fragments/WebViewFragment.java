package com.ingic.cavalliclub.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.PayNowEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WebViewFragment extends BaseFragment {
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    private static String CASE_PAYED = "CASE_PAYED";
    private PayNowEntity entity;
    Unbinder unbinder;

    public static WebViewFragment newInstance(PayNowEntity entity) {
        Bundle args = new Bundle();
        args.putString(CASE_PAYED, new Gson().toJson(entity));
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static WebViewFragment newInstance() {
        Bundle args = new Bundle();
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            CASE_PAYED = getArguments().getString(CASE_PAYED);
        }
        if (CASE_PAYED != null) {
            entity = new Gson().fromJson(CASE_PAYED, PayNowEntity.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                WebAction();
            }
        });
        WebAction();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.hideButtons();
        titleBar.setSubHeading("Payment Method");
        titleBar.showBackButton();
    }

    public void WebAction() {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.loadUrl("http://cavalli.stagingic.com/payment?o_id=" + entity.getId());
        //webView.loadUrl("https://www.google.com/");
        swipe.setRefreshing(true);
        webView.setWebViewClient(new WebViewClient() {

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                //webView.loadUrl("file:///android_assets/error.html");
                UIHelper.showShortToastInCenter(getDockActivity(), "Error! Please try again later.");

            }

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                swipe.setRefreshing(false);
            }


            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                if (url.equals("http://cavalli.stagingic.com/success?id=" + entity.getId())) {
                    getDockActivity().popBackStackTillEntry(0);
                    UIHelper.showLongToastInCenter(getDockActivity(), "Payment successful.");
                    getDockActivity().replaceDockableFragment(OrderTokenFragment.newInstance(entity.getOrderNo()), "OrderTokenFragment");
                }
                if (url.equals("http://cavalli.stagingic.com/fail?id=" + entity.getId())){
                    getDockActivity().popBackStackTillEntry(0);
                    UIHelper.showLongToastInCenter(getDockActivity(), "Payment cancelled.");
                    getDockActivity().replaceDockableFragment(BarOrderingFragment.newInstance(), "BarOrderingFragment");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
