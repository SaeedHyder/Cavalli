package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.LatestUpdatesEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.helpers.DateHelper;
import com.application.cavalliclub.helpers.SelectShareIntent;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LatestUpdateDetailFragment extends BaseFragment {

    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.titleBackBtn)
    ImageView titleBackBtn;
    @BindView(R.id.txt_heading)
    AnyTextView txtHeading;
    @BindView(R.id.txt_description)
    AnyTextView txtDescription;
    @BindView(R.id.txt_Date)
    AnyTextView txtDate;
    @BindView(R.id.iv_fb)
    ImageView ivFb;
    @BindView(R.id.iv_twitter)
    ImageView ivTwitter;
    @BindView(R.id.iv_google)
    ImageView ivGoogle;
    Unbinder unbinder;

    private static String UPDATED_NEWS = "UPDATED_NEWS";
    LatestUpdatesEntity entity;
    private ImageLoader imageLoader;
    String splittedDate;

    public static LatestUpdateDetailFragment newInstance(LatestUpdatesEntity entity) {
        Bundle args = new Bundle();
        args.putString(UPDATED_NEWS, new Gson().toJson(entity));
        LatestUpdateDetailFragment fragment = new LatestUpdateDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        if (getArguments() != null) {
            UPDATED_NEWS = getArguments().getString(UPDATED_NEWS);
        }
        if (UPDATED_NEWS != null) {
            entity = new Gson().fromJson(UPDATED_NEWS, LatestUpdatesEntity.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_update_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivity().hideBottomTab();
        UIHelper.hideSoftKeyboard(getDockActivity(), getView());
        setData();
    }

    private void setData() {

        if (entity != null) {

            String str = entity.getCreatedAt();
            String[] splited = str.split("\\s");
            splittedDate = splited[0];

            if (entity.getImageUrl() != null)
                imageLoader.displayImage(entity.getNewsImages().get(0).getImageUrl(), ivHeader);
            if (entity.getTitle() != null)
                txtHeading.setText(entity.getTitle() + "");
            if (entity.getDescription() != null)
                txtDescription.setText(entity.getDescription() + "");
            if (entity.getCreatedAt() != null)
                txtDate.setText(DateHelper.getFormatedDate(AppConstants.DateFormat_YMD, AppConstants.DateFormat_MDY, splittedDate));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().popFragment();
            }
        },getDockActivity());
       // titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.details));
    }
    
    @OnClick({R.id.iv_fb, R.id.iv_twitter, R.id.iv_google})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fb:
                SelectShareIntent.facebookShare(getDockActivity(), entity.getImageUrl(),
                        entity.getTitle(), entity.getDescription());
                break;
            case R.id.iv_twitter:
                SelectShareIntent.twitterShare(getDockActivity(), entity.getImageUrl(),
                        entity.getTitle(), entity.getDescription());
                break;
            case R.id.iv_google:
//                Intent shareIntent = new PlusShare.Builder(getDockActivity())
//                        .setType("text/plain")
//                        .setText(entity.getTitle() + "\n" + entity.getDescription() + "\n" + entity.getImageUrl())
//                        .setContentUrl(Uri.parse("https://developers.google.com/+/"))
//                        .getIntent();
//
//                startActivityForResult(shareIntent, 0);
                break;
            case R.id.titleBackBtn:
                getDockActivity().popFragment();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        UIHelper.hideSoftKeyboard(getDockActivity(), getView());
    }
}
