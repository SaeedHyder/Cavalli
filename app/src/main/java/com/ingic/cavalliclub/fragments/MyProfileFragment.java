package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.SideMenuChooser;
import com.ingic.cavalliclub.global.SideMenuDirection;
import com.ingic.cavalliclub.helpers.InternetHelper;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MyProfileFragment extends BaseFragment {
    @BindView(R.id.iv_picture_background)
    ImageView ivPictureBackground;
    @BindView(R.id.titleMenuBtn)
    ImageView titleMenuBtn;
    @BindView(R.id.relativeLayout2)
    RelativeLayout relativeLayout2;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.txt_userName)
    AnyTextView txtUserName;
    @BindView(R.id.txt_email_address)
    AnyTextView txtEmailAddress;
    @BindView(R.id.iv_reservation)
    ImageView ivReservation;
    @BindView(R.id.txt_reservation)
    AnyTextView txtReservation;
    @BindView(R.id.ll_reservations)
    LinearLayout llReservations;
    Unbinder unbinder;
    @BindView(R.id.iv_bar_orders)
    ImageView ivBarOrders;
    @BindView(R.id.txt_bar_orders)
    AnyTextView txtBarOrders;
    @BindView(R.id.ll_bar_orders)
    LinearLayout llBarOrders;
    @BindView(R.id.ll_guest_list_main)
    LinearLayout llGuestListMain;
    @BindView(R.id.iv_myevents)
    ImageView ivMyevents;
    @BindView(R.id.txt_myevents)
    AnyTextView txtMyevents;
    @BindView(R.id.ll_myevents)
    LinearLayout llMyevents;
    @BindView(R.id.iv_guest_list)
    ImageView ivGuestList;
    @BindView(R.id.txt_guest_list)
    AnyTextView txtGuestList;
    @BindView(R.id.ll_guest_list)
    LinearLayout llGuestList;
    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.txt_profile)
    AnyTextView txtProfile;
    @BindView(R.id.ll_profile)
    LinearLayout llProfile;
    @BindView(R.id.iv_membership)
    ImageView ivMembership;
    @BindView(R.id.txt_membership)
    AnyTextView txtMembership;
    @BindView(R.id.ll_membership)
    LinearLayout llMembership;
    @BindView(R.id.iv_competition)
    ImageView ivCompetition;
    @BindView(R.id.txt_competition)
    AnyTextView txtCompetition;
    @BindView(R.id.ll_competition)
    LinearLayout llCompetition;
    @BindView(R.id.iv_message)
    ImageView ivMessage;
    @BindView(R.id.txt_message)
    AnyTextView txtMessage;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;
    @BindView(R.id.iv_favorite)
    ImageView ivFavorite;
    @BindView(R.id.txt_favorite)
    AnyTextView txtFavorite;
    @BindView(R.id.ll_favorite)
    LinearLayout llFavorite;

    ImageLoader imageLoader;
    @BindView(R.id.iv_guest_list_main)
    ImageView ivGuestListMain;
    @BindView(R.id.txt_guest_list_main)
    AnyTextView txtGuestListMain;

    public static MyProfileFragment newInstance() {
        Bundle args = new Bundle();

        MyProfileFragment fragment = new MyProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageLoader = ImageLoader.getInstance();
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myprofile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().showBottomTab(AppConstants.home);

      /*  DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        ImageLoader.getInstance().displayImage("drawable://" + R.drawable.profile_image, imageView, options);*/
/*        imageLoader.displayImage("drawable://" + R.drawable.profile_image, imageView);
        imageLoader.displayImage("drawable://" + R.drawable.profile_image, ivPictureBackground);*/
        setProfileData();
    }

    private void setProfileData() {
        if (prefHelper.getUpdatedUser() != null) {
            if (prefHelper.getUpdatedUser().getImageUrl() != null)
                ImageLoader.getInstance().displayImage(prefHelper.getUpdatedUser().getImageUrl(), imageView,getMainActivity().getImageLoaderRoundCornerTransformation(6));
            if (prefHelper.getUpdatedUser().getImageUrl() != null)
                ImageLoader.getInstance().displayImage(prefHelper.getUpdatedUser().getImageUrl(), ivPictureBackground);
            if (prefHelper.getUpdatedUser().getUserName() != null)
                txtUserName.setText(prefHelper.getUpdatedUser().getUserName() + "");
            if (prefHelper.getUpdatedUser().getEmail() != null)
                txtEmailAddress.setText(prefHelper.getUpdatedUser().getEmail() + "");
        } else if (prefHelper.getSignUpUser() != null) {
            if (prefHelper.getSignUpUser().getImageUrl() != null)
                ImageLoader.getInstance().displayImage(prefHelper.getSignUpUser().getImageUrl(), imageView,getMainActivity().getImageLoaderRoundCornerTransformation(6));
            if (prefHelper.getSignUpUser().getImageUrl() != null)
                ImageLoader.getInstance().displayImage(prefHelper.getSignUpUser().getImageUrl(), ivPictureBackground);
            if (prefHelper.getSignUpUser().getUserName() != null)
                txtUserName.setText(prefHelper.getSignUpUser().getUserName() + "");
            if (prefHelper.getSignUpUser().getEmail() != null)
                txtEmailAddress.setText(prefHelper.getSignUpUser().getEmail() + "");
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    private void setProfileItemBackgroung(String type) {

        if (type.equals(AppConstants.barOrders)) {
            unSelectAll();

            llBarOrders.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtBarOrders.setTextColor(getResources().getColor(R.color.white));
            ivBarOrders.setImageResource(R.drawable.barorder2);

        } else if (type.equals(AppConstants.myEvents)) {
            unSelectAll();

            llMyevents.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtMyevents.setTextColor(getResources().getColor(R.color.white));
            ivMyevents.setImageResource(R.drawable.myevents);

        } else if (type.equals(AppConstants.guestList)) {
            unSelectAll();

            llGuestList.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtGuestList.setTextColor(getResources().getColor(R.color.white));
            ivGuestList.setImageResource(R.drawable.guestlist);

        } else if (type.equals(AppConstants.guestListmain)) {
            unSelectAll();

            llGuestListMain.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtGuestListMain.setTextColor(getResources().getColor(R.color.white));
            ivGuestListMain.setImageResource(R.drawable.guestlist);

        } else if (type.equals(AppConstants.profile)) {
            unSelectAll();

            llProfile.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtProfile.setTextColor(getResources().getColor(R.color.white));
            ivProfile.setImageResource(R.drawable.profile3);
        } else if (type.equals(AppConstants.membership)) {
            unSelectAll();

            llMembership.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtMembership.setTextColor(getResources().getColor(R.color.white));
            ivMembership.setImageResource(R.drawable.membership2);

        } else if (type.equals(AppConstants.competition)) {
            unSelectAll();

            llCompetition.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtCompetition.setTextColor(getResources().getColor(R.color.white));
            ivCompetition.setImageResource(R.drawable.competition2);

        } else if (type.equals(AppConstants.messages)) {
            unSelectAll();
            llMessage.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtMessage.setTextColor(getResources().getColor(R.color.white));
            ivMessage.setImageResource(R.drawable.message2);

        } else if (type.equals(AppConstants.favorite)) {
            unSelectAll();
            llFavorite.setBackgroundColor(getResources().getColor(R.color.app_golden));
            txtFavorite.setTextColor(getResources().getColor(R.color.white));
            ivFavorite.setImageResource(R.drawable.fav2);
        }
    }

    void unSelectAll() {
        llReservations.setBackgroundColor(getResources().getColor(R.color.transparent));
        llBarOrders.setBackgroundColor(getResources().getColor(R.color.transparent));
        llMyevents.setBackgroundColor(getResources().getColor(R.color.transparent));
        llGuestList.setBackgroundColor(getResources().getColor(R.color.transparent));
        llGuestListMain.setBackgroundColor(getResources().getColor(R.color.transparent));
        llProfile.setBackgroundColor(getResources().getColor(R.color.transparent));
        llMembership.setBackgroundColor(getResources().getColor(R.color.transparent));
        llMessage.setBackgroundColor(getResources().getColor(R.color.transparent));
        llFavorite.setBackgroundColor(getResources().getColor(R.color.transparent));
        llCompetition.setBackgroundColor(getResources().getColor(R.color.transparent));

        ivReservation.setImageResource(R.drawable.reservation);
        ivBarOrders.setImageResource(R.drawable.barorder);
        ivMyevents.setImageResource(R.drawable.myevents2);
        ivGuestList.setImageResource(R.drawable.guestlist2);
        ivGuestListMain.setImageResource(R.drawable.guestlist2);
        ivProfile.setImageResource(R.drawable.profile2);
        ivMembership.setImageResource(R.drawable.membership);
        ivMessage.setImageResource(R.drawable.message);
        ivFavorite.setImageResource(R.drawable.fav);
        ivCompetition.setImageResource(R.drawable.competition);

        txtReservation.setTextColor(getResources().getColor(R.color.black));
        txtBarOrders.setTextColor(getResources().getColor(R.color.black));
        txtMyevents.setTextColor(getResources().getColor(R.color.black));
        txtGuestList.setTextColor(getResources().getColor(R.color.black));
        txtGuestListMain.setTextColor(getResources().getColor(R.color.black));
        txtProfile.setTextColor(getResources().getColor(R.color.black));
        txtMembership.setTextColor(getResources().getColor(R.color.black));
        txtMessage.setTextColor(getResources().getColor(R.color.black));
        txtFavorite.setTextColor(getResources().getColor(R.color.black));
        txtCompetition.setTextColor(getResources().getColor(R.color.black));
    }


    @OnClick({R.id.ll_bar_orders, R.id.ll_myevents, R.id.ll_reservations, R.id.ll_guest_list, R.id.ll_profile, R.id.ll_membership, R.id.ll_competition, R.id.ll_message, R.id.ll_favorite, R.id.titleMenuBtn, R.id.ll_guest_list_main})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.titleMenuBtn:

                if (getMainActivity().sideMenuType.equals(SideMenuChooser.DRAWER.getValue()) && getMainActivity().getDrawerLayout() != null) {
                    if (getMainActivity().sideMenuDirection.equals(SideMenuDirection.LEFT.getValue())) {
                        getMainActivity().drawerLayout.openDrawer(Gravity.LEFT);
                    } else {
                        getMainActivity().drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                }
                break;

            case R.id.ll_bar_orders:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    setProfileItemBackgroung(AppConstants.barOrders);
                    getDockActivity().replaceDockableFragment(PendingOrderFragment.newInstance(), "PendingOrderFragment");
                }
                break;
            case R.id.ll_myevents:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    setProfileItemBackgroung(AppConstants.myEvents);
                    getDockActivity().replaceDockableFragment(UpcomingEventsFragment.newInstance("ComingFromMyProfile"), "UpcomingEventsFragment");
                }
                break;
            case R.id.ll_guest_list:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    setProfileItemBackgroung(AppConstants.guestList);
                    getDockActivity().replaceDockableFragment(InviteGuestUpcomingProfileFragment.newInstance(), "InviteGuestFragment");
                }
                break;
            case R.id.ll_profile:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    setProfileItemBackgroung(AppConstants.profile);
                    getDockActivity().replaceDockableFragment(EditProfilefragment.newInstance(), "EditProfilefragment");
                }
                break;
            case R.id.ll_membership:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    setProfileItemBackgroung(AppConstants.membership);
                    getDockActivity().replaceDockableFragment(CurrentMembershipFragment.newInstance(), "CurrentMembershipFragment");
                }
                break;
            case R.id.ll_competition:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    setProfileItemBackgroung(AppConstants.competition);
                    getDockActivity().replaceDockableFragment(LatestCompetitionFragment.newInstance(AppConstants.home, AppConstants.fromPROFILE), "LatestCompetitionFragment");
                }
                break;
            case R.id.ll_message:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    setProfileItemBackgroung(AppConstants.messages);
                    getDockActivity().replaceDockableFragment(MessagesFragment.newInstance(), "MessagesFragment");
                }
                break;
            case R.id.ll_favorite:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    getMainActivity().notImplemented();
                }
                break;
            case R.id.ll_reservations:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    setProfileItemBackgroung(AppConstants.reservation);
                    prefHelper.setStringPreference(AppConstants.BOOKING_DATE_KEY, null);
                    getDockActivity().replaceDockableFragment(ReserveNowfragment.newInstance(), "ReserveNowfragment");
                }
                break;
            case R.id.ll_guest_list_main:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    setProfileItemBackgroung(AppConstants.guestListmain);
                    getDockActivity().replaceDockableFragment(GuestListListingFragment.newInstance(), "GuestListListingFragment");
                }
                break;
        }
    }
}
