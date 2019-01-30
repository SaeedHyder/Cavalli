package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.SignUpEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.ui.views.AnyEditTextView;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ingic.cavalliclub.global.WebServiceConstants.CHECKVERIFICATIONNEW;
import static com.ingic.cavalliclub.global.WebServiceConstants.CHECKVERIFICATIONOLD;


/**
 * Created by ahmedsyed on 10/19/2018.
 */
public class ChangePhoneNumFragment extends BaseFragment {
    @BindView(R.id.Countrypicker)
    CountryCodePicker Countrypicker;
    @BindView(R.id.edt_phone_number)
    AnyEditTextView edtPhoneNumber;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;
    PhoneNumberUtil phoneUtil;

    private static boolean isVerifiedUser = false;

    @BindView(R.id.txtPhoneNum)
    AnyTextView txtPhoneNum;

    private String CombineNumber = "";

    public static ChangePhoneNumFragment newInstance() {
        Bundle args = new Bundle();
        ChangePhoneNumFragment fragment = new ChangePhoneNumFragment();
        fragment.setArguments(args);
        isVerifiedUser = false;
        return fragment;
    }

    public static ChangePhoneNumFragment newInstance(boolean isVerified) {
        Bundle args = new Bundle();
        ChangePhoneNumFragment fragment = new ChangePhoneNumFragment();
        fragment.setArguments(args);
        isVerifiedUser = isVerified;
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
        View view = inflater.inflate(R.layout.fragment_change_phone, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        phoneUtil = PhoneNumberUtil.getInstance();
        Countrypicker.registerCarrierNumberEditText(edtPhoneNumber);

        Countrypicker.setCountryForPhoneCode(Integer.parseInt(prefHelper.getPhoneCode()));
        if (isVerifiedUser) {
           // Countrypicker.setCountryForNameCode(prefHelper.getCode());
            // edtPhoneNumber.setHint(getDockActivity().getResources().getString(R.string.new_phone_num));
            txtPhoneNum.setText(getDockActivity().getResources().getString(R.string.new_phone_num));
            btnSubmit.setText(getDockActivity().getResources().getText(R.string.update));
        }

        listner();

    }

    private void listner() {

        Countrypicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
               /* prefHelper.setCode(Countrypicker.getSelectedCountryNameCode());
                prefHelper.setPhoneCode(Countrypicker.getSelectedCountryCodeWithPlus());*/
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.hideTwoTabsLayout();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.change_phone_number));
    }


    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        submit();
/*
        if (isValidate()) {
            if (isPhoneNumberValid()) {
                if (isVerifiedUser) {
                    CombineNumber = Countrypicker.getSelectedCountryCodeWithPlus().toString() + edtPhoneNumber.getText().toString();
                    prefHelper.setCode(Countrypicker.getSelectedCountryNameCode());
                    serviceHelper.enqueueCall(webService.checkPhoneNumber(CombineNumber, AppConstants.New, prefHelper.getSignUpUser().getToken() + ""), CHECKVERIFICATIONNEW);

                } else {
                    CombineNumber = Countrypicker.getSelectedCountryCodeWithPlus().toString() + edtPhoneNumber.getText().toString();
                    prefHelper.setCode(Countrypicker.getSelectedCountryNameCode());
                    serviceHelper.enqueueCall(webService.checkPhoneNumber(CombineNumber, AppConstants.Old, prefHelper.getSignUpUser().getToken() + ""), CHECKVERIFICATIONOLD);

                }
            }
        }
*/
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);
        switch (Tag) {
            case CHECKVERIFICATIONNEW:
                SignUpEntity entityNew = (SignUpEntity) result;
                getDockActivity().popFragment();
                getDockActivity().replaceDockableFragment(PhoneNoVerificationFragment.newInstance(true,entityNew,CombineNumber), "PhoneNoVerificationFragment");
                break;

            case CHECKVERIFICATIONOLD:
                SignUpEntity entityOld = (SignUpEntity) result;
                getDockActivity().popFragment();
                getDockActivity().replaceDockableFragment(PhoneNoVerificationFragment.newInstance(false,entityOld,CombineNumber), "PhoneNoVerificationFragment");
                break;
        }
    }

    private boolean isValidate() {

        if (edtPhoneNumber.getText().toString() == null || edtPhoneNumber.getText().toString().trim().equals("") || edtPhoneNumber.getText().toString().isEmpty()) {
            edtPhoneNumber.setError("Enter phone number to proceed");
            return false;
        } else {
            return true;
        }
    }

    private boolean isPhoneNumberValid() {


        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(edtPhoneNumber.getText().toString(), Countrypicker.getSelectedCountryNameCode());
            if (phoneUtil.isValidNumber(number)) {
                return true;
            } else {
                edtPhoneNumber.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
                return false;
            }
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
            edtPhoneNumber.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
            return false;

        }

    }

    private void submit(){
        if(Countrypicker.getSelectedCountryCodeWithPlus().toString().length() > 0){
            if(edtPhoneNumber.getText().toString().length() == 0){
                edtPhoneNumber.setError("Enter Phone Number");
                return;
            }
            if(edtPhoneNumber.getText().toString().length() >= 7){
                if(!isVerifiedUser){
                    if(prefHelper.getUpdatedUser() != null){
                        if(prefHelper.getUpdatedUser().getPhone().equals(Countrypicker.getSelectedCountryCodeWithPlus().toString() + edtPhoneNumber.getText().toString())){
                            getDockActivity().popFragment();
                            getDockActivity().replaceDockableFragment(ChangePhoneNumFragment.newInstance(true), "ChangePhoneNumFragment");
                        }else{
                            Toast.makeText(getDockActivity(), "Old Phone number is not correct.", Toast.LENGTH_SHORT).show();
                        }
                    }else if(prefHelper.getSignUpUser() != null){
                        if(prefHelper.getSignUpUser().getPhone().equals(Countrypicker.getSelectedCountryCodeWithPlus().toString() + edtPhoneNumber.getText().toString())){
                            getDockActivity().popFragment();
                            getDockActivity().replaceDockableFragment(ChangePhoneNumFragment.newInstance(true), "ChangePhoneNumFragment");
                        }else{
                            Toast.makeText(getDockActivity(), "Old Phone number is not correct.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    CombineNumber = Countrypicker.getSelectedCountryCodeWithPlus().toString() + edtPhoneNumber.getText().toString();
                    prefHelper.setCode(Countrypicker.getSelectedCountryNameCode());
                    prefHelper.setPhoneCode(Countrypicker.getSelectedCountryCodeWithPlus());
                    serviceHelper.enqueueCall(webService.checkPhoneNumber(CombineNumber, AppConstants.New, prefHelper.getSignUpUser().getToken() + ""), CHECKVERIFICATIONNEW);
                }
            }else{
                edtPhoneNumber.setError("Enter Valid Phone Number");
            }
        }else{
            Toast.makeText(getDockActivity(), "Select Country Code", Toast.LENGTH_SHORT).show();
        }
    }


}
