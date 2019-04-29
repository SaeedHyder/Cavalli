package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.EntityCavalliNights;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.DateHelper;
import com.application.cavalliclub.interfaces.ViewPagerClickListner;
import com.application.cavalliclub.ui.adapters.CavalliNightsViewPagerAdapter;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CavalliNightsFragment extends BaseFragment implements ViewPagerClickListner {

    CavalliNightsViewPagerAdapter adapter;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tv_track_title)
    AnyTextView tvTrackTitle;
    @BindView(R.id.tv_performing_now)
    AnyTextView tvPerformingNow;
    @BindView(R.id.view_line)
    View view_line;
    Unbinder unbinder;
    private static String COMING_FROM = "";
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.ll_main)
    LinearLayout llMain;

    private ArrayList<EntityCavalliNights> cavalliNightsList = new ArrayList<>();
    private ArrayList<EntityCavalliNights> userCollection;
    private int pos = -1;

    public static CavalliNightsFragment newInstance() {
        return new CavalliNightsFragment();
    }

    public static CavalliNightsFragment newInstance(String ComingFrom) {
        COMING_FROM = ComingFrom;
        return new CavalliNightsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new CavalliNightsViewPagerAdapter(getFragmentManager(), getDockActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cavalli_nights, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setPagerSetting();
        getCavalliEvents();
        view_line.setVisibility(View.GONE);
    }

    private void setPagerSetting() {
        pager.setClipToPadding(false);
        pager.setPageMargin(10);
       /* pager.setPadding(20, 8, 20, 8);
        pager.setOffscreenPageLimit(3);*/
        pager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                int pageWidth = pager.getMeasuredWidth() -
                        pager.getPaddingLeft() - pager.getPaddingRight();
                int pageHeight = pager.getHeight();
                int paddingLeft = pager.getPaddingLeft();
                float transformPos = (float) (page.getLeft() -
                        (pager.getScrollX() + paddingLeft)) / pageWidth;
                int max = pageHeight / 10;

                if (transformPos < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.setAlpha(0.7f);// to make left transparent
                    page.setScaleY(0.9f);
                } else if (transformPos <= 1) { // [-1,1]
                    page.setAlpha(1f);
                    page.setScaleY(1f);
                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.setAlpha(0.7f);// to make right transparent
                    page.setScaleY(0.9f);
                }
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
            }
        },getDockActivity());
     //   titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.hideTwoTabsLayout();
        titleBar.setSubHeading(getString(R.string.cavalli_night_small));
    }

    private void getCavalliEvents() {
        serviceHelper.enqueueCall(webService.getCavalliEvents(AppConstants.CAVALLI_EVENTS), WebServiceConstants.CAVALLI_EVENTS);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.CAVALLI_EVENTS:
                cavalliNightsList = (ArrayList<EntityCavalliNights>) result;
                if (cavalliNightsList.size() > 0) {
                    setCavalliNightsData(cavalliNightsList);
                    txtNoData.setVisibility(View.GONE);
                    llMain.setVisibility(View.VISIBLE);
                } else {
                    txtNoData.setVisibility(View.VISIBLE);
                    llMain.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void setCavalliNightsData(ArrayList<EntityCavalliNights> result) {

        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);

        if (userCollection != null && userCollection.get(0) != null && userCollection.get(0).getTitle() != null && userCollection.get(0).getEventDate() != null) {
            tvTrackTitle.setText(userCollection.get(0).getTitle() + "");
            tvPerformingNow.setText(DateHelper.getPagerDate(userCollection.get(0).getEventDate()));
            //tvPerformingNow.setText(DateHelper.getFormatedDate(AppConstants.DateFormat_YMDHMS, AppConstants.DateFormat_EMD, userCollection.get(0).getEventDate()));
        }
    }

    private void bindData(final ArrayList<EntityCavalliNights> userCollection) {

        List<Fragment> mylist = new ArrayList<>();
        CavalliPager fragment;
        for (int i = 0; i < userCollection.size(); i++) {
            fragment = new CavalliPager();
            fragment.setCheck(true);
            fragment.setInterFaceListner(this);
            fragment.setPagerData(userCollection.get(i));
            mylist.add(fragment);
        }

        adapter = new CavalliNightsViewPagerAdapter(getChildFragmentManager(), userCollection, mylist);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(userCollection.size());
        pager.setCurrentItem(0, true);
        pos = 0;

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
                tvTrackTitle.setText(userCollection.get(position).getTitle() + "");
                tvPerformingNow.setText(DateHelper.getPagerDate(userCollection.get(position).getEventDate()));
                //tvPerformingNow.setText(DateHelper.getFormatedDate(AppConstants.DateFormat_YMDHMS, AppConstants.DateFormat_EMD, userCollection.get(position).getEventDate()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        getMainActivity().showBottomTab(AppConstants.home);
    }

    @Override
    public void onClick() {

        if (pos != -1)
            getDockActivity().replaceDockableFragment(CavalliDetailFragment.newInstance(userCollection.get(pos), COMING_FROM), "CavalliDetailFragment");
    }
}