package com.ingic.cavalliclub.fragments;


import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.activities.DockActivity;
import com.ingic.cavalliclub.entities.AddEditGuestListEntity;
import com.ingic.cavalliclub.entities.EntityGetTotalList;
import com.ingic.cavalliclub.entities.EntityGuestListMember;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.helpers.DatePickerHelper;
import com.ingic.cavalliclub.helpers.DialogHelper;
import com.ingic.cavalliclub.helpers.InternetHelper;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.interfaces.RecyclerViewItemListener;
import com.ingic.cavalliclub.ui.adapters.ArrayListAdapter;
import com.ingic.cavalliclub.ui.binders.BinderAddMoreFriends;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.ExpandedListView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddMoreFriendsFragment extends BaseFragment implements RecyclerViewItemListener {


    Unbinder unbinder;

    private static String sourceKey = "sourceKey";
    @BindView(R.id.lv_guest_lists)
    ExpandedListView lvGuestLists;
    @BindView(R.id.btn_add_list)
    Button btnAddList;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private static String MEMBER_KEY = "MEMBER_KEY";
    DialogHelper dialogHelper;
    private static int ID;
    private static String id_binder;
    private static int Checker;
    @BindView(R.id.iv_gv_gallery)
    ImageView ivGvGallery;
    @BindView(R.id.tv_guest_list)
    AnyTextView tvGuestList;
    private ArrayListAdapter<EntityGuestListMember> adapter;
    private ArrayList<EntityGetTotalList> entityGetTotalLists = new ArrayList<>();
    private List<EntityGuestListMember> entityGuestListMember = new ArrayList<>();
    private String splittedDate;
    private Date DateSelected;
    private ArrayList<String> MrSpinnerList = new ArrayList<>();
    String SpinnerValue;
    String SpinnerValue2;
    private AddEditGuestListEntity addEditGuestListEntity;
    private EntityGuestListMember entity;
    private static String TITLE = "";

    public static AddMoreFriendsFragment newInstance(Integer id, int backChecker) {
        Bundle args = new Bundle();
        ID = id;
        Checker = backChecker;
        AddMoreFriendsFragment fragment = new AddMoreFriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AddMoreFriendsFragment newInstance(Integer id, int backChecker, String titleName) {
        Bundle args = new Bundle();
        ID = id;
        Checker = backChecker;
        TITLE = titleName;
        AddMoreFriendsFragment fragment = new AddMoreFriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderAddMoreFriends(getDockActivity(), webService, serviceHelper, this, getMainActivity(), this, prefHelper));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_more_guests, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivity().hideBottomTab();
        tvGuestList.setText(TITLE);
        getOnlyGuestList();
        lvGuestLists.setOnTouchListener(null);
        lvGuestLists.setScrollContainer(false);
        lvGuestLists.setExpanded(true);
    }

    private void getOnlyGuestList() {
        serviceHelper.enqueueCall(webService.getGuestList(ID), WebServiceConstants.ADD_GUEST_LIST_SEPARATE);
    }

    private void bindData(List<EntityGuestListMember> myGuestCollection) {
        adapter.clearList();
        lvGuestLists.setAdapter(adapter);

        adapter.addAll(entityGuestListMember);
        if (entityGuestListMember.size() == 0) {
            llNoData.setVisibility(View.VISIBLE);
            lvGuestLists.setVisibility(View.GONE);
        } else {
            llNoData.setVisibility(View.GONE);
            lvGuestLists.setVisibility(View.VISIBLE);
        }
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
                if (Checker == 0) {
                    getDockActivity().replaceDockableFragment(InviteGuestHistoryProfileFragment.newInstance(), "InviteGuestHistoryProfileFragment");
                } else if (Checker == 1) {
                    getDockActivity().replaceDockableFragment(InviteGuestUpcomingProfileFragment.newInstance(), "InviteGuestUpcomingProfileFragment");
                } else if (Checker == 3) {
                    //getDockActivity().replaceDockableFragment(InviteGuestFragment.newInstance(), "InviteGuestHistoryProfileFragment");
                    getDockActivity().replaceDockableFragment(GuestListListingFragment.newInstance(), "GuestListListingFragment");
                }
            }
        },getDockActivity());
        titleBar.hideTwoTabsLayout();
        //titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
    }

    private void initFromPickerValidated(final AnyTextView textView) {

        Calendar calendar = Calendar.getInstance();
        final DatePickerHelper datePickerHelper = new DatePickerHelper();
        datePickerHelper.initDateDialog(
                getDockActivity(),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
                , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date date = new Date();
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // and get that as a Date
                        Date dateSpecified = c.getTime();
                        /*if (dateSpecified.after(date)) {
                            UIHelper.showShortToastInCenter(getDockActivity(), "Please enter valid date.");
                        } else {*/
                        DateSelected = dateSpecified;
                        String predate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

                        String[] splited = predate.split("\\s");
                        splittedDate = splited[0];

                        textView.setText(splittedDate);
                        textView.setPaintFlags(Typeface.BOLD);
                        //}
                    }
                }, "PreferredDate", new Date());

        datePickerHelper.showDate();
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.ADD_GUEST_LIST_MEMBER:
                UIHelper.hideSoftKeyboard(getDockActivity(), getView());
                addEditGuestListEntity = (AddEditGuestListEntity) result;
                UIHelper.showShortToastInCenter(getDockActivity(), "Guest successfully added.");
                getOnlyGuestList();
                break;

            case WebServiceConstants.ADD_GUEST_LIST_SEPARATE:
                entityGetTotalLists = (ArrayList<EntityGetTotalList>) result;
                if (entityGetTotalLists!=null && entityGetTotalLists.size()!=0) {
                    entityGuestListMember = entityGetTotalLists.get(0).getGuestListMember();
                    bindData(entityGuestListMember);
                }
                break;

            case WebServiceConstants.EDIT_LIST:
                UIHelper.hideSoftKeyboard(getDockActivity(), getView());
                addEditGuestListEntity = (AddEditGuestListEntity) result;
                UIHelper.showShortToastInCenter(getDockActivity(), "Guest edited successfully.");
                getOnlyGuestList();
                break;

            case WebServiceConstants.DELETE_LIST:
                UIHelper.showShortToastInCenter(getDockActivity(), "Guest deleted successfully.");
                getOnlyGuestList();
                break;
        }
    }

    @Override
    public void onEditItemClicked(Object Ent, int position, DockActivity dockActivity) {
    }

    @Override
    public void onEditItemClicked(Object Ent, int position, String id) {
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            id_binder = id;
            entity = (EntityGuestListMember) Ent;

            final DialogHelper dialogHelper = new DialogHelper(getDockActivity());
            dialogHelper.addGuest(R.layout.dialog_add_guest, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UIHelper.hideSoftKeyboard(getDockActivity(), getView());


                    if (dialogHelper.validateGuestName() && dialogHelper.validateEmail() && dialogHelper.validatePhone()) {
                        if (dialogHelper.addGuestFieldsName().getText().toString().length() < 3) {
                            UIHelper.showShortToastInCenter(getDockActivity(), "List name show should be of more than 3 digits.");
                        } else if (dialogHelper.addPhoneNo().getText().toString().length() < 3) {
                            UIHelper.showShortToastInCenter(getDockActivity(), "List phone number show should be of more than 3 digits.");
                        } else if (dialogHelper.addDate().getText().toString().equalsIgnoreCase(AppConstants.SelectDate)) {
                            UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.please_select_date));
                        } else {

                            serviceHelper.enqueueCall(webService.editGuestList(dialogHelper.addSpinner().getSelectedItem().toString(), dialogHelper.addGuestFieldsName().getText().toString() + "",
                                    dialogHelper.addGuestFieldsEmail().getText().toString(), entity.getId() + "", dialogHelper.addPhoneNo().getText().toString(),
                                    dialogHelper.addDate().getText().toString()), WebServiceConstants.EDIT_LIST);
                            dialogHelper.hideDialog();
                        }
                    }

                }
            }, entity.getFullName(), entity.getEmailAddress(), entity.getMobileNo(), entity.getDate(), entity.getNameTitle(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initFromPickerValidated(dialogHelper.addDate());
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            dialogHelper.showDialog();
        }
    }

    @Override
    public void onDeleteItemClicked(Object Ent, int position) {
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            final EntityGuestListMember entity = (EntityGuestListMember) Ent;
            serviceHelper.enqueueCall(webService.deleteGuestList(entity.getId() + ""), WebServiceConstants.DELETE_LIST);
        }
    }

    @Override
    public void onClickItemFilter() {
    }

    @OnClick({R.id.btn_add_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_add_list:

                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    final DialogHelper dialogHelper = new DialogHelper(getDockActivity());
                    dialogHelper.addGuest(R.layout.dialog_add_guest, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UIHelper.hideSoftKeyboard(getDockActivity(), getView());
                            if (dialogHelper.validateGuestName() && dialogHelper.validateEmail() && dialogHelper.validatePhone()) {
                                if (dialogHelper.addGuestFieldsName().getText().toString().length() < 3) {
                                    UIHelper.showShortToastInCenter(getDockActivity(), "List name show should be of more than 3 digits.");
                                } else if (dialogHelper.addPhoneNo().getText().toString().length() < 3) {
                                    UIHelper.showShortToastInCenter(getDockActivity(), "List phone number show should be of more than 3 digits.");
                                } else if (dialogHelper.addDate().getText().toString().equalsIgnoreCase(AppConstants.SelectDate)) {
                                    UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.please_select_date));
                                } else {
                                    serviceHelper.enqueueCall(webService.addGuestListMember(dialogHelper.addSpinner().getSelectedItem().toString(), dialogHelper.addGuestFieldsName().getText().toString() + "",
                                            dialogHelper.addGuestFieldsEmail().getText().toString(), dialogHelper.addPhoneNo().getText().toString(),
                                            dialogHelper.addDate().getText().toString(), ID + ""), WebServiceConstants.ADD_GUEST_LIST_MEMBER);
                                    dialogHelper.hideDialog();
                                }
                            }
                        }
                    }, "", "", "", "Select Date", "", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            initFromPickerValidated(dialogHelper.addDate());
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialogHelper.addSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (dialogHelper.addSpinner().getSelectedItemPosition() == 0) {
                                        SpinnerValue = "Mr";
                                    } else if (dialogHelper.addSpinner().getSelectedItemPosition() == 1) {
                                        SpinnerValue = "Mrs";
                                    } else if (dialogHelper.addSpinner().getSelectedItemPosition() == 2)
                                        SpinnerValue = "Miss";
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }
                    });
                    dialogHelper.showDialog();
                }
                break;
        }
    }
}
