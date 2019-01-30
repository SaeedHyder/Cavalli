package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrderTokenFragment extends BaseFragment {

    @BindView(R.id.tv_name)
    AnyTextView tvName;
    @BindView(R.id.tv_oder_no)
    AnyTextView tvOderNo;
    private static int ORDER_NUMBER;
    Unbinder unbinder;

    public static OrderTokenFragment newInstance(int orderNumber) {
        ORDER_NUMBER = orderNumber;
        return new OrderTokenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_token, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setData();
        getMainActivity().hideBottomTab();
    }

    private void setData() {

        if (prefHelper.getUpdatedUser() != null && prefHelper.getUpdatedUser() != null) {
            String sourceString = "Hey " + "<b>" + prefHelper.getUpdatedUser().getUserName() + "</b> " + "!";
            tvName.setText(Html.fromHtml(sourceString));
            tvOderNo.setText(ORDER_NUMBER + "");
        } else if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser() != null) {
            String sourceString = "Hey " + "<b>" + prefHelper.getSignUpUser().getUserName() + "</b> " + "!";
            tvName.setText(Html.fromHtml(sourceString));
            tvOderNo.setText(ORDER_NUMBER + "");
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(BarOrderingFragment.newInstance(), "BarOrderingFragment");
            }
        },getDockActivity());
        titleBar.hideTwoTabsLayout();
     //   titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.order_token));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
