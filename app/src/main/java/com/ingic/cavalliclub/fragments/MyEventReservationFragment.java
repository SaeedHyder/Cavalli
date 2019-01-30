package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.activities.DockActivity;
import com.ingic.cavalliclub.entities.EntityCavalliNights;
import com.ingic.cavalliclub.entities.EntityMyReservations;
import com.ingic.cavalliclub.entities.GetReservationsEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.interfaces.RecyclerViewItemListener;
import com.ingic.cavalliclub.ui.adapters.ArrayListAdapter;
import com.ingic.cavalliclub.ui.binders.BinderEventMyReservations;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyEventReservationFragment extends BaseFragment implements RecyclerViewItemListener {

    @BindView(R.id.lv_my_reservations)
    ListView lvMyReservations;
    Unbinder unbinder;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    private ArrayList<EntityMyReservations> myReservationsCollection = new ArrayList<>();
    private ArrayListAdapter<GetReservationsEntity> adapter;
    private static String CavalliNightDetailKey = "CavalliNightDetailKey";
    EntityCavalliNights entityCavalliNights;
    private ArrayList<GetReservationsEntity> getReservationsEntities;
    private ArrayList<GetReservationsEntity> userCollection;

    public static MyEventReservationFragment newInstance(EntityCavalliNights entityCavalliNights) {
        Bundle args = new Bundle();
        args.putString(CavalliNightDetailKey, new Gson().toJson(entityCavalliNights));
        MyEventReservationFragment fragment = new MyEventReservationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderEventMyReservations(getDockActivity(), getDockActivity(), prefHelper));

        if (getArguments() != null) {
            CavalliNightDetailKey = getArguments().getString(CavalliNightDetailKey);

            if (CavalliNightDetailKey != null) {
                entityCavalliNights = new Gson().fromJson(CavalliNightDetailKey, EntityCavalliNights.class);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_reservations, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getReservationsData();
        getMainActivity().filterMenuFragment.setInterfaceListner(this);
        getMainActivity().showBottomTab(AppConstants.home);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading("Event Reservations");
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(CavalliDetailFragment.newInstance(entityCavalliNights), "CavalliDetailFragment");
            }
        },getDockActivity());
        titleBar.showfilterButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().refreshFilter();
                getMainActivity().filterMenuFragment.setIsFromInviteGuest(false);
                getMainActivity().drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
    //    titleBar.setTitlebarBackgroundColor(R.drawable.tab_header,getDockActivity());
        titleBar.showTwoTabsLayout(false, "Reserve Now", "My Reservations");
        titleBar.setLayout_below();
        titleBar.tabsClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(EventReserveNowFragment.newInstance(entityCavalliNights), "EventReserveNowFragment");

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefHelper.setFilterData(getAllFilters());
                getDockActivity().replaceDockableFragment(MyEventReservationFragment.newInstance(entityCavalliNights), "MyEventReservationFragment");

            }
        });
    }

    private void getReservationsData() {
        if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getToken() != null)
            serviceHelper.enqueueCall(webService.getReservations(entityCavalliNights.getId() + "", prefHelper.getSignUpUser().getToken()), WebServiceConstants.GET_RESERVATIONS);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.GET_RESERVATIONS:
                getReservationsEntities = (ArrayList<GetReservationsEntity>) result;

                if (getReservationsEntities.size() <= 0) {
                    txtNoData.setVisibility(View.VISIBLE);
                    lvMyReservations.setVisibility(View.GONE);
                } else {
                    txtNoData.setVisibility(View.GONE);
                    getMyReservationsListData(getReservationsEntities);
                    lvMyReservations.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void getMyReservationsListData(ArrayList<GetReservationsEntity> result) {

        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);
    }

    private void bindData(ArrayList<GetReservationsEntity> myReservationsCollection) {

        adapter.clearList();
        lvMyReservations.setAdapter(adapter);
        adapter.addAll(myReservationsCollection);
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