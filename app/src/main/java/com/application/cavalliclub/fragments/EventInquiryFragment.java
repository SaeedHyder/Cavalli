package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.application.cavalliclub.R;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.ui.views.AnyEditTextView;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EventInquiryFragment extends BaseFragment {

    @BindView(R.id.edt_fullname)
    AnyTextView edtFullname;
    @BindView(R.id.edt_msg)
    AnyEditTextView edtMsg;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;
    private static int ID;

    public static EventInquiryFragment newInstance(Integer id) {
        ID = id;
        return new EventInquiryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_inquiry, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSubmit.setEnabled(true);
        setNamedata();
    }

    private void setNamedata() {
        if (prefHelper.getUpdatedUser() != null && prefHelper.getUpdatedUser().getUserName() != null) {
            edtFullname.setText(prefHelper.getUpdatedUser().getUserName().toString() + "");
        } else if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getUserName() != null) {
            edtFullname.setText(prefHelper.getSignUpUser().getUserName().toString() + "");
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.inquiry));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (validate())
            if (edtFullname.getText().toString().length() < 3) {
                UIHelper.showShortToastInCenter(getDockActivity(), "Full name is too short.");
            } else if (edtMsg.getText().toString().length() < 5) {
                UIHelper.showShortToastInCenter(getDockActivity(), "Message is too short.");
            } else {
                getInquiry();
            }
    }

    private boolean validate() {
        //return edtFullname.testValidity() && edtMsg.testValidity();
        return edtMsg.testValidity();
    }

    private void getInquiry() {
        btnSubmit.setEnabled(false);
        serviceHelper.enqueueCall(webService.addInquiry(ID + "", edtMsg.getText().toString() + ""), WebServiceConstants.INQUIRY);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.INQUIRY:
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                break;
        }
    }
}
