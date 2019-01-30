package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.PurchaseMembershipEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WebViewMembershipFragment extends BaseFragment {
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    private static String PURCHASE_MEMBERSHIP = "PURCHASE_MEMBERSHIP";
    private PurchaseMembershipEntity entity;
    Unbinder unbinder;

    public static WebViewMembershipFragment newInstance(PurchaseMembershipEntity entity) {
        Bundle args = new Bundle();
        args.putString(PURCHASE_MEMBERSHIP, new Gson().toJson(entity));
        WebViewMembershipFragment fragment = new WebViewMembershipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static WebViewMembershipFragment newInstance() {
        Bundle args = new Bundle();
        WebViewMembershipFragment fragment = new WebViewMembershipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            PURCHASE_MEMBERSHIP = getArguments().getString(PURCHASE_MEMBERSHIP);
        }
        if (PURCHASE_MEMBERSHIP != null) {
            entity = new Gson().fromJson(PURCHASE_MEMBERSHIP, PurchaseMembershipEntity.class);
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
        titleBar.hideTwoTabsLayout();
        titleBar.setSubHeading("Payment Method");
        titleBar.showBackButton();
    }

    public void WebAction() {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.loadUrl("http://cavalli.stagingic.com/membership-payment?m_id=" + entity.getId());
        swipe.setRefreshing(true);
        webView.setWebViewClient(new WebViewClient() {

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                UIHelper.showShortToastInCenter(getDockActivity(), "Error! Please try again later.");
            }

            public void onPageFinished(WebView view, String url) {
                swipe.setRefreshing(false);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                if (url.equals("http://cavalli.stagingic.com/membership-success?id=" + entity.getId())) {
                    getDockActivity().popBackStackTillEntry(0);
                    UIHelper.showLongToastInCenter(getDockActivity(), "Payment successful.");
                    getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                }
                if (url.equals("http://cavalli.stagingic.com/membership-fail?id=" + entity.getId())){
                    getDockActivity().popBackStackTillEntry(0);
                    UIHelper.showLongToastInCenter(getDockActivity(), "Payment cancelled.");
                    getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
