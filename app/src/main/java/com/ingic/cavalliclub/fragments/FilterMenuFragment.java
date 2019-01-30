package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.interfaces.RecyclerViewItemListener;
import com.ingic.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FilterMenuFragment extends BaseFragment {
    @BindView(R.id.iv_cross)
    ImageView ivCross;
    @BindView(R.id.btn_reserved)
    CheckBox btnReserved;
    @BindView(R.id.btn_pending)
    CheckBox btnPending;
    @BindView(R.id.btn_rejected)
    CheckBox btnRejected;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.btn_done)
    Button btnDone;
    Unbinder unbinder;
    @BindView(R.id.btn_cancelled)
    CheckBox btnCancelled;
    private boolean isFromInvite = false;
    private HashMap<String, String> hashMap = new HashMap<>();
    private ArrayList<String> allData = new ArrayList<>();

    RecyclerViewItemListener recyclerViewItemListener;

    public void setInterfaceListner(RecyclerViewItemListener recyclerViewItemListener) {
        this.recyclerViewItemListener = recyclerViewItemListener;
    }

    public void setIsFromInviteGuest(boolean x) {
        isFromInvite = x;
        if (isFromInvite && btnPending != null && btnCancelled != null) {
            btnPending.setVisibility(View.GONE);
            btnCancelled.setVisibility(View.GONE);
        } else {
            visiblityVisible();
        }
    }

    public static FilterMenuFragment newInstance() {
        Bundle args = new Bundle();

        FilterMenuFragment fragment = new FilterMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_menu, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        checkBoxListners();

    }


    @Override
    public void onResume() {
        super.onResume();

    }


    private void checkBoxListners() {

        btnReserved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    hashMap.put(AppConstants.RESERVED_ID, AppConstants.RESERVED);
                } else {
                    hashMap.remove(AppConstants.RESERVED_ID);
                }
            }
        });

        btnPending.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    hashMap.put(AppConstants.PENDING_ID, AppConstants.PENDING);
                } else {
                    hashMap.remove(AppConstants.PENDING_ID);
                }
            }
        });

        btnRejected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    hashMap.put(AppConstants.REJECTED_ID, AppConstants.REJECTED);
                } else {
                    hashMap.remove(AppConstants.REJECTED_ID);
                }
            }
        });

        btnCancelled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    hashMap.put(AppConstants.CANCELLED_ID, AppConstants.CANCELLED);
                } else {
                    hashMap.remove(AppConstants.CANCELLED_ID);
                }
            }
        });


    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.hideTitleBar();
    }

    @OnClick({R.id.iv_cross, R.id.btn_clear, R.id.btn_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_cross:
                getMainActivity().closeDrawer();
                break;
            case R.id.btn_clear:
                prefHelper.setFilterData(AppConstants.EMPTY);
                recyclerViewItemListener.onClickItemFilter();
                btnReserved.setChecked(false);
                btnPending.setChecked(false);
                btnRejected.setChecked(false);
                btnCancelled.setChecked(false);
                getMainActivity().closeDrawer();
                break;
            case R.id.btn_done:
                //checkById();
                prefHelper.setFilterData(getAllCheckBoxIds());
                recyclerViewItemListener.onClickItemFilter();
                getMainActivity().closeDrawer();
                break;
        }
    }

    private String getAllCheckBoxIds() {

        String commaSepratedString = "";
        if (hashMap.size() > 0) {
            commaSepratedString = android.text.TextUtils.join(",", hashMap.keySet());
        } else {
            allData = new ArrayList<>();
            allData.add(AppConstants.RESERVED_ID);
            allData.add(AppConstants.PENDING_ID);
            allData.add(AppConstants.REJECTED_ID);
            allData.add(AppConstants.CANCELLED_ID);
            commaSepratedString = android.text.TextUtils.join(",", allData);
        }
        return commaSepratedString;
    }

    private void check() {
        if (btnReserved.isChecked()) {
            prefHelper.setFilterData("Reserved ");
        } else if (btnPending.isChecked()) {
            prefHelper.setFilterData(AppConstants.PENDING);
        } else if (btnRejected.isChecked()) {
            prefHelper.setFilterData(AppConstants.REJECTED);
        } else if (btnCancelled.isChecked()) {
            prefHelper.setFilterData(AppConstants.CANCELLED);
        } else {
            prefHelper.setFilterData(AppConstants.EMPTY);
        }
    }

    private void checkById() {
        if (btnReserved.isChecked()) {
            prefHelper.setFilterData(AppConstants.RESERVED_ID);
        } else if (btnPending.isChecked()) {
            prefHelper.setFilterData(AppConstants.PENDING_ID);
        } else if (btnRejected.isChecked()) {
            prefHelper.setFilterData(AppConstants.REJECTED_ID);
        } else if (btnCancelled.isChecked()) {
            prefHelper.setFilterData(AppConstants.CANCELLED_ID);
        } else if (btnReserved.isChecked() && btnPending.isChecked()) {

            StringBuilder commaSeparatedNames = new StringBuilder();
            commaSeparatedNames.append(AppConstants.RESERVED_ID);
            commaSeparatedNames.append(", " + AppConstants.PENDING_ID);
            String names = String.valueOf(commaSeparatedNames);

        } else if (btnReserved.isChecked() && btnRejected.isChecked())

        {

        } else if (btnReserved.isChecked() && btnCancelled.isChecked())

        {

        } else if (btnReserved.isChecked() && btnPending.isChecked() && btnRejected.isChecked())

        {

        } else if (btnReserved.isChecked() && btnPending.isChecked() && btnCancelled.isChecked())

        {

        } else if (btnReserved.isChecked() && btnRejected.isChecked() && btnCancelled.isChecked())

        {

        } else if (btnReserved.isChecked() && btnPending.isChecked() && btnRejected.isChecked() && btnCancelled.isChecked())

        {

        } else if (btnPending.isChecked() && btnRejected.isChecked())

        {

        } else if (btnPending.isChecked() && btnCancelled.isChecked())

        {

        } else if (btnPending.isChecked() && btnCancelled.isChecked() && btnReserved.isChecked())

        {

        } else if (btnPending.isChecked() && btnRejected.isChecked() && btnReserved.isChecked())

        {

        } else if (btnPending.isChecked() && btnRejected.isChecked() && btnCancelled.isChecked())

        {

        } else if (btnRejected.isChecked() && btnCancelled.isChecked())

        {

        } else

        {
            prefHelper.setFilterData(AppConstants.EMPTY);
        }

    }

    public void visiblityGone() {
        btnPending.setVisibility(View.GONE);
        btnCancelled.setVisibility(View.GONE);
    }

    public void visiblityVisible() {
        btnPending.setVisibility(View.VISIBLE);
        btnCancelled.setVisibility(View.VISIBLE);
        btnReserved.setVisibility(View.VISIBLE);
        btnRejected.setVisibility(View.VISIBLE);
    }

    public void refreshMenuOption() {

        btnReserved.setChecked(false);
        btnCancelled.setChecked(false);
        btnRejected.setChecked(false);
        btnPending.setChecked(false);

        hashMap = new HashMap<>();
        allData = new ArrayList<>();
    }
}
