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
import com.application.cavalliclub.entities.EntityMyReservations;
import com.application.cavalliclub.entities.GetReservationsEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.interfaces.RecyclerViewItemListener;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.BinderMyReservations;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyReservationsFragment extends BaseFragment implements RecyclerViewItemListener {

    @BindView(R.id.lv_my_reservations)
    ListView lvMyReservations;
    Unbinder unbinder;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    private ArrayList<EntityMyReservations> myReservationsCollection = new ArrayList<>();
    private ArrayListAdapter<GetReservationsEntity> adapter;
    private ArrayList<GetReservationsEntity> getReservationsEntities;
    private ArrayList<GetReservationsEntity> userCollection;

    public static MyReservationsFragment newInstance() {
        return new MyReservationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderMyReservations(getDockActivity(), getDockActivity(), prefHelper));
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
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().onBackPressed();
            }
        },getDockActivity());
        titleBar.showfilterButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().refreshFilter();
                getMainActivity().setCheckboxDataFilter();
                getMainActivity().filterMenuFragment.setIsFromInviteGuest(false);
                getMainActivity().drawerLayout.openDrawer(Gravity.RIGHT);

            }
        });
        titleBar.setSubHeading("Reservations");
       // titleBar.setTitlebarBackgroundColor(R.drawable.tab_header,getDockActivity());
        titleBar.showTwoTabsLayout(false, "Reserve Now", "My Reservations");
        titleBar.setLayout_below();
        titleBar.tabsClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().popFragment();
                getDockActivity().replaceFragment(ReserveNowfragment.newInstance(), ReserveNowfragment.class.getSimpleName(), true, false);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                prefHelper.setFilterData(getAllFilters());
            }
        });
    }

    private void getReservationsData() {
        if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getToken() != null)
            serviceHelper.enqueueCall(webService.getReservations("", prefHelper.getSignUpUser().getToken()), WebServiceConstants.GET_RESERVATIONS);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.GET_RESERVATIONS:
                getReservationsEntities = (ArrayList<GetReservationsEntity>) result;
                getMyReservationsListData(getReservationsEntities);
                break;
        }
    }

    private void getMyReservationsListData(ArrayList<GetReservationsEntity> result) {

        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);

        if (userCollection.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvMyReservations.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvMyReservations.setVisibility(View.VISIBLE);
        }
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
