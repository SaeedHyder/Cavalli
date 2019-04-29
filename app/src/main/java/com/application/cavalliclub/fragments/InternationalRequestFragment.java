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

public class InternationalRequestFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.edt_fullname)
    AnyEditTextView edtFullname;
    @BindView(R.id.edt_subject)
    AnyEditTextView edtSubject;
    @BindView(R.id.edt_msg)
    AnyEditTextView edtMsg;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    public static InternationalRequestFragment newInstance() {
        Bundle args = new Bundle();

        InternationalRequestFragment fragment = new InternationalRequestFragment();
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
        View view = inflater.inflate(R.layout.fragment_international_request, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivity().showBottomTab(AppConstants.home);
        setNamedata();
        btnSubmit.setEnabled(true);
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
        titleBar.hideTwoTabsLayout();
       // titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.international_request));
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (validate())
            if (edtFullname.getText().toString().length() < 3) {
                UIHelper.showShortToastInCenter(getDockActivity(), "Full name is too short.");
            } else if (edtSubject.getText().toString().length() < 2) {
                UIHelper.showShortToastInCenter(getDockActivity(), "Subject name is too short.");
            } else if (edtMsg.getText().toString().length() < 5) {
                UIHelper.showShortToastInCenter(getDockActivity(), "Message is too short.");
            } else {
                internationalRequest();
            }
    }

    private void internationalRequest() {
        btnSubmit.setEnabled(false);
        if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getToken() != null)
            serviceHelper.enqueueCall(webService.internationalRequest(edtSubject.getText().toString() + "", edtMsg.getText().toString() + "", prefHelper.getSignUpUser().getToken()), WebServiceConstants.INTERNATIONAL_REQUEST);
    }

    private boolean validate() {
        return edtFullname.testValidity() && edtSubject.testValidity() && edtMsg.testValidity();
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.INTERNATIONAL_REQUEST:
                UIHelper.showShortToastInCenter(getDockActivity(), "International request submitted successfully.");
                getMainActivity().popFragment();
                break;
        }
    }
}
