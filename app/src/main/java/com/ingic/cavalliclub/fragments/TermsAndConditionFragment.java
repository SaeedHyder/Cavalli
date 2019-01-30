package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.CmsEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class TermsAndConditionFragment extends BaseFragment {
    @BindView(R.id.txt_term_condition)
    AnyTextView txtTermCondition;
    @BindView(R.id.btn_accept)
    Button btnAccept;
    Unbinder unbinder;

    public static TermsAndConditionFragment newInstance() {
        Bundle args = new Bundle();

        TermsAndConditionFragment fragment = new TermsAndConditionFragment();
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
        View view = inflater.inflate(R.layout.fragment_termscondition, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().hideBottomTab();
        getTermsAndConditions();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.hideTwoTabsLayout();
        titleBar.setSubHeading(getString(R.string.terms_condition));
    }

    @OnClick(R.id.btn_accept)
    public void onViewClicked() {
        getMainActivity().notImplemented();
    }

    private void getTermsAndConditions(){
        serviceHelper.enqueueCall(webService.Cms(AppConstants.TERMS_AND_CONDITION), WebServiceConstants.TERMS_AND_CONDITION);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.TERMS_AND_CONDITION:
                CmsEntity entity=(CmsEntity)result;

                CharSequence placeDescription = Html.fromHtml(entity.getDescription() + "");
                txtTermCondition.setText(placeDescription);
                break;
        }
    }
}
