package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.EntityCavalliNights;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.interfaces.ViewPagerClickListner;
import com.ingic.cavalliclub.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CavalliPager extends BaseFragment {

    @BindView(R.id.iv_latest_updates)
    ImageView ivLatestUpdates;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    Unbinder unbinder;
    private boolean check;
    public ViewPagerClickListner clickListner;
    ImageLoader imageLoader;
    EntityCavalliNights userCollection;

    public static CavalliPager newInstance() {
        return new CavalliPager();
    }

    public void setInterFaceListner(ViewPagerClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public void setPagerData(EntityCavalliNights userCollection) {
        this.userCollection = userCollection;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cavalli_pager, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        if (userCollection != null && userCollection.getImageUrl() != null) {
            imageLoader.displayImage(userCollection.getImageUrl(), ivLatestUpdates, getMainActivity().getImageLoaderRoundCornerTransformation(10));
        } /*else {
            imageLoader.displayImage("drawable://" + R.drawable.placeholder_thumb, ivLatestUpdates, getMainActivity().getImageLoaderRoundCornerTransformation(10));
        }*/

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListner.onClick();
                //getDockActivity().replaceDockableFragment(CavalliDetailFragment.newInstance(), "CavalliDetailFragment");
            }
        });

    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}