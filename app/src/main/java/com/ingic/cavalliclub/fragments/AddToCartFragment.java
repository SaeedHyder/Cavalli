package com.ingic.cavalliclub.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.AddCartEntity;
import com.ingic.cavalliclub.entities.AddToCardJson;
import com.ingic.cavalliclub.entities.PayNowEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.interfaces.TotalSum;
import com.ingic.cavalliclub.retrofit.GsonFactory;
import com.ingic.cavalliclub.ui.adapters.ArrayListAdapter;
import com.ingic.cavalliclub.ui.binders.BinderAddToCart;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.ExpandedListView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;


public class AddToCartFragment extends BaseFragment implements TotalSum {
    @BindView(R.id.lv_add_to_cart)
    ExpandedListView lvAddToCart;
    @BindView(R.id.tv_sub_amount)
    AnyTextView tvSubAmount;
    @BindView(R.id.tv_total_amount)
    AnyTextView tvTotalAmount;
    @BindView(R.id.btn_pay_now)
    Button btnPayNow;
    Unbinder unbinder;
    @BindView(R.id.tv_tax)
    AnyTextView tvTax;
    @BindView(R.id.btn_0)
    Button btn0;
    @BindView(R.id.btn_5)
    Button btn5;
    @BindView(R.id.btn_10)
    Button btn10;
    @BindView(R.id.btn_15)
    Button btn15;
    private ArrayList<Integer> totalsum = new ArrayList<>();
    RealmResults<AddCartEntity> results;
    ArrayList<AddCartEntity> resultWithoutRealm;
    int totalSum = 0;
    int totalAmount = 0;
    double percentageAmount = 0;
    int TotalAmountForPercentage = 0;
    PayNowEntity payNowEntity;
    String OrderNumber = "";
    private boolean ListEmpty;
    int subtotal = 0;
    private List<AddCartEntity> collection;
    private int i = 1;
    int TaxAmount;
    double TipValue;
    int rate;
    float TaxAmountInclusive = 0;

    private List<AddCartEntity> myAddToCartCollection = new ArrayList<>();
    private ArrayListAdapter<AddCartEntity> adapter;

    public static AddToCartFragment newInstance() {
        return new AddToCartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderAddToCart(getDockActivity(), this, getMainActivity(), adapter));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_to_cart, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListEmpty = false;
        getAddToCartList();
        getMainActivity().hideBottomTab();
        //tvTax.setText("AED " + prefHelper.getTaxData() + "%");
        if (tvTotalAmount.getText().toString().equalsIgnoreCase("")) {
            tvTotalAmount.setText("AED " + "0");
        }
        prefHelper.setTipValue(AppConstants.TIP_0);
        unselectButtons();
        if (prefHelper.getTipValue().equals(AppConstants.TIP_0)) {
            btn0.setBackground(getResources().getDrawable(R.drawable.circle_drawable_selected));
            btn0.setTextColor(getDockActivity().getResources().getColor(R.color.white));
        } else if (prefHelper.getTipValue().equals(AppConstants.TIP_5)) {
            btn5.setBackground(getResources().getDrawable(R.drawable.circle_drawable_selected));
            btn5.setTextColor(getDockActivity().getResources().getColor(R.color.white));
        } else if (prefHelper.getTipValue().equals(AppConstants.TIP_10)) {
            btn10.setBackground(getResources().getDrawable(R.drawable.circle_drawable_selected));
            btn10.setTextColor(getDockActivity().getResources().getColor(R.color.white));
        } else if (prefHelper.getTipValue().equals(AppConstants.TIP_15)) {
            btn15.setBackground(getResources().getDrawable(R.drawable.circle_drawable_selected));
            btn15.setTextColor(getDockActivity().getResources().getColor(R.color.white));
        }
        lvAddToCart.setOnTouchListener(null);
        lvAddToCart.setScrollContainer(false);
        lvAddToCart.setExpanded(true);
    }

    public void getAddToCartList() {
        Realm realm;
        realm = Realm.getDefaultInstance();
        results = realm.where(AddCartEntity.class).findAll();
        if (results != null && results.size() != 0) {
            bindData(results.subList(0, results.size()));
        } else {
            ListEmpty = true;
        }
    }

    public List<AddToCardJson> getModelList() {
        List<AddToCardJson> list = new ArrayList<>();
        Realm realm;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<AddCartEntity> results = realm
                    .where(AddCartEntity.class)
                    .findAll();
            for (AddCartEntity item : results) {
                list.add(new AddToCardJson(item.getId() + "", item.getProductQuantity() + "", item.getProductPrice() + ""));
            }
            // list.addAll(realm.copyFromRealm(results));
        } finally {
            if (getMainActivity().realm != null) {
                getMainActivity().realm.close();
            }
        }
        return list;
    }

    private void bindData(List<AddCartEntity> myAddToCartCollection) {
        collection = new ArrayList<>();
        collection.addAll(myAddToCartCollection);
        adapter.clearList();
        lvAddToCart.setAdapter(adapter);
        adapter.addAll(myAddToCartCollection);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.hideTwoTabsLayout();
      //  titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.order_summary));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getMainActivity().realm.close();
    }

    @Override
    public void sum(int multiplyAmount, int position) {

        if (i <= collection.size()) {
            subtotal = subtotal + multiplyAmount;
            tvSubAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(subtotal));
            TaxAmountInclusive = ((float)subtotal/100) * 12;
            rate = (int) TaxAmountInclusive;
            tvTax.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(rate));
            if (i == collection.size()) {
                int finalValue = subtotal / 100 * Integer.valueOf(prefHelper.getTaxData());
                TaxAmount = finalValue;
                finalValue = subtotal + finalValue + rate;

                tvTotalAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(finalValue));
            }
            i++;
        }
    }

    private void unselectButtons() {
        btn0.setBackground(getResources().getDrawable(R.drawable.circle_drawable_unselected));
        btn5.setBackground(getResources().getDrawable(R.drawable.circle_drawable_unselected));
        btn10.setBackground(getResources().getDrawable(R.drawable.circle_drawable_unselected));
        btn15.setBackground(getResources().getDrawable(R.drawable.circle_drawable_unselected));

        btn0.setTextColor(getDockActivity().getResources().getColor(R.color.app_golden));
        btn5.setTextColor(getDockActivity().getResources().getColor(R.color.app_golden));
        btn10.setTextColor(getDockActivity().getResources().getColor(R.color.app_golden));
        btn15.setTextColor(getDockActivity().getResources().getColor(R.color.app_golden));
    }

    @Override
    public void addAmount(int multiplyAmount, int pos, int amount) {
        subtotal = subtotal + multiplyAmount;
        tvSubAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(subtotal) + "");
        TaxAmountInclusive = ((float)subtotal/100) * 12;
        rate = (int) TaxAmountInclusive;
        tvTax.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(rate) + "");
        if (prefHelper.getTipValue().equals(AppConstants.TIP_0)) {
            //int finalValue = Integer.valueOf(prefHelper.getTaxData()) + subtotal;

            int finalValue = subtotal / 100 * Integer.valueOf(prefHelper.getTaxData());
            TaxAmount = finalValue;
            finalValue = subtotal + finalValue + rate;

            tvTotalAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(finalValue));

        } else if (prefHelper.getTipValue().equals(AppConstants.TIP_5)) {

            percentageAmount = 0;
            percentageAmount = subtotal * 0.05;
            TipValue = percentageAmount;
            //int value5 = Integer.valueOf(prefHelper.getTaxData()) + subtotal + (int) percentageAmount;
            int value = subtotal + (int) percentageAmount;
            int value5 = subtotal / 100 * Integer.valueOf(prefHelper.getTaxData());
            TaxAmount = value5;
            value5 = value + value5;

            tvTotalAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(value5));

        } else if (prefHelper.getTipValue().equals(AppConstants.TIP_10)) {

            percentageAmount = 0;
            percentageAmount = subtotal * 0.10;
            TipValue = percentageAmount;
            //int value10 = Integer.valueOf(prefHelper.getTaxData()) + subtotal + (int) percentageAmount;
            int value = subtotal + (int) percentageAmount;
            int value10 = subtotal / 100 * Integer.valueOf(prefHelper.getTaxData());
            TaxAmount = value10;
            value10 = value + value10;

            tvTotalAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(value10));

        } else if (prefHelper.getTipValue().equals(AppConstants.TIP_15)) {

            percentageAmount = 0;
            percentageAmount = subtotal * 0.15;
            TipValue = percentageAmount;
            //int value15 = Integer.valueOf(prefHelper.getTaxData()) + subtotal + (int) percentageAmount;
            int value = subtotal + (int) percentageAmount;
            int value15 = subtotal / 100 * Integer.valueOf(prefHelper.getTaxData());
            TaxAmount = value15;
            value15 = value + value15;

            tvTotalAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(value15));
        }
    }

    @Override
    public void substractAmount(int multiplyAmount, int pos, int amount) {
        subtotal = subtotal - multiplyAmount;
        tvSubAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(subtotal));
        TaxAmountInclusive = ((float)subtotal/100) * 12;
        rate = (int) TaxAmountInclusive;
        tvTax.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(rate));
        if (prefHelper.getTipValue().equals(AppConstants.TIP_0)) {
            //int finalValue = Integer.valueOf(prefHelper.getTaxData()) + subtotal;
            int finalValue = subtotal / 100 * Integer.valueOf(prefHelper.getTaxData());
            TaxAmount = finalValue;
            finalValue = subtotal + finalValue + rate;
            tvTotalAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(finalValue));
        } else if (prefHelper.getTipValue().equals(AppConstants.TIP_5)) {
            percentageAmount = 0;
            percentageAmount = subtotal * 0.05;
            TipValue = percentageAmount;
            //int value5 = Integer.valueOf(prefHelper.getTaxData()) + subtotal + (int) percentageAmount;
            int value = subtotal + (int) percentageAmount;
            int value5 = subtotal / 100 * Integer.valueOf(prefHelper.getTaxData());
            TaxAmount = value5;
            value5 = value + value5;
            tvTotalAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(value5));
        } else if (prefHelper.getTipValue().equals(AppConstants.TIP_10)) {
            percentageAmount = 0;
            percentageAmount = subtotal * 0.10;
            TipValue = percentageAmount;
            //int value10 = Integer.valueOf(prefHelper.getTaxData()) + subtotal + (int) percentageAmount;
            int value = subtotal + (int) percentageAmount;
            int value10 = subtotal / 100 * Integer.valueOf(prefHelper.getTaxData());
            TaxAmount = value10;
            value10 = value + value10;
            tvTotalAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(value10));
        } else if (prefHelper.getTipValue().equals(AppConstants.TIP_15)) {
            percentageAmount = 0;
            percentageAmount = subtotal * 0.15;
            TipValue = percentageAmount;
            //int value15 = Integer.valueOf(prefHelper.getTaxData()) + subtotal + (int) percentageAmount;
            int value = subtotal + (int) percentageAmount;
            int value15 = subtotal / 100 * Integer.valueOf(prefHelper.getTaxData());
            TaxAmount = value15;
            value15 = value + value15;
            tvTotalAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(value15));
        }
    }

    @Override
    public void onClickAdapterNotify(int position) {

        adapter.removeAtPosition(position);
        ListEmpty = true;
    }

    @OnClick({R.id.btn_0, R.id.btn_5, R.id.btn_10, R.id.btn_15, R.id.btn_pay_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_0:
                unselectButtons();
                btn0.setBackground(getResources().getDrawable(R.drawable.circle_drawable_selected));
                btn0.setTextColor(getDockActivity().getResources().getColor(R.color.white));
                //int finalValue = Integer.valueOf(prefHelper.getTaxData()) + subtotal;
                int finalValue = subtotal / 100 * Integer.valueOf(prefHelper.getTaxData());
                finalValue = subtotal + finalValue + rate;
                tvTotalAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(finalValue));
                prefHelper.setTipValue(AppConstants.TIP_0);
                break;
            case R.id.btn_5:
                unselectButtons();
                btn5.setBackground(getResources().getDrawable(R.drawable.circle_drawable_selected));
                btn5.setTextColor(getDockActivity().getResources().getColor(R.color.white));
                percentageAmount = 0;
                percentageAmount = subtotal * 0.05;
                TipValue = percentageAmount;
                //int value5 = Integer.valueOf(prefHelper.getTaxData()) + subtotal + (int) percentageAmount;
                int value = subtotal + (int) percentageAmount;
                int value5 = subtotal / 100 * Integer.valueOf(prefHelper.getTaxData());
                TaxAmount = value5;
                value5 = value + value5 + rate;
                tvTotalAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(value5));
                prefHelper.setTipValue(AppConstants.TIP_5);
                break;
            case R.id.btn_10:
                unselectButtons();
                btn10.setBackground(getResources().getDrawable(R.drawable.circle_drawable_selected));
                btn10.setTextColor(getDockActivity().getResources().getColor(R.color.white));
                percentageAmount = 0;
                percentageAmount = subtotal * 0.1;
                TipValue = percentageAmount;
                //int value10 = Integer.valueOf(prefHelper.getTaxData()) + subtotal + (int) percentageAmount;
                int value2 = subtotal + (int) percentageAmount;
                int value10 = subtotal / 100 * Integer.valueOf(prefHelper.getTaxData());
                TaxAmount = value10;
                value10 = value2 + value10 + rate;
                tvTotalAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(value10));
                prefHelper.setTipValue(AppConstants.TIP_10);
                break;
            case R.id.btn_15:
                unselectButtons();
                btn15.setBackground(getResources().getDrawable(R.drawable.circle_drawable_selected));
                btn15.setTextColor(getDockActivity().getResources().getColor(R.color.white));
                percentageAmount = 0;
                percentageAmount = subtotal * 0.15;
                TipValue = percentageAmount;
                //int value15 = Integer.valueOf(prefHelper.getTaxData()) + subtotal + (int) percentageAmount;
                int value3 = subtotal + (int) percentageAmount;
                int value15 = subtotal / 100 * Integer.valueOf(prefHelper.getTaxData());
                TaxAmount = value15;
                value15 = value3 + value15 + rate;
                tvTotalAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(value15));
                prefHelper.setTipValue(AppConstants.TIP_15);
                break;
            case R.id.btn_pay_now:
                if (ListEmpty) {
                    UIHelper.showShortToastInCenter(getDockActivity(), "No item selected." + "\n" + "Please select any item to proceed");
                } else {
                    payNow();
                }
                break;
        }
    }

    private void payNow() {

        String jsonAddToCart = GsonFactory.getConfiguredGson().toJson(getModelList());
        // getModelList();

        String strSubAmount = tvSubAmount.getText().toString() + "";
        String[] splitStrSubAmount = strSubAmount.split("\\s+"); // 004
        String strSubAmountWithoutAED = splitStrSubAmount[1];

        String strTotalAmount = tvTotalAmount.getText().toString() + "";
        String[] splitStrTotalAmount = strTotalAmount.split("\\s+"); // 004
        String strTotalAmountWithoutAED = splitStrTotalAmount[1];



        if (prefHelper != null && prefHelper.getTaxData() != null && prefHelper.getTipValue() != null)
            serviceHelper.enqueueCall(webService.addOrder(jsonAddToCart, strSubAmountWithoutAED.replaceAll(",", ""),
                    rate + "", strTotalAmountWithoutAED.replaceAll(",", ""),
                    TipValue + ""), WebServiceConstants.ADD_ORDER);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.ADD_ORDER:

                Realm realm;
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();

                totalAmount = 0;
                subtotal = 0;
                totalSum = 0;

                tvSubAmount.setText("AED " + "0");
                tvTotalAmount.setText("AED " + "0");

                payNowEntity = (PayNowEntity) result;
                getDockActivity().replaceDockableFragment(WebViewFragment.newInstance(payNowEntity), "WebViewFragment");
                break;
        }
    }
}