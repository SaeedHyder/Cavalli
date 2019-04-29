package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.UpdateProfileEntity;
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

public class ForgotPasswordFragment extends BaseFragment {


    @BindView(R.id.edt_email)
    AnyEditTextView edtEmail;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;
    UpdateProfileEntity updateProfileEntity;

    public static ForgotPasswordFragment newInstance() {
        return new ForgotPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
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
            public void onClick(View view) {
              getDockActivity().popFragment();
            }
        },getDockActivity());
        titleBar.hideTwoTabsLayout();
      //  titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.forgot_password));
    }

    private boolean validate() {
        return edtEmail.testValidity();
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        UIHelper.hideSoftKeyboard(getDockActivity(), getView());
        if (validate()){
            forgotPassword();
        }
    }

    private void forgotPassword(){
        serviceHelper.enqueueCall(webService.forgotPassword(edtEmail.getText().toString()), WebServiceConstants.FORGOT_PASSWORD);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.FORGOT_PASSWORD:
                UIHelper.showShortToastInCenter(getDockActivity(), "A new verification code has been sent to your email.");
                getDockActivity().replaceDockableFragment(VerificationFragment.newInstance( AppConstants.FORGOT_PASSWORD_SCENARIO_STRING, AppConstants.FORGOT), "VerificationFragment");
                updateProfileEntity = (UpdateProfileEntity) result;
                prefHelper.putUpdatedUser(updateProfileEntity);
/*                UIHelper.showShortToastInCenter(getDockActivity(), "A new password has been sent to your email.");
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "VerificationFragment");*/
                break;
        }
    }
}
