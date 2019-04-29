package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.iid.FirebaseInstanceId;
import com.application.cavalliclub.R;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.DialogHelper;
import com.application.cavalliclub.helpers.InternetHelper;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;

public class SideMenuFragment extends BaseFragment {

    @BindView(R.id.tv_event_calendar)
    AnyTextView tvEventCalendar;
    @BindView(R.id.ll_event_calendar)
    LinearLayout llEventCalendar;
    @BindView(R.id.tv_cavalli_social)
    AnyTextView tvCavalliSocial;
    @BindView(R.id.ll_cavalli_social)
    LinearLayout llCavalliSocial;
    @BindView(R.id.tv_package_information)
    AnyTextView tvPackageInformation;
    @BindView(R.id.ll_package_information)
    LinearLayout llPackageInformation;
    @BindView(R.id.tv_international_request)
    AnyTextView tvInternationalRequest;
    @BindView(R.id.ll_international_request)
    LinearLayout llInternationalRequest;
    @BindView(R.id.tv_gallery)
    AnyTextView tvGallery;
    @BindView(R.id.ll_gallery)
    LinearLayout llGallery;
    @BindView(R.id.tv_about_us)
    AnyTextView tvAboutUs;
    @BindView(R.id.ll_about_us)
    LinearLayout llAboutUs;
    @BindView(R.id.tv_logout)
    AnyTextView tvLogout;
    @BindView(R.id.ll_logout)
    LinearLayout llLogout;

    @BindView(R.id.tv_privacy_policy)
    AnyTextView tvPrivacyPolicy;
    @BindView(R.id.ll_privacy_policy)
    LinearLayout llPrivacyPolicy;

    @BindView(R.id.tv_terms_condition)
    AnyTextView tvTermsCondition;
    @BindView(R.id.ll_terms_condition)
    LinearLayout llTermsCondition;

    Unbinder unbinder;

    public static SideMenuFragment newInstance() {
        return new SideMenuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sidemenu, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getMainActivity().realm.close();
    }


    @OnClick({R.id.ll_event_calendar, R.id.ll_cavalli_social, R.id.ll_package_information, R.id.ll_international_request, R.id.ll_gallery, R.id.ll_about_us, R.id.ll_privacy_policy, R.id.ll_terms_condition, R.id.ll_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_event_calendar:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    getMainActivity().closeDrawer();
                    getDockActivity().replaceDockableFragment(EventsBookingFragment.newInstance(), "EventsBookingFragment");
                }
                break;
            case R.id.ll_cavalli_social:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    getMainActivity().closeDrawer();
                    getDockActivity().replaceDockableFragment(CavalliSocialFragment.newInstance(), "CavalliSocialFragment");
                }
                break;
            case R.id.ll_package_information:
                //removed from project
                getMainActivity().closeDrawer();
                getMainActivity().notImplemented();
                //getDockActivity().replaceDockableFragment(PackageDetailFragment.newInstance(),"PackageDetailFragment");
                break;
            case R.id.ll_international_request:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    getMainActivity().closeDrawer();
                    getDockActivity().replaceDockableFragment(InternationalRequestFragment.newInstance(), "InternationalRequestFragment");
                }
                break;
            case R.id.ll_gallery:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    getMainActivity().closeDrawer();
                    getDockActivity().replaceDockableFragment(GalleryFragment.newInstance(), "GalleryFragment");
                }
                break;
            case R.id.ll_about_us:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    getMainActivity().closeDrawer();
                    getDockActivity().replaceDockableFragment(AboutUsFragment.newInstance(), "AboutUsFragment");
                }
                break;
            case R.id.ll_privacy_policy:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    getMainActivity().closeDrawer();
                    getDockActivity().replaceDockableFragment(PrivacyPolicyFragment.newInstance(), "PrivacyPolicyFragment");
                }
                break;
            case R.id.ll_terms_condition:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    getMainActivity().closeDrawer();
                    getDockActivity().replaceDockableFragment(TermsAndConditionFragment.newInstance(), "TermsAndConditionFragment");
                }
                break;
            case R.id.ll_logout:
                final DialogHelper logout = new DialogHelper(getDockActivity());
                logout.initlogout(R.layout.logout_dialoge, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getMainActivity().closeDrawer();

                        logoutToken();

                        logout.hideDialog();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout.hideDialog();
                    }
                });

                logout.showDialog();

                break;
        }
    }

    private void logoutToken() {
        serviceHelper.enqueueCall(webService.logoutToken(FirebaseInstanceId.getInstance().getToken() + "", prefHelper.getSignUpUser().getToken() + ""), WebServiceConstants.LOGOUT_TOKEN);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.LOGOUT_TOKEN:

                prefHelper.setLoginStatus(false);
                prefHelper.putUpdatedUser(null);
                prefHelper.putSignupUser(null);
                getMainActivity().popBackStackTillEntry(0);

                Realm realm;
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();
                getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");

                break;
        }
    }
}
