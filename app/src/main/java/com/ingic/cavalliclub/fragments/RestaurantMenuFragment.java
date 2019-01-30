package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.MenuCategoryEntity;
import com.ingic.cavalliclub.entities.MenuCategoryParentEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.interfaces.MenuInterface;
import com.ingic.cavalliclub.ui.adapters.ArrayListAdapter;
import com.ingic.cavalliclub.ui.binders.RestuarantMenuItemBinder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RestaurantMenuFragment extends BaseFragment implements MenuInterface {
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.lv_restaurant_menu)
    ListView lvRestaurantMenu;
    Unbinder unbinder;
    private ImageLoader imageLoader;
    MenuInterface menuInterface;
    private ArrayList<MenuCategoryParentEntity> menuCategoryParentEntities;
    private ArrayList<MenuCategoryEntity> productData = new ArrayList<>();
    private ArrayList<MenuCategoryEntity> userCollection;
    private static int ID;
    private static String NAME;
    private static String IMAGE;
    private ArrayListAdapter<MenuCategoryEntity> adapter;

    public static RestaurantMenuFragment newInstance(Integer id, String name, String image) {
        Bundle args = new Bundle();
        ID = id;
        NAME = name;
        IMAGE = image;
        RestaurantMenuFragment fragment = new RestaurantMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        adapter = new ArrayListAdapter<>(getDockActivity(), new RestuarantMenuItemBinder(getDockActivity(), prefHelper));
        if (getArguments() != null) {
        }
    }

    public void setMenuInterface(MenuInterface menuInterface) {
        this.menuInterface = menuInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resturant_menu, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().showBottomTab(AppConstants.home);
        //bindData();
        getMenuCategories();
        Listner();
    }

    private void Listner() {
        lvRestaurantMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (productData.get(position) != null)
                    getDockActivity().replaceDockableFragment(MenuItemDetailFragment.newInstance(productData.get(position)), "MenuItemDetailFragment");
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        //titleBar.setTitlebarBackgroundColor(R.drawable.tab_header);

        titleBar.setTitlebarBackgroundMenu(IMAGE,getDockActivity());

     /*   titleBar.setRecyclerViewData(getDockActivity());*/
        titleBar.setSubHeading(NAME + "");
    }

    private void getMenuCategories() {
        if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getToken() != null)
            serviceHelper.enqueueCall(webService.MenuCategories(ID, prefHelper.getSignUpUser().getToken()), WebServiceConstants.MENU_CATEGORIES);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.MENU_CATEGORIES:
                menuCategoryParentEntities = (ArrayList<MenuCategoryParentEntity>) result;
                if (menuCategoryParentEntities != null && menuCategoryParentEntities.size() > 0)
                    productData = menuCategoryParentEntities.get(0).getProducts();
                if (menuCategoryParentEntities != null && menuCategoryParentEntities.size() > 0 && menuCategoryParentEntities.get(0).getProducts() != null)
                    setMenuCategoryData(menuCategoryParentEntities.get(0).getProducts());
                if (getMainActivity().titleBar != null)
                    getMainActivity().titleBar.setRecyclerViewData(getDockActivity(), menuCategoryParentEntities, this);
                break;
        }
    }

    private void setMenuCategoryData(List<MenuCategoryEntity> result) {

        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);
    }

    private void bindData(ArrayList<MenuCategoryEntity> userCollection) {

        adapter.clearList();
        lvRestaurantMenu.setAdapter(adapter);
        adapter.addAll(userCollection);
        adapter.notifyDataSetChanged();

        if (userCollection.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvRestaurantMenu.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvRestaurantMenu.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void menuItemClick(MenuCategoryParentEntity menuCategoryParentEntity) {
        productData = menuCategoryParentEntity.getProducts();
        setMenuCategoryData(menuCategoryParentEntity.getProducts());
    }
}
