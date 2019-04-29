package com.application.cavalliclub.helpers;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.application.cavalliclub.global.AppConstants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class SelectShareIntent {
    private static final String[] items = {"Facebook", "Twitter", "WhatsApp", "Email"};


    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("TAG: ", "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }

    //Share on Twitter
    public static void twitterShare(Context context, String image, String title, String desc) {
        // Create intent using ACTION_VIEW and a normal Twitter url:
        String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                urlEncode(title + "\n" + desc),
                urlEncode(image));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

        // Narrow down to official Twitter app, if available:
        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName);
            }
        }
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, "No Application Found to share on twitter", Toast.LENGTH_SHORT).show();
        }
    }

    //Share on Facebook
    public static void facebookShare(Activity activity, String image, String title, String desc) {
        ShareDialog shareDialog = new ShareDialog(activity);
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setQuote(title + "\n" + desc)
                    .setContentUrl(Uri.parse(image))
                    .build();
            shareDialog.show(linkContent);
        }
    }

    //Email Share
    private static void emailShare(Context context, String msg) {
        String msgWithLink = msg + "\n" +AppConstants.PLAYSTORE_APP_LINK+"\n";
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "", null));
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, Constant.SHARE_SUBJECT);
        emailIntent.putExtra(Intent.EXTRA_TEXT, msgWithLink);
        context.startActivity(Intent.createChooser(emailIntent, ""));
    }

    private static void whatsAppShare(Context context, String msg) {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, msg + "\n" + AppConstants.PLAYSTORE_APP_LINK + "\n");
        try {
            context.startActivity(whatsappIntent);
        } catch (ActivityNotFoundException ex) {
            UIHelper.showShortToastInCenter(context, "Whatsapp have not been installed.");
        }
    }

    //SMS Share
//    private static void messageShare(Context context, String msg) {
//        String msgWithLink = msg + "\n\n" + Constant.SHARE_LINK;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) //At least KitKat
//        {
//            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context); //Need to change the build to API 19
//
//            Intent sendIntent = new Intent(Intent.ACTION_SEND);
//            sendIntent.setType("text/plain");
//            sendIntent.putExtra(Intent.EXTRA_TEXT, msgWithLink);
//
//            if (defaultSmsPackageName != null)//Can be null in case that there is no default, then the user would be able to choose any app that support this intent.
//            {
//                sendIntent.setPackage(defaultSmsPackageName);
//            }
//            context.startActivity(sendIntent);
//
//        } else //For early versions, do what worked for you before.
//        {
//            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//            sendIntent.setData(Uri.parse("sms:"));
//            sendIntent.putExtra("sms_body", msgWithLink);
//            context.startActivity(sendIntent);
//        }
//    }

}
