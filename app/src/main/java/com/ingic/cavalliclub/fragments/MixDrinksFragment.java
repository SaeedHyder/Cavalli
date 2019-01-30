package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.BarCategoriesEntity;
import com.ingic.cavalliclub.entities.EntityMixDrinks;
import com.ingic.cavalliclub.entities.MenuCategoryEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.ui.adapters.ArrayListAdapter;
import com.ingic.cavalliclub.ui.binders.BinderMixDrinks;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MixDrinksFragment extends BaseFragment {

    @BindView(R.id.lv_mix_drink)
    ListView lvMixDrink;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;

    private ArrayList<EntityMixDrinks> myMixDrinksCollection = new ArrayList<>();
    private ArrayListAdapter<MenuCategoryEntity> adapter;

    private static String mixDrinkKey = "mixDrinkKey";
    private static String mixDrinkKeyString = "mixDrinkKeyString";
    private BarCategoriesEntity barCategoriesEntity;
    private ArrayList<MenuCategoryEntity> myproductBarCategoriesCollection = new ArrayList<>();
    private MenuCategoryEntity productBarCategoriesEntity;

    public static MixDrinksFragment newInstance(ArrayList<MenuCategoryEntity> entityMixDrinks) {
        Bundle args = new Bundle();
        args.putString(mixDrinkKey, new Gson().toJson(entityMixDrinks));
        MixDrinksFragment fragment = new MixDrinksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MixDrinksFragment newInstance(BarCategoriesEntity entityMixDrinks) {
        Bundle args = new Bundle();
        args.putString(mixDrinkKeyString, new Gson().toJson(entityMixDrinks));
        MixDrinksFragment fragment = new MixDrinksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MixDrinksFragment newInstance() {
        return new MixDrinksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderMixDrinks(getDockActivity()));

        mixDrinkKey = getArguments().getString(mixDrinkKey);
        if (mixDrinkKey != null) {
            productBarCategoriesEntity = new Gson().fromJson(mixDrinkKey, MenuCategoryEntity.class);
        }

        if (getArguments() != null) {
            mixDrinkKeyString = getArguments().getString(mixDrinkKeyString);
        }
        if (mixDrinkKeyString != null) {
            barCategoriesEntity = new Gson().fromJson(mixDrinkKeyString, BarCategoriesEntity.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mix_drinks, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMyMixDrinksList();
        getMainActivity().showBottomTab(AppConstants.home);
        ListViewOnClicks();
        prefHelper.setDrinkType(barCategoriesEntity.getName() + "");
    }

    private void getMyMixDrinksList() {
        bindData(barCategoriesEntity.getProducts());
    }

    private void bindData(ArrayList<MenuCategoryEntity> myproductBarCategoriesCollection) {

        adapter.clearList();
        lvMixDrink.setAdapter(adapter);
        adapter.addAll(myproductBarCategoriesCollection);
        adapter.notifyDataSetChanged();

        if (myproductBarCategoriesCollection.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvMixDrink.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvMixDrink.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.hideTwoTabsLayout();
     //   titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(barCategoriesEntity.getName() + "");
    }

    private void ListViewOnClicks() {
        lvMixDrink.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDockActivity().replaceDockableFragment(MixDrinksDetailFragment.newInstance(barCategoriesEntity.getProducts().get(position)), "MixDrinksDetailFragment");
            }
        });
    }
}
