package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.entities.EntityGuestListProfile;
import com.application.cavalliclub.entities.EntityUpcomingHistoryGuestListProfile;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.interfaces.RecyclerViewItemListener;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.BinderGuestHistoryProfile;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class InviteGuestHistoryProfileFragment extends BaseFragment implements RecyclerViewItemListener {

    @BindView(R.id.lv_pending_order)
    ListView lvPendingOrder;
    Unbinder unbinder;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;

    private ArrayListAdapter<EntityUpcomingHistoryGuestListProfile> adapter;
    EntityGuestListProfile entityGuestListProfile;
    private ArrayList<EntityUpcomingHistoryGuestListProfile> entityUpcomingHistoryGuestListProfiles = new ArrayList<>();
    private ArrayList<EntityUpcomingHistoryGuestListProfile> myGuestUpcomingCollection = new ArrayList<>();

    public static InviteGuestHistoryProfileFragment newInstance() {
        return new InviteGuestHistoryProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderGuestHistoryProfile(getDockActivity(), getDockActivity(), prefHelper));
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
        getMainActivity().filterMenuFragment.setInterfaceListner(this);
        getMainActivity().showBottomTab(AppConstants.home);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
      /*  titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(MyProfileFragment.newInstance(), "MyProfileFragment");
            }
        },getDockActivity());*/
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().onBackPressed();
            }
        }, getDockActivity());
        titleBar.setSubHeading("My Guests");
        titleBar.showfilterButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // filterInterface.viewGoneFilter();

                getMainActivity().refreshFilter();
                getMainActivity().filterMenuFragment.setIsFromInviteGuest(true);
                getMainActivity().setCheckboxDataFilter();
                getMainActivity().drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        //titleBar.setTitlebarBackgroundColor(R.drawable.tab_header,getDockActivity());
        titleBar.showTwoTabsLayout(false, "Upcoming", "History");
        titleBar.setLayout_below();
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
        serviceHelper.enqueueCall(webService.guestListProfile(), WebServiceConstants.GUEST_LIST_PROFILE_HISTORY);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.GUEST_LIST_PROFILE_HISTORY:

                entityGuestListProfile = (EntityGuestListProfile) result;
                entityUpcomingHistoryGuestListProfiles = entityGuestListProfile.getHistory();
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

    @Override
    public void onEditItemClicked(Object Ent, int position, DockActivity dockActivity) {
    }

    @Override
    public void onEditItemClicked(Object Ent, int position, String id) {

    }

    @Override
    public void onDeleteItemClicked(Object Ent, int position) {

    }

    @Override
    public void onClickItemFilter() {
        adapter.notifyDataSetChanged();
    }
}
