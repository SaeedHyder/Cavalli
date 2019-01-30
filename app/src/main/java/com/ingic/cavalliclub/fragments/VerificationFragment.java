package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.iid.FirebaseInstanceId;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.SignUpEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.ui.views.PinEntryEditText;
import com.ingic.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class VerificationFragment extends BaseFragment {

    @BindView(R.id.txt_pin_entry)
    PinEntryEditText txtPinEntry;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.scrollview_bigdaddy)
    LinearLayout scrollviewBigdaddy;
    Unbinder unbinder;
    @BindView(R.id.back_btn)
    ImageView backBtn;
    private static String FORGOT_SCENARIO = "";
    private static String HANDLE_BACK = "";

    public static VerificationFragment newInstance(String isComingFrom) {
        HANDLE_BACK = isComingFrom;
        return new VerificationFragment();
    }

    public static VerificationFragment newInstance(String forgotPassword, String isComingFrom) {
        FORGOT_SCENARIO = forgotPassword;
        HANDLE_BACK = isComingFrom;
        return new VerificationFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verification, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @OnClick({R.id.back_btn, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                if (HANDLE_BACK.equalsIgnoreCase(AppConstants.LOGIN)){
                    getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
                } else if (HANDLE_BACK.equalsIgnoreCase(AppConstants.RESET)){
                    getDockActivity().replaceDockableFragment(ResetPasswordFragment.newInstance(), "ResetPasswordFragment");
                } else if (HANDLE_BACK.equalsIgnoreCase(AppConstants.FORGOT)){
                    getDockActivity().replaceDockableFragment(ForgotPasswordFragment.newInstance(), "ForgotPasswordFragment");
                } else if (HANDLE_BACK.equalsIgnoreCase(AppConstants.SIGNUP)){
                    getDockActivity().replaceDockableFragment(SignupFragment.newInstance(), "SignupFragment");
                } else {
                    getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
                }
                break;
            case R.id.btn_login:
                UIHelper.hideSoftKeyboard(getDockActivity(), getView());
                if (txtPinEntry.getText().toString().equalsIgnoreCase("")) {
                    UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.enter_verification_code));
                } else if (txtPinEntry.getText().toString().length() < 4) {
                    UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.correct_verification_code));
                } else {
                    getVerificationCode();
                }
                break;
        }
    }

    private void getVerificationCode() {
        if (prefHelper != null && prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getId() != null) {
            serviceHelper.enqueueCall(webService.Verification(prefHelper.getSignUpUser().getId(), txtPinEntry.getText().toString()), WebServiceConstants.VERIFICATION);
        } else {
            serviceHelper.enqueueCall(webService.Verification(prefHelper.getUpdatedUser().getId(), txtPinEntry.getText().toString()), WebServiceConstants.VERIFICATION);
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.VERIFICATION:
                if (FORGOT_SCENARIO != null)
                    if (FORGOT_SCENARIO.equalsIgnoreCase(AppConstants.FORGOT_PASSWORD_SCENARIO_STRING)) {
                        getDockActivity().popBackStackTillEntry(0);
                        getDockActivity().replaceDockableFragment(ResetPasswordFragment.newInstance(), "ResetPasswordFragment");
                    } else {
                        prefHelper.setLoginStatus(true);
                        getDockActivity().popBackStackTillEntry(0);
                        SignUpEntity entity = (SignUpEntity) result;
                        prefHelper.putSignupUser(entity);
                        prefHelper.setFirebase_TOKEN(FirebaseInstanceId.getInstance().getToken());
                        getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                        break;
                    }
        }
    }
}
