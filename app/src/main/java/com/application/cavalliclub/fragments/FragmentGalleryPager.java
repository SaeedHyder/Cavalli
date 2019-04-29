package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.EntityGalleryList;
import com.application.cavalliclub.entities.MenuCategoryProductImageEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.ui.adapters.CustomPageAdapter;
import com.application.cavalliclub.ui.adapters.MyCustomPagerAdapter;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentGalleryPager extends BaseFragment {

    Unbinder unbinder;
    int images[] = {R.drawable.logo_splash_updated, R.drawable.logo_splash_updated, R.drawable.logo_splash_updated, R.drawable.logo_splash_updated};
    MyCustomPagerAdapter myCustomPagerAdapter;
    CustomPageAdapter customPageAdapter;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private static String GALLERY_KEY = "gallery_key";
    private static String MENU_KEY = "MENU_KEY";
    EntityGalleryList entityGalleryList;
    ArrayList<String> imagesGallery = new ArrayList<>();
    ArrayList<MenuCategoryProductImageEntity> imagesMenu = new ArrayList<>();
    private static int pos;
    private static boolean isStarter = false;
    @BindView(R.id.indicator)
    CircleIndicator indicator;

    public static FragmentGalleryPager newInstance() {
        return new FragmentGalleryPager();
    }

    public static FragmentGalleryPager newInstance(List<MenuCategoryProductImageEntity> images, int position) {
        Bundle args = new Bundle();
        args.putString(MENU_KEY, new Gson().toJson(images));
        pos = position;
        isStarter =false;
        FragmentGalleryPager fragment = new FragmentGalleryPager();
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentGalleryPager newInstance(List<MenuCategoryProductImageEntity> images, int position, boolean Starter) {
        Bundle args = new Bundle();
        args.putString(MENU_KEY, new Gson().toJson(images));
        pos = position;
        isStarter = Starter;
        FragmentGalleryPager fragment = new FragmentGalleryPager();
        fragment.setArguments(args);
        return fragment;
    }


    public static FragmentGalleryPager newInstance(ArrayList<String> images, int position) {
        Bundle args = new Bundle();
        args.putString(GALLERY_KEY, new Gson().toJson(images));
        pos = position;
        isStarter =false;
        FragmentGalleryPager fragment = new FragmentGalleryPager();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String galleryJson = getArguments().getString(GALLERY_KEY);
            String menuJson = getArguments().getString(MENU_KEY);

            if (galleryJson != null) {
                imagesGallery = new Gson().fromJson(galleryJson, new TypeToken<List<String>>() {
                }.getType());
            }
            if (menuJson != null) {
                imagesMenu = new Gson().fromJson(menuJson, new TypeToken<List<MenuCategoryProductImageEntity>>() {
                }.getType());
            }
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery_pager, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading("Photos");
        titleBar.hideTwoTabsLayout();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivity().hideBottomTab();
        if (imagesGallery != null && imagesGallery.size() > 0) {
            myCustomPagerAdapter = new MyCustomPagerAdapter(getApplicationContext(), imagesGallery, null);
            viewPager.setAdapter(myCustomPagerAdapter);
        } else {
            customPageAdapter = new CustomPageAdapter(getApplicationContext(), imagesMenu);
            viewPager.setAdapter(customPageAdapter);
        }

        if (!isStarter) {
            viewPager.setCurrentItem(pos);
        }
        indicator.setViewPager(viewPager);
    }
}
