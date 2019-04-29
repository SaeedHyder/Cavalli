package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.NotificationEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.helpers.DateHelper;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class NotificationDetailFragment extends BaseFragment {

    @BindView(R.id.ivNotification)
    ImageView ivNotification;
    @BindView(R.id.tv_msg)
    AnyTextView tvMsg;
    @BindView(R.id.tv_date)
    AnyTextView tvDate;
    @BindView(R.id.tv_time)
    AnyTextView tvTime;
    Unbinder unbinder;

    private static String COMPLETE_NOTIFICATIONS = "COMPLETE_NOTIFICATIONS";
    NotificationEntity entity;

    public static NotificationDetailFragment newInstance(NotificationEntity entity) {
        Bundle args = new Bundle();
        args.putString(COMPLETE_NOTIFICATIONS, new Gson().toJson(entity));
        NotificationDetailFragment fragment = new NotificationDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            COMPLETE_NOTIFICATIONS = getArguments().getString(COMPLETE_NOTIFICATIONS);
        }
        if (COMPLETE_NOTIFICATIONS != null) {
            entity = new Gson().fromJson(COMPLETE_NOTIFICATIONS, NotificationEntity.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivity().hideBottomTab();
        setData();
    }

    private void setData() {

        tvMsg.setText(entity.getMessage() + "");
        tvDate.setText(DateHelper.getLocalDateNotification(entity.getCreatedAt()));
        tvTime.setText(DateHelper.getLocalTime(entity.getCreatedAt()));
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
        //titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.details));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
