package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.EntityMemberships;
import com.application.cavalliclub.entities.PurchaseMembershipEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MembershipPlatinumFragment extends BaseFragment {
    @BindView(R.id.iv_card)
    ImageView ivCard;
    @BindView(R.id.CardLogo)
    ImageView CardLogo;
    @BindView(R.id.memberShip)
    AnyTextView memberShip;
    @BindView(R.id.memberShipPrice)
    AnyTextView memberShipPrice;
    @BindView(R.id.btn_buyNow)
    Button btnBuyNow;
    Unbinder unbinder;
    ArrayList<EntityMemberships> entityMemberships;
    ImageLoader imageLoader;
    String image;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    private boolean isServiceComplete = false;
    private PurchaseMembershipEntity purchaseMembershipEntity;

    public static MembershipPlatinumFragment newInstance() {
        Bundle args = new Bundle();

        MembershipPlatinumFragment fragment = new MembershipPlatinumFragment();
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
        View view = inflater.inflate(R.layout.fragment_membership_platinum, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        isServiceComplete = false;
        getMainActivity().hideBottomTab();
        getMemberships();
    }

    @Override
    public void setTitleBar(final TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
            }
        }, getDockActivity());
     //   titleBar.setTitlebarBackgroundColor(R.drawable.tab_header, getDockActivity());
        titleBar.setLayout_below();
        titleBar.showThreeTabsLayout(AppConstants.tab3, getString(R.string.silver), getString(R.string.gold), getString(R.string.platinum));
        titleBar.setSubHeading(getString(R.string.membership_small));
        titleBar.threetabsClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().popFragment();
                titleBar.showThreeTabsLayout(AppConstants.tab1, getString(R.string.silver), getString(R.string.gold), getString(R.string.platinum));
                getDockActivity().replaceDockableFragment(MembershipSilverFragment.newInstance(), "MembershipSilverFragment");

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().popFragment();
                titleBar.showThreeTabsLayout(AppConstants.tab2, getString(R.string.silver), getString(R.string.gold), getString(R.string.platinum));
                getDockActivity().replaceDockableFragment(MembershipGoldFragment.newInstance(), "MembershipGoldFragment");
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().popFragment();
                titleBar.showThreeTabsLayout(AppConstants.tab3, getString(R.string.silver), getString(R.string.gold), getString(R.string.platinum));
                getDockActivity().replaceDockableFragment(MembershipPlatinumFragment.newInstance(), "MembershipPlatinumFragment");

            }
        });
    }

    private void getMemberships() {
        serviceHelper.enqueueCall(webService.getMemberships(), WebServiceConstants.GET_MEMBERSHIPS);
    }

    private void purchaseMemberships() {
        if (entityMemberships != null && entityMemberships.get(2) != null && entityMemberships.get(2).getId() != null)
            serviceHelper.enqueueCall(webService.purchaseMemberships(entityMemberships.get(2).getId()), WebServiceConstants.PURCHASE_MEMBERSHIPS);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.GET_MEMBERSHIPS:
                entityMemberships = (ArrayList<EntityMemberships>) result;


                if (entityMemberships.size() <= 0) {
                    txtNoData.setVisibility(View.VISIBLE);
                    llMain.setVisibility(View.GONE);
                } else {
                    txtNoData.setVisibility(View.GONE);
                    llMain.setVisibility(View.VISIBLE);
                    setData(entityMemberships);
                }

                break;
            case WebServiceConstants.PURCHASE_MEMBERSHIPS:
                purchaseMembershipEntity = (PurchaseMembershipEntity) result;
                getDockActivity().replaceDockableFragment(WebViewMembershipFragment.newInstance(purchaseMembershipEntity), "WebViewMembershipFragment");
                break;
        }
    }

    private void setData(final ArrayList<EntityMemberships> entityMemberships) {
        if (entityMemberships != null) {
            if (entityMemberships.get(2) != null) {
                if (entityMemberships.get(2) != null && entityMemberships.get(2).getImageUrl() != null)
                   /* imageLoader.displayImage(entityMemberships.get(2).getImageUrl(), ivCard, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                            if (entityMemberships.get(2) != null && entityMemberships.get(2).getPrice() != null)
                                memberShipPrice.setText("AED " + entityMemberships.get(2).getPrice() + "");
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });*/
                    Picasso.with(getDockActivity()).load(entityMemberships.get(2).getImageUrl()).into(ivCard, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (entityMemberships.get(2) != null && entityMemberships.get(2).getPrice() != null)
                                memberShipPrice.setText("AED " + entityMemberships.get(2).getPrice() + "");
                        }

                        @Override
                        public void onError() {

                        }
                    });
                   // imageLoader.displayImage(entityMemberships.get(2).getImageUrl(), ivCard);


            }
        }
        if (entityMemberships != null &&
                entityMemberships.get(2) != null && entityMemberships.get(2).getMemberShipImages() != null && entityMemberships.get(2).getMemberShipImages().get(0) != null
                && entityMemberships.get(2).getMemberShipImages().get(0).getImageUrl() != null)
            image = entityMemberships.get(2).getMemberShipImages().get(0).getImageUrl();

        isServiceComplete = true;
    }

    @OnClick({R.id.iv_card, R.id.btn_buyNow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_card:
                if (entityMemberships.get(2) != null)
                    getDockActivity().replaceDockableFragment(MembershipDetailFragment.newInstance(entityMemberships.get(2), image), "MembershipDetailFragment");

                break;
            case R.id.btn_buyNow:
                if (isServiceComplete) {
                    purchaseMemberships();
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), "Please wait while data is loading.");
                }
                break;
        }
    }
}
