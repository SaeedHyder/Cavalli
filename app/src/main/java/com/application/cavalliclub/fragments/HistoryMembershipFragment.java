package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.EntityHistoryProfile;
import com.application.cavalliclub.entities.EntityMembershipHistory;
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
import butterknife.Unbinder;


public class HistoryMembershipFragment extends BaseFragment {
    @BindView(R.id.iv_card)
    ImageView ivCard;
    @BindView(R.id.CardLogo)
    ImageView CardLogo;
    @BindView(R.id.txt_expired)
    AnyTextView txtExpired;
    @BindView(R.id.memberShip)
    AnyTextView memberShip;
    @BindView(R.id.memberShipPrice)
    AnyTextView memberShipPrice;
    @BindView(R.id.txt_title)
    AnyTextView txtTitle;
    @BindView(R.id.txt_no_data)
    AnyTextView txt_no_data;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.ll_main)
    LinearLayout ll_main;
    @BindView(R.id.txt_description)
    AnyTextView txtDescription;
    Unbinder unbinder;

    EntityHistoryProfile entityHistoryProfile;
    List<EntityMembershipHistory> entityMembershipHistory;
    ImageLoader imageLoader;

    public static HistoryMembershipFragment newInstance() {
        Bundle args = new Bundle();

        HistoryMembershipFragment fragment = new HistoryMembershipFragment();
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
        View view = inflater.inflate(R.layout.fragment_history_membership, container, false);
        unbinder = ButterKnife.bind(this, view);
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
       // titleBar.setTitlebarBackgroundColor(R.drawable.tab_header,getDockActivity());
        titleBar.showTwoTabsLayout(false, getString(R.string.current_membership), getString(R.string.history));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
                if (entityHistoryProfile != null && entityHistoryProfile.getMembershipHistory() != null
                        && entityHistoryProfile.getMembershipHistory().size() != 0) {
                    entityMembershipHistory = entityHistoryProfile.getMembershipHistory();
                    ll_main.setVisibility(View.VISIBLE);
                    txt_no_data.setVisibility(View.GONE);
                    setCurrentMembershipHistoryData(entityMembershipHistory);
                } else {
                    ll_main.setVisibility(View.GONE);
                    txt_no_data.setVisibility(View.VISIBLE);
                    UIHelper.showShortToastInCenter(getDockActivity(), "Membership history not available.");
                }
                break;
        }
    }

    private void setCurrentMembershipHistoryData(List<EntityMembershipHistory> entityMembershipHistory) {

        if (entityMembershipHistory != null && entityMembershipHistory.get(0) != null) {

            if (entityMembershipHistory.get(0).getImageUrl() != null) {
                Picasso.with(getDockActivity()).load(entityMembershipHistory.get(0).getImageUrl()).into(ivCard);
               // imageLoader.displayImage(entityMembershipHistory.get(0).getImageUrl(), ivCard);
            }
            if (entityMembershipHistory.get(0).getTitle() != null)
                memberShip.setText(entityMembershipHistory.get(0).getTitle() + "");
            if (entityMembershipHistory.get(0).getPrice() != null)
                memberShipPrice.setText("AED " + entityMembershipHistory.get(0).getPrice() + "");
            if (entityMembershipHistory.get(0).getDescription() != null)
                txtDescription.setText(entityMembershipHistory.get(0).getDescription() + "");
        }
    }
}
