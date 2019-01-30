package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.ui.adapters.CavalliNightsViewPagerAdapter;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PackageDetailFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tv_package_title)
    AnyTextView tvPackageTitle;
    @BindView(R.id.tv_package_info)
    AnyTextView tvPackageInfo;
    CavalliNightsViewPagerAdapter adapter;
    public static PackageDetailFragment newInstance() {
        return new PackageDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new CavalliNightsViewPagerAdapter(getFragmentManager(), getDockActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_package_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

/*        setPagerSetting();
        adapter = new CavalliNightsViewPagerAdapter(getChildFragmentManager(), userCollection);
        pager.setAdapter(adapter);
        pager.setCurrentItem(1, true);
        getMainActivity().showBottomTab(AppConstants.home);*/

        tvPackageTitle.setText("DELUX PACKAGE");
        tvPackageInfo.setText(getString(R.string.lorem_ipsum_small));
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
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.packages_detail));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
