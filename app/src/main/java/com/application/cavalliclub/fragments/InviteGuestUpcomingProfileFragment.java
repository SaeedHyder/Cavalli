package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.EntityGuestListProfile;
import com.application.cavalliclub.entities.EntityUpcomingHistoryGuestListProfile;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.BinderGuestUpcomingProfile;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InviteGuestUpcomingProfileFragment extends BaseFragment {


    @BindView(R.id.lv_pending_order)
    ListView lvPendingOrder;
    Unbinder unbinder;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;

    private ArrayListAdapter<EntityUpcomingHistoryGuestListProfile> adapter;
    EntityGuestListProfile entityGuestListProfile;
    private ArrayList<EntityUpcomingHistoryGuestListProfile> entityUpcomingHistoryGuestListProfiles = new ArrayList<>();
    private ArrayList<EntityUpcomingHistoryGuestListProfile> myGuestUpcomingCollection = new ArrayList<>();

    public static InviteGuestUpcomingProfileFragment newInstance() {
        return new InviteGuestUpcomingProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderGuestUpcomingProfile(getDockActivity(), getDockActivity(), prefHelper));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getInviteGuestUpcomingData();
        getMainActivity().showBottomTab(AppConstants.home);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(MyProfileFragment.newInstance(), "MyProfileFragment");
            }
        },getDockActivity());
        titleBar.setSubHeading("My Guests");
      //  titleBar.setTitlebarBackgroundColor(R.drawable.tab_header,getDockActivity());
        titleBar.setLayout_below();
        titleBar.showTwoTabsLayout(true, "Upcoming", "History");
        titleBar.tabsClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().popFragment();
                getDockActivity().replaceDockableFragment(InviteGuestUpcomingProfileFragment.newInstance(), "PendingOrderFragment");
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().popFragment();
                getDockActivity().replaceDockableFragment(InviteGuestHistoryProfileFragment.newInstance(), "OrderHistoryBarFragment");
            }
        });
    }

    private void getInviteGuestUpcomingData() {
        serviceHelper.enqueueCall(webService.guestListProfile(), WebServiceConstants.GUEST_LIST_PROFILE_UPCOMING);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.GUEST_LIST_PROFILE_UPCOMING:

                entityGuestListProfile = (EntityGuestListProfile) result;
                entityUpcomingHistoryGuestListProfiles = entityGuestListProfile.getUpcoming();
                setGuestUpcomingProfileData(entityUpcomingHistoryGuestListProfiles);
                break;
        }
    }

    private void setGuestUpcomingProfileData(ArrayList<EntityUpcomingHistoryGuestListProfile> result) {
        myGuestUpcomingCollection = new ArrayList<>();
        myGuestUpcomingCollection.addAll(result);
        bindData(myGuestUpcomingCollection);

        if (myGuestUpcomingCollection.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvPendingOrder.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvPendingOrder.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(final ArrayList<EntityUpcomingHistoryGuestListProfile> myUpcomingGuestCollection) {

        adapter.clearList();
        lvPendingOrder.setAdapter(adapter);
        adapter.addAll(myUpcomingGuestCollection);
        adapter.notifyDataSetChanged();
    }
}
