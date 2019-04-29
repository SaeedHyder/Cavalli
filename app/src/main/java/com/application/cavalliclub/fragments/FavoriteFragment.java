package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.FavoriteEnt;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.FavoriteItemBinder;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoriteFragment extends BaseFragment {
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.lv_favorite)
    ListView lvFavorite;
    Unbinder unbinder;

    private ArrayList<FavoriteEnt> favoriteCollection;
    private ArrayListAdapter<FavoriteEnt> adapter;

    public static FavoriteFragment newInstance() {
        Bundle args = new Bundle();

        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new FavoriteItemBinder(getDockActivity(),prefHelper));
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().showBottomTab(AppConstants.home);

        bindData();

    }

    private void bindData() {

        favoriteCollection = new ArrayList<>();

        favoriteCollection.add(new FavoriteEnt("drawable://" + R.drawable.placeholder_thumb,"Party Night",getString(R.string.lorem_ipsum_small)));
        favoriteCollection.add(new FavoriteEnt("drawable://" + R.drawable.placeholder_thumb,"Party Night",getString(R.string.lorem_ipsum_small)));
        favoriteCollection.add(new FavoriteEnt("drawable://" + R.drawable.placeholder_thumb,"Party Night",getString(R.string.lorem_ipsum_small)));
        favoriteCollection.add(new FavoriteEnt("drawable://" + R.drawable.placeholder_thumb,"Party Night",getString(R.string.lorem_ipsum_small)));


        if (favoriteCollection.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvFavorite.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvFavorite.setVisibility(View.VISIBLE);
        }

        adapter.clearList();
        lvFavorite.setAdapter(adapter);
        adapter.addAll(favoriteCollection);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.hideTwoTabsLayout();
       // titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.favorite));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
