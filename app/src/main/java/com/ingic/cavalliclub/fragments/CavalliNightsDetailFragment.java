package com.ingic.cavalliclub.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.plus.PlusShare;
import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.EntityCavalliNights;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.helpers.DateHelper;
import com.ingic.cavalliclub.helpers.SelectShareIntent;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CavalliNightsDetailFragment extends BaseFragment {


    @BindView(R.id.iv_header)
    ImageView ivHeader;
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
    @BindView(R.id.titleBackBtn)
    ImageView titleBackBtn;

    SelectShareIntent selectShareIntent;
    private static String cavalliNightDetailKey = "cavalliNightDetailKey";
    EntityCavalliNights entityCavalliNights;
    @BindView(R.id.tv_date_day)
    AnyTextView tvDateDay;
    private ImageLoader imageLoader;
    String splittedDate;

    public static CavalliNightsDetailFragment newInstance() {
        return new CavalliNightsDetailFragment();
    }

    public static CavalliNightsDetailFragment newInstance(EntityCavalliNights entityCavalliNights) {
        Bundle args = new Bundle();
        args.putString(cavalliNightDetailKey, new Gson().toJson(entityCavalliNights));
        CavalliNightsDetailFragment fragment = new CavalliNightsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            cavalliNightDetailKey = getArguments().getString(cavalliNightDetailKey);
        }
        if (cavalliNightDetailKey != null) {
            entityCavalliNights = new Gson().fromJson(cavalliNightDetailKey, EntityCavalliNights.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cavalli_nights_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivity().hideBottomTab();
        UIHelper.hideSoftKeyboard(getDockActivity(), getView());
        imageLoader = ImageLoader.getInstance();
        setCavalliNightDetailData();
    }

    private void setCavalliNightDetailData() {

        if (entityCavalliNights != null) {

            String str = entityCavalliNights.getEventDate();
            String[] splited = str.split("\\s");
            splittedDate = splited[0];

            if (entityCavalliNights.getEventImages().get(0).getImageUrl() != null)
                imageLoader.displayImage(entityCavalliNights.getEventImages().get(0).getImageUrl(), ivHeader);
            if (entityCavalliNights.getTitle() != null)
                txtHeading.setText(entityCavalliNights.getTitle() + "");
            if (entityCavalliNights.getDescription() != null)
                txtDescription.setText(entityCavalliNights.getDescription() + "");

            if (entityCavalliNights.getEvent_schedule().equalsIgnoreCase(AppConstants.ONE_TIME)) {
                if (entityCavalliNights.getEventDate() != null) {
                    txtDate.setText(DateHelper.getFormatedDate(AppConstants.DateFormat_YMD, AppConstants.DateFormat_MDY, splittedDate));
                }
            } else if (entityCavalliNights.getEvent_schedule().equalsIgnoreCase(AppConstants.RECURRING)) {
                tvDateDay.setText("Day:");
                String name = entityCavalliNights.getEventDay();
                name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                txtDate.setText(name);
            }
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.iv_fb, R.id.iv_twitter, R.id.iv_google, R.id.titleBackBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fb:
                SelectShareIntent.facebookShare(getDockActivity(), entityCavalliNights.getEventImages().get(0).getImageUrl(),
                        entityCavalliNights.getTitle(), entityCavalliNights.getDescription());
                break;
            case R.id.iv_twitter:
                SelectShareIntent.twitterShare(getDockActivity(), entityCavalliNights.getEventImages().get(0).getImageUrl(),
                        entityCavalliNights.getTitle(), entityCavalliNights.getDescription());
                break;
            case R.id.iv_google:

                // Launch the Google+ share dialog with attribution to your app.
                Intent shareIntent = new PlusShare.Builder(getDockActivity())
                        .setType("text/plain")
                        .setText(entityCavalliNights.getTitle() + "\n" + entityCavalliNights.getDescription() + "\n" + entityCavalliNights.getEventImages().get(0).getImageUrl())
                        .setContentUrl(Uri.parse("https://developers.google.com/+/"))
                        .getIntent();

                startActivityForResult(shareIntent, 0);
                //share_image_text_GPLUS();
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
