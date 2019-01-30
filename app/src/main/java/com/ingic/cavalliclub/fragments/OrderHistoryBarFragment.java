package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.EntityOrderHistory;
import com.ingic.cavalliclub.entities.EntityOrderProduct;
import com.ingic.cavalliclub.entities.EntityPendingsOrders;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.ui.adapters.ArrayListAdapter;
import com.ingic.cavalliclub.ui.binders.BinderPendingOrder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrderHistoryBarFragment extends BaseFragment {


    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    private EntityOrderHistory entityOrderHistories;
    private ArrayList<EntityPendingsOrders> pendingOrderList = new ArrayList<>();

    private ArrayList<EntityPendingsOrders> myPendingOrderCollection = new ArrayList<>();
    private ArrayListAdapter<EntityPendingsOrders> adapter;

    @BindView(R.id.lv_oder_history_bar)
    ListView lvOderHistoryBar;

    String ArrayStringContainer;
    private ArrayList<EntityOrderProduct> productList = new ArrayList<>();

    Unbinder unbinder;

    public static OrderHistoryBarFragment newInstance() {
        return new OrderHistoryBarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderPendingOrder(getDockActivity(), true));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history_bar, container, false);
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
        titleBar.setSubHeading(getString(R.string.bar_order));
       // titleBar.setTitlebarBackgroundColor(R.drawable.tab_header,getDockActivity());
        titleBar.showTwoTabsLayout(false, "Pending Orders", "Order History");
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
                pendingOrderList = entityOrderHistories.getOrderHistory();
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
            lvOderHistoryBar.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvOderHistoryBar.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(final ArrayList<EntityPendingsOrders> myPendingOrderCollection) {

        adapter.clearList();
        lvOderHistoryBar.setAdapter(adapter);
        adapter.addAll(myPendingOrderCollection);
        adapter.notifyDataSetChanged();

        lvOderHistoryBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                productList = myPendingOrderCollection.get(position).getOrderProduct();
                if (myPendingOrderCollection.get(position).getOrderProduct().size() != 0) {
                    Gson gson = new Gson();
                    ArrayStringContainer = gson.toJson(productList);
                }
                if (myPendingOrderCollection.get(position).getOrderProduct().size() != 0) {
                    getDockActivity().replaceDockableFragment(PendingOrdersDetailProfileFragment.newInstance(ArrayStringContainer, myPendingOrderCollection.get(position).getOrderNo()), "PendingOrdersDetailProfileFragment");
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), "No data available.");
                }
            }
        });
    }
}
