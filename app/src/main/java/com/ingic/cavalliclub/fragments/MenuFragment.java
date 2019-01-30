package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.MenuEnt;
import com.ingic.cavalliclub.entities.MenuEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.ui.adapters.ArrayListAdapter;
import com.ingic.cavalliclub.ui.binders.MenuItemBinder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MenuFragment extends BaseFragment {
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.lv_menu)
    ListView lvMenu;
    private ArrayList<MenuEntity> menuEntities;
    private ArrayList<MenuEntity> userCollection;
    Unbinder unbinder;

    private ArrayList<MenuEnt> collection;
    private ArrayListAdapter<MenuEntity> adapter;

    public static MenuFragment newInstance() {
        Bundle args = new Bundle();

        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayListAdapter<>(getDockActivity(), new MenuItemBinder(getDockActivity(), prefHelper, getMainActivity()));
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().showBottomTab(AppConstants.home);

        getMenuItems();
        listners();
    }

    private void listners() {

        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (menuEntities.get(position).getCategoryProduct().equalsIgnoreCase(AppConstants.IMAGE)) {
                    getDockActivity().replaceDockableFragment(StarterMenuFragment.newInstance(menuEntities.get(position).getId(), menuEntities.get(position).getName(), menuEntities.get(position).getCategoryImage()), "RestaurantMenuFragment");

                } else {
                    getDockActivity().replaceDockableFragment(RestaurantMenuFragment.newInstance(menuEntities.get(position).getId(), menuEntities.get(position).getName(), menuEntities.get(position).getCategoryImage()), "RestaurantMenuFragment");

                }
            }
        });
    }

    private void bindData() {

/*        collection=new ArrayList<>();
        collection.add(new MenuEnt("drawable://"+R.drawable.image,"Lounge Menu"));
        collection.add(new MenuEnt("drawable://"+R.drawable.image,"Restaurent Menu"));
        collection.add(new MenuEnt("drawable://"+R.drawable.image,"Cigar Menu"));
        collection.add(new MenuEnt("drawable://"+R.drawable.image,"Wine Menu"));*/

        if (collection.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvMenu.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvMenu.setVisibility(View.VISIBLE);
        }

/*        adapter.clearList();
        lvMenu.setAdapter(adapter);
        adapter.addAll(collection);
        adapter.notifyDataSetChanged();*/
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.hideTwoTabsLayout();
        //  titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.menu_small));
    }

    private void getMenuItems() {
        if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getToken() != null)
            serviceHelper.enqueueCall(webService.Menu(prefHelper.getSignUpUser().getToken()), WebServiceConstants.MENU);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.MENU:
                menuEntities = (ArrayList<MenuEntity>) result;
                setMenuData(menuEntities);
                break;
        }
    }

    private void setMenuData(ArrayList<MenuEntity> result) {

        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);
    }

    private void bindData(ArrayList<MenuEntity> userCollection) {

        adapter.clearList();
        lvMenu.setAdapter(adapter);
        adapter.addAll(userCollection);
        adapter.notifyDataSetChanged();

        if (userCollection.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvMenu.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvMenu.setVisibility(View.VISIBLE);
        }
    }
}
