package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.EntityMemberships;
import com.application.cavalliclub.entities.PurchaseMembershipEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MembershipDetailFragment extends BaseFragment {
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.txt_heading)
    AnyTextView txtHeading;
    @BindView(R.id.btn_buyNow)
    Button btnBuyNow;
    private static String MembershipDetailKey;
    EntityMemberships entityMemberships;
    Unbinder unbinder;
    ImageLoader imageLoader;
    @BindView(R.id.tv_description)
    AnyTextView tvDescription;
    private static String IMAGE_BANNER;
    @BindView(R.id.tv_TandC)
    AnyTextView tvTandC;
    private boolean isServiceComplete = false;
    private PurchaseMembershipEntity purchaseMembershipEntity;

    public static MembershipDetailFragment newInstance(EntityMemberships entityMemberships, String image) {
        Bundle args = new Bundle();
        IMAGE_BANNER = image;
        args.putString(MembershipDetailKey, new Gson().toJson(entityMemberships));
        MembershipDetailFragment fragment = new MembershipDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            MembershipDetailKey = getArguments().getString(MembershipDetailKey);
        }
        if (MembershipDetailKey != null) {
            entityMemberships = new Gson().fromJson(MembershipDetailKey, EntityMemberships.class);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_membership_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isServiceComplete = false;
        imageLoader = ImageLoader.getInstance();
        getMainActivity().hideBottomTab();

        setImageDescriptionData();
    }

    private void purchaseMemberships() {
        if (entityMemberships != null  && entityMemberships.getId() != null)
            serviceHelper.enqueueCall(webService.purchaseMemberships(entityMemberships.getId()), WebServiceConstants.PURCHASE_MEMBERSHIPS);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {

            case WebServiceConstants.PURCHASE_MEMBERSHIPS:
                getDockActivity().popFragment();
                purchaseMembershipEntity = (PurchaseMembershipEntity) result;
                getDockActivity().replaceDockableFragment(WebViewMembershipFragment.newInstance(purchaseMembershipEntity), "WebViewMembershipFragment");
                break;
        }
    }

    private void setImageDescriptionData() {

        if (entityMemberships != null) {
            if (entityMemberships.getTitle() != null)
                txtHeading.setText(entityMemberships.getTitle() + "");
            if (entityMemberships.getDescription() != null)
                tvDescription.setText(entityMemberships.getDescription() + "");
            if (entityMemberships.getTerm_condition() != null)
                tvTandC.setText(entityMemberships.getTerm_condition() + "");
        }
        if (IMAGE_BANNER != null)
            imageLoader.displayImage(IMAGE_BANNER, ivImage);
        isServiceComplete = true;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.hideTwoTabsLayout();
        titleBar.showBackButton();
     //   titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.details));
    }


    @OnClick(R.id.btn_buyNow)
    public void onViewClicked() {
        if (isServiceComplete) {
            purchaseMemberships();
        } else {
            UIHelper.showShortToastInCenter(getDockActivity(), "Error performing task.");
        }
    }
}
