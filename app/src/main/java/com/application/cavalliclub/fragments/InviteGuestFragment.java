package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.entities.EntityGetTotalList;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.DialogHelper;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.interfaces.RecyclerViewItemListener;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.BinderInviteGuest;
import com.application.cavalliclub.ui.views.ExpandedListView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class InviteGuestFragment extends BaseFragment implements RecyclerViewItemListener {


    Unbinder unbinder;

    private static String sourceKey = "sourceKey";
    @BindView(R.id.lv_guest_lists)
    ExpandedListView lvGuestLists;
    @BindView(R.id.btn_add_list)
    Button btnAddList;

    ArrayList<EntityGetTotalList> entityGetTotalLists = new ArrayList<>();
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;

    private ArrayList<EntityGetTotalList> myGuestCollection = new ArrayList<>();
    private ArrayListAdapter<EntityGetTotalList> adapter;
    private static int ID;

    public static InviteGuestFragment newInstance(String home, Integer id) {
        ID = id;
        return new InviteGuestFragment();
    }

    public static InviteGuestFragment newInstance(String source) {
        Bundle args = new Bundle();
        args.putString(sourceKey, source);
        InviteGuestFragment fragment = new InviteGuestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static InviteGuestFragment newInstance() {
        return new InviteGuestFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sourceKey = getArguments().getString(sourceKey);
        }
        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderInviteGuest(getDockActivity(), true, webService, serviceHelper, this, prefHelper, this));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite_guest, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivity().hideBottomTab();
        lvGuestLists.setOnTouchListener(null);
        lvGuestLists.setScrollContainer(false);
        lvGuestLists.setExpanded(true);
        getTotalLists();
    }

    private void getTotalLists() {
        serviceHelper.enqueueCall(webService.getGuestList(ID), WebServiceConstants.GET_LIST);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.GET_LIST:
                entityGetTotalLists = (ArrayList<EntityGetTotalList>) result;
                setGuestListData(entityGetTotalLists);
                if (entityGetTotalLists.size() > 0) {
                    llNoData.setVisibility(View.GONE);
                    lvGuestLists.setVisibility(View.VISIBLE);
                    btnAddList.setVisibility(View.GONE);

                } else {
                    llNoData.setVisibility(View.VISIBLE);
                    lvGuestLists.setVisibility(View.GONE);
                    btnAddList.setVisibility(View.VISIBLE);
                }
                break;

            case WebServiceConstants.CREATE_LIST:
                UIHelper.hideSoftKeyboard(getDockActivity(), getView());
                UIHelper.showShortToastInCenter(getDockActivity(), "New list has been created successfully.");
                getTotalLists();
                break;

            case WebServiceConstants.EDIT_LIST_TITLE:
                UIHelper.hideSoftKeyboard(getDockActivity(), getView());
                UIHelper.showShortToastInCenter(getDockActivity(), "List has been edited successfully.");
                getTotalLists();
                break;
        }
    }

    private void setGuestListData(ArrayList<EntityGetTotalList> entityGetTotalLists) {
        bindData(entityGetTotalLists);
    }

    private void bindData(ArrayList<EntityGetTotalList> mylistCollection) {
        adapter.clearList();
        lvGuestLists.setAdapter(adapter);
        adapter.addAll(mylistCollection);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.invite_your_guest));
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(GuestListListingFragment.newInstance(), "GuestListListingFragment");
            }
        },getDockActivity());
        titleBar.hideTwoTabsLayout();
      //  titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.btn_add_list)
    public void onViewClicked() {

        final DialogHelper addList = new DialogHelper(getDockActivity());
        addList.addList(R.layout.dialog_add_list, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.hideSoftKeyboard(getDockActivity(), getView());
                if(addList.nameField().getText().toString().trim().equalsIgnoreCase("")){
                    UIHelper.showShortToastInCenter(getDockActivity(), "Please enter valid List Name to proceed.");
                } else if (addList.nameField().getText().toString().length() < 3) {
                    UIHelper.showShortToastInCenter(getDockActivity(), "List name show should be of more than 3 digits.");
                } else {
                    serviceHelper.enqueueCall(webService.createGuestList(addList.nameField().getText().toString() + "", String.valueOf(ID)), WebServiceConstants.CREATE_LIST);
                    addList.hideDialog();
                }
            }
        }, "");
        addList.showDialog();
    }

    @Override
    public void onEditItemClicked(Object Ent, int position, DockActivity dockActivity) {

    }

    @Override
    public void onEditItemClicked(Object Ent, int position, String id) {

        final EntityGetTotalList entity = (EntityGetTotalList) Ent;
        final DialogHelper addList = new DialogHelper(getDockActivity());
        addList.addList(R.layout.dialog_add_list, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.hideSoftKeyboard(getDockActivity(), getView());
                if(addList.nameField().getText().toString().trim().equalsIgnoreCase("")){
                    UIHelper.showShortToastInCenter(getDockActivity(), "Please enter valid List Name to proceed.");
                } else if (addList.nameField().getText().toString().length() < 3) {
                    UIHelper.showShortToastInCenter(getDockActivity(), "List name show should be of more than 3 digits.");
                } else {
                    serviceHelper.enqueueCall(webService.editListTitle(addList.nameField().getText().toString() + "", entity.getId() + ""), WebServiceConstants.EDIT_LIST_TITLE);
                    addList.hideDialog();
                }
            }
        }, entity.getTitle());
        addList.showDialog();
    }

    @Override
    public void onDeleteItemClicked(Object Ent, int position) {
    }

    @Override
    public void onClickItemFilter() {
    }
}

