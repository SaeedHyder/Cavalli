package com.application.cavalliclub.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.AddCartEntity;
import com.application.cavalliclub.fragments.AddToCartFragment;
import com.application.cavalliclub.fragments.EventsBookingFragment;
import com.application.cavalliclub.fragments.FilterMenuFragment;
import com.application.cavalliclub.fragments.HomeFragment;
import com.application.cavalliclub.fragments.LoginFragment;
import com.application.cavalliclub.fragments.NotificationsFragment;
import com.application.cavalliclub.fragments.SettingFragment;
import com.application.cavalliclub.fragments.SideMenuFragment;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.SideMenuChooser;
import com.application.cavalliclub.global.SideMenuDirection;
import com.application.cavalliclub.helpers.DialogHelper;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.interfaces.RecyclerViewItemListener;
import com.application.cavalliclub.residemenu.ResideMenu;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenFile;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.FileChooserListener;
import com.kbeanie.imagechooser.api.FileChooserManager;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

import static com.application.cavalliclub.global.AppConstants.Block_User;
import static com.application.cavalliclub.global.AppConstants.Delete_User;


public class MainActivity extends DockActivity implements OnClickListener, ImageChooserListener, FileChooserListener {
    public TitleBar titleBar;
    @BindView(R.id.sideMneuFragmentContainer)
    public FrameLayout sideMneuFragmentContainer;
    @BindView(R.id.filterMneuFragmentContainer)
    public FrameLayout filterMneuFragmentContainer;
    @BindView(R.id.header_main)
    TitleBar header_main;
    @BindView(R.id.mainFrameLayout)
    FrameLayout mainFrameLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.txt_home)
    AnyTextView txtHome;
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.iv_events_booking)
    ImageView ivEventsBooking;
    @BindView(R.id.txt_events_booking)
    AnyTextView txtEventsBooking;
    @BindView(R.id.ll_events_booking)
    LinearLayout llEventsBooking;
    @BindView(R.id.iv_settings)
    ImageView ivSettings;
    @BindView(R.id.txt_settings)
    AnyTextView txtSettings;
    @BindView(R.id.ll_settings)
    LinearLayout llSettings;
    @BindView(R.id.iv_notifications)
    ImageView ivNotifications;
    @BindView(R.id.txt_notifications)
    AnyTextView txtNotifications;
    @BindView(R.id.ll_notifications)
    LinearLayout llNotifications;
    @BindView(R.id.ll_bottom_tab)
    LinearLayout llBottomTab;
    private MainActivity mContext;
    private boolean loading;
    AddCartEntity addCartEntity;
    private ResideMenu resideMenu;

    private float lastTranslate = 0.0f;

    public String sideMenuType;
    public String sideMenuDirection;
    public String filterMenuType;
    public String filterMenuDirection;
    public RecyclerViewItemListener recyclerViewItemListener;

    private ImageChooserManager imageChooserManager;
    protected BroadcastReceiver broadcastReceiver;
    private String filePath;

    private int chooserType;
    private final static String TAG = "ICA";

    private boolean isActivityResultOver = false;

    private String originalFilePath;
    private String thumbnailFilePath;
    private String thumbnailSmallFilePath;

    private boolean mIsFromPush;

    ImageSetter imageSetter;
    private FileChooserManager fm;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_dock);
        ButterKnife.bind(this);
        titleBar = header_main;
        // setBehindContentView(R.layout.fragment_frame);
        mContext = this;
        //  Log.i("Screen Density", ScreenHelper.getDensity(this) + "");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        sideMenuType = SideMenuChooser.DRAWER.getValue();
        sideMenuDirection = SideMenuDirection.LEFT.getValue();
        filterMenuType = SideMenuChooser.DRAWER.getValue();
        filterMenuDirection = SideMenuDirection.RIGHT.getValue();
        AppEventsLogger.activateApp(this);
        settingSideMenu(sideMenuType, sideMenuDirection);
        settingFilterMenu(filterMenuType, filterMenuDirection);
        onNotificationReceived();
        titleBar.setMenuButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sideMenuType.equals(SideMenuChooser.DRAWER.getValue()) && getDrawerLayout() != null) {
                    if (sideMenuDirection.equals(SideMenuDirection.LEFT.getValue())) {
                        drawerLayout.openDrawer(Gravity.LEFT);
                    } else {
                        drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                } else {
                    resideMenu.openMenu(sideMenuDirection);
                }
            }
        });

        titleBar.setFilterButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        titleBar.setBackButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (loading) {
                    UIHelper.showLongToastInCenter(getApplicationContext(),
                            R.string.message_wait);
                } else {

                    popFragment();
                    UIHelper.hideSoftKeyboard(getApplicationContext(),
                            titleBar);
                }
            }
        });

        titleBar.setNotificationButtonListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceDockableFragment(NotificationsFragment.newInstance(), "NotificationsFragment");
            }
        });

        titleBar.setCartButtonListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addCartEntity = new AddCartEntity();

                //if (addCartEntity.getId() != null) {
                replaceDockableFragment(AddToCartFragment.newInstance(), "AddToCartFragment");
                /*} else {
                    UIHelper.showShortToastInCenter(getApplicationContext(), "Please select item to proceed.");
                }*/
            }
        });




        initFragment();

        if (prefHelper != null) {
            if (prefHelper.getPushValue() == (AppConstants.PUSH_NOTIFICATION_ON)) {
                prefHelper.setPushValue(AppConstants.PUSH_NOTIFICATION_ON);
            } else if (prefHelper.getPushValue() == (AppConstants.PUSH_NOTIFICATION_OFF)) {
                prefHelper.setPushValue(AppConstants.PUSH_NOTIFICATION_OFF);
            } else {
                prefHelper.setPushValue(AppConstants.PUSH_NOTIFICATION_ON);
            }
        }
        if (prefHelper != null) {
            prefHelper.setFlag(AppConstants.FLAG_0);
        }
    }


    private void settingFilterMenu(String type, String direction) {

        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams((int) getResources().getDimension(R.dimen.x250), (int) DrawerLayout.LayoutParams.MATCH_PARENT);

        params.gravity = Gravity.RIGHT;
        filterMneuFragmentContainer.setLayoutParams(params);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        filterMenuFragment = FilterMenuFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.filterMneuFragmentContainer, filterMenuFragment).commit();

        drawerLayout.closeDrawers();
    }

    public View getDrawerView() {
        return getLayoutInflater().inflate(getSideMenuFrameLayoutId(), null);
    }

    private void settingSideMenu(String type, String direction) {

        if (type.equals(SideMenuChooser.DRAWER.getValue())) {


            DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams((int) getResources().getDimension(R.dimen.x250), (int) DrawerLayout.LayoutParams.MATCH_PARENT);


            if (direction.equals(SideMenuDirection.LEFT.getValue())) {
                params.gravity = Gravity.LEFT;
                sideMneuFragmentContainer.setLayoutParams(params);
            } else {
                params.gravity = Gravity.RIGHT;
                sideMneuFragmentContainer.setLayoutParams(params);
            }
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

            sideMenuFragment = SideMenuFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(getSideMenuFrameLayoutId(), sideMenuFragment).commit();

            drawerLayout.closeDrawers();
        } else {
            resideMenu = new ResideMenu(this);
            resideMenu.attachToActivity(this);
            resideMenu.setMenuListener(getMenuListener());
            resideMenu.setScaleValue(0.52f);
            resideMenu.closeMenu();
            setMenuItemDirection(direction);
        }
    }

    private void setMenuItemDirection(String direction) {

        if (direction.equals(SideMenuDirection.LEFT.getValue())) {

            SideMenuFragment leftSideMenuFragment = SideMenuFragment.newInstance();
            resideMenu.addMenuItem(leftSideMenuFragment, "LeftSideMenuFragment", direction);

        } else if (direction.equals(SideMenuDirection.RIGHT.getValue())) {

            SideMenuFragment rightSideMenuFragment = SideMenuFragment.newInstance();
            resideMenu.addMenuItem(rightSideMenuFragment, "RightSideMenuFragment", direction);
        }
    }

    private int getSideMenuFrameLayoutId() {
        return R.id.sideMneuFragmentContainer;
    }


    public void initFragment() {
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        if (prefHelper.isLogin()) {
            replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
        } else {
            replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String Message = bundle.getString("message");

            if (prefHelper.isLogin()) {
                if (Message != null && !Message.equals("") && !Message.isEmpty() && !Message.equals("null")) {
                    replaceDockableFragment(NotificationsFragment.newInstance(), "NotificationsFragment");
                }

            }
        }
    }

    private void onNotificationReceived() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
              if (intent.getAction().equals(AppConstants.PUSH_NOTIFICATION)) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        String Type = bundle.getString("type");

                      if (Type != null && Type.equals(Delete_User)) {
                            final DialogHelper dialogHelper = new DialogHelper(getDockActivity());
                            dialogHelper.alertDialoge(R.layout.alert_dialoge, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogHelper.hideDialog();
                                }
                            }, getString(R.string.account_deleted_by_admin));

                            dialogHelper.showDialog();
                            prefHelper.setLoginStatus(false);
                            getDockActivity().popBackStackTillEntry(0);
                            replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
                        }
                    else if(Type != null && Type.equals(Block_User)){
                          final DialogHelper dialogHelper = new DialogHelper(getDockActivity());
                          dialogHelper.alertDialoge(R.layout.alert_dialoge, new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  dialogHelper.hideDialog();
                              }
                          }, getDockActivity().getResources().getString(R.string.blocked_by_admin));

                          dialogHelper.showDialog();
                          prefHelper.setLoginStatus(false);
                          getDockActivity().popBackStackTillEntry(0);
                          replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
                      }

                    }

                }
            }

        };
    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null) {
                    BaseFragment currFrag = (BaseFragment) manager.findFragmentById(getDockFrameLayoutId());
                    if (currFrag != null) {
                        currFrag.fragmentResume();
                    }
                }
            }
        };
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.PUSH_NOTIFICATION));
    }

    /*    @Override
    public void onLoadingStarted() {

        if (mainFrameLayout != null) {
            mainFrameLayout.setVisibility(View.VISIBLE);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            loading = true;
        }
    }

    @Override
    public void onLoadingFinished() {
        mainFrameLayout.setVisibility(View.VISIBLE);

        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        loading = false;

    }*/

    @Override
    public void onProgressUpdated(int percentLoaded) {

    }

    @Override
    public int getDockFrameLayoutId() {
        return R.id.mainFrameLayout;
    }

    @Override
    public void onMenuItemActionCalled(int actionId, String data) {
    }

    @Override
    public void setSubHeading(String subHeadText) {
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public void hideHeaderButtons(boolean leftBtn, boolean rightBtn) {
    }

    @Override
    public void onBackPressed() {
        if (loading) {
            UIHelper.showLongToastInCenter(getApplicationContext(),
                    R.string.message_wait);
        } else
            super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
    }


    public void notImplemented() {
        UIHelper.showLongToastInCenter(this, "Will be implemented in future version");
    }

    public void showBottomTab(String type) {
        setBottomTabStyle(type);
        llBottomTab.setVisibility(View.VISIBLE);

    }

    public void hideBottomTab() {
        llBottomTab.setVisibility(View.GONE);
    }

    private void setBottomTabStyle(String type) {

        if (type.equals(AppConstants.home)) {
            ivHome.setImageResource(R.drawable.home);
            ivEventsBooking.setImageResource(R.drawable.calender);
            ivSettings.setImageResource(R.drawable.settings);
            ivNotifications.setImageResource(R.drawable.notification);

            llHome.setBackgroundColor(getResources().getColor(R.color.app_golden));
            llEventsBooking.setBackgroundColor(getResources().getColor(R.color.transparent));
            llSettings.setBackgroundColor(getResources().getColor(R.color.transparent));
            llNotifications.setBackgroundColor(getResources().getColor(R.color.transparent));
        } else if (type.equals(AppConstants.eventsBooking)) {
            ivHome.setImageResource(R.drawable.home2);
            ivEventsBooking.setImageResource(R.drawable.calender2);
            ivSettings.setImageResource(R.drawable.settings);
            ivNotifications.setImageResource(R.drawable.notification);

            llHome.setBackgroundColor(getResources().getColor(R.color.transparent));
            llEventsBooking.setBackgroundColor(getResources().getColor(R.color.app_golden));
            llSettings.setBackgroundColor(getResources().getColor(R.color.transparent));
            llNotifications.setBackgroundColor(getResources().getColor(R.color.transparent));

        } else if (type.equals(AppConstants.setting)) {
            ivHome.setImageResource(R.drawable.home2);
            ivEventsBooking.setImageResource(R.drawable.calender);
            ivSettings.setImageResource(R.drawable.settings2);
            ivNotifications.setImageResource(R.drawable.notification);

            llHome.setBackgroundColor(getResources().getColor(R.color.transparent));
            llEventsBooking.setBackgroundColor(getResources().getColor(R.color.transparent));
            llSettings.setBackgroundColor(getResources().getColor(R.color.app_golden));
            llNotifications.setBackgroundColor(getResources().getColor(R.color.transparent));

        } else if (type.equals(AppConstants.notifications)) {
            ivHome.setImageResource(R.drawable.home2);
            ivEventsBooking.setImageResource(R.drawable.calender);
            ivSettings.setImageResource(R.drawable.settings);
            ivNotifications.setImageResource(R.drawable.notification2);

            llHome.setBackgroundColor(getResources().getColor(R.color.transparent));
            llEventsBooking.setBackgroundColor(getResources().getColor(R.color.transparent));
            llSettings.setBackgroundColor(getResources().getColor(R.color.transparent));
            llNotifications.setBackgroundColor(getResources().getColor(R.color.app_golden));

        }

    }

    @OnClick({R.id.ll_home, R.id.ll_events_booking, R.id.ll_settings, R.id.ll_notifications, R.id.ll_bottom_tab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                txtHome.setSelected(true);
                setBottomTabStyle(AppConstants.home);
                popBackStackTillEntry(0);
                replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                break;
            case R.id.ll_events_booking:
                txtEventsBooking.setSelected(true);
                setBottomTabStyle(AppConstants.eventsBooking);
                replaceDockableFragment(EventsBookingFragment.newInstance(), "EventsBookingFragment");
                break;
            case R.id.ll_settings:
                txtEventsBooking.setSelected(true);
                setBottomTabStyle(AppConstants.setting);
                replaceDockableFragment(SettingFragment.newInstance(), "SettingFragment");
                break;
            case R.id.ll_notifications:
                txtEventsBooking.setSelected(true);
                setBottomTabStyle(AppConstants.notifications);
                replaceDockableFragment(NotificationsFragment.newInstance(), "NotificationsFragment");
                break;

        }
    }

    public DisplayImageOptions getImageLoaderRoundCornerTransformation(int raduis) {
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.placeholder_banner)
                .showImageOnFail(R.drawable.placeholder_banner).resetViewBeforeLoading(true)
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .displayer(new RoundedBitmapDisplayer(raduis))
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }


    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "Saving Stuff");
        Log.i(TAG, "File Path: " + filePath);
        Log.i(TAG, "Chooser Type: " + chooserType);
        outState.putBoolean("activity_result_over", isActivityResultOver);
        outState.putInt("chooser_type", chooserType);
        outState.putString("media_path", filePath);
        outState.putString("orig", originalFilePath);
        outState.putString("thumb", thumbnailFilePath);
        outState.putString("thumbs", thumbnailSmallFilePath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("chooser_type")) {
                chooserType = savedInstanceState.getInt("chooser_type");
            }
            if (savedInstanceState.containsKey("media_path")) {
                filePath = savedInstanceState.getString("media_path");
            }
            if (savedInstanceState.containsKey("activity_result_over")) {
                isActivityResultOver = savedInstanceState.getBoolean("activity_result_over");
                originalFilePath = savedInstanceState.getString("orig");
                thumbnailFilePath = savedInstanceState.getString("thumb");
                thumbnailSmallFilePath = savedInstanceState.getString("thumbs");
            }
        }
        Log.i(TAG, "Restoring Stuff");
        Log.i(TAG, "File Path: " + filePath);
        Log.i(TAG, "Chooser Type: " + chooserType);
        Log.i(TAG, "Activity Result Over: " + isActivityResultOver);
        if (isActivityResultOver) {
            //populateData();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_PICK_PICTURE, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.clearOldFiles();
        try {
            //pbar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_CAPTURE_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);
        try {
            //pbar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pickFile() {
        fm = new FileChooserManager(this);
        fm.setFileChooserListener(this);
        try {
            // progressBar.setVisibility(View.VISIBLE);
            fm.choose();
        } catch (Exception e) {
            // progressBar.setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
/*        if (requestCode == -1) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {*/
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "OnActivityResult");
        Log.i(TAG, "File Path : " + filePath);
        Log.i(TAG, "Chooser Type: " + chooserType);

        if (requestCode == ChooserType.REQUEST_PICK_FILE && resultCode == RESULT_OK) {
            if (fm == null) {
                fm = new FileChooserManager(this);
                fm.setFileChooserListener(this);
            }
            Log.i(TAG, "Probable file size: " + fm.queryProbableFileSize(data.getData(), this));
            fm.submit(requestCode, data);
        }

        if (resultCode == RESULT_OK
                && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        } else {
            //pbar.setVisibility(View.GONE);
        }
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(getDockFrameLayoutId());

        if (fragment != null) {
            try {
                fragment.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //}

    @Override
    public void onImageChosen(final ChosenImage image) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "Chosen Image: O - " + image.getFilePathOriginal());
                Log.i(TAG, "Chosen Image: T - " + image.getFileThumbnail());
                Log.i(TAG, "Chosen Image: Ts - " + image.getFileThumbnailSmall());
                isActivityResultOver = true;
                originalFilePath = image.getFilePathOriginal();
                thumbnailFilePath = image.getFileThumbnail();
                thumbnailSmallFilePath = image.getFileThumbnailSmall();
                //pbar.setVisibility(View.GONE);
                if (image != null) {
                    Log.i(TAG, "Chosen Image: Is not null");

                    imageSetter.setImage(thumbnailFilePath);
                    //loadImage(imageViewThumbnail, image.getFileThumbnail());
                } else {
                    Log.i(TAG, "Chosen Image: Is null");
                }
            }
        });
    }

    @Override
    public void onFileChosen(final ChosenFile file) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // progressBar.setVisibility(View.INVISIBLE);
                imageSetter.setFilePath(file.getFilePath());
                populateFileDetails(file);
            }
        });
    }

    private void populateFileDetails(ChosenFile file) {
        StringBuffer text = new StringBuffer();
        text.append("File name: " + file.getFileName() + "\n\n");
        text.append("File path: " + file.getFilePath() + "\n\n");
        text.append("Mime type: " + file.getMimeType() + "\n\n");
        text.append("File extn: " + file.getExtension() + "\n\n");
        text.append("File size: " + file.getFileSize() / 1024 + "KB");

        //Toast.makeText(this,text.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "OnError: " + reason);
                // pbar.setVisibility(View.GONE);
                UIHelper.showLongToastInCenter(MainActivity.this, reason);

            }
        });
    }

    @Override
    public void onImagesChosen(final ChosenImages images) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "On Images Chosen: " + images.size());
                onImageChosen(images.getImage(0));
            }
        });
    }

    public interface ImageSetter {

        public void setImage(String imagePath);

        public void setFilePath(String filePath);

        public void setVideo(String videoPath);

    }

    public void setImageSetter(ImageSetter imageSetter) {
        this.imageSetter = imageSetter;
    }

    @Override
    public void onLoadingStarted() {

        if (mainFrameLayout != null) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mainFrameLayout.setVisibility(View.VISIBLE);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
            loading = true;
        }
    }

    @Override
    public void onLoadingFinished() {
        mainFrameLayout.setVisibility(View.VISIBLE);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (progressBar != null) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
        loading = false;
    }


    public void refreshFilter() {

        if (filterMenuFragment != null) {
            filterMenuFragment.refreshMenuOption();
        }
    }
    public void setCheckboxDataFilter() {

        if (filterMenuFragment != null) {
            filterMenuFragment.setCheckBox();
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(getDockActivity()).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }


}
