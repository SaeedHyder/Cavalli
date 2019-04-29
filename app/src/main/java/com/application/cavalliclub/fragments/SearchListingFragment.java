package com.application.cavalliclub.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.EntityCavalliNights;
import com.application.cavalliclub.entities.EntityHomeSearch;
import com.application.cavalliclub.entities.MenuCategoryEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.BinderSearchListing;
import com.application.cavalliclub.ui.views.AnyEditTextView;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SearchListingFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.lv_home_search_listing)
    ListView lvHomeSearchListing;
    EntityCavalliNights entityCavalliNights;
    MenuCategoryEntity menuCategoryEntity;
    @BindView(R.id.edt_name)
    AnyEditTextView edtName;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.ll_search_bar)
    LinearLayout llSearchBar;
    @BindView(R.id.rl_searchbar)
    RelativeLayout rlSearchbar;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    private ArrayList<EntityHomeSearch> homeSeachListCollection = new ArrayList<>();
    private ArrayListAdapter<EntityHomeSearch> adapter;
    ArrayList<EntityHomeSearch> entityHomeSearchList = new ArrayList<>();

    private static String searchText;

    public static SearchListingFragment newInstance() {
        return new SearchListingFragment();
    }

    public static SearchListingFragment newInstance(String text) {
        searchText=text;
        return new SearchListingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderSearchListing(getDockActivity(), prefHelper));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_listing, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setSearchDataListing();
        if(edtName.requestFocus()) {
            getDockActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        InputMethodManager imm = (InputMethodManager) getDockActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtName, InputMethodManager.SHOW_IMPLICIT);

        txtNoData.setText("Please type something to search.");
        txtNoData.setVisibility(View.VISIBLE);

      /*  if (edtName.length() > 0) {
            txtNoData.setVisibility(View.GONE);
            searchData();
        }
        if (edtName.length() == 0) {
            adapter.clearList();
            txtNoData.setText("Please type something to search.");
            txtNoData.setVisibility(View.VISIBLE);
            edtName.requestFocus();
            InputMethodManager imm = (InputMethodManager) getDockActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtName, InputMethodManager.SHOW_IMPLICIT);
            //   UIHelper.hideSoftKeyboard(getDockActivity(), getView());
        } else {
            txtNoData.setVisibility(View.GONE);
        }*/


        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (edtName.length() > 0) {
                    txtNoData.setVisibility(View.GONE);
                    searchData();
                }

                if (edtName.length() == 0) {
                    adapter.clearList();
                    txtNoData.setText("Please type something to search.");
                    txtNoData.setVisibility(View.VISIBLE);
                 //   UIHelper.hideSoftKeyboard(getDockActivity(), getView());
                } else {
                    txtNoData.setVisibility(View.GONE);
                }
            }
        });
    }

    private void searchData() {
        serviceHelper.enqueueCall(webService.getHomeSearch(edtName.getText().toString() + ""), WebServiceConstants.HOME_SEARCH);
    }

    private void setSearchDataListing() {
        bindData(homeSeachListCollection);
    }

    private void bindData(final ArrayList<EntityHomeSearch> homeSearchListEntities) {

        adapter.clearList();
        lvHomeSearchListing.setAdapter(adapter);

  /*      final ArrayList<EntityHomeSearch> entityHomeSearches = new Gson().fromJson(prefHelper.getHomeSearch(), new TypeToken<ArrayList<EntityHomeSearch>>() {
        }.getType());*/
        adapter.addAll(homeSearchListEntities);
        adapter.notifyDataSetChanged();

        lvHomeSearchListing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //new service will hit here for details -- will be directed to different fragments as per type

                if (homeSearchListEntities.get(position).getType().equalsIgnoreCase(AppConstants.TYPE_EVENT)) {
                    serviceHelper.enqueueCall(webService.getHomeSearchEventDetail(homeSearchListEntities.get(position).getId() + "", AppConstants.TYPE_EVENT), WebServiceConstants.HOME_SEARCH_EVENT);
                } else if (homeSearchListEntities.get(position).getType().equalsIgnoreCase(AppConstants.TYPE_NEWS)) {
                    getDockActivity().replaceDockableFragment(LatestUpdatesMusicFragment.newInstance(), "LatestUpdatesMusicFragment");
                } else if (homeSearchListEntities.get(position).getType().equalsIgnoreCase(AppConstants.TYPE_PRODUCT)) {
                    serviceHelper.enqueueCall(webService.getHomeSearchProductDetail(homeSearchListEntities.get(position).getId() + "", AppConstants.TYPE_PRODUCT), WebServiceConstants.HOME_SEARCH_PRODUCT);
                }
            }
        });
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.HOME_SEARCH_EVENT:
                entityCavalliNights = (EntityCavalliNights) result;

                if (entityCavalliNights.getEventTypeId().equalsIgnoreCase(AppConstants.CAVALLI_EVENTS)) {
                    getDockActivity().replaceDockableFragment(CavalliDetailFragment.newInstance(entityCavalliNights), "CavalliDetailFragment");
                } else if (entityCavalliNights.getEventTypeId().equalsIgnoreCase(AppConstants.MUSIC_EVENTS)) {
                    getDockActivity().replaceDockableFragment(CavalliNightsDetailFragment.newInstance(entityCavalliNights), "CavalliNightsDetailFragment");
                }
                break;

            case WebServiceConstants.HOME_SEARCH_PRODUCT:
                menuCategoryEntity = (MenuCategoryEntity) result;
                if (menuCategoryEntity.getCategory().equalsIgnoreCase(AppConstants.MENU_FOOD)) {
                    getDockActivity().replaceDockableFragment(MenuItemDetailFragment.newInstance(menuCategoryEntity), "CavalliDetailFragment");
                } else if (menuCategoryEntity.getCategory().equalsIgnoreCase(AppConstants.BAR_FOOD)) {
                    getDockActivity().replaceDockableFragment(MixDrinksDetailFragment.newInstance(menuCategoryEntity), "MixDrinksDetailFragment");
                }
                break;

            case WebServiceConstants.HOME_SEARCH:

                entityHomeSearchList = (ArrayList<EntityHomeSearch>) result;

                if (entityHomeSearchList.size() > 0) {
                    bindData(entityHomeSearchList);
                    txtNoData.setVisibility(View.GONE);
                } else {
                    //UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getString(R.string.no_data_available));
                    txtNoData.setText("No search result found.");
                    txtNoData.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.hideTwoTabsLayout();
      //  titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.result));
    }

    @OnClick({R.id.edt_name, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edt_name:
                break;
            case R.id.iv_search:
                break;
        }
    }
}
