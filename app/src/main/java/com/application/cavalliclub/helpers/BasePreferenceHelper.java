package com.application.cavalliclub.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.application.cavalliclub.entities.EntityLiveMusic;
import com.application.cavalliclub.entities.SignUpEntity;
import com.application.cavalliclub.entities.UpdateProfileEntity;
import com.application.cavalliclub.retrofit.GsonFactory;


public class BasePreferenceHelper extends PreferenceHelper {

    private Context context;

    protected static final String KEY_LOGIN_STATUS = "islogin";
    protected static final String KEY_SOCIAL_LOGIN_STATUS = "isSociallogin";

    private static final String FILENAME = "preferences";

    protected static final String Firebase_TOKEN = "Firebasetoken";

    protected static final String NotificationCount = "NotificationCount";

    protected static final String KEY_SIGNUP_USER = "KEY_SIGNUP_USER";
    protected static final String KEY_UPDATED_USER = "KEY_UPDATED_USER";
    protected static final String LATEST_UPDATES = "LATEST_UPDATES";
    protected static final String GALLERY_UPDATES = "GALLERY_UPDATES";
    protected static final String MEMBER_LIST = "MEMBER_LIST";
    protected static final String FILTER_DATA = "FILTER_DATA";
    protected static final String DRINK_TYPE = "DRINK_TYPE";
    protected static final String TAX_DATA = "TAX_DATA";
    protected static final String TOKEN = "TOKEN";
    protected static final String TIP_VALUE = "TIP_VALUE";
    protected static final String TOTAL_LIST_ID = "TOTAL_LIST_ID";
    protected static final String FLAG = "FLAG";
    protected static final String CODE = "CODE";
    protected static final String PHONECODE = "PHONECODE";
    protected static final String MUSIC_UPDATES = "MUSIC_UPDATES";
    protected static final String LIVE_MUSIC = "LIVE_MUSIC";
    protected static final String PUSH_TOGGLE = "PUSH_TOGGLE";
    protected static final String HOME_SEARCH = "HOME_SEARCH";
    protected static final String SEE_ALL_PRODUCTS = "SEE_ALL_PRODUCTS";
    protected static final String COUNTRY_CODE = "COUNTRY_CODE";
    protected static final String DAYS_GALLERY = "DAYS_GALLERY";
    protected static final String DAY = "DAY";


    public BasePreferenceHelper(Context c) {
        this.context = c;
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
    }

    public void setLoginStatus( boolean isLogin ) {
        putBooleanPreference( context, FILENAME, KEY_LOGIN_STATUS, isLogin );
    }

    public boolean isLogin() {
        return getBooleanPreference(context, FILENAME, KEY_LOGIN_STATUS);
    }

    public void setSocialLoginStatus( boolean isLogin ) {
        putBooleanPreference( context, FILENAME, KEY_SOCIAL_LOGIN_STATUS, isLogin );
    }

    public boolean isSocialLogin() {
        return getBooleanPreference(context, FILENAME, KEY_SOCIAL_LOGIN_STATUS);
    }

    public String getFirebase_TOKEN() {
        return getStringPreference(context, FILENAME, Firebase_TOKEN);
    }

    public void setFirebase_TOKEN(String _token) {
        putStringPreference(context, FILENAME, Firebase_TOKEN, _token);
    }
    public int getNotificationCount() {
        return getIntegerPreference(context, FILENAME, NotificationCount);
    }

    public void setNotificationCount(int count) {
        putIntegerPreference(context, FILENAME, NotificationCount, count);
    }

    public SignUpEntity getSignUpUser() {
        return GsonFactory.getConfiguredGson().fromJson(
                getStringPreference(context, FILENAME, KEY_SIGNUP_USER), SignUpEntity.class);
    }

    public void putSignupUser(SignUpEntity signupuser) {
        putStringPreference(context, FILENAME, KEY_SIGNUP_USER, GsonFactory
                .getConfiguredGson().toJson(signupuser));
    }

    public void setCountryCode(String Country) {
        putStringPreference(context, FILENAME, COUNTRY_CODE, Country);
    }

    public String getCountryCode() {
        return getStringPreference(context, FILENAME, COUNTRY_CODE);
    }

    public void setDaysGallery(String Days) {
        putStringPreference(context, FILENAME, DAYS_GALLERY, Days);
    }

    public String getDaysGallery() {
        return getStringPreference(context, FILENAME, DAYS_GALLERY);
    }

    public EntityLiveMusic getLiveMusic() {
        return GsonFactory.getConfiguredGson().fromJson(
                getStringPreference(context, FILENAME, LIVE_MUSIC), EntityLiveMusic.class);
    }

    public void setLiveMusic(EntityLiveMusic liveMusic) {
        putStringPreference(context, FILENAME, LIVE_MUSIC, GsonFactory
                .getConfiguredGson().toJson(liveMusic));
    }

    public UpdateProfileEntity getUpdatedUser() {
        return GsonFactory.getConfiguredGson().fromJson(
                getStringPreference(context, FILENAME, KEY_UPDATED_USER), UpdateProfileEntity.class);
    }

    public void putUpdatedUser(UpdateProfileEntity updateProfileEntity) {
        putStringPreference(context, FILENAME, KEY_UPDATED_USER, GsonFactory
                .getConfiguredGson().toJson(updateProfileEntity));
    }


    public String getLatestUpdates() {
        return getStringPreference(context, FILENAME, LATEST_UPDATES);
    }

    public void setLatestUpdates(String latestUpdates) {
        putStringPreference(context, FILENAME, LATEST_UPDATES, latestUpdates);
    }

    public String getGalleryPager() {
        return getStringPreference(context, FILENAME, GALLERY_UPDATES);
    }

    public void setgalleryPager(String gallerypager) {
        putStringPreference(context, FILENAME, GALLERY_UPDATES, gallerypager);
    }

    public String getHomeSearch() {
        return getStringPreference(context, FILENAME, HOME_SEARCH);
    }

    public void setHomeSearch(String homeSearch) {
        putStringPreference(context, FILENAME, HOME_SEARCH, homeSearch);
    }

    public String getSeeAllProducts() {
        return getStringPreference(context, FILENAME, SEE_ALL_PRODUCTS);
    }

    public void setSeeAllProducts(String seeAllProducts) {
        putStringPreference(context, FILENAME, SEE_ALL_PRODUCTS, seeAllProducts);
    }

    public String getGuestMemberList() {
        return getStringPreference(context, FILENAME, MEMBER_LIST);
    }

    public void setGuestMemebrList(String guestMemebrList) {
        putStringPreference(context, FILENAME, MEMBER_LIST, guestMemebrList);
    }

    public String getMusicUpdates() {
        return getStringPreference(context, FILENAME, MUSIC_UPDATES);
    }

    public void setMusicUpdates(String musicUpdates) {
        putStringPreference(context, FILENAME, MUSIC_UPDATES, musicUpdates);
    }

    public String getFilterData() {
        return getStringPreference(context, FILENAME, FILTER_DATA);
    }

    public void setFilterData(String filterData) {
        putStringPreference(context, FILENAME, FILTER_DATA, filterData);
    }

    public String getDrinkType() {
        return getStringPreference(context, FILENAME, DRINK_TYPE);
    }

    public void setDrinkType(String drinkType) {
        putStringPreference(context, FILENAME, DRINK_TYPE, drinkType);
    }

    public String getTaxData() {
        return getStringPreference(context, FILENAME, TAX_DATA);
    }

    public void setTaxData(String taxData) {
        putStringPreference(context, FILENAME, TAX_DATA, taxData);
    }

    public String getOnlyToken() {
        return getStringPreference(context, FILENAME, TOKEN);
    }

    public void setOnlyToken(String token) {
        putStringPreference(context, FILENAME, TOKEN, token);
    }

    public String getTipValue() {
        return getStringPreference(context, FILENAME, TIP_VALUE);
    }

    public void setTipValue(String tipValue) {
        putStringPreference(context, FILENAME, TIP_VALUE, tipValue);
    }

    public String getDay() {
        return getStringPreference(context, FILENAME, DAY);
    }

    public void setDay(String day) {
        putStringPreference(context, FILENAME, DAY, day);
    }

    public String getTotalListId() {
        return getStringPreference(context, FILENAME, TOTAL_LIST_ID);
    }

    public void setTotalListId(String tipValue) {
        putStringPreference(context, FILENAME, TOTAL_LIST_ID, tipValue);
    }

    public String getFlag() {
        return getStringPreference(context, FILENAME, FLAG);
    }

    public void setFlag(String flag) {
        putStringPreference(context, FILENAME, FLAG, flag);
    }

    public String getCode() {
        return getStringPreference(context, FILENAME, CODE);
    }

    public void setCode(String code) {
        putStringPreference(context, FILENAME, CODE, code);
    }
    public String getPhoneCode() {
        return getStringPreference(context, FILENAME, PHONECODE);
    }

    public void setPhoneCode(String code) {
        putStringPreference(context, FILENAME, PHONECODE, code);
    }


    public int getPushValue() {
        return getIntegerPreference(context, FILENAME, PUSH_TOGGLE);
    }

    public void setPushValue(int pushValue) {
        putIntegerPreference(context, FILENAME, PUSH_TOGGLE, pushValue);
    }

    public void setStringPreference(String key, String value) {
        putStringPreference(context, FILENAME, key, value);
    }

    public String getStringPreference(String key) {
        return getStringPreference(context, FILENAME, key);
    }


}
