package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.CmsEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PrivacyPolicyFragment extends BaseFragment {
    @BindView(R.id.txt_privacy_policy)
    AnyTextView txtPrivacyPolicy;
    Unbinder unbinder;

    public static PrivacyPolicyFragment newInstance() {
        Bundle args = new Bundle();

        PrivacyPolicyFragment fragment = new PrivacyPolicyFragment();
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
        View view = inflater.inflate(R.layout.fragment_privacypolicy, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().hideBottomTab();

        getPrivacyPolicy();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.hideTwoTabsLayout();
        titleBar.setSubHeading(getString(R.string.privacy_policy));
    }

    private void getPrivacyPolicy() {
        serviceHelper.enqueueCall(webService.Cms(AppConstants.PRIVACY_POLICY), WebServiceConstants.PRIVACY_POLICY);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.PRIVACY_POLICY:
                CmsEntity entity = (CmsEntity) result;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    txtPrivacyPolicy.setText(Html.fromHtml(entity.getDescription(),Html.FROM_HTML_MODE_LEGACY));
                } else {
                    txtPrivacyPolicy.setText(Html.fromHtml(entity.getDescription()));
                }
                break;
        }
    }
}
