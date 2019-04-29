package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.AddCartEntity;
import com.application.cavalliclub.entities.BarCategoriesEntity;
import com.application.cavalliclub.entities.EntityRecyclerviewHome;
import com.application.cavalliclub.entities.MenuCategoryEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.ui.adapters.AdapterRecyclerViewBarOrdering;
import com.application.cavalliclub.ui.adapters.ArrayListExpandableAdapter;
import com.application.cavalliclub.ui.binders.ExpandableBarOrderBinder;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BarOrderingFragment extends BaseFragment {

    @BindView(R.id.ll_see_all)
    LinearLayout llSeeAll;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.ll_see_all2)
    LinearLayout llSeeAll2;
    @BindView(R.id.recycler_view2)
    RecyclerView recyclerView2;
    @BindView(R.id.ll_see_all3)
    LinearLayout llSeeAll3;
    @BindView(R.id.recycler_view3)
    RecyclerView recyclerView3;
    Unbinder unbinder;
    @BindView(R.id.elv_bar_category)
    ExpandableListView elvBarCategory;
    AddCartEntity addCartEntity;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;

    private ArrayListExpandableAdapter<BarCategoriesEntity, BarCategoriesEntity> adapter;
    private ArrayList<BarCategoriesEntity> collectionGroup;
    private ArrayList<BarCategoriesEntity> collectionChild;

    private HashMap<BarCategoriesEntity, BarCategoriesEntity> listDataChild;

    private List<EntityRecyclerviewHome> barOrderingList = new ArrayList<>();
    private AdapterRecyclerViewBarOrdering mAdapter;

    private List<EntityRecyclerviewHome> barOrderingList2 = new ArrayList<>();
    private AdapterRecyclerViewBarOrdering mAdapter2;

    private List<EntityRecyclerviewHome> barOrderingList3 = new ArrayList<>();
    private AdapterRecyclerViewBarOrdering mAdapter3;

    public static BarOrderingFragment newInstance() {
        return new BarOrderingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_ordering, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivity().showBottomTab(AppConstants.home);


        getBarOrderingData();

        elvBarCategory.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true; // This way the expander cannot be collapsed
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
            }
        },getDockActivity());
        titleBar.hideTwoTabsLayout();
       // titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.bar_orderings));
        titleBar.showCartButton(getCartCount());
    }

    @OnClick({R.id.ll_see_all, R.id.ll_see_all2, R.id.ll_see_all3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_see_all:
                break;
            case R.id.ll_see_all2:
                break;
            case R.id.ll_see_all3:
                break;
        }
    }

    private void setInProgressJobsData(ArrayList<BarCategoriesEntity> result) {

        collectionGroup = new ArrayList<>();
        collectionChild = new ArrayList<>();

        listDataChild = new HashMap<>();
        for (BarCategoriesEntity item : result) {

            if (item.getProducts().size() > 0) {
                collectionGroup.add(item);
                for (MenuCategoryEntity childItem : item.getProducts()) {
                    collectionChild.add(item);
                }

                listDataChild.put(item, item);
                collectionChild = new ArrayList<>();
            } else {
                //listDataChild.put(item, new BarCategoriesEntity());
            }
        }

        adapter = new ArrayListExpandableAdapter<>(getDockActivity(), collectionGroup, listDataChild, new ExpandableBarOrderBinder(getDockActivity(), prefHelper));
        bindData();

        if (listDataChild.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            elvBarCategory.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            elvBarCategory.setVisibility(View.VISIBLE);
        }

    }

    private void bindData() {

        if (elvBarCategory != null)
            elvBarCategory.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        for (int i = 0; i < collectionGroup.size(); i++) {
            elvBarCategory.expandGroup(i);
        }
    }

    private void getBarOrderingData() {
        serviceHelper.enqueueCall(webService.getBarData(), WebServiceConstants.BAR_CATEGORY);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.BAR_CATEGORY:
                ArrayList<BarCategoriesEntity> entity = (ArrayList<BarCategoriesEntity>) result;
                setInProgressJobsData(entity);
                break;
        }
    }
}