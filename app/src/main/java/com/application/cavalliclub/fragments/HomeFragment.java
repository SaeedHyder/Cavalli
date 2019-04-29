package com.application.cavalliclub.fragments;


import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.entities.BannerEnt;
import com.application.cavalliclub.entities.EntityHomeSearch;
import com.application.cavalliclub.entities.LatestUpdatesEntity;
import com.application.cavalliclub.entities.TaxEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.SideMenuChooser;
import com.application.cavalliclub.global.SideMenuDirection;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.InternetHelper;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.ui.adapters.AdapterRecyclerViewHome;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.facebook.GraphRequest.TAG;
import static com.application.cavalliclub.global.WebServiceConstants.BANNERIMAGE;


public class HomeFragment extends BaseFragment {

    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.titleMenuBtn)
    ImageView titleMenuBtn;
    @BindView(R.id.titleBtnProfile)
    ImageView titleBtnProfile;
    @BindView(R.id.txt_heading)
    AnyTextView txtHeading;
    @BindView(R.id.txt_sub_heading)
    AnyTextView txtSubHeading;
    @BindView(R.id.edt_name)
    AnyTextView edtName;
    @BindView(R.id.ll_search_bar)
    LinearLayout llSearchBar;
    @BindView(R.id.btn_search)
    Button btnSearchbtnSearch;
    @BindView(R.id.txt_categories)
    AnyTextView txtCategories;
    @BindView(R.id.iv_reservation)
    ImageView ivReservation;
    @BindView(R.id.txt_reservation)
    AnyTextView txtReservation;
    @BindView(R.id.ll_reservations)
    LinearLayout llReservations;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.txt_menu)
    AnyTextView txtMenu;
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;
    @BindView(R.id.iv_competition)
    ImageView ivCompetition;
    @BindView(R.id.txt_competition)
    AnyTextView txtCompetition;
    @BindView(R.id.ll_competition)
    LinearLayout llCompetition;
    @BindView(R.id.iv_music)
    ImageView ivMusic;
    @BindView(R.id.txt_music)
    AnyTextView txtMusic;
    @BindView(R.id.ll_music)
    LinearLayout llMusic;
    @BindView(R.id.iv_bar_odering)
    ImageView ivBarOdering;
    @BindView(R.id.txt_bar_ordering)
    AnyTextView txtBarOrdering;
    @BindView(R.id.ll_bar_ordering)
    LinearLayout llBarOrdering;
    @BindView(R.id.iv_cavalli_nights)
    ImageView ivCavalliNights;
    @BindView(R.id.txt_cavalli_nights)
    AnyTextView txtCavalliNights;
    @BindView(R.id.ll_cavalli_nights)
    LinearLayout llCavalliNights;
    @BindView(R.id.iv_membership)
    ImageView ivMembership;
    @BindView(R.id.txt_membership)
    AnyTextView txtMembership;
    @BindView(R.id.ll_membership)
    LinearLayout llMembership;
    @BindView(R.id.txt_updates)
    AnyTextView txtUpdates;
    @BindView(R.id.ll_see_all)
    LinearLayout llSeeAll;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_myevents)
    ImageView ivMyevents;
    @BindView(R.id.txt_myevents)
    AnyTextView txtMyevents;
    @BindView(R.id.ll_myevents)
    LinearLayout llMyevents;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.mainFrameLayout)
    LinearLayout mainFrameLayout;

    private ArrayList<LatestUpdatesEntity> homeList = new ArrayList<>();
    private ArrayList<LatestUpdatesEntity> userCollection;
    private AdapterRecyclerViewHome mAdapter;
    public String ArrayStringContainer;
    public String ArrayStringContainerHomeSearch;
    ArrayList<EntityHomeSearch> entityHomeSearchList = new ArrayList<>();
    Unbinder unbinder;
    TaxEntity taxEntity;

    private ImageLoader imageLoader;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader=ImageLoader.getInstance();



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serviceHelper.enqueueCall(webService.getBaneer(), BANNERIMAGE);

        UIHelper.hideSoftKeyboard(getDockActivity(),edtName);

        getMainActivity().showBottomTab(AppConstants.home);
        getLatestUpdates();
        getTaxData();
        printHashKey(getDockActivity());
        printKeyHash(getDockActivity());
        prefHelper.setFilterData(AppConstants.EMPTY);
        prefHelper.setOnlyToken(prefHelper.getSignUpUser().getToken() + "");



    }

    private void searchData() {
        serviceHelper.enqueueCall(webService.getHomeSearch(edtName.getText().toString() + ""), WebServiceConstants.HOME_SEARCH);
    }

    private void getTaxData() {
        serviceHelper.enqueueCall(webService.getTax(), WebServiceConstants.TAX_DATA);
    }

    public static void printHashKey(DockActivity dockActivity) {
        try {
            PackageInfo info = dockActivity.getPackageManager().getPackageInfo(dockActivity.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    private void setHomeItemBackgroung(String type) {

        if (type.equals(AppConstants.reservation)) {
            unSelectAll();

            llReservations.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtReservation.setTextColor(getResources().getColor(R.color.white));
            ivReservation.setImageResource(R.drawable.reservation2);

        } else if (type.equals(AppConstants.menu)) {
            unSelectAll();

            llMenu.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtMenu.setTextColor(getResources().getColor(R.color.white));
            ivMenu.setImageResource(R.drawable.menu);

        } else if (type.equals(AppConstants.event_calendar)) {
            unSelectAll();

            llMyevents.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtMyevents.setTextColor(getResources().getColor(R.color.white));
            ivMyevents.setImageResource(R.drawable.myevents);

        } else if (type.equals(AppConstants.competition)) {
            unSelectAll();

            llCompetition.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtCompetition.setTextColor(getResources().getColor(R.color.white));
            ivCompetition.setImageResource(R.drawable.competition2);
        } else if (type.equals(AppConstants.music)) {
            unSelectAll();

            llMusic.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtMusic.setTextColor(getResources().getColor(R.color.white));
            ivMusic.setImageResource(R.drawable.music);

        } else if (type.equals(AppConstants.barOrdering)) {
            unSelectAll();

            llBarOrdering.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtBarOrdering.setTextColor(getResources().getColor(R.color.white));
            ivBarOdering.setImageResource(R.drawable.barordering);

        } else if (type.equals(AppConstants.cavaliNights)) {
            unSelectAll();
            llCavalliNights.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtCavalliNights.setTextColor(getResources().getColor(R.color.white));
            ivCavalliNights.setImageResource(R.drawable.cavlinights);

        } else if (type.equals(AppConstants.membership)) {
            unSelectAll();
            llMembership.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtMembership.setTextColor(getResources().getColor(R.color.white));
            ivMembership.setImageResource(R.drawable.membership2);
        }
    }

    void unSelectAll() {
        llReservations.setBackgroundColor(getResources().getColor(R.color.transparent));
        llMenu.setBackgroundColor(getResources().getColor(R.color.transparent));
        llMyevents.setBackgroundColor(getResources().getColor(R.color.transparent));
        llCompetition.setBackgroundColor(getResources().getColor(R.color.transparent));
        llMusic.setBackgroundColor(getResources().getColor(R.color.transparent));
        llBarOrdering.setBackgroundColor(getResources().getColor(R.color.transparent));
        llCavalliNights.setBackgroundColor(getResources().getColor(R.color.transparent));
        llMembership.setBackgroundColor(getResources().getColor(R.color.transparent));

        ivReservation.setImageResource(R.drawable.reservation);
        ivMenu.setImageResource(R.drawable.menu2);
        ivMyevents.setImageResource(R.drawable.myevents2);
        ivCompetition.setImageResource(R.drawable.competition);
        ivMusic.setImageResource(R.drawable.music2);
        ivBarOdering.setImageResource(R.drawable.barordering2);
        ivCavalliNights.setImageResource(R.drawable.cavlinights2);
        ivMembership.setImageResource(R.drawable.membership);

        txtReservation.setTextColor(getResources().getColor(R.color.black));
        txtMenu.setTextColor(getResources().getColor(R.color.black));
        txtMyevents.setTextColor(getResources().getColor(R.color.black));
        txtCompetition.setTextColor(getResources().getColor(R.color.black));
        txtMusic.setTextColor(getResources().getColor(R.color.black));
        txtBarOrdering.setTextColor(getResources().getColor(R.color.black));
        txtCavalliNights.setTextColor(getResources().getColor(R.color.black));
        txtMembership.setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @OnClick({R.id.ll_reservations, R.id.ll_music, R.id.iv_search, R.id.titleMenuBtn, R.id.titleBtnProfile, R.id.ll_myevents, R.id.ll_menu, R.id.ll_competition, R.id.ll_bar_ordering, R.id.ll_cavalli_nights, R.id.ll_membership, R.id.ll_see_all, R.id.btn_search, R.id.edt_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_reservations:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    edtName.setText("");
                    setHomeItemBackgroung(AppConstants.reservation);
                    prefHelper.setStringPreference(AppConstants.BOOKING_DATE_KEY, null);
                    getDockActivity().replaceDockableFragment(ReserveNowfragment.newInstance(), "ReserveNowfragment");
                }
                break;

            case R.id.ll_music:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    edtName.setText("");
                    setHomeItemBackgroung(AppConstants.music);
                    getDockActivity().replaceDockableFragment(MusicFragment.newInstance(), "MusicFragment");
                }
                break;


            case R.id.ll_myevents:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    edtName.setText("");
                    setHomeItemBackgroung(AppConstants.event_calendar);
                    getDockActivity().replaceDockableFragment(EventsBookingFragment.newInstance(), "EventsBookingFragment");
                }
                break;

            case R.id.titleMenuBtn:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    edtName.setText("");
                    if (getMainActivity().sideMenuType.equals(SideMenuChooser.DRAWER.getValue()) && getMainActivity().getDrawerLayout() != null) {
                        if (getMainActivity().sideMenuDirection.equals(SideMenuDirection.LEFT.getValue())) {
                            getMainActivity().drawerLayout.openDrawer(Gravity.LEFT);
                        } else {
                            getMainActivity().drawerLayout.openDrawer(Gravity.RIGHT);
                        }
                    }
                }
                break;

            case R.id.titleBtnProfile:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    edtName.setText("");
                    getDockActivity().replaceDockableFragment(MyProfileFragment.newInstance(), "MyProfileFragment");
                }
                break;

            case R.id.ll_menu:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    edtName.setText("");
                    setHomeItemBackgroung(AppConstants.menu);
                    getDockActivity().replaceDockableFragment(MenuFragment.newInstance(), "MenuFragment");
                }
                break;

            case R.id.ll_competition:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    edtName.setText("");
                    setHomeItemBackgroung(AppConstants.competition);
                    getDockActivity().replaceDockableFragment(LatestCompetitionFragment.newInstance(AppConstants.home, AppConstants.fromHOME), "LatestCompetitionFragment");
                }
                break;

            case R.id.ll_bar_ordering:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    edtName.setText("");
                    setHomeItemBackgroung(AppConstants.barOrdering);
                    getDockActivity().replaceDockableFragment(BarOrderingFragment.newInstance(), "BarOrderingFragment");
                }
                break;

            case R.id.ll_cavalli_nights:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    edtName.setText("");
                    setHomeItemBackgroung(AppConstants.cavaliNights);
                    getDockActivity().replaceDockableFragment(CavalliNightsFragment.newInstance("ComingFromHome"), "CavalliNightsFragment");
                }
                break;

            case R.id.ll_membership:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    edtName.setText("");
                    setHomeItemBackgroung(AppConstants.membership);
                    getDockActivity().replaceDockableFragment(HomeMembershipFragment.newInstance(), "HomeMembershipFragment");
                }
                break;

            case R.id.ll_see_all:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    edtName.setText("");
                    getDockActivity().replaceDockableFragment(LatestUpdatesMusicFragment.newInstance(), "LatestUpdatesMusicFragment");
                }
                break;

            case R.id.btn_search:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    UIHelper.hideSoftKeyboard(getDockActivity(), getView());
                    //searchData();
                }
                break;

            case R.id.edt_name:
                 getDockActivity().replaceDockableFragment(SearchListingFragment.newInstance(), "SearchListingFragment");
                break;

            case R.id.iv_search:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    UIHelper.hideSoftKeyboard(getDockActivity(), getView());

                 /*   if (edtName.getText().toString().isEmpty() || edtName.getText().toString().trim().equals("")) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "please enter text to proceed");
                    } else {
                        getDockActivity().replaceDockableFragment(SearchListingFragment.newInstance(edtName.getText().toString()), "SearchListingFragment");
                    }*/

                    //searchData();
                }
                break;
        }
    }

    private void getLatestUpdates() {
        serviceHelper.enqueueCall(webService.LatestUpdates(), WebServiceConstants.LATEST_UPDATES);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.LATEST_UPDATES:
                edtName.setText("");
                homeList = (ArrayList<LatestUpdatesEntity>) result;
                setHomeData(homeList);
                Gson gson = new Gson();
                ArrayStringContainer = gson.toJson(homeList);
                prefHelper.setLatestUpdates(ArrayStringContainer);
                break;

            case WebServiceConstants.TAX_DATA:
                taxEntity = (TaxEntity) result;
                //prefHelper.setTaxData(((TaxEntity) result).getValue());
                prefHelper.setTaxData("0");
                break;

            case WebServiceConstants.HOME_SEARCH:

                entityHomeSearchList = (ArrayList<EntityHomeSearch>) result;

                if (entityHomeSearchList.size() != 0) {
                    Gson gsonSearch = new Gson();
                    ArrayStringContainerHomeSearch = gsonSearch.toJson(entityHomeSearchList);
                    prefHelper.setHomeSearch(ArrayStringContainerHomeSearch);
                    edtName.setText("");
                    getDockActivity().replaceDockableFragment(SearchListingFragment.newInstance(), "SearchListingFragment");
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getString(R.string.no_data_available));
                }
                break;

            case BANNERIMAGE:
                BannerEnt data=(BannerEnt)result;
                imageLoader.displayImage(data.getBanner(),ivHeader);
              //  Picasso.with(getDockActivity()).load(data.getBanner()).into(ivHeader);
                break;
        }
    }

    private void setHomeData(ArrayList<LatestUpdatesEntity> result) {

        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);

        if (userCollection.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(ArrayList<LatestUpdatesEntity> userCollection) {

        mAdapter = new AdapterRecyclerViewHome(this.homeList, getDockActivity(),getMainActivity(), false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}

