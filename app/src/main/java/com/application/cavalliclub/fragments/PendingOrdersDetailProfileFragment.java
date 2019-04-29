package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.EntityOrderProduct;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.BinderPendingOrdersDetailProfile;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PendingOrdersDetailProfileFragment extends BaseFragment {


    @BindView(R.id.lv_oder_history_bar)
    ListView lvOderHistoryBar;
    Unbinder unbinder;

    private static ArrayList<EntityOrderProduct> entityOrderProduct;
    private ArrayList<EntityOrderProduct> myPendingOrderDetailCollection = new ArrayList<>();
    private ArrayListAdapter<EntityOrderProduct> adapter;
    private static String ORDER_NUMBER;

    public static BaseFragment newInstance(String arrayStringContainer, String orderNo) {

        Bundle args = new Bundle();
        Gson gson = new Gson();
        entityOrderProduct = new Gson().fromJson(arrayStringContainer, new TypeToken<ArrayList<EntityOrderProduct>>() {}.getType());
        ORDER_NUMBER = orderNo;
        PendingOrdersDetailProfileFragment fragment = new PendingOrdersDetailProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderPendingOrdersDetailProfile(getDockActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_orders_detail_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivity().hideBottomTab();
        setOrdersDetailData(entityOrderProduct);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().popFragment();
            }
        },getDockActivity());
       // titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
       // titleBar.setSubHeading(ORDER_NUMBER);
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.order_detail));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setOrdersDetailData(ArrayList<EntityOrderProduct> result) {
        myPendingOrderDetailCollection = new ArrayList<>();
        myPendingOrderDetailCollection.addAll(result);
        bindData(myPendingOrderDetailCollection);
    }

    private void bindData(ArrayList<EntityOrderProduct> myPendingOrderDetailCollection) {

        adapter.clearList();
        lvOderHistoryBar.setAdapter(adapter);
        adapter.addAll(myPendingOrderDetailCollection);
        adapter.notifyDataSetChanged();
    }
}
