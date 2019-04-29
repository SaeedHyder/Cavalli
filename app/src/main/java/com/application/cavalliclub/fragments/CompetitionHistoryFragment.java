package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.GetCompetitionHistoryEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.BinderCompetitionHistory;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CompetitionHistoryFragment extends BaseFragment {


    @BindView(R.id.lv_competition_history)
    ListView lvCompetitionHistory;
    Unbinder unbinder;

    private static String sourceKey = "sourceKey";
    private static String checker;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;

    private ArrayListAdapter<GetCompetitionHistoryEntity> adapter;
    private ArrayList<GetCompetitionHistoryEntity> getCompetitionHistoryEntities;
    private ArrayList<GetCompetitionHistoryEntity> userCollection;

    public static CompetitionHistoryFragment newInstance() {
        return new CompetitionHistoryFragment();
    }

    public static CompetitionHistoryFragment newInstance(String source) {
        Bundle args = new Bundle();
        args.putString(sourceKey, source);
        checker = source;
        CompetitionHistoryFragment fragment = new CompetitionHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sourceKey = getArguments().getString(sourceKey);
        }
        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderCompetitionHistory(getDockActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competition_history, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivity().showBottomTab(AppConstants.home);
        getCompetitionHistoryData();
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
        }, getDockActivity());
        titleBar.setSubHeading("Competitions");
        //   titleBar.setTitlebarBackgroundColor(R.drawable.tab_header,getDockActivity());
        titleBar.showTwoTabsLayout(false, "Latest Competitions", "History");
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

    private void getCompetitionHistoryData() {
        serviceHelper.enqueueCall(webService.getCompetitionsHistory(), WebServiceConstants.HISTORY_COMPETITIONS);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.HISTORY_COMPETITIONS:
                getCompetitionHistoryEntities = (ArrayList<GetCompetitionHistoryEntity>) result;
                getMyReservationsListData(getCompetitionHistoryEntities);
                break;
        }
    }

    private void getMyReservationsListData(ArrayList<GetCompetitionHistoryEntity> result) {

        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);

        if (userCollection.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvCompetitionHistory.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvCompetitionHistory.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(ArrayList<GetCompetitionHistoryEntity> myCompetitionHistoryCollection) {

        adapter.clearList();
        lvCompetitionHistory.setAdapter(adapter);
        adapter.addAll(myCompetitionHistoryCollection);
        adapter.notifyDataSetChanged();

        ListViewOnClick();
    }

    private void ListViewOnClick() {
        lvCompetitionHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getCompetitionHistoryEntities.get(position) != null && getCompetitionHistoryEntities.get(position).getCompetitionImages() != null && getCompetitionHistoryEntities.get(position).getCompetitionImages().size() > 0) {
                    getDockActivity().addDockableFragment(CompetitionHistoryDetailFragment.newInstance(getCompetitionHistoryEntities.get(position)), "FragmentCompetitionDetail");
                }
            }
        });
    }
}
