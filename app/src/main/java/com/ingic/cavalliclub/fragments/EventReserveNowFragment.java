package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.EntityCavalliNights;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.helpers.DateHelper;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.ui.views.AnyEditTextView;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EventReserveNowFragment extends BaseFragment {
    private static String CavalliNightDetailKey = "CavalliNightDetailKey";
    @BindView(R.id.edt_reservation_title)
    AnyTextView edtReservationTitle;
    @BindView(R.id.tv_name)
    AnyTextView tvName;
    @BindView(R.id.tv_email)
    AnyTextView tvEmail;
    @BindView(R.id.tv_phone_number)
    AnyTextView tvPhoneNumber;
    @BindView(R.id.edt_secondary_number)
    AnyEditTextView edtSecondaryNumber;
    @BindView(R.id.edt_select_date)
    AnyTextView edtSelectDate;
    @BindView(R.id.sp_category)
    Spinner spCategory;
    @BindView(R.id.sp_time)
    Spinner spTime;
    @BindView(R.id.tv_time)
    AnyTextView tvTime;
    @BindView(R.id.sp_total_people)
    Spinner spTotalPeople;
    @BindView(R.id.btn_submit_request)
    Button btnSubmitRequest;

    /*    @BindView(R.id.edt_reservation_title)
        AnyEditTextView edtReservationTitle;
        @BindView(R.id.tv_name)
        AnyTextView tvName;
        @BindView(R.id.tv_email)
        AnyTextView tvEmail;
        @BindView(R.id.tv_phone_number)
        AnyTextView tvPhoneNumber;
        @BindView(R.id.edt_secondary_number)
        AnyEditTextView edtSecondaryNumber;
        @BindView(R.id.edt_select_date)
        AnyTextView edtSelectDate;
        @BindView(R.id.sp_category)
        Spinner spCategory;
        @BindView(R.id.sp_total_people)
        Spinner spTotalPeople;
        @BindView(R.id.sp_time)
        Spinner spTime;
        @BindView(R.id.btn_submit_request)
        Button btnSubmitRequest;*/
    @BindView(R.id.tv_time_day_date)
    AnyTextView tvTimeDayDate;
    EntityCavalliNights entityCavalliNights;
    String edtFormatedDate;
    String Hourtime;
    String SelectedDate;
    Unbinder unbinder;
    private ArrayList<String> categoryList = new ArrayList<>();
    private ArrayList<String> totalPeopleList = new ArrayList<>();
    private ArrayList<String> timeList = new ArrayList<>();
    private Date DateSelected;
    private String SpinnerValue;


    public static EventReserveNowFragment newInstance(EntityCavalliNights entityCavalliNights) {
        Bundle args = new Bundle();
        args.putString(CavalliNightDetailKey, new Gson().toJson(entityCavalliNights));
        EventReserveNowFragment fragment = new EventReserveNowFragment();
        fragment.setArguments(args);
        return fragment;
    }

/*    public static EventReserveNowFragment newInstance() {
        Bundle args = new Bundle();
        EventReserveNowFragment fragment = new EventReserveNowFragment();
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            CavalliNightDetailKey = getArguments().getString(CavalliNightDetailKey);

            if (CavalliNightDetailKey != null) {
                entityCavalliNights = new Gson().fromJson(CavalliNightDetailKey, EntityCavalliNights.class);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_event_reserve_now, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setCategoryData();
        setTotalPeopleData();
        //setTimeData();
        setDataFromPreferences();
        getMainActivity().hideBottomTab();
        edtSecondaryNumber.setHint(Html.fromHtml("<font color='#858992'>Secondary Phone Number</font>  <font color='#D1D1D1'>(Optional)</font>"));
    }

    private void setDataFromPreferences() {


        if (prefHelper.getSignUpUser() != null) {
            if (prefHelper.getSignUpUser().getUserName() != null)
                tvName.setText(prefHelper.getSignUpUser().getUserName() + "");
            if (prefHelper.getSignUpUser().getEmail() != null)
                tvEmail.setText(prefHelper.getSignUpUser().getEmail() + "");
            if (prefHelper.getSignUpUser().getPhone() != null)
                tvPhoneNumber.setText(prefHelper.getSignUpUser().getPhone() + "");
        }

        if (entityCavalliNights != null) {
            edtReservationTitle.setText(entityCavalliNights.getTitle() + "");
            if(entityCavalliNights.getEventDate()!=null) {
                tvTimeDayDate.setText(DateHelper.getEventDateTime(entityCavalliNights.getEventDate()));

                Hourtime = DateHelper.getLocalTimeAmPm(entityCavalliNights.getEventDate());
                Hourtime = Hourtime.replace("AM", "am").replace("PM", "pm");
                SelectedDate = DateHelper.getLocalDateEvent(entityCavalliNights.getEventDate());
            }

            /*if (entityCavalliNights.getEvent_schedule().equalsIgnoreCase(AppConstants.ONE_TIME)) {
                if (entityCavalliNights.getEventDate() != null) {
                   // edtSelectDate.setText(DateHelper.getLocalDateNotification(entityCavalliNights.getEventDate()));
                    edtFormatedDate = edtSelectDate.getText().toString();
                    edtSelectDate.setText(DateHelper.getLocalDateEvent(entityCavalliNights.getEventDate()));
                    tvTime.setText(DateHelper.getLocalTime(entityCavalliNights.getEventDate()));
                    tvTimeDayDate.setText(edtFormatedDate + ", " + tvTime.getText().toString());
                    dayDateChecker = false;
                }
            } else if (entityCavalliNights.getEvent_schedule().equalsIgnoreCase(AppConstants.RECURRING)) {
                String name = " - ";
                if (entityCavalliNights.getEventDay() != null && !entityCavalliNights.getEventDay().equals("")) {
                    name = entityCavalliNights.getEventDay();
                    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                }
                edtSelectDate.setText(name + "");
                dayDateChecker = true;
                tvTime.setText(DateHelper.getLocalTime(entityCavalliNights.getEventDate()));
                tvTimeDayDate.setText(edtSelectDate.getText().toString() + ", " + tvTime.getText().toString());
            }*/

        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading("Event Reservations");
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(CavalliDetailFragment.newInstance(entityCavalliNights), "CavalliDetailFragment");
            }
        }, getDockActivity());
        // titleBar.setTitlebarBackgroundColor(R.drawable.tab_header,getDockActivity());
        titleBar.showTwoTabsLayout(true, "Reserve Now", "My Reservations");
        titleBar.setLayout_below();
        titleBar.tabsClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(EventReserveNowFragment.newInstance(entityCavalliNights), "EventReserveNowFragment");

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefHelper.setFilterData(getAllFilters());
                getDockActivity().replaceDockableFragment(MyEventReservationFragment.newInstance(entityCavalliNights), "MyEventReservationFragment");
            }
        });
    }

    private void setTotalPeopleData() {

        totalPeopleList = new ArrayList<>();
        totalPeopleList.add("Select Total Number of People");
        totalPeopleList.add("1");
        totalPeopleList.add("2");
        totalPeopleList.add("3");
        totalPeopleList.add("4");
        totalPeopleList.add("5");
        totalPeopleList.add("6");
        totalPeopleList.add("7");
        totalPeopleList.add("8");
        totalPeopleList.add("9");
        totalPeopleList.add("10");
        totalPeopleList.add("11");
        totalPeopleList.add("12");
        totalPeopleList.add("13");
        totalPeopleList.add("14");
        totalPeopleList.add("15");
        totalPeopleList.add("16");
        totalPeopleList.add("17");
        totalPeopleList.add("18");
        totalPeopleList.add("19");
        totalPeopleList.add("20");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, totalPeopleList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
        spTotalPeople.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.edt_select_date, R.id.btn_submit_request})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edt_select_date:
                //FromDateListner();
                break;
            case R.id.btn_submit_request:

                if (spTotalPeople.getSelectedItemPosition() == 0) {
                    UIHelper.showLongToastInCenter(getDockActivity(), getString(R.string.please_select_people));
                } else {
                    reserveNow();
                }

                break;
        }
    }

    private void reserveNow() {

        if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getToken() != null ) {
            serviceHelper.enqueueCall(webService.reserveNowEvent(SelectedDate!=null?SelectedDate:"", Hourtime!=null?Hourtime:"",
                    edtSecondaryNumber.getText().toString() + "", Integer.valueOf(spTotalPeople.getSelectedItem().toString()),
                    edtReservationTitle.getText().toString() + "", entityCavalliNights.getId() + "", "",
                    prefHelper.getSignUpUser().getToken() + ""), WebServiceConstants.RESERVE_NOW);
        }

      /*  if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getToken() != null) {
            serviceHelper.enqueueCall(webService.reserveNow(edtSelectDate.getText().toString() + "", Hourtime,
                    edtSecondaryNumber.getText().toString() + "", Integer.valueOf(spTotalPeople.getSelectedItem().toString()),
                    edtReservationTitle.getText().toString() + "", entityCavalliNights.getId() + "", "",
                    prefHelper.getSignUpUser().getToken() + ""), WebServiceConstants.RESERVE_NOW);
        } else if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getToken() != null) {
            serviceHelper.enqueueCall(webService.reserveNowEvent(entityCavalliNights.getEventDay() + "", Hourtime,
                    edtSecondaryNumber.getText().toString() + "", Integer.valueOf(spTotalPeople.getSelectedItem().toString()),
                    edtReservationTitle.getText().toString() + "", entityCavalliNights.getId() + "", "",
                    prefHelper.getSignUpUser().getToken() + ""), WebServiceConstants.RESERVE_NOW);
        }
*/
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.RESERVE_NOW:
                //UIHelper.showShortToastInCenter(getDockActivity(), "Hey");
                prefHelper.setFilterData(getAllFilters());
                getDockActivity().replaceDockableFragment(MyEventReservationFragment.newInstance(entityCavalliNights), "MyEventReservationFragment");
                break;
        }
    }

}

