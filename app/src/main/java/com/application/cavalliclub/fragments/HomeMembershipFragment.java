package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.interfaces.MenuInterface;
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

public class HomeMembershipFragment extends BaseFragment implements MenuInterface {


    Unbinder unbinder;
    ArrayList<EntityMemberships> entityMemberships;
    ImageLoader imageLoader;
    String image;
    @BindView(R.id.iv_card)
    ImageView ivCard;
    @BindView(R.id.CardLogo)
    ImageView CardLogo;
    @BindView(R.id.memberShip)
    AnyTextView memberShip;
    @BindView(R.id.memberShipPrice)
    AnyTextView memberShipPrice;
    @BindView(R.id.memberShipDescription)
    AnyTextView memberShipDescription;
    @BindView(R.id.btn_buyNow)
    Button btnBuyNow;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    private PurchaseMembershipEntity purchaseMembershipEntity;
    private boolean isServiceComplete = false;
    private Integer position = 0;

    public static HomeMembershipFragment newInstance() {
        Bundle args = new Bundle();

        HomeMembershipFragment fragment = new HomeMembershipFragment();
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
        View view = inflater.inflate(R.layout.fragment_membership_gold, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isServiceComplete = false;
        imageLoader = ImageLoader.getInstance();
        memberShipPrice.setVisibility(View.GONE);

        getMainActivity().hideBottomTab();
        getMemberships();
        getDockActivity().getSupportFragmentManager().addOnBackStackChangedListener(getListener());
    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getDockActivity().getSupportFragmentManager();

                if (manager != null) {
                    Fragment currFrag = (Fragment) manager.findFragmentById(getDockActivity().getDockFrameLayoutId());
                    if (currFrag != null) {
                        if (currFrag instanceof HomeMembershipFragment) {
                            if (getTitleBar() != null) {
                                getTitleBar().showMembershipRecyclerDataVisible();
                            }
                        }

                    }
                }
            }
        };
        return result;
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
        titleBar.setRecyclerview_below();
        // titleBar.setTitlebarBackgroundColor(R.drawable.tab_header, getDockActivity());
        titleBar.setSubHeading(getString(R.string.membership_small));

    }

    private void getMemberships() {
        serviceHelper.enqueueCall(webService.getMemberships(), WebServiceConstants.GET_MEMBERSHIPS);
    }

    private void purchaseMemberships() {
        if (entityMemberships != null && entityMemberships.get(position) != null && entityMemberships.get(position).getId() != null)
            serviceHelper.enqueueCall(webService.purchaseMemberships(entityMemberships.get(position).getId()), WebServiceConstants.PURCHASE_MEMBERSHIPS);
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

                    if (getMainActivity().titleBar != null) {
                        getMainActivity().titleBar.setMembershipRecyclerData(getDockActivity(), entityMemberships, this);
                        position = 0;
                        setData(entityMemberships.get(0));
                    }
                }

                break;

            case WebServiceConstants.PURCHASE_MEMBERSHIPS:
                //UIHelper.showShortToastInCenter(getDockActivity(), "Membership purchased successfully");
                purchaseMembershipEntity = (PurchaseMembershipEntity) result;
                getDockActivity().replaceDockableFragment(WebViewMembershipFragment.newInstance(purchaseMembershipEntity), "WebViewMembershipFragment");
                break;
        }
    }

    private void setData(final EntityMemberships entityMemberships) {
        if (entityMemberships != null) {

            if (entityMemberships != null && entityMemberships.getImageUrl() != null)

                Picasso.with(getDockActivity()).load(entityMemberships.getImageUrl()).into(ivCard, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (entityMemberships != null && entityMemberships.getPrice() != null) {
                            memberShipPrice.setVisibility(View.VISIBLE);
                            memberShipPrice.setText("AED " + entityMemberships.getPrice() + "");
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
            // imageLoader.displayImage(entityMemberships.get(0).getImageUrl(), ivCard);


        }


        if (entityMemberships != null && entityMemberships != null && entityMemberships.getMemberShipImages() != null && entityMemberships.getMemberShipImages().get(0) != null && entityMemberships.getMemberShipImages().get(0).getImageUrl() != null)
            image = entityMemberships.getMemberShipImages().get(0).getImageUrl();
        isServiceComplete = true;
    }

    @OnClick({R.id.iv_card, R.id.btn_buyNow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_card:
                if (entityMemberships.get(position) != null)
                    getDockActivity().addDockableFragment(MembershipDetailFragment.newInstance(entityMemberships.get(position), image), "MembershipDetailFragment");

                break;
            case R.id.btn_buyNow:
                if (isServiceComplete) {
                    purchaseMemberships();
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), "Error performing task.");
                }
                break;
        }
    }


    @Override
    public void menuItemClick(Object data, int pos) {

        try {
            EntityMemberships entity = (EntityMemberships) data;
            memberShipPrice.setVisibility(View.GONE);
            position = pos;
            setData(entity);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
