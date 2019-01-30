package com.ingic.cavalliclub.fragments;

import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.GetReservationsEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.helpers.DateHelper;
import com.ingic.cavalliclub.helpers.DatePickerHelper;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.ui.views.AnyEditTextView;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UpdatedEventReserveFragment extends BaseFragment {


    Unbinder unbinder;
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
    @BindView(R.id.tv_time_day_date)
    AnyTextView tvTimeDayDate;
    @BindView(R.id.btn_cancel_request)
    Button btnCancelRequest;
    String DateFormatted;
    @BindView(R.id.ll_mainFrame)
    LinearLayout ll_mainFrame;


    private Date DateSelected;
    private ArrayList<String> totalPeopleList = new ArrayList<>();
    private static String updateReserveData = "updateReserveData";
    private GetReservationsEntity getReservationsEntity;
    String differentFormatDate;
    private ArrayList<String> categoryList = new ArrayList<>();
    private ArrayList<String> timeList = new ArrayList<>();
    private String selectedDate = "";
    private String selectedTime = "";

    public static UpdatedEventReserveFragment newInstance() {
        return new UpdatedEventReserveFragment();
    }

    public static UpdatedEventReserveFragment newInstance(GetReservationsEntity getReservationsEntity) {

        Bundle args = new Bundle();
        args.putString(updateReserveData, new Gson().toJson(getReservationsEntity));
        UpdatedEventReserveFragment fragment = new UpdatedEventReserveFragment();
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
        View view = inflater.inflate(R.layout.fragment_event_updated_reserve, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        setTotalPeopleData();
        setDataFromEntity();
        setDataFromPreferences();
        edtSecondaryNumber.setHint(Html.fromHtml("<font color='#858992'>Secondary Phone Number</font>  <font color='#D1D1D1'>(Optional)</font>"));
    }

    private void setDataFromEntity() {


        if (getReservationsEntity != null) {
            if (getReservationsEntity.getTitle() != null)
                edtReservationTitle.setText(getReservationsEntity.getTitle() + "");
            if (getReservationsEntity.getSecondaryPhoneNo() != null)
                edtSecondaryNumber.setText(getReservationsEntity.getSecondaryPhoneNo() + "");
            if (getReservationsEntity.getDate() != null && getReservationsEntity.getTime() != null) {
                tvTimeDayDate.setText(DateHelper.getLocalDateEventReservation(getReservationsEntity.getDate()) + "  |  " + getReservationsEntity.getTime());
                selectedDate = getReservationsEntity.getDate();
                selectedTime = getReservationsEntity.getTime();
            } else if (getReservationsEntity.getDay() != null && !getReservationsEntity.getDay().equals("null") && !getReservationsEntity.getDay().equals("") && getReservationsEntity.getTime() != null) {
                tvTimeDayDate.setText(getReservationsEntity.getDay() + ", " + getReservationsEntity.getTime());
                selectedDate = getReservationsEntity.getDay() + "";
                selectedTime = getReservationsEntity.getTime();

            } else if(getReservationsEntity.getDate() != null && getReservationsEntity.getDate().equals("null") && !getReservationsEntity.getDate().equals("")) {
                tvTimeDayDate.setText(getReservationsEntity.getDate()+"");
                selectedDate = getReservationsEntity.getDate() + "";
            }else if(getReservationsEntity.getDay() != null && !getReservationsEntity.getDay().equals("null") && !getReservationsEntity.getDay().equals("")){
                tvTimeDayDate.setText(getReservationsEntity.getDay());
                selectedDate = getReservationsEntity.getDay() + "";
            }else{
                tvTimeDayDate.setText("-");
            }

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
                        //  splittedDate = splited[0];


                     /*   textView.setText(DateHelper.getFormatedDate(AppConstants.DateFormat_YMD, AppConstants.DateFormat_DMY3,
                                splittedDate));*/

                        //differentFormatDate = (DateHelper.getFormatedDate(AppConstants.DateFormat_DMY3, AppConstants.DateFormat_YMD, splittedDate));
                        //textView.setText(predate);
                        textView.setPaintFlags(Typeface.BOLD);
                        //}
                    }
                }, "PreferredDate", new Date());

        datePickerHelper.showDate();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        //   titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
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

    @OnClick({R.id.edt_select_date, R.id.btn_submit_request, R.id.btn_cancel_request})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edt_select_date:
                //initFromPickerValidated(edtSelectDate);
                break;
            case R.id.btn_submit_request:

                if (spTotalPeople.getSelectedItemPosition() == 0) {
                    UIHelper.showLongToastInCenter(getDockActivity(), getString(R.string.please_select_people));
                } else {
                    reserveChanges();
                }
                break;
            case R.id.btn_cancel_request:
                cancelRequest();
                break;
        }
    }

    private void reserveChanges() {
        if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getToken() != null) {
            serviceHelper.enqueueCall(webService.editReservation(getReservationsEntity.getId(), edtSecondaryNumber.getText().toString() + "",
                    selectedTime, selectedDate + "",
                    Integer.valueOf(spTotalPeople.getSelectedItem().toString()),
                    edtReservationTitle.getText().toString(), "", prefHelper.getSignUpUser().getToken()), WebServiceConstants.EDIT_RESERVATIONS);
        }
       /* if (!dayDateChecker) {
            if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getToken() != null) {
                serviceHelper.enqueueCall(webService.editReservation(getReservationsEntity.getId(), edtSecondaryNumber.getText().toString() + "",
                        tvTime.getText().toString() + "", splittedDate + "",
                        Integer.valueOf(spTotalPeople.getSelectedItem().toString()),
                        edtReservationTitle.getText().toString(), "", prefHelper.getSignUpUser().getToken()), WebServiceConstants.EDIT_RESERVATIONS);
            }
        } else {
            serviceHelper.enqueueCall(webService.editReservationDay(getReservationsEntity.getId(), edtSecondaryNumber.getText().toString() + "",
                    tvTime.getText().toString() + "", getReservationsEntity.getDay() + "",
                    Integer.valueOf(spTotalPeople.getSelectedItem().toString()),
                    edtReservationTitle.getText().toString(), "", prefHelper.getSignUpUser().getToken()), WebServiceConstants.EDIT_RESERVATIONS);
        }*/
    }

    private void cancelRequest() {
        serviceHelper.enqueueCall(webService.cancelReservation(getReservationsEntity.getId() + ""), WebServiceConstants.CANCEL_RESERVATIONS);
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
        }
    }
}
