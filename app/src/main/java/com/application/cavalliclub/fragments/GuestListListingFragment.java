package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.EntityCategoriesGuestList;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.BinderCategoriesGuestList;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GuestListListingFragment extends BaseFragment {

    @BindView(R.id.lv_my_reservations)
    ListView lvMyReservations;
    Unbinder unbinder;

    private ArrayListAdapter<EntityCategoriesGuestList> adapter;
    private ArrayList<EntityCategoriesGuestList> getCategoriesEntities;
    private ArrayList<EntityCategoriesGuestList> userCollection;

    public static GuestListListingFragment newInstance() {
        return new GuestListListingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderCategoriesGuestList(getDockActivity(), getMainActivity(), prefHelper));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_list_listing, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivity().showBottomTab(AppConstants.home);
        getCategories();
    }

    private void getCategories() {
        serviceHelper.enqueueCall(webService.getGuestListCategories(), WebServiceConstants.GUEST_CATEGORIES);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.GUEST_CATEGORIES:
                getCategoriesEntities = (ArrayList<EntityCategoriesGuestList>) result;
                getMyReservationsListData(getCategoriesEntities);
                break;
        }
    }

    private void getMyReservationsListData(ArrayList<EntityCategoriesGuestList> result) {

        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);
    }

    private void bindData(ArrayList<EntityCategoriesGuestList> myCompetitionCollection) {

        adapter.clearList();
        lvMyReservations.setAdapter(adapter);
        adapter.addAll(myCompetitionCollection);
        adapter.notifyDataSetChanged();
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
        titleBar.hideTwoTabsLayout();
     //   titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading("Guest List");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
