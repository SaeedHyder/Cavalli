package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.EntityMyReservations;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.ui.adapters.ArrayListAdapter;
import com.ingic.cavalliclub.ui.binders.BinderMyReservations;
import com.ingic.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyGuestFragment extends BaseFragment {

    @BindView(R.id.lv_my_reservations)
    ListView lvMyReservations;
    Unbinder unbinder;

    private static String sourceKey = "sourceKey";

    private ArrayList<EntityMyReservations> myGuestCollection = new ArrayList<>();
    private ArrayListAdapter<EntityMyReservations> adapter;

    public static MyGuestFragment newInstance() {
        return new MyGuestFragment();
    }

    public static MyGuestFragment newInstance(String source) {
        Bundle args = new Bundle();
        args.putString(sourceKey, source);
        MyGuestFragment fragment = new MyGuestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            sourceKey = getArguments().getString(sourceKey);
        }
        //adapter = new ArrayListAdapter<>(getDockActivity(), new BinderMyReservations(getDockActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_guest, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMyGuestList();
    }

    private void getMyGuestList() {

        myGuestCollection.add(new EntityMyReservations( "Allen Joe", "Sunday, 31 Decemeber 2017", "100","drawable://" + R.drawable.pending, getString(R.string.status_pending),R.color.pending));
        myGuestCollection.add(new EntityMyReservations( "Joseph Hex", "Tuesday, 15 January 2018", "60","drawable://" + R.drawable.reserved, getString(R.string.status_reserved),R.color.reserved));
        myGuestCollection.add(new EntityMyReservations( "Joe Marry", "Friday, 20 January 2018", "250","drawable://" + R.drawable.rejected, getString(R.string.status_rejected),R.color.rejected));
        myGuestCollection.add(new EntityMyReservations( "Harry Porter", "Monday, 1 February 2018", "35","drawable://" + R.drawable.reserved, getString(R.string.status_reserved),R.color.reserved));
        bindData(myGuestCollection);
    }

    private void bindData(ArrayList<EntityMyReservations> myGuestCollection) {

        adapter.clearList();
        lvMyReservations.setAdapter(adapter);
        adapter.addAll(myGuestCollection);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.reservations));
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sourceKey.equals(AppConstants.home)) {
                    getDockActivity().popBackStackTillEntry(0);
                    getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                } else {
                    getDockActivity().replaceDockableFragment(MyProfileFragment.newInstance(), "MyProfileFragment");
                }
            }
        },getDockActivity());

      //  titleBar.setTitlebarBackgroundColor(R.drawable.tab_header,getDockActivity());
        titleBar.showTwoTabsLayout(false,"Invite Guest","My Guest");
        titleBar.setLayout_below();
        titleBar.tabsClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sourceKey.equals(AppConstants.home)) {
                    getDockActivity().replaceDockableFragment(InviteGuestFragment.newInstance(AppConstants.home), "ReserveNowfragment");
                } else {
                    getDockActivity().replaceDockableFragment(InviteGuestFragment.newInstance(AppConstants.profile), "ReserveNowfragment");
                }
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sourceKey.equals(AppConstants.home)) {
                    getDockActivity().replaceDockableFragment(MyGuestFragment.newInstance(AppConstants.home), "MyReservationsFragment");
                } else {
                    getDockActivity().replaceDockableFragment(MyGuestFragment.newInstance(AppConstants.profile), "MyReservationsFragment");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
