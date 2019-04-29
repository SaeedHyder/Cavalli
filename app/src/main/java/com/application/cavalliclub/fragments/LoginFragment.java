package com.application.cavalliclub.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.FacebookLoginEnt;
import com.application.cavalliclub.entities.SignUpEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.FacebookLoginHelper;
import com.application.cavalliclub.helpers.InternetHelper;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.interfaces.FacebookLoginListener;
import com.application.cavalliclub.ui.views.AnyEditTextView;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class LoginFragment extends BaseFragment implements FacebookLoginListener {


    @BindView(R.id.edt_email)
    AnyEditTextView edtEmail;
    @BindView(R.id.edt_password)
    AnyEditTextView edtPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_forgot_password)
    AnyTextView tvForgotPassword;
    @BindView(R.id.tv_signup)
    AnyTextView tvSignup;
    @BindView(R.id.ll_loginfacebook)
    RelativeLayout llLoginfacebook;
    CallbackManager callbackManager;
    FacebookLoginHelper facebookLoginHelper;
    FacebookLoginEnt facebookLoginEnt;
    String SocialMediaId = "";
    Unbinder unbinder;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().hideBottomTab();
        setupFacebookLogin();
    }

    private boolean validate() {
        return edtEmail.testValidity() && edtPassword.testValidity();
    }

    @OnClick({R.id.btn_login, R.id.tv_forgot_password, R.id.tv_signup, R.id.ll_loginfacebook})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                UIHelper.hideSoftKeyboard(getDockActivity(), getView());
                if (validate())
                    if (edtPassword.getText().toString().length() < 6) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.password_length_alert));
                    } else {
                        //prefHelper.setLoginStatus(true);
                        prefHelper.setFirebase_TOKEN(FirebaseInstanceId.getInstance().getToken());
                        getLoginInfo();
                    }
                break;
            case R.id.tv_forgot_password:
                getDockActivity().replaceDockableFragment(ForgotPasswordFragment.newInstance(), "ForgotPasswordFragment");
                break;
            case R.id.tv_signup:
                getDockActivity().replaceDockableFragment(SignupFragment.newInstance(), "SignupFragment");
                break;
            case R.id.ll_loginfacebook:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    LoginManager.getInstance().logOut();
                    LoginManager.getInstance().logInWithReadPermissions(LoginFragment.this, facebookLoginHelper.getPermissionNeeds());
                }
                break;
        }
    }


    private void getLoginFacebookInfo() {
        serviceHelper.enqueueCall(webService.socialMediaLogin(AppConstants.SOCIAL_MEDIA_TYPE, SocialMediaId,
                AppConstants.Device_Type, FirebaseInstanceId.getInstance().getToken() + ""),
                WebServiceConstants.SOCIAL_MEDIA_LOGIN);
    }

    private void setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        //btnfbLogin.setFragment(this);
        facebookLoginHelper = new FacebookLoginHelper(getDockActivity(), this, this);
        LoginManager.getInstance().registerCallback(callbackManager, facebookLoginHelper);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccessfulFacebookLogin(FacebookLoginEnt loginEnt) {
        facebookLoginEnt = loginEnt;
        SocialMediaId = loginEnt.getFacebookUID();

        UIHelper.hideSoftKeyboard(getDockActivity(), getView());
        getLoginFacebookInfo();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    private void getLoginInfo() {
        serviceHelper.enqueueCall(webService.Login(edtEmail.getText().toString(), edtPassword.getText().toString(), AppConstants.Device_Type, FirebaseInstanceId.getInstance().getToken() + ""), WebServiceConstants.LOGIN);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.LOGIN:
                //getDockActivity().replaceDockableFragment(VerificationFragment.newInstance(), "VerificationFragment");

                getDockActivity().popBackStackTillEntry(0);
                SignUpEntity entity = (SignUpEntity) result;
                prefHelper.putSignupUser(entity);

                if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getActive() != null) {
                    if (prefHelper.getSignUpUser().getActive().equalsIgnoreCase("0")) {
                       // UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.code_sent_to_email));
                        getDockActivity().replaceDockableFragment(VerificationFragment.newInstance(AppConstants.LOGIN), "VerificationFragment");
                    } else {
                        prefHelper.setLoginStatus(true);
                        getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                    }
                }
                break;

            case WebServiceConstants.SOCIAL_MEDIA_LOGIN:
                //getDockActivity().replaceDockableFragment(VerificationFragment.newInstance(), "VerificationFragment");
                getDockActivity().popBackStackTillEntry(0);
                SignUpEntity entitySocial = (SignUpEntity) result;
                prefHelper.putSignupUser(entitySocial);

                if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getActive() != null) {
                    if (prefHelper.getSignUpUser().getActive().equalsIgnoreCase("0")) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.code_sent_to_email));
                        getDockActivity().replaceDockableFragment(VerificationFragment.newInstance(AppConstants.LOGIN), "VerificationFragment");
                    } else {
                        prefHelper.setLoginStatus(true);
                        getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                    }
                }
                break;
        }
    }
}
