package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.SignUpEntity;
import com.application.cavalliclub.entities.UpdateProfileEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.ui.views.PinEntryEditText;
import com.application.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.application.cavalliclub.global.WebServiceConstants.VERIFICATIONNEWPASSWORD;

/**
 * Created by ahmedsyed on 10/19/2018.
 */


public class PhoneNoVerificationFragment extends BaseFragment {

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
    private static boolean isNewPassword = false;
    private static String DataKey = "DataKey";
    private static String PhoneNumber;

    private SignUpEntity entity;

    public static PhoneNoVerificationFragment newInstance() {
        isNewPassword = false;
        PhoneNumber = "";
        return new PhoneNoVerificationFragment();
    }

    public static PhoneNoVerificationFragment newInstance(boolean isNewPass, SignUpEntity data, String number) {
        Bundle args = new Bundle();
        args.putString(DataKey, new Gson().toJson(data));
        isNewPassword = isNewPass;
        PhoneNumber = number;
        PhoneNoVerificationFragment fragment = new PhoneNoVerificationFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String JsonData = getArguments().getString(DataKey);
            if (JsonData != null) {
                entity = new Gson().fromJson(JsonData, SignUpEntity.class);
            }
        }
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
                getDockActivity().popFragment();
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
        if (isNewPassword) {
            if (entity != null && PhoneNumber != null && entity.getVerificationCode() != null && entity.getVerificationCode().equals(txtPinEntry.getText().toString())) {
                serviceHelper.enqueueCall(webService.updatePhoneNumber(PhoneNumber, prefHelper.getSignUpUser().getToken() + ""), VERIFICATIONNEWPASSWORD);
            } else {
                UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.invalid_code));
            }

        } else {
            if (entity != null && entity.getVerificationCode() != null && entity.getVerificationCode().equals(txtPinEntry.getText().toString())) {
                getDockActivity().popFragment();
                getDockActivity().replaceDockableFragment(ChangePhoneNumFragment.newInstance(true), "ChangePhoneNumFragment");
            } else {
                UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.invalid_code));
            }

        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

     //   23268
        switch (Tag) {

            case VERIFICATIONNEWPASSWORD:
                SignUpEntity userEnt = prefHelper.getSignUpUser();
                UpdateProfileEntity updateProfileEntity=prefHelper.getUpdatedUser();
                updateProfileEntity.setPhone(((SignUpEntity) result).getPhone());
                userEnt.setPhone(((SignUpEntity) result).getPhone());
                prefHelper.putSignupUser(userEnt);
                prefHelper.putUpdatedUser(updateProfileEntity);
                UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.update_successful));
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                break;
        }
    }
}
