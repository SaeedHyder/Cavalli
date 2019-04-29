package com.application.cavalliclub.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.application.cavalliclub.R;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.ui.views.AnyEditTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ResetPasswordFragment extends BaseFragment {

    @BindView(R.id.txt_new_pass)
    AnyEditTextView txtNewPass;
    @BindView(R.id.txt_confirm_new_pass)
    AnyEditTextView txtConfirmNewPass;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;

    public static ResetPasswordFragment newInstance() {
        Bundle args = new Bundle();

        ResetPasswordFragment fragment = new ResetPasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
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
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(VerificationFragment.newInstance(AppConstants.RESET), "VerificationFragment");
            }
        },getDockActivity());
        titleBar.hideTwoTabsLayout();
        titleBar.setSubHeading(getString(R.string.reset_password));
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        UIHelper.hideSoftKeyboard(getDockActivity(), getView());
        if (validate())
            if (txtNewPass.getText().toString().length() < 6) {
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.password_length_alert));
            } else if (txtConfirmNewPass.getText().toString().length() < 6) {
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.password_length_alert));
            } else if (txtNewPass.getText().toString().equals(txtConfirmNewPass.getText().toString())) {
                resetPassword();
            } else {
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.mismatch_password));
            }
    }

    private boolean validate() {
        return txtNewPass.testValidity() && txtConfirmNewPass.testValidity();
    }

    private void resetPassword() {
        if (prefHelper != null && prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getId() != null) {
            serviceHelper.enqueueCall(webService.resetPassword(txtConfirmNewPass.getText().toString(), prefHelper.getSignUpUser().getId() + ""), WebServiceConstants.RESET_PASSWORD);
        } else {
            serviceHelper.enqueueCall(webService.resetPassword(txtConfirmNewPass.getText().toString(), prefHelper.getUpdatedUser().getId() + ""), WebServiceConstants.RESET_PASSWORD);
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.RESET_PASSWORD:
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
                break;
        }
    }
}
