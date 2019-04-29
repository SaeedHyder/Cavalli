package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.AddCartEntity;
import com.application.cavalliclub.entities.MenuCategoryEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.helpers.DialogHelper;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MixDrinksDetailFragment extends BaseFragment {

    @BindView(R.id.iv_drinks_detail)
    ImageView ivDrinksDetail;
    @BindView(R.id.tv_amount)
    AnyTextView tvAmount;
    @BindView(R.id.txt_heading)
    AnyTextView txtHeading;
    @BindView(R.id.tv_category)
    AnyTextView tvCategory;
    @BindView(R.id.tv_description)
    AnyTextView tvDescription;
    @BindView(R.id.btn_add_to_cart)
    Button btnAddToCart;
    Unbinder unbinder;
    AddCartEntity addCartEntity;

    private long mLastClickTime = 0;

    private static String mixDrinkDetailKey = "mixDrinkDetailKey";
    private MenuCategoryEntity productBarCategoriesEntity;
    ImageLoader imageLoader;

    public static MixDrinksDetailFragment newInstance() {
        return new MixDrinksDetailFragment();
    }

    public static MixDrinksDetailFragment newInstance(MenuCategoryEntity productBarCategoriesEntity) {
        Bundle args = new Bundle();
        args.putString(mixDrinkDetailKey, new Gson().toJson(productBarCategoriesEntity));
        MixDrinksDetailFragment fragment = new MixDrinksDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mixDrinkDetailKey = getArguments().getString(mixDrinkDetailKey);
        }
        if (mixDrinkDetailKey != null) {
            productBarCategoriesEntity = new Gson().fromJson(mixDrinkDetailKey, MenuCategoryEntity.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mix_drinks_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivity().hideBottomTab();
        getMainActivity().RealmFunction();
        imageLoader = ImageLoader.getInstance();
        setDataTextViewFonts();
    }

    private void setDataTextViewFonts() {
        imageLoader.displayImage(productBarCategoriesEntity.getProductImages().get(0).getImageUrl(), ivDrinksDetail);
        if (productBarCategoriesEntity.getTotal_price() != null) {
            tvAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(productBarCategoriesEntity.getTotal_price())));
        }
        txtHeading.setText(productBarCategoriesEntity.getTitle() + "");

        String styledText = "<b><font color='#7D7F82'>Category: </font></b><font color='#9FA1A3'> " + prefHelper.getDrinkType() + "</font>";
        tvCategory.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);

        String styledText2 = "<b><font color='#7D7F82'>Description: </font></b><font color='#9FA1A3'>" + productBarCategoriesEntity.getDescription() + "</font>";
        tvDescription.setText(Html.fromHtml(styledText2), TextView.BufferType.SPANNABLE);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.hideTwoTabsLayout();
        titleBar.showCartButton(getCartCount());
        //    titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.details));
    }

    @OnClick(R.id.btn_add_to_cart)
    public void onViewClicked() {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        addCartEntity = new AddCartEntity();

        AddCartEntity entity = null;
        try {
            entity = getMainActivity().realm
                    .where(AddCartEntity.class)
                    .equalTo("ProductName", productBarCategoriesEntity.getTitle()).findFirst();
            if (entity == null) {
                //FirstTime
                addCartEntity.setProductName(productBarCategoriesEntity.getTitle());
                addCartEntity.setProductPrice(productBarCategoriesEntity.getPrice());
                addCartEntity.setVat(productBarCategoriesEntity.getVat());
                addCartEntity.setId(productBarCategoriesEntity.getId());
                addCartEntity.setProductQuantity(1);
                getMainActivity().realm.beginTransaction();

                if (entity != null)
                    entity.deleteFromRealm();
                getMainActivity().realm.copyToRealm(addCartEntity);
                getMainActivity().realm.commitTransaction();
                UIHelper.showShortToastInCenter(getDockActivity(), "Item added successfully");
                if (getTitleBar() != null)
                    getTitleBar().showCartButton(getCartCount());
            } else {
                //ifSecondTime
                final DialogHelper logout = new DialogHelper(getDockActivity());
                final AddCartEntity finalEntity = entity;
                logout.initlogout(R.layout.dialogue_add_to_cart, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addCartEntity.setProductName(productBarCategoriesEntity.getTitle());
                        addCartEntity.setVat(productBarCategoriesEntity.getVat());
                        addCartEntity.setProductPrice(productBarCategoriesEntity.getPrice());
                        addCartEntity.setId(productBarCategoriesEntity.getId());
                        addCartEntity.setProductQuantity(finalEntity.getProductQuantity() + 1);

                        if (getMainActivity() != null && getMainActivity().realm != null) {
                            getMainActivity().realm.beginTransaction();
                            if (finalEntity != null)
                                finalEntity.deleteFromRealm();
                            getMainActivity().realm.copyToRealm(addCartEntity);
                            getMainActivity().realm.commitTransaction();
                        }
                        UIHelper.showShortToastInCenter(getDockActivity(), "Item added successfully");
                        if (getTitleBar() != null)
                            getTitleBar().showCartButton(getCartCount());
                        logout.hideDialog();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout.hideDialog();
                    }
                });

                logout.showDialog();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDockActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}
