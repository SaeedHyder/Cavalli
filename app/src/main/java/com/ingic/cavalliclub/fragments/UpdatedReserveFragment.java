package com.ingic.cavalliclub.fragments;

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
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.GetReservationsEntity;
import com.ingic.cavalliclub.entities.NewReservationEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.helpers.DateHelper;
import com.ingic.cavalliclub.helpers.DatePickerHelper;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.ui.views.AnyEditTextView;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;

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

public class UpdatedReserveFragment extends BaseFragment {


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
    @BindView(R.id.btn_submit_request)
    Button btnSubmitRequest;
    @BindView(R.id.btn_cancel_request)
    Button btnCancelRequest;
    @BindView(R.id.ll_mainFrame)
    LinearLayout ll_mainFrame;

    private Date DateSelected;
    private ArrayList<String> totalPeopleList = new ArrayList<>();
    Unbinder unbinder;
    private static String updateReserveData = "updateReserveData";
    private GetReservationsEntity getReservationsEntity;
    String selectedDate="";
    String differentFormatDate;
    private ArrayList<String> categoryList = new ArrayList<>();
    private ArrayList<String> timeList = new ArrayList<>();
    String category = "";

    ArrayList<NewReservationEntity> newReservationSlots = new ArrayList<>();

    int selectedCategoryID = 0;

    public static UpdatedReserveFragment newInstance() {
        return new UpdatedReserveFragment();
    }

    public static UpdatedReserveFragment newInstance(GetReservationsEntity getReservationsEntity) {

        Bundle args = new Bundle();
        args.putString(updateReserveData, new Gson().toJson(getReservationsEntity));
        UpdatedReserveFragment fragment = new UpdatedReserveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateReserveData = getArguments().getString(updateReserveData);
        if (updateReserveData != null) {
            getReservationsEntity = new Gson().fromJson(updateReserveData, GetReservationsEntity.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_updated_reserve, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTimeSpinnerByDefault();
        getReservationSlots();
//        setCategoryData();
        setTotalPeopleData();
//        setTimeData();
//        setDataFromEntity();
        setDataFromPreferences();

        if (getReservationsEntity.getReservationStatusId()!=null && getReservationsEntity.getReservationStatusId().equalsIgnoreCase("2")) {
            btnSubmitRequest.setVisibility(View.GONE);
            btnCancelRequest.setVisibility(View.GONE);

            for ( int i = 0; i < ll_mainFrame.getChildCount();  i++ ){
                View viewTextBoxes = ll_mainFrame.getChildAt(i);
                viewTextBoxes.setEnabled(false); // Or whatever you want to do with the view.
            }
        }else{
            btnSubmitRequest.setVisibility(View.VISIBLE);
            btnCancelRequest.setVisibility(View.VISIBLE);

            for ( int i = 0; i < ll_mainFrame.getChildCount();  i++ ){
                View viewTextBoxes = ll_mainFrame.getChildAt(i);
                viewTextBoxes.setEnabled(true); // Or whatever you want to do with the view.
            }
        }

        edtSecondaryNumber.setHint(Html.fromHtml("<font color='#858992'>Secondary Phone Number</font>  <font color='#D1D1D1'>(Optional)</font>"));
    }

    private void setTimeSpinnerByDefault() {
        if (getReservationsEntity.getReservationType() != null && getReservationsEntity.getReservationType().equalsIgnoreCase("Dinner and Lounge")) {

            timeList = new ArrayList<>();
            timeList.add("Select Time");
            timeList.add("08:30 pm");
            timeList.add("09:00 pm");
            timeList.add("09:30 pm");
            timeList.add("10:00 pm");
            timeList.add("10:30 pm");
            timeList.add("11:00 pm");
            timeList.add("11:30 pm");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, timeList);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
            spTime.setAdapter(adapter);

        } else if (getReservationsEntity.getReservationType() != null && getReservationsEntity.getReservationType().equalsIgnoreCase("Dinner and Lounge")) {
            timeList = new ArrayList<>();
            timeList.add("Select Time");
            timeList.add("11:30 pm");
            timeList.add("12:00 am");
            timeList.add("12:30 am");
            timeList.add("01:00 am");
            timeList.add("01:30 am");
            timeList.add("02:00 am");
            timeList.add("02:30 am");
            timeList.add("03:00 am");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, timeList);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
            spTime.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void setDataFromEntity() {


        if (getReservationsEntity != null) {
            if (getReservationsEntity.getTitle() != null)
                edtReservationTitle.setText(getReservationsEntity.getTitle() + "");
            if (getReservationsEntity.getSecondaryPhoneNo() != null)
                edtSecondaryNumber.setText(getReservationsEntity.getSecondaryPhoneNo() + "");
            if (getReservationsEntity.getDate() != null) {
                edtSelectDate.setText(DateHelper.getLocalDateReservation(getReservationsEntity.getDate()));
                selectedDate=getReservationsEntity.getDate();
            } else {
                edtSelectDate.setText(getReservationsEntity.getDay());
            }

/*            if (getReservationsEntity.getReservationType() != null) {
                spCategory.setSelection(((ArrayAdapter<String>) spCategory.getAdapter()).getPosition(String.valueOf(getReservationsEntity.getReservationType())));
            }*/
/*
            if (getReservationsEntity.getReservationType() != null && getReservationsEntity.getReservationType().equalsIgnoreCase("dinner")) {
                spCategory.setSelection(1);
            } else if (getReservationsEntity.getReservationType() != null && getReservationsEntity.getReservationType().equalsIgnoreCase("club")) {
                spCategory.setSelection(2);
            }
*/

            int catPos = 0;

            for(int pos = 0; pos < newReservationSlots.size(); pos++){
                if(getReservationsEntity.getReservation_slot_id() == newReservationSlots.get(pos).getId()){
                    catPos = pos;
                    selectedCategoryID = getReservationsEntity.getReservation_slot_id();
                    spCategory.setSelection(pos + 1);
                    break;
                }
            }

            if(selectedCategoryID > 0){
                timeList = new ArrayList<>();
               // timeList.add("Select Time");

                String[] startTimeSplitted = newReservationSlots.get(catPos).getFrom().split(":");
                String[] endTimeSplitted = newReservationSlots.get(catPos).getTo().split(":");

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
                while (startCal.compareTo(endCal) < 0 ||  startCal.compareTo(endCal) == 0) {
//                        Log.i("testing", df.format(cal.getTime()));
                    timeList.add(getFormattedDate(df.format(startCal.getTime()), "HH:mm:ss", "h:mm a"));
                    startCal.add(Calendar.MINUTE, newReservationSlots.get(catPos).getInterval());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, timeList);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
                spTime.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            spTime.setSelection(((ArrayAdapter<String>) spTime.getAdapter()).getPosition(String.valueOf(getReservationsEntity.getTime())));

            if (getReservationsEntity.getNoPeople() != null)
                spTotalPeople.setSelection(((ArrayAdapter<String>) spTotalPeople.getAdapter()).getPosition(String.valueOf(getReservationsEntity.getNoPeople())));
        }
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

    private void setCategoryData() {

        categoryList = new ArrayList<>();
        categoryList.add("Select Category");
        categoryList.add("Dinner and Lounge");
        categoryList.add("Club");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, categoryList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
        spCategory.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setTimeData() {

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spCategory.getSelectedItemPosition() == 1) {

                    timeList = new ArrayList<>();
                    timeList.add("Select Time");
                    timeList.add("08:30 pm");
                    timeList.add("09:00 pm");
                    timeList.add("09:30 pm");
                    timeList.add("10:00 pm");
                    timeList.add("10:30 pm");
                    timeList.add("11:00 pm");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, timeList);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
                    spTime.setAdapter(adapter);

                } else if (spCategory.getSelectedItemPosition() == 2) {
                    timeList = new ArrayList<>();
                    timeList.add("Select Time");
                    timeList.add("11:30 pm");
                    timeList.add("12:00 am");
                    timeList.add("12:30 am");
                    timeList.add("01:00 am");
                    timeList.add("01:30 am");
                    timeList.add("02:00 am");
                    timeList.add("02:30 am");
                    timeList.add("03:00 am");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, timeList);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
                    spTime.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    timeList = new ArrayList<>();
                    timeList.add("Please Select Category First");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, timeList);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
                    spTime.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                if (spTime.getAdapter() != null)
                    if (getReservationsEntity.getTime() != null) {
                        spTime.setSelection(((ArrayAdapter<String>) spTime.getAdapter()).getPosition(String.valueOf(getReservationsEntity.getTime())));
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spTime.post(new Runnable() {
            @Override
            public void run() {


            }
        });
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
                if(position > 0){
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
                    while (startCal.compareTo(endCal) < 0 ||  startCal.compareTo(endCal) == 0) {
//                        Log.i("testing", df.format(cal.getTime()));
                        timeList.add(getFormattedDate(df.format(startCal.getTime()), "HH:mm:ss", "h:mm a"));
                        startCal.add(Calendar.MINUTE, newReservationSlots.get(position - 1).getInterval());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, timeList);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
                    spTime.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (spTime.getAdapter() != null)
                        if (getReservationsEntity.getTime() != null) {
                            spTime.setSelection(((ArrayAdapter<String>) spTime.getAdapter()).getPosition(String.valueOf(getReservationsEntity.getTime())));
                        }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        adapter.notifyDataSetChanged();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        // titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.reservations_small));
    }

    private void setTotalPeopleData() {

        totalPeopleList = new ArrayList<>();
        totalPeopleList.add("Total Number of People");
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
                        String predate = new SimpleDateFormat(AppConstants.DateFormat_DMY3).format(c.getTime());
                        selectedDate=new SimpleDateFormat(AppConstants.DateFormat_YMD2).format(c.getTime());
                        textView.setText(predate);

                        //differentFormatDate = (DateHelper.getFormatedDate(AppConstants.DateFormat_DMY3, AppConstants.DateFormat_YMD, splittedDate));
                        //textView.setText(predate);
                        textView.setPaintFlags(Typeface.BOLD);
                        //}
                    }
                }, "PreferredDate", new Date());

        datePickerHelper.showDate();
    }

    private boolean validate() {
        return edtReservationTitle.testValidity();
    }

    @OnClick({R.id.edt_select_date, R.id.btn_submit_request, R.id.btn_cancel_request})
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
                        reserveChanges();
                    }
                }
                break;
            case R.id.btn_cancel_request:
                cancelRequest();
                break;
        }
    }

    private void reserveChanges() {
/*
        if (spCategory.getSelectedItemPosition() == 1) {
            category = "dinner";
        } else if (spCategory.getSelectedItemPosition() == 2) {
            category = "club";
        }
*/
        if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getToken() != null)
            serviceHelper.enqueueCall(webService.editReservationNew(getReservationsEntity.getId(), edtSecondaryNumber.getText().toString() + "",
                    spTime.getSelectedItem().toString(), selectedDate + "", Integer.valueOf(spTotalPeople.getSelectedItem().toString()),
                    edtReservationTitle.getText().toString(), selectedCategoryID, prefHelper.getSignUpUser().getToken()),
                    WebServiceConstants.EDIT_RESERVATIONS);
    }

    private void cancelRequest() {
        serviceHelper.enqueueCall(webService.cancelReservation(getReservationsEntity.getId() + ""), WebServiceConstants.CANCEL_RESERVATIONS);
    }

    private void getReservationSlots() {
        serviceHelper.enqueueCall(webService.getReservationSlots(prefHelper.getSignUpUser().getToken()), WebServiceConstants.GET_RESERVATION_CATEGORIES);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.EDIT_RESERVATIONS:
                getDockActivity().popFragment();
                break;
            case WebServiceConstants.CANCEL_RESERVATIONS:
                getDockActivity().popFragment();
                break;
            case WebServiceConstants.GET_RESERVATION_CATEGORIES:
                newReservationSlots = (ArrayList<NewReservationEntity>) result;
                setNewCategoryData();
                setDataFromEntity();
                break;
        }
    }

    public static String getFormattedDate(String date, String oldFormat, String newFormat){
        String formattedDate= "";
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
