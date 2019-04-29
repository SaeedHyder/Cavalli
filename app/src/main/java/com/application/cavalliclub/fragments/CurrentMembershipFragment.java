package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.EntityHistoryProfile;
import com.application.cavalliclub.entities.EntityMemberships;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrentMembershipFragment extends BaseFragment {
    @BindView(R.id.iv_card)
    ImageView ivCard;
    @BindView(R.id.CardLogo)
    ImageView CardLogo;
    @BindView(R.id.memberShip)
    AnyTextView memberShip;
    @BindView(R.id.memberShipPrice)
    AnyTextView memberShipPrice;
    @BindView(R.id.txt_title)
    AnyTextView txtTitle;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.txt_description)
    AnyTextView txtDescription;
    @BindView(R.id.ll_main)
    LinearLayout ll_main;
    @BindView(R.id.txt_no_data)
    AnyTextView txt_no_data;
    EntityHistoryProfile entityHistoryProfile;
    List<EntityMemberships> entityMembershipsCurrent;
    ImageLoader imageLoader;


    public static CurrentMembershipFragment newInstance() {
        Bundle args = new Bundle();

        CurrentMembershipFragment fragment = new CurrentMembershipFragment();
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
        View view = inflater.inflate(R.layout.fragment_current_membership, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        getMainActivity().showBottomTab(AppConstants.home);

        membershipHistory();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
     //   titleBar.setTitlebarBackgroundColor(R.drawable.tab_header, getDockActivity());
        titleBar.showTwoTabsLayout(true, getString(R.string.current_membership), getString(R.string.history));
        titleBar.setLayout_below();
        titleBar.setSubHeading(getString(R.string.membership_small));
        titleBar.tabsClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().popFragment();
                getDockActivity().replaceDockableFragment(CurrentMembershipFragment.newInstance(), "CurrentMembershipFragment");
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().popFragment();
                getDockActivity().replaceDockableFragment(HistoryMembershipFragment.newInstance(), "HistoryMembershipFragment");
            }
        });
    }

    private void membershipHistory() {
        serviceHelper.enqueueCall(webService.membershipHistory(), WebServiceConstants.MEMBERSHIP_HISTORY);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.MEMBERSHIP_HISTORY:
                entityHistoryProfile = (EntityHistoryProfile) result;
                if (entityHistoryProfile != null && entityHistoryProfile.getCurrentMembership() != null
                        && entityHistoryProfile.getCurrentMembership().size() != 0) {
                    entityMembershipsCurrent = entityHistoryProfile.getCurrentMembership();
                    ll_main.setVisibility(View.VISIBLE);
                    txt_no_data.setVisibility(View.GONE);
                    setCurrentMembershipData(entityMembershipsCurrent);
                } else {
                    ll_main.setVisibility(View.GONE);
                    txt_no_data.setVisibility(View.VISIBLE);
                    UIHelper.showShortToastInCenter(getDockActivity(), "Current membership not available.");
                }
                break;
        }
    }

    private void setCurrentMembershipData(final List<EntityMemberships> entityMembershipsCurrent) {

        if (entityMembershipsCurrent != null && entityMembershipsCurrent.get(0) != null) {
            if (entityMembershipsCurrent.get(0).getImageUrl() != null) {
              //  imageLoader.displayImage(entityMembershipsCurrent.get(0).getImageUrl(), ivCard);
                Picasso.with(getDockActivity()).load(entityMembershipsCurrent.get(0).getImageUrl()).into(ivCard);
            }
            if (entityMembershipsCurrent.get(0).getTitle() != null)
                memberShip.setText(entityMembershipsCurrent.get(0).getTitle() + "");
            if (entityMembershipsCurrent.get(0).getPrice() != null)
                memberShipPrice.setText("AED " + entityMembershipsCurrent.get(0).getPrice() + "");
            if (entityMembershipsCurrent.get(0).getDescription() != null)
                txtDescription.setText(entityMembershipsCurrent.get(0).getDescription() + "");


        }
    }
}
