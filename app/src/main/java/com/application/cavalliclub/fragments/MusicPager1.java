package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MusicPager1 extends BaseFragment {

    @BindView(R.id.iv_item_pager_1)
    ImageView ivItemPager1;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    ImageLoader imageLoader;
    Unbinder unbinder;

    public static MusicPager1 newInstance() {
        return new MusicPager1();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.music_pager_1, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        if (prefHelper != null && prefHelper.getLiveMusic() != null && prefHelper.getLiveMusic().getImageUrl() != null) {
            imageLoader.displayImage(prefHelper.getLiveMusic().getImageUrl(), ivItemPager1);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(MusicPlayerFragment.newInstance(), "MusicPlayerFragment");
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
