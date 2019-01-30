package com.ingic.cavalliclub.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.TaxEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.interfaces.SimpleDialogActionListener;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CavalliSocialFragment extends BaseFragment {


    @BindView(R.id.tv_descriptions)
    AnyTextView tvDescriptions;
    @BindView(R.id.iv_fb)
    ImageView ivFb;
    @BindView(R.id.iv_twitter)
    ImageView ivTwitter;
    @BindView(R.id.iv_insta)
    ImageView ivInsta;
    @BindView(R.id.iv_google)
    ImageView ivGoogle;
    @BindView(R.id.iv_youtube)
    ImageView ivYoutube;
    @BindView(R.id.iv_whatsapp)
    ImageView ivWhatsapp;
    @BindView(R.id.iv_call)
    ImageView ivCall;
    @BindView(R.id.iv_web)
    ImageView ivWeb;
    private ArrayList<TaxEntity> socialLinks;
    String FACEBOOK, TWITTER, INSTAGRAM, YOUTUBE, WEBSITE, PHONE_NO, WHATSAPP;
    Unbinder unbinder;

    public static CavalliSocialFragment newInstance() {
        return new CavalliSocialFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cavalli_social, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().showBottomTab(AppConstants.home);

        getCavalliSocialLinks();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.cavalli_social));
    }

    private void getCavalliSocialLinks() {
        serviceHelper.enqueueCall(webService.getCavalliSocialLinks(), WebServiceConstants.CAVALLI_SOCIAL_LINKS);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.CAVALLI_SOCIAL_LINKS:
                socialLinks = (ArrayList<TaxEntity>) result;

                if (socialLinks != null && socialLinks.size() != 0) {
                    int i;
                    for (i = 0; i < socialLinks.size(); i++) {
                        if (socialLinks.get(i).getKey().equalsIgnoreCase("facebook")) {
                            FACEBOOK = socialLinks.get(i).getValue();
                        } else if (socialLinks.get(i).getKey().equalsIgnoreCase("twitter")) {
                            TWITTER = socialLinks.get(i).getValue();
                        } else if (socialLinks.get(i).getKey().equalsIgnoreCase("instagram")) {
                            INSTAGRAM = socialLinks.get(i).getValue();
                        } else if (socialLinks.get(i).getKey().equalsIgnoreCase("youtube")) {
                            YOUTUBE = socialLinks.get(i).getValue();
                        } else if (socialLinks.get(i).getKey().equalsIgnoreCase("social_content")) {
                            tvDescriptions.setText(socialLinks.get(i).getValue() + "");
                        } else if (socialLinks.get(i).getKey().equalsIgnoreCase("website")) {
                            WEBSITE = socialLinks.get(i).getValue();
                        } else if (socialLinks.get(i).getKey().equalsIgnoreCase("phone_no")) {
                            PHONE_NO = socialLinks.get(i).getValue();
                        } else if (socialLinks.get(i).getKey().equalsIgnoreCase("whatsapp")) {
                            WHATSAPP = socialLinks.get(i).getValue();
                        }
                    }
                }
                break;
        }
    }

    @OnClick({R.id.iv_fb, R.id.iv_twitter, R.id.iv_insta, R.id.iv_google, R.id.iv_youtube, R.id.iv_whatsapp, R.id.iv_call, R.id.iv_web})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fb:
                if (FACEBOOK != null && (!(FACEBOOK.equalsIgnoreCase("")))) {
                    String urlFb = FACEBOOK;
                    Intent iFb = new Intent(Intent.ACTION_VIEW);
                    iFb.setData(Uri.parse(urlFb));
                    startActivity(iFb);
                }
                break;
            case R.id.iv_twitter:
                if (TWITTER != null && (!(TWITTER.equalsIgnoreCase("")))) {
                    String urlTwitter = TWITTER;
                    Intent iTwitter = new Intent(Intent.ACTION_VIEW);
                    iTwitter.setData(Uri.parse(urlTwitter));
                    startActivity(iTwitter);
                }
                break;
            case R.id.iv_insta:
                if (INSTAGRAM != null && (!(INSTAGRAM.equalsIgnoreCase("")))) {
                    String urlInsta = INSTAGRAM;
                    Intent iInsta = new Intent(Intent.ACTION_VIEW);
                    iInsta.setData(Uri.parse(urlInsta));
                    startActivity(iInsta);
                }
                break;
            case R.id.iv_google:
                String urlGoogle = "https://plus.google.com/discover";
                Intent iGoogle = new Intent(Intent.ACTION_VIEW);
                iGoogle.setData(Uri.parse(urlGoogle));
                startActivity(iGoogle);
                break;
            case R.id.iv_youtube:
                if (YOUTUBE != null && (!(YOUTUBE.equalsIgnoreCase("")))) {
                    String urlYoutube = YOUTUBE;
                    Intent iYoutube = new Intent(Intent.ACTION_VIEW);
                    iYoutube.setData(Uri.parse(urlYoutube));
                    startActivity(iYoutube);
                }
                break;

            case R.id.iv_whatsapp:
                launchWhatsapp();
                break;

            case R.id.iv_call:
                UIHelper.showSimpleDialog(
                        getDockActivity(),
                        0,
                        getDockActivity().getResources().getString(R.string.call_phone),
                        getDockActivity().getResources().getString(R.string.call_phone_surety),
                        getDockActivity().getResources().getString(R.string.call),
                        getDockActivity().getResources().getString(R.string.cancel),
                        false,
                        false,
                        new SimpleDialogActionListener() {
                            @Override
                            public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                                if (positive) {
                                    dialog.dismiss();
                                    openCallMaker(PHONE_NO, getDockActivity());
                                } else {
                                    dialog.dismiss();
                                }
                            }
                        }
                );
                break;
            case R.id.iv_web:
                openChromeBrowser(getDockActivity());
                break;
        }
    }

    public void openChromeBrowser(Context context) {
        String urlString = WEBSITE;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null);
            context.startActivity(intent);
        }
    }

    private void openWhatsApp() {
/*
        Intent intentWhatsapp = new Intent("android.intent.action.MAIN");
        intentWhatsapp.setAction(Intent.ACTION_VIEW);
        String url = "https://api.whatsapp.com";
        intentWhatsapp.setData(Uri.parse(url));
        intentWhatsapp.setPackage("com.whatsapp");
*/

        String toNumber = "+92 331 2320050";
        toNumber = toNumber.replace("+", "").replace(" ", "");
        Intent sendIntent = new Intent("android.intent.action.MAIN");
        // sendIntent.setComponent(new ComponentName(“com.whatsapp”, “com.whatsapp.Conversation”));
        sendIntent.putExtra("jid", toNumber + " @s.whatsapp.net");
//        sendIntent.putExtra(Intent.EXTRA_TEXT, “message”);
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setPackage("com.whatsapp");
        sendIntent.setType("text / plain");
//        startActivity(sendIntent);

        if (isPackageInstalled("com.whatsapp", getDockActivity().getPackageManager())) {
            startActivity(sendIntent);
        } else {
            Toast.makeText(getDockActivity(), getDockActivity().getResources().getString(R.string.missing_whatsapp_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void launchWhatsapp() {
        Intent intentWhatsapp = new Intent("android.intent.action.MAIN");
        intentWhatsapp.setAction(Intent.ACTION_VIEW);
        String url = "https://api.whatsapp.com/send?phone=" + WHATSAPP;
        intentWhatsapp.setData(Uri.parse(url));
        intentWhatsapp.setPackage("com.whatsapp");
        if (isPackageInstalled("com.whatsapp", getDockActivity().getPackageManager())) {
            startActivity(intentWhatsapp);
        } else {
            Toast.makeText(getDockActivity(), getDockActivity().getResources().getString(R.string.missing_whatsapp_error), Toast.LENGTH_SHORT).show();
        }
    }

    public static void openCallMaker(final String phoneNumber, final Activity context) {
        /*
        Dexter.withActivity(context)
                .withPermissions(
                        Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            CameraHelper.uploadPhotoDialog(context);
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            requestCameraPermission();

                        } else if (report.getDeniedPermissionResponses().size() > 0) {
                            requestCameraPermission();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "Grant Camera And Storage Permission to processed");
                        openSettings();
                    }
                })

                .onSameThread()
                .check();
        */

        TedPermission.with(context)
                .setPermissions(Manifest.permission.CALL_PHONE)
                .setPermissionListener(new PermissionListener() {

                    public void onPermissionGranted() {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        context.startActivity(intent);

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> arrayList) {
                        //Utils.showToast(context, context.getString(R.string.permission_denied));
                        Toast.makeText(context, context.getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    }
                }).check();

    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {

        boolean found = true;

        try {

            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {

            found = false;
        }

        return found;
    }

}
