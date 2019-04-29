package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.EntityOrderHistory;
import com.application.cavalliclub.entities.EntityOrderProduct;
import com.application.cavalliclub.entities.EntityPendingsOrders;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.InternetHelper;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.BinderPendingOrder;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PendingOrderFragment extends BaseFragment {


    @BindView(R.id.lv_pending_order)
    ListView lvPendingOrder;
    Unbinder unbinder;

    String ArrayStringContainer;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    private ArrayList<EntityOrderProduct> productList = new ArrayList<>();

    private EntityOrderHistory entityOrderHistories;
    private ArrayList<EntityPendingsOrders> pendingOrderList = new ArrayList<>();

    private ArrayList<EntityPendingsOrders> myPendingOrderCollection = new ArrayList<>();
    private ArrayListAdapter<EntityPendingsOrders> adapter;

    public static PendingOrderFragment newInstance() {
        return new PendingOrderFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderPendingOrder(getDockActivity(), false));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getOrderHistoryPending();
        getMainActivity().showBottomTab(AppConstants.home);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading("Bar Order");
       // titleBar.setTitlebarBackgroundColor(R.drawable.tab_header,getDockActivity());
        titleBar.showTwoTabsLayout(true, "Pending Orders", "Order History");
        titleBar.setLayout_below();
        titleBar.tabsClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().popFragment();
                getDockActivity().replaceDockableFragment(PendingOrderFragment.newInstance(), "PendingOrderFragment");
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().popFragment();
                getDockActivity().replaceDockableFragment(OrderHistoryBarFragment.newInstance(), "OrderHistoryBarFragment");
            }
        });
    }

    private void getOrderHistoryPending() {
        serviceHelper.enqueueCall(webService.getOrdersHistoryPending(), WebServiceConstants.ORDER_HISTORY_PENDING);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.ORDER_HISTORY_PENDING:
                entityOrderHistories = (EntityOrderHistory) result;
                pendingOrderList = entityOrderHistories.getPendingOrder();
                setPendingOrderData(pendingOrderList);
                break;
        }
    }

    private void setPendingOrderData(ArrayList<EntityPendingsOrders> result) {
        myPendingOrderCollection = new ArrayList<>();
        myPendingOrderCollection.addAll(result);
        bindData(myPendingOrderCollection);

        if (myPendingOrderCollection.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvPendingOrder.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvPendingOrder.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(final ArrayList<EntityPendingsOrders> myPendingOrderCollection) {

        adapter.clearList();
        lvPendingOrder.setAdapter(adapter);
        adapter.addAll(myPendingOrderCollection);
        adapter.notifyDataSetChanged();

        lvPendingOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                productList = myPendingOrderCollection.get(position).getOrderProduct();
                if (myPendingOrderCollection.get(position).getOrderProduct().size() != 0) {
                    Gson gson = new Gson();
                    ArrayStringContainer = gson.toJson(productList);
                }
                if (myPendingOrderCollection.get(position).getOrderProduct().size() != 0) {
                    if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                        getDockActivity().replaceDockableFragment(PendingOrdersDetailProfileFragment.newInstance(ArrayStringContainer, myPendingOrderCollection.get(position).getOrderNo()), "PendingOrdersDetailProfileFragment");
                    }
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), "No data available.");
                }
            }
        });

    }
}
