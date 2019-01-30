package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.ui.views.AnyEditTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ChangePasswordFragment extends BaseFragment {
    @BindView(R.id.txt_current_pass)
    AnyEditTextView txtCurrentPass;
    @BindView(R.id.txt_new_pass)
    AnyEditTextView txtNewPass;
    @BindView(R.id.txt_confirm_new_pass)
    AnyEditTextView txtConfirmNewPass;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;

    public static ChangePasswordFragment newInstance() {
        Bundle args = new Bundle();

        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_changepassword, container, false);
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
        titleBar.setSubHeading(getString(R.string.change_password));
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        UIHelper.hideSoftKeyboard(getDockActivity(), getView());
        if (validate())
            if (txtCurrentPass.getText().toString().length() < 6) {
                UIHelper.showShortToastInCenter(getDockActivity(), "Password should be of at-least 6 characters.");
            } else if (txtNewPass.getText().toString().length() < 6) {
                UIHelper.showShortToastInCenter(getDockActivity(), "Password should be of at-least 6 characters.");
            } else if (txtConfirmNewPass.getText().toString().length() < 6) {
                UIHelper.showShortToastInCenter(getDockActivity(), "Password should be of at-least 6 characters.");
            } else if (!txtNewPass.getText().toString().equals(txtConfirmNewPass.getText().toString())) {
                UIHelper.showShortToastInCenter(getDockActivity(), "Password does not match");
            }else if (txtNewPass.getText().toString().equals(txtCurrentPass.getText().toString())) {
                UIHelper.showShortToastInCenter(getDockActivity(), "New password should be changed from old password");
            } else {
                changePassword();
            }
    }

    private void changePassword() {
        if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getToken() != null)
            serviceHelper.enqueueCall(webService.changePassword(txtCurrentPass.getText().toString(),
                    txtConfirmNewPass.getText().toString()), WebServiceConstants.CHANGE_PASSWORD);
    }

    private boolean validate() {
        return txtCurrentPass.testValidity() && txtNewPass.testValidity() && txtConfirmNewPass.testValidity();
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.CHANGE_PASSWORD:
                UIHelper.showShortToastInCenter(getDockActivity(),"Password changed successfully");
                getMainActivity().popFragment();
                break;
        }
    }
}
