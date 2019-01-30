package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.MenuCategoryEntity;
import com.ingic.cavalliclub.entities.MenuCategoryParentEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.interfaces.MenuInterface;
import com.ingic.cavalliclub.interfaces.ViewPagerItemClickListner;
import com.ingic.cavalliclub.ui.adapters.ArrayListAdapter;
import com.ingic.cavalliclub.ui.adapters.CustomPageAdapter;
import com.ingic.cavalliclub.ui.adapters.MyCustomPagerAdapter;
import com.ingic.cavalliclub.ui.adapters.ViewPagerAdapterMenu;
import com.ingic.cavalliclub.ui.binders.StarterItemBinder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.ExpendedGridView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ahmedsyed on 10/9/2018.
 */
public class StarterMenuFragment extends BaseFragment implements MenuInterface,ViewPagerItemClickListner {
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.lv_restaurant_menu)
    ExpendedGridView lvRestaurantMenu;
    Unbinder unbinder;
    private static String NAME;
    private static String IMAGE;
    private static int ID;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;

    private ArrayList<MenuCategoryParentEntity> entity;
    private ArrayList<String> imagesCollection;
    private ArrayList<MenuCategoryEntity> productData = new ArrayList<>();
    private ViewPagerAdapterMenu viewPagerAdapterMenu;


    private ArrayListAdapter<MenuCategoryEntity> adapter;

    public static StarterMenuFragment newInstance() {
        Bundle args = new Bundle();

        StarterMenuFragment fragment = new StarterMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static StarterMenuFragment newInstance(Integer id, String name, String image) {
        Bundle args = new Bundle();
        ID = id;
        NAME = name;
        IMAGE = image;

        StarterMenuFragment fragment = new StarterMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new StarterItemBinder(getDockActivity(), getMainActivity(), prefHelper));
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_starter_menu, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getToken() != null)
            serviceHelper.enqueueCall(webService.MenuCategories(ID, prefHelper.getSignUpUser().getToken()), WebServiceConstants.MENU_CATEGORIES);

        lvRestaurantMenu.setExpanded(true);

        listner();


    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.MENU_CATEGORIES:
                entity = (ArrayList<MenuCategoryParentEntity>) result;

                if (entity != null && entity.size() > 0) {
                    productData = entity.get(0).getProducts();
                    setViewPager(entity.get(0).getProducts());
                    bindData(entity.get(0).getProducts());

                    if (getMainActivity().titleBar != null)
                        getMainActivity().titleBar.setRecyclerViewData(getDockActivity(), entity, this);
                }

                break;
        }
    }

    private void setViewPager(ArrayList<MenuCategoryEntity> products) {

        imagesCollection=new ArrayList<>();
        for(MenuCategoryEntity item: products){
            imagesCollection.add(item.getImageUrl());
        }
        if (imagesCollection != null && imagesCollection.size() > 0) {
            txtNoData.setVisibility(View.GONE);
            viewPagerAdapterMenu = new ViewPagerAdapterMenu(getApplicationContext(), imagesCollection,this);
            viewPager.setAdapter(viewPagerAdapterMenu);
        } else {
           txtNoData.setVisibility(View.VISIBLE);
        }

        indicator.setViewPager(viewPager);

    }




    private void listner() {

        lvRestaurantMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (productData != null && productData.size() > 0) {
                    getDockActivity().replaceDockableFragment(FragmentGalleryPager.newInstance(productData.get(position).getProductImages(), position,true), "FragmentGalleryPager");
                }
            }
        });
    }

    private void bindData(ArrayList<MenuCategoryEntity> entity) {

        adapter.clearList();
        lvRestaurantMenu.setAdapter(adapter);
        adapter.addAll(entity);

        if (entity.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvRestaurantMenu.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvRestaurantMenu.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(NAME);
        titleBar.setTitlebarBackgroundMenu(IMAGE, getDockActivity());
    }

    @Override
    public void menuItemClick(MenuCategoryParentEntity menuCategoryParentEntity) {
        productData = menuCategoryParentEntity.getProducts();
        setViewPager(menuCategoryParentEntity.getProducts());
        bindData(menuCategoryParentEntity.getProducts());
    }


    @Override
    public void pagerClick(int position) {
        if (productData != null && productData.size() > 0) {
            getDockActivity().replaceDockableFragment(FragmentGalleryPager.newInstance(productData.get(position).getProductImages(), position,true), "FragmentGalleryPager");
        }
    }
}
