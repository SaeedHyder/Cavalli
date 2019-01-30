package com.ingic.cavalliclub.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.FacebookLoginEnt;
import com.ingic.cavalliclub.entities.SignUpEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.helpers.DatePickerHelper;
import com.ingic.cavalliclub.helpers.FacebookLoginHelper;
import com.ingic.cavalliclub.helpers.InternetHelper;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.interfaces.FacebookLoginListener;
import com.ingic.cavalliclub.ui.views.AnyEditTextView;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignupFragment extends BaseFragment implements FacebookLoginListener {

    @BindView(R.id.edt_name)
    AnyEditTextView edtName;
    @BindView(R.id.edt_email)
    AnyEditTextView edtEmail;
    @BindView(R.id.edt_phone_number)
    AnyEditTextView edtPhoneNumber;
    @BindView(R.id.edt_password)
    AnyEditTextView edtPassword;
    @BindView(R.id.edt_cfm_password)
    AnyEditTextView edtCfmPassword;
    @BindView(R.id.btn_signup)
    Button btnSignup;
    @BindView(R.id.tv_login)
    AnyTextView tvLogin;
    @BindView(R.id.back_btn)
    ImageView backBtn;
    @BindView(R.id.scrollview_bigdaddy)
    ScrollView scrollviewBigdaddy;
    Unbinder unbinder;
    @BindView(R.id.loginButton_fb)
    LoginButton loginButtonFb;
    @BindView(R.id.rb_terms)
    CheckBox rbTerms;
    @BindView(R.id.txtFacebookLogin)
    AnyTextView txtFacebookLogin;
    @BindView(R.id.tv_terms_condition)
    AnyTextView tvTermsCondition;
    @BindView(R.id.ll_passwords)
    LinearLayout llPasswords;
    @BindView(R.id.ll_loginfacebook)
    RelativeLayout llLoginfacebook;
    CallbackManager callbackManager;
    FacebookLoginHelper facebookLoginHelper;
    FacebookLoginEnt facebookLoginEnt;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    String CombineNumber;
    String SocialMediaId = "";
    @BindView(R.id.btn_fb)
    Button btnFb;
    @BindView(R.id.tv_dob)
    AnyTextView tvDob;
    @BindView(R.id.sp_gender)
    Spinner spGender;
    @BindView(R.id.ll_btn_signup)
    LinearLayout llBtnSignup;
    private ArrayList<String> genderList = new ArrayList<>();
    private Date DateSelected;
    String splittedDate;
    String dateKeeper;
    String Social = "";

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().hideBottomTab();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
            }
        });
        llPasswords.setVisibility(View.VISIBLE);
        llBtnSignup.setVisibility(View.VISIBLE);

        if (prefHelper != null) {
            if (prefHelper.getCode() != null && !prefHelper.getCode().equalsIgnoreCase("")) {
                ccp.setDefaultCountryUsingNameCode(prefHelper.getCode());
                ccp.setCountryForNameCode(prefHelper.getCode());
                //   ccp.setCountryForPhoneCode(Integer.valueOf(prefHelper.getPhoneCode()));
            } else if (prefHelper.getFlag() != null && prefHelper.getFlag().equalsIgnoreCase(AppConstants.FLAG_0)) {
                prefHelper.setFlag(AppConstants.FLAG_1);
                ccp.setDefaultCountryUsingNameCode("US");
                ccp.setCountryForNameCode("US");
            }
        }

        ccp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prefHelper != null) {
                    ccp.setDefaultCountryUsingNameCode(prefHelper.getCode());
                    ccp.setCountryForNameCode(prefHelper.getCode());
                  //  ccp.setCountryForPhoneCode(Integer.valueOf(prefHelper.getPhoneCode()));
                }
            }
        });


        setGenderData();
        setupFacebookLogin();

        tvDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initFromPickerValidated(tvDob);
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    private boolean validate() {
        return edtName.testValidity() && edtEmail.testValidity() &&
                edtPhoneNumber.testValidity() && edtPassword.testValidity() && edtCfmPassword.testValidity();
    }

    @OnClick({R.id.btn_signup, R.id.tv_login, R.id.ll_loginfacebook, R.id.tv_terms_condition})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                UIHelper.hideSoftKeyboard(getDockActivity(), getView());
                if (validate()) {
                    CombineNumber = ccp.getSelectedCountryCodeWithPlus().toString() + edtPhoneNumber.getText().toString();
                    if (edtName.getText().toString().length() < 3) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.enter_correct_name));
                    } else if (edtPhoneNumber.getText().toString().length() < 8) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.correct_phone_number));
                    } else if (tvDob.getText().toString().equalsIgnoreCase(getString(R.string.date_of_birth))) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.please_select_date_birth));
                    } else if (spGender.getSelectedItemPosition() == 0) {
                        UIHelper.showLongToastInCenter(getDockActivity(), getString(R.string.please_select_gender));
                    } else if (edtPassword.getText().toString().length() < 6) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.password_length_alert));
                    } else if (edtCfmPassword.getText().toString().length() < 6) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.password_length_alert));
                    } else if (edtPassword.getText().toString().equals(edtCfmPassword.getText().toString())) {
                        prefHelper.setFirebase_TOKEN(FirebaseInstanceId.getInstance().getToken());
                        prefHelper.setPhoneCode(ccp.getSelectedCountryCodeWithPlus());
                        prefHelper.setCode(ccp.getSelectedCountryNameCode());
                        getSignupInfo();
                    } else {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.mismatch_confirm_password));
                    }
                }

                break;
            case R.id.tv_login:
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
                break;

            case R.id.ll_loginfacebook:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    Social = "fb";
                    llPasswords.setVisibility(View.GONE);
                    llBtnSignup.setVisibility(View.GONE);
                    LoginManager.getInstance().logOut();
                    LoginManager.getInstance().logInWithReadPermissions(SignupFragment.this, facebookLoginHelper.getPermissionNeeds());
                }
                break;

            case R.id.tv_dob:
                dateOfBirthListner();
                break;

            case R.id.tv_terms_condition:
                prefHelper.setPhoneCode(ccp.getSelectedCountryCodeWithPlus());
                prefHelper.setCode(ccp.getSelectedCountryNameCode());
                getDockActivity().replaceDockableFragment(TermsAndConditionFragment.newInstance(), "TermsAndConditionFragment");
                break;
        }
    }

    public void dateOfBirthListner() {
        tvDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFromPickerValidated(tvDob);
            }
        });
    }

    private void initFromPickerValidated(final AnyTextView textView) {

        Calendar calendar = Calendar.getInstance();
        final DatePickerHelper datePickerHelper = new DatePickerHelper();
        datePickerHelper.initDateDialogMin(
                getDockActivity(),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
                , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date date = new Date();
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // and get that as a Date
                        Date dateSpecified = c.getTime();
                        /*if (dateSpecified.after(date)) {
                            UIHelper.showShortToastInCenter(getDockActivity(), "Please enter valid date.");
                        } else {*/
                        DateSelected = dateSpecified;
                        String predate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

                        String[] splited = predate.split("\\s");
                        splittedDate = splited[0];
                        dateKeeper = splittedDate;
                        textView.setText(splittedDate);
                        textView.setPaintFlags(Typeface.BOLD);
                        //}
                    }
                }, "PreferredDate", new Date());
        datePickerHelper.showDate();
    }

    private void setGenderData() {

        genderList = new ArrayList<>();
        genderList.add("Select Gender");
        genderList.add("Male");
        genderList.add("Female");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, genderList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
        spGender.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getSignupInfo() {
        if (rbTerms.isChecked())
            serviceHelper.enqueueCall(webService.SimpleSignUp(edtName.getText().toString(), edtEmail.getText().toString(),
                    CombineNumber,
                    tvDob.getText().toString(),
                    spGender.getSelectedItem().toString(),
                    edtCfmPassword.getText().toString(), AppConstants.Device_Type,
                    FirebaseInstanceId.getInstance().getToken() + ""), WebServiceConstants.SIMPLE_SIGN_UP);
        else
            UIHelper.showShortToastInCenter(getDockActivity(), "Please check Terms and Conditions");
    }

    private void getSignupFacebookInfo() {
        if (rbTerms.isChecked())
            serviceHelper.enqueueCall(webService.SignUpWithSocialMedia(edtName.getText().toString(), edtEmail.getText().toString(),
                    CombineNumber,
                    tvDob.getText().toString(),
                    spGender.getSelectedItem().toString(),
                    SocialMediaId, AppConstants.SOCIAL_MEDIA_TYPE,
                    AppConstants.Device_Type,
                    FirebaseInstanceId.getInstance().getToken() + ""), WebServiceConstants.SOCIAL_MEDIA_SIGN_UP);
        else
            UIHelper.showShortToastInCenter(getDockActivity(), "Please check Terms and Conditions");
    }

    private boolean validateForFacebook() {
        return edtName.testValidity() && edtEmail.testValidity() && edtPhoneNumber.testValidity();
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.SIMPLE_SIGN_UP:
                SignUpEntity entity = (SignUpEntity) result;
                prefHelper.putSignupUser(entity);
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.code_sent_to_email));
                getDockActivity().replaceDockableFragment(VerificationFragment.newInstance(AppConstants.SIGNUP), "VerificationFragment");
                break;

            case WebServiceConstants.SOCIAL_MEDIA_SIGN_UP:
                SignUpEntity entity_social = (SignUpEntity) result;
                prefHelper.putSignupUser(entity_social);
                //prefHelper.setSocialLoginStatus(true);
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.code_sent_to_email));
                getDockActivity().replaceDockableFragment(VerificationFragment.newInstance(AppConstants.SIGNUP), "VerificationFragment");
                break;
        }
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
        prefHelper.setPhoneCode(ccp.getSelectedCountryCodeWithPlus());
        prefHelper.setCode(ccp.getSelectedCountryNameCode());
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccessfulFacebookLogin(FacebookLoginEnt loginEnt) {
        facebookLoginEnt = loginEnt;
        edtName.setText(checkForNullOREmpty(loginEnt.getFacebookFullName()));
        if (loginEnt.getFacebookEmail() != null && (!(loginEnt.getFacebookEmail().equalsIgnoreCase("")))) {
            edtEmail.setText(checkForNullOREmpty(loginEnt.getFacebookEmail()));
        }
        SocialMediaId = loginEnt.getFacebookUID();

        UIHelper.hideSoftKeyboard(getDockActivity(), getView());

        prefHelper.setPhoneCode(ccp.getSelectedCountryCodeWithPlus());
        prefHelper.setCode(ccp.getSelectedCountryNameCode());

        if (validateForFacebook()) {
            CombineNumber = ccp.getSelectedCountryCodeWithPlus().toString() + edtPhoneNumber.getText().toString();
            edtPhoneNumber.setImeOptions(EditorInfo.IME_ACTION_DONE);
            if (edtPhoneNumber.getText().toString().length() <= 0) {
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.enter_phone_number));
            } else if (edtPhoneNumber.getText().toString().length() < 6) {
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.correct_phone_number));
            } else if (tvDob.getText().toString().equalsIgnoreCase(getString(R.string.date_of_birth))) {
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.please_select_date_birth));
            } else if (spGender.getSelectedItemPosition() == 0) {
                UIHelper.showLongToastInCenter(getDockActivity(), getString(R.string.please_select_gender));
            } else {
                getSignupFacebookInfo();
            }
        } else {
            if (prefHelper != null) {
                if (prefHelper.getCode() != null && !prefHelper.getCode().equalsIgnoreCase("")) {
                    ccp.setDefaultCountryUsingNameCode(prefHelper.getCode());
                    ccp.setCountryForNameCode(prefHelper.getCode());
                   // ccp.setCountryForPhoneCode(Integer.valueOf(prefHelper.getPhoneCode()));
                } else if (prefHelper.getFlag() != null && prefHelper.getFlag().equalsIgnoreCase(AppConstants.FLAG_0)) {
                    prefHelper.setFlag(AppConstants.FLAG_1);
                    ccp.setDefaultCountryUsingNameCode("US");
                    ccp.setCountryForNameCode("US");
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dateKeeper != null && (!(dateKeeper.trim().equalsIgnoreCase(""))))
            tvDob.setText(dateKeeper + "");
        if (prefHelper != null) {
            if (prefHelper.getCode() != null && !prefHelper.getCode().equalsIgnoreCase("")) {
                ccp.setDefaultCountryUsingNameCode(prefHelper.getCode());
                ccp.setCountryForNameCode(prefHelper.getCode());
                /*if (prefHelper.getPhoneCode() != null && !prefHelper.getPhoneCode().equalsIgnoreCase("")) {
                    ccp.setCountryForPhoneCode(Integer.valueOf(prefHelper.getPhoneCode()));
                }*/
            } else if (prefHelper.getFlag() != null && prefHelper.getFlag().equalsIgnoreCase(AppConstants.FLAG_0)) {
                prefHelper.setFlag(AppConstants.FLAG_1);
                ccp.setDefaultCountryUsingNameCode("US");
                ccp.setCountryForNameCode("US");
            }
        }
        if (Social.equalsIgnoreCase("fb")) {
            llBtnSignup.setVisibility(View.GONE);
            llPasswords.setVisibility(View.GONE);
        }

    }
}

