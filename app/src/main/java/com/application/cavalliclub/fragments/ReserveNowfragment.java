package com.application.cavalliclub.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.NewReservationEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.DatePickerHelper;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.ui.views.AnyEditTextView;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ReserveNowfragment extends BaseFragment {

    @BindView(R.id.edt_reservation_title)
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
    private ArrayList<String> categoryList = new ArrayList<>();
    private ArrayList<String> totalPeopleList = new ArrayList<>();
    private ArrayList<String> timeList = new ArrayList<>();
    String splittedDate;
    @BindView(R.id.btn_submit_request)
    Button btnSubmitRequest;
    private Date DateSelected;
    private String SpinnerValue;
    private String category = "";
    private String defaultDate = null;
    Unbinder unbinder;
    ArrayList<NewReservationEntity> newReservationSlots = new ArrayList<>();

    int selectedCategoryID = 0;

    public static ReserveNowfragment newInstance() {
        return new ReserveNowfragment();
    }

    public static ReserveNowfragment newInstance(String defaultDate) {
        return new ReserveNowfragment(defaultDate);
    }

    public ReserveNowfragment() {
    }

    @SuppressLint("ValidFragment")
    public ReserveNowfragment(String defaultDate) {
        this.defaultDate = defaultDate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserve_now, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        defaultDate = prefHelper.getStringPreference(AppConstants.BOOKING_DATE_KEY);
        if (defaultDate != null && (!defaultDate.equals(""))) {
            edtSelectDate.setText(defaultDate.trim());
        }

        getReservationSlots();

//        setCategoryData();
        setTotalPeopleData();
        setTimeData();
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
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading("Reservations");
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().onBackPressed();
            }
        }, getDockActivity());
        // titleBar.setTitlebarBackgroundColor(R.drawable.tab_header,getDockActivity());
        titleBar.showTwoTabsLayout(true, "Reserve Now", "My Reservations");
        titleBar.setLayout_below();
        titleBar.tabsClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getDockActivity().replaceFragment(ReserveNowfragment.newInstance(), ReserveNowfragment.class.getSimpleName(), true, false);

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().popFragment();
                prefHelper.setFilterData(getAllFilters());
                getDockActivity().replaceFragment(MyReservationsFragment.newInstance(), MyReservationsFragment.class.getSimpleName(), true, false);

            }
        });
    }

    private void setCategoryData() {

        categoryList = new ArrayList<>();
        categoryList.add(getDockActivity().getResources().getString(R.string.select_category));
        categoryList.add(getDockActivity().getResources().getString(R.string.dinner_bookings));
        categoryList.add(getDockActivity().getResources().getString(R.string.club_bookings));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, categoryList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
        spCategory.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setTimeData() {
        timeList = new ArrayList<>();
        timeList.add("Please Select Category First");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, timeList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
        spTime.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void setNewCategoryData() {
        categoryList = new ArrayList<>();
        categoryList.add(getDockActivity().getResources().getString(R.string.select_category));
        for (int pos = 0; pos < newReservationSlots.size(); pos++) {
            categoryList.add(newReservationSlots.get(pos).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, categoryList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
        spCategory.setAdapter(adapter);
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedCategoryID = newReservationSlots.get(position - 1).getId();

                    timeList = new ArrayList<>();
                    timeList.add("Select Time");

                    String[] startTimeSplitted = newReservationSlots.get(position - 1).getFrom().split(":");
                    String[] endTimeSplitted = newReservationSlots.get(position - 1).getTo().split(":");

                    DateFormat df = new SimpleDateFormat("HH:mm:ss");

                    Calendar startCal = Calendar.getInstance();
                    startCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimeSplitted[0]));
                    startCal.set(Calendar.MINUTE, Integer.parseInt(startTimeSplitted[1]));
                    startCal.set(Calendar.SECOND, Integer.parseInt(startTimeSplitted[2]));

                    Calendar endCal = Calendar.getInstance();
                    endCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTimeSplitted[0]));
                    endCal.set(Calendar.MINUTE, Integer.parseInt(endTimeSplitted[1]));
                    endCal.set(Calendar.SECOND, Integer.parseInt(endTimeSplitted[2]));

                    int endDate = endCal.get(Calendar.DATE);
                    boolean enteredLoop = false;
                    while (startCal.compareTo(endCal) < 0 || startCal.compareTo(endCal) == 0) {
//                        Log.i("testing", df.format(cal.getTime()));
                        enteredLoop = true;
                        timeList.add(getFormattedDate(df.format(startCal.getTime()), "HH:mm:ss", "h:mm a"));
                        startCal.add(Calendar.MINUTE, newReservationSlots.get(position - 1).getInterval());
                    }

                    if (!enteredLoop) {
                        enteredLoop = false;

                        do {
                            timeList.add(getFormattedDate(df.format(startCal.getTime()), "HH:mm:ss", "h:mm a"));
                            startCal.add(Calendar.MINUTE, newReservationSlots.get(position - 1).getInterval());
                        }while (startCal.get(Calendar.HOUR_OF_DAY) > endCal.get(Calendar.HOUR_OF_DAY));

                        do {
                            timeList.add(getFormattedDate(df.format(startCal.getTime()), "HH:mm:ss", "h:mm a"));
                            startCal.add(Calendar.MINUTE, newReservationSlots.get(position - 1).getInterval());
                        }while (startCal.get(Calendar.HOUR_OF_DAY) <= endCal.get(Calendar.HOUR_OF_DAY));


                    }

                    if(timeList.size() > 0){
                        timeList.remove(timeList.size() - 1);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, timeList);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
                    spTime.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                if(position == 0){
                    timeList = new ArrayList<>();
                    timeList.add("Select Time");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, timeList);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
                    spTime.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter.notifyDataSetChanged();
    }

    private void getReservationSlots() {
        serviceHelper.enqueueCall(webService.getReservationSlots(prefHelper.getSignUpUser().getToken()), WebServiceConstants.GET_RESERVATION_CATEGORIES);
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
                FromDateListner();
                break;
            case R.id.btn_submit_request:
                if (validate()) {
                    if (edtSelectDate.getText().toString().equalsIgnoreCase(AppConstants.SelectDate)) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.please_select_date));
                    } else if (spCategory.getSelectedItemPosition() == 0) {
                        UIHelper.showLongToastInCenter(getDockActivity(), getString(R.string.please_select_category));
                    } else if (spTime.getSelectedItemPosition() == 0) {
                        UIHelper.showLongToastInCenter(getDockActivity(), getString(R.string.please_select_time));
                    } else if (spTotalPeople.getSelectedItemPosition() == 0) {
                        UIHelper.showLongToastInCenter(getDockActivity(), getString(R.string.please_select_people));
                    } else {
                        reserveNow();
                    }
                }
                break;
        }
    }

    public void FromDateListner() {
        edtSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFromPickerValidated(edtSelectDate);
            }
        });
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

    private boolean validate() {
        return edtReservationTitle.testValidity();
    }

    private void reserveNow() {

        if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getToken() != null) {
            serviceHelper.enqueueCall(webService.reserveNowNew(edtSelectDate.getText().toString() + "", spTime.getSelectedItem().toString(),
                    edtSecondaryNumber.getText().toString() + "", Integer.valueOf(spTotalPeople.getSelectedItem().toString()),
                    edtReservationTitle.getText().toString() + "", "", selectedCategoryID,
                    prefHelper.getSignUpUser().getToken() + ""), WebServiceConstants.RESERVE_NOW);
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.RESERVE_NOW:
                prefHelper.setFilterData(getAllFilters());
                getDockActivity().replaceDockableFragment(MyReservationsFragment.newInstance(), "MyReservationsFragment");
                break;

            case WebServiceConstants.GET_RESERVATION_CATEGORIES:
                newReservationSlots = (ArrayList<NewReservationEntity>) result;
                setNewCategoryData();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static String getFormattedDate(String date, String oldFormat, String newFormat) {
        String formattedDate = "";
        SimpleDateFormat input = new SimpleDateFormat(oldFormat);
        SimpleDateFormat output = new SimpleDateFormat(newFormat);
        try {
            Date newDate = input.parse(date);
            formattedDate = output.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
}
