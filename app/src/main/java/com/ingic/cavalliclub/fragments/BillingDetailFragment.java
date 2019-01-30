package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by saeedhyder on 1/25/2018.
 */
public class BillingDetailFragment extends BaseFragment {
    @BindView(R.id.ll_creditCard)
    LinearLayout llCreditCard;
    @BindView(R.id.ll_CashOnDelivery)
    LinearLayout llCashOnDelivery;
    Unbinder unbinder;

    public static BillingDetailFragment newInstance() {
        Bundle args = new Bundle();

        BillingDetailFragment fragment = new BillingDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_billing_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().hideBottomTab();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.hideTwoTabsLayout();
     //   titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.billing_detail));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_creditCard, R.id.ll_CashOnDelivery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_creditCard:
                getDockActivity().replaceDockableFragment(CreditCardDetailFragment.newInstance(),"CreditCardDetailFragment");
                break;
            case R.id.ll_CashOnDelivery:
                UIHelper.showShortToastInCenter(getDockActivity(), "Will be implemented");
                break;
        }
    }
}
