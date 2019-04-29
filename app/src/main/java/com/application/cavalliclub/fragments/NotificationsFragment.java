package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.NotificationEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.interfaces.NotificationDelete;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.BinderNotification;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NotificationsFragment extends BaseFragment implements NotificationDelete{

    @BindView(R.id.lv_notification)
    ListView lvNotification;
    Unbinder unbinder;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    private List<NotificationEntity> notificationCollection;
    private ArrayListAdapter<NotificationEntity> adapter;

    private ArrayList<NotificationEntity> getNotifications;
    private ArrayList<NotificationEntity> userCollection;

    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderNotification(getDockActivity(), prefHelper, this));
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.hideTwoTabsLayout();
     //   titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.notification));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().hideBottomTab();
        getNotifications();
        listViewOnItemClickListner();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void getNotifications() {
        serviceHelper.enqueueCall(webService.getNotifications(), WebServiceConstants.GET_NOTIFICATIONS);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.GET_NOTIFICATIONS:
                getNotifications = (ArrayList<NotificationEntity>) result;
                getMyReservationsListData(getNotifications);
                break;
            case WebServiceConstants.DELETE_NOTIFICATIONS:
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.notification_deleted));
                getNotifications();
                break;
        }
    }

    private void getMyReservationsListData(ArrayList<NotificationEntity> result) {

        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);

        if (userCollection.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvNotification.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvNotification.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(ArrayList<NotificationEntity> notificationCollection) {

        adapter.clearList();
        lvNotification.setAdapter(adapter);
        adapter.addAll(notificationCollection);
        adapter.notifyDataSetChanged();
    }

    private void listViewOnItemClickListner(){

        lvNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getDockActivity().replaceDockableFragment(NotificationDetailFragment.newInstance(userCollection.get(i)), "NotificationDetailFragment");
            }
        });
    }

    @Override
    public void OnClickService(int id) {
        serviceHelper.enqueueCall(webService.deleteNotification(id), WebServiceConstants.DELETE_NOTIFICATIONS);
    }
}
