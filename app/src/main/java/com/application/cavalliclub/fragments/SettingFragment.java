package com.application.cavalliclub.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.application.cavalliclub.R;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingFragment extends BaseFragment {
    @BindView(R.id.txtNotifications)
    AnyTextView txtNotifications;
    @BindView(R.id.txt_no)
    AnyTextView txtNo;
    @BindView(R.id.toggleBtn)
    ToggleButton toggleBtn;
    @BindView(R.id.txt_yes)
    AnyTextView txtYes;
    @BindView(R.id.txtChangePassword)
    AnyTextView txtChangePassword;
    @BindView(R.id.txtBillingDetail)
    AnyTextView txtBillingDetail;
    @BindView(R.id.txtPrivacyPolicy)
    AnyTextView txtPrivacyPolicy;
    @BindView(R.id.txtTermsCond)
    AnyTextView txtTermsCond;
    @BindView(R.id.txtEditProfile)
    AnyTextView txtEditProfile;
    @BindView(R.id.ll_change_password)
    LinearLayout llChangePassword;
    Unbinder unbinder;
    @BindView(R.id.txtChangePhone)
    AnyTextView txtChangePhone;
    @BindView(R.id.tvVersionCode)
    TextView tvVersionCode;

    public static SettingFragment newInstance() {
        Bundle args = new Bundle();

        SettingFragment fragment = new SettingFragment();
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToggleBtnListner();
        if (prefHelper != null && prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getSocialmediaId() != null
                && (!(prefHelper.getSignUpUser().getSocialmediaId().equalsIgnoreCase("")))) {
            llChangePassword.setVisibility(View.GONE);
        } else if (prefHelper != null && prefHelper.getUpdatedUser() != null && prefHelper.getUpdatedUser().getSocialmediaId() != null
                && (!(prefHelper.getUpdatedUser().getSocialmediaId().equalsIgnoreCase("")))) {
        } else {
            llChangePassword.setVisibility(View.VISIBLE);
        }

        try {
            PackageInfo pInfo = getDockActivity().getPackageManager().getPackageInfo(getDockActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            tvVersionCode.setText(version.trim());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setToggleBtnListner() {

        getMainActivity().hideBottomTab();
        if (prefHelper.getPushValue() == (AppConstants.PUSH_NOTIFICATION_ON))
            toggleBtn.setChecked(true);
        else if (prefHelper.getPushValue() == (AppConstants.PUSH_NOTIFICATION_OFF)) {
            toggleBtn.setChecked(false);
        } else {
            toggleBtn.setChecked(true);
        }

        toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pushNotificationToggle();
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.hideTwoTabsLayout();
        //  titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.setting));
    }

    @OnClick({R.id.txtChangePassword, R.id.txtEditProfile, R.id.txtBillingDetail, R.id.txtPrivacyPolicy, R.id.txtTermsCond, R.id.toggleBtn, R.id.txtChangePhone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txtChangePassword:
                getDockActivity().replaceDockableFragment(ChangePasswordFragment.newInstance(), "ChangePasswordFragment");
                break;
            case R.id.txtEditProfile:
                getDockActivity().replaceDockableFragment(EditProfilefragment.newInstance(), "EditProfilefragment");
                break;
            case R.id.txtBillingDetail:
                //getDockActivity().replaceDockableFragment(BillingDetailFragment.newInstance(),"BillingDetailFragment");
                getMainActivity().notImplemented();
                break;
            case R.id.txtPrivacyPolicy:
                getDockActivity().replaceDockableFragment(PrivacyPolicyFragment.newInstance(), "PrivacyPolicyFragment");
                break;
            case R.id.txtTermsCond:
                getDockActivity().replaceDockableFragment(TermsAndConditionFragment.newInstance(), "TermsAndConditionFragment");
                break;
            case R.id.toggleBtn:
                break;

            case R.id.txtChangePhone:
                getDockActivity().replaceDockableFragment(ChangePhoneNumFragment.newInstance(), "ChangePhoneNumFragment");
                break;
        }
    }

    private void pushNotificationToggle() {
        if (prefHelper.getPushValue() == (AppConstants.PUSH_NOTIFICATION_ON))
            serviceHelper.enqueueCall(webService.pushNotification(AppConstants.PUSH_NOTIFICATION_OFF), WebServiceConstants.PUSH_NOTIFICATION_TOGGLE_OFF);
        else if (prefHelper.getPushValue() == (AppConstants.PUSH_NOTIFICATION_OFF)) {
            serviceHelper.enqueueCall(webService.pushNotification(AppConstants.PUSH_NOTIFICATION_ON), WebServiceConstants.PUSH_NOTIFICATION_TOGGLE_ON);
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.PUSH_NOTIFICATION_TOGGLE_OFF:
                prefHelper.setPushValue(AppConstants.PUSH_NOTIFICATION_OFF);
                UIHelper.showShortToastInCenter(getDockActivity(), "Notification status updated successfully");
                break;
            case WebServiceConstants.PUSH_NOTIFICATION_TOGGLE_ON:
                prefHelper.setPushValue(AppConstants.PUSH_NOTIFICATION_ON);
                UIHelper.showShortToastInCenter(getDockActivity(), "Notification status updated successfully");
                break;
        }
    }
}
