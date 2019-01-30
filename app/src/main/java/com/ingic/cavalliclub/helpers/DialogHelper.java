package com.ingic.cavalliclub.helpers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;


import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.activities.DockActivity;
import com.ingic.cavalliclub.entities.EntityGalleryList;
import com.ingic.cavalliclub.ui.views.AnyEditTextView;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TouchImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Date;

public class DialogHelper {
    private Dialog dialog;
    private DockActivity context;
    private ImageLoader imageLoader;
    private RadioGroup rg;
    private String splittedDate;
    private Date DateSelected;
    private ArrayList<String> MrSpinnerList = new ArrayList<>();
    private String spinnerValue = "";

    public DialogHelper(DockActivity context) {
        this.context = context;
        this.dialog = new Dialog(context);
        imageLoader = ImageLoader.getInstance();
    }


    public Dialog initlogout(int layoutID, View.OnClickListener onokclicklistener, View.OnClickListener oncancelclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button okbutton = (Button) dialog.findViewById(R.id.btn_yes);
        okbutton.setOnClickListener(onokclicklistener);
        Button cancelbutton = (Button) dialog.findViewById(R.id.btn_No);
        cancelbutton.setOnClickListener(oncancelclicklistener);
        return this.dialog;
    }
    public Dialog alertDialoge(int layoutID, View.OnClickListener onokclicklistener,String text) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button okbutton = (Button) dialog.findViewById(R.id.btn_yes);
        AnyTextView textView = (AnyTextView) dialog.findViewById(R.id.txt_text);
        okbutton.setOnClickListener(onokclicklistener);
        textView.setText(text);
        return this.dialog;
    }

    public Dialog initAddToCart(int layoutID, View.OnClickListener onokclicklistener, View.OnClickListener oncancelclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button okbutton = (Button) dialog.findViewById(R.id.btn_yes);
        okbutton.setOnClickListener(onokclicklistener);
        Button cancelbutton = (Button) dialog.findViewById(R.id.btn_No);
        cancelbutton.setOnClickListener(oncancelclicklistener);
        return this.dialog;
    }

    public Dialog initDeleteNotification(int layoutID, View.OnClickListener onokclicklistener, View.OnClickListener oncancelclicklistener, String title, String body) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        AnyTextView txt_Logout = (AnyTextView) dialog.findViewById(R.id.txt_Logout);
        txt_Logout.setText(title + "");
        AnyTextView txt_logout_text = (AnyTextView) dialog.findViewById(R.id.txt_logout_text);
        txt_logout_text.setText(body + "");
        Button okbutton = (Button) dialog.findViewById(R.id.btn_yes);
        okbutton.setOnClickListener(onokclicklistener);
        Button cancelbutton = (Button) dialog.findViewById(R.id.btn_No);
        cancelbutton.setOnClickListener(oncancelclicklistener);
        return this.dialog;
    }

    public Dialog imageDisplayDialog(int layoutID, View.OnClickListener onokclicklistener, ArrayList<EntityGalleryList> myGridCollection, int position) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button okbutton = (Button) dialog.findViewById(R.id.btn_yes);
        okbutton.setOnClickListener(onokclicklistener);
        TouchImageView iv_image = (TouchImageView) dialog.findViewById(R.id.iv_image);
        imageLoader.displayImage(myGridCollection.get(position).getImageUrl(), iv_image);
        return this.dialog;
    }

    public Dialog addList(int layoutID, View.OnClickListener onsubmitclicklistener, String title) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button submitbtn = (Button) dialog.findViewById(R.id.btn_submit);
        submitbtn.setOnClickListener(onsubmitclicklistener);
        AnyEditTextView edtName = (AnyEditTextView) dialog.findViewById(R.id.edt_name_list);
        edtName.setText(title);
        return this.dialog;
    }

    public Dialog addGuest(int layoutID, View.OnClickListener onsubmitclicklistener, String fullName, String emailAddress, String phone_no, String date, String nameTitle, View.OnClickListener listener, View.OnClickListener spinner_listener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button submitbtn = (Button) dialog.findViewById(R.id.btn_submit);
        submitbtn.setOnClickListener(onsubmitclicklistener);
        AnyEditTextView guestName = (AnyEditTextView) dialog.findViewById(R.id.guest_name);
        guestName.setText(fullName);
        AnyEditTextView edtEmail = (AnyEditTextView) dialog.findViewById(R.id.edt_guest_email);
        edtEmail.setText(emailAddress);
        AnyEditTextView edt_guest_number = (AnyEditTextView) dialog.findViewById(R.id.edt_guest_number);
        edt_guest_number.setText(phone_no);
        AnyTextView edt_select_date = (AnyTextView) dialog.findViewById(R.id.edt_select_date);
        edt_select_date.setOnClickListener(listener);
        edt_select_date.setText(date);
        edt_select_date.setOnClickListener(listener);
        Spinner sp_category = (Spinner) dialog.findViewById(R.id.sp_category);
        spinnerValue = nameTitle;
        setSpinnerAdapter();
        return this.dialog;
    }

    public AnyEditTextView nameField() {
        AnyEditTextView edtName = (AnyEditTextView) dialog.findViewById(R.id.edt_name_list);
        return edtName;
    }

    public AnyEditTextView addGuestFieldsName() {
        AnyEditTextView guestName = (AnyEditTextView) dialog.findViewById(R.id.guest_name);
        return guestName;
    }

    public AnyEditTextView addPhoneNo() {
        AnyEditTextView edt_guest_number = (AnyEditTextView) dialog.findViewById(R.id.edt_guest_number);
        return edt_guest_number;
    }

    public AnyTextView addDate() {
        AnyTextView edt_select_date = (AnyTextView) dialog.findViewById(R.id.edt_select_date);
        return edt_select_date;
    }

    public Spinner addSpinner() {
        Spinner sp_category = (Spinner) dialog.findViewById(R.id.sp_category);
        return sp_category;
    }

    public void setSpinnerAdapter() {

        Spinner sp_category = (Spinner) dialog.findViewById(R.id.sp_category);

        MrSpinnerList = new ArrayList<>();
        MrSpinnerList.add("Mr");
        MrSpinnerList.add("Mrs");
        MrSpinnerList.add("Miss");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_item_2, MrSpinnerList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
        sp_category.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (spinnerValue.equalsIgnoreCase("")) {
            MrSpinnerList = new ArrayList<>();
            MrSpinnerList.add("Mr");
            MrSpinnerList.add("Mrs");
            MrSpinnerList.add("Miss");
            adapter = new ArrayAdapter<String>(context, R.layout.spinner_item_2, MrSpinnerList);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
            sp_category.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            if (spinnerValue.equalsIgnoreCase("Mr")) {
                sp_category.setSelection(0);
            } else if (spinnerValue.equalsIgnoreCase("Mrs")) {
                sp_category.setSelection(1);
            } else if (spinnerValue.equalsIgnoreCase("Miss"))
                sp_category.setSelection(2);
        }
    }

    public void addGuestFieldsName(String text) {
        AnyEditTextView guestName = (AnyEditTextView) dialog.findViewById(R.id.guest_name);
        guestName.setText(text);
    }

    public boolean validateEmail() {
        AnyEditTextView edtEmail = (AnyEditTextView) dialog.findViewById(R.id.edt_guest_email);
        return edtEmail.testValidity();
    }

    public boolean validateGuestName() {
        AnyEditTextView guestName = (AnyEditTextView) dialog.findViewById(R.id.guest_name);
        return guestName.testValidity();
    }

    public boolean validatePhone() {
        AnyEditTextView edt_guest_number = (AnyEditTextView) dialog.findViewById(R.id.edt_guest_number);
        return edt_guest_number.testValidity();
    }

    public AnyEditTextView addGuestFieldsEmail() {
        AnyEditTextView edtEmail = (AnyEditTextView) dialog.findViewById(R.id.edt_guest_email);
        return edtEmail;
    }

    public void addGuestFieldsEmail(String text) {
        AnyEditTextView edtEmail = (AnyEditTextView) dialog.findViewById(R.id.edt_guest_email);
        edtEmail.setText(text);
    }

   /* public Dialog openSlider(int layoutID, ArrayList<String> images, int position, Context context) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        SliderLayout imageSlider = (SliderLayout) dialog.findViewById(R.id.slider);
        PagerIndicator pagerIndicator= (PagerIndicator) dialog.findViewById(R.id.custom_indicator);

        for (String item : images) {
            DefaultSliderView textSliderView = new DefaultSliderView(context);
            // initialize a SliderLayout
            textSliderView
                    //.image(item.getImage())
                    .image(item)
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                 //   .putString("extra", item.getImage() + "");
                    .putString("extra", item);

            imageSlider.addSlider(textSliderView);
        }
      //  imageSlider.setCurrentPosition(position);
        imageSlider.setCustomIndicator(pagerIndicator);
        imageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imageSlider.setCustomAnimation(new DescriptionAnimation());
        imageSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
        imageSlider.stopAutoCycle();

        return this.dialog;
    }*/


    public void showDialog() {
        dialog.show();
    }

    public void setCancelable(boolean isCancelable) {
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelable);
    }

    public void hideDialog() {
        dialog.dismiss();
    }
}
