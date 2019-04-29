package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.EntityLatestCompetition;
import com.application.cavalliclub.entities.GetCompetitionEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.BinderLatestCompetition;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LatestCompetitionFragment extends BaseFragment {


    @BindView(R.id.lv_latest_competition)
    ListView lvLatestCompetition;
    Unbinder unbinder;

    private static String sourceKey = "sourceKey";
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;

    private ArrayList<EntityLatestCompetition> myLatestCompetitionCollection = new ArrayList<>();
    private ArrayListAdapter<GetCompetitionEntity> adapter;

    private ArrayList<GetCompetitionEntity> getCompetitionEntities;
    private ArrayList<GetCompetitionEntity> userCollection;

    private static String checker;

    public static LatestCompetitionFragment newInstance(String home, String checkerBack) {
        checker = checkerBack;
        return new LatestCompetitionFragment();
    }

    public static LatestCompetitionFragment newInstance(String source) {
        Bundle args = new Bundle();
        args.putString(sourceKey, source);
        LatestCompetitionFragment fragment = new LatestCompetitionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sourceKey = getArguments().getString(sourceKey);
        }
        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderLatestCompetition(getDockActivity(), true));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_competition, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().showBottomTab(AppConstants.home);
        getCompetitions();
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals(AppConstants.fromHOME)) {
                    getDockActivity().popBackStackTillEntry(0);
                    getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                } else {
                    getDockActivity().replaceDockableFragment(MyProfileFragment.newInstance(), "MyProfileFragment");
                }
            }
        },getDockActivity());
        titleBar.setSubHeading("Competitions");
      //  titleBar.setTitlebarBackgroundColor(R.drawable.tab_header,getDockActivity());
        titleBar.showTwoTabsLayout(true, "Latest Competitions", "History");
        titleBar.setLayout_below();
        titleBar.tabsClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals(AppConstants.fromHOME)) {
                    getDockActivity().replaceDockableFragment(LatestCompetitionFragment.newInstance(AppConstants.fromHOME), "LatestCompetitionFragment");
                } else {
                    getDockActivity().replaceDockableFragment(LatestCompetitionFragment.newInstance(AppConstants.fromPROFILE), "LatestCompetitionFragment");
                }

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals(AppConstants.fromHOME)) {
                    getDockActivity().replaceDockableFragment(CompetitionHistoryFragment.newInstance(AppConstants.fromHOME), "CompetitionHistoryFragment");
                } else {
                    getDockActivity().replaceDockableFragment(CompetitionHistoryFragment.newInstance(AppConstants.fromPROFILE), "CompetitionHistoryFragment");
                }

            }
        });
    }

    private void ListViewOnClick() {
        lvLatestCompetition.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDockActivity().addDockableFragment(FragmentCompetitionDetail.newInstance(getCompetitionEntities.get(position)), "FragmentCompetitionDetail");
            }
        });
    }

    private void getCompetitions() {
        serviceHelper.enqueueCall(webService.getCompetitions(), WebServiceConstants.GET_COMPETITIONS);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.GET_COMPETITIONS:
                getCompetitionEntities = (ArrayList<GetCompetitionEntity>) result;
                getMyReservationsListData(getCompetitionEntities);
                break;
        }
    }

    private void getMyReservationsListData(ArrayList<GetCompetitionEntity> result) {

        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);

        if (userCollection.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvLatestCompetition.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvLatestCompetition.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(ArrayList<GetCompetitionEntity> myCompetitionCollection) {

        adapter.clearList();
        lvLatestCompetition.setAdapter(adapter);
        adapter.addAll(myCompetitionCollection);
        adapter.notifyDataSetChanged();
        ListViewOnClick();
    }
}
