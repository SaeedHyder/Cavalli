package com.ingic.cavalliclub.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.EntityCavalliNights;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.helpers.DateHelper;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.ui.views.TitleBar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class EventsBookingFragment extends BaseFragment implements OnDateSelectedListener {

    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;

    @BindView(R.id.btn_view_events)
    Button btnViewEvents;
    String DATE;
    String DAY;
    String MONTH;
    String YEAR;
    String EVENT_DATE_FOR_ARRAY;
    int intYEAR;
    int intMonth;
    int intDay;
    String splittedDay;
    String splittedMonth;
    String splittedYear;
    String splittedEventDate;
    String wholeDate;
    Unbinder unbinder;
    ArrayList<String> stringArrayList;
    Date date2;
    String eventDate;
    int x;
    int i;
    private int selectedPosition = 0;
    private ArrayList<EntityCavalliNights> calendarList = new ArrayList<>();
    private ArrayList<EntityCavalliNights> userCollection;

    public static EventsBookingFragment newInstance() {
        Bundle args = new Bundle();

        EventsBookingFragment fragment = new EventsBookingFragment();
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
        View view = inflater.inflate(R.layout.fragment_events_booking, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        getMainActivity().showBottomTab(AppConstants.eventsBooking);
        DAY = "1";
        MONTH = String.valueOf(calendarView.getCurrentDate().getMonth() + 1);
        YEAR = String.valueOf(calendarView.getCurrentDate().getYear());
        DATE = DAY + "-" + MONTH + "-" + YEAR;
        getCallendarEvents();
        forwardAndBackwardCalendarListners();


        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (isSelectedDate(date)) {
                    getDockActivity().replaceDockableFragment(CavalliDetailFragment.newInstance(calendarList.get(selectedPosition), "comingFromCalendar"), "CavalliDetailFragment");
                } else {
//                    UIHelper.showShortToastInCenter(getDockActivity(), "No event marked against this date.");
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String calendarDate = null;
                    calendarDate = dateFormat.format(date.getDate());
                    prefHelper.setStringPreference(AppConstants.BOOKING_DATE_KEY, calendarDate);
                    getDockActivity().replaceDockableFragment(ReserveNowfragment.newInstance(), "ReserveNowfragment");
                }
            }
        });

       /* calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                if (calendarView.getSelectedDates().size() != 0) {
                    for (i = 0; i < calendarView.getSelectedDates().size(); i++) {

                        date2 = new Date();
                        date2 = calendarView.getSelectedDates().get(i).getDate();

                        //for already selected days
                        String str = String.valueOf(calendarView.getSelectedDates().get(i).getDate());
                        String[] splited = str.split("\\s");
                        splittedYear = splited[5];
                        splittedMonth = splited[1];
                        splittedDay = splited[2];
                        wholeDate = splittedYear + " " + splittedMonth + " " + splittedDay;

                        String date5 = wholeDate;
                        SimpleDateFormat spf = new SimpleDateFormat("yyyy MMM dd");
                        Date newDate = null;
                        try {
                            newDate = spf.parse(date5);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        spf = new SimpleDateFormat("yyyy MM dd");
                        date5 = spf.format(newDate);

                        //for current selected days
                        String strCurrentSelected = String.valueOf(date.getDate());
                        String[] splitedCurrentSelected = strCurrentSelected.split("\\s");
                        splittedYear = splitedCurrentSelected[5];
                        splittedMonth = splitedCurrentSelected[1];
                        splittedDay = splitedCurrentSelected[2];
                        wholeDate = splittedYear + " " + splittedMonth + " " + splittedDay;

                        String dateCurrentSelected = wholeDate;
                        SimpleDateFormat spfCurrentSlected = new SimpleDateFormat("yyyy MMM dd");
                        Date newDateCurrentSelected = null;
                        try {
                            newDateCurrentSelected = spfCurrentSlected.parse(dateCurrentSelected);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        spfCurrentSlected = new SimpleDateFormat("yyyy MM dd");
                        dateCurrentSelected = spfCurrentSlected.format(newDateCurrentSelected);


                        if ((date5.equalsIgnoreCase(dateCurrentSelected))) {
                            //cavalliDateail
                            for (x = 0; x < calendarList.size(); x++) {
                                eventDate = calendarList.get(x).getEventDate();

                                String strEventDate = eventDate;
                                String[] splitedEventDate = strEventDate.split("\\s");
                                splittedEventDate = splitedEventDate[0];

                                String strSplittedEventDate = splittedEventDate;
                                String[] splitedEventDateExtra = strSplittedEventDate.split("\\-");
                                splittedYear = splitedEventDateExtra[0];
                                splittedMonth = splitedEventDateExtra[1];
                                splittedDay = splitedEventDateExtra[2];

                                wholeDate = splittedYear + " " + splittedMonth + " " + splittedDay;

                                if ((date5.equalsIgnoreCase(wholeDate))) {
                                    getDockActivity().replaceDockableFragment(CavalliDetailFragment.newInstance(calendarList.get(x), "comingFromCalendar"), "CavalliDetailFragment");
                                } else {

                                    splittedYear = splitedEventDateExtra[0];
                                    splittedMonth = splitedEventDateExtra[1];
                                    int dayIncrement = Integer.parseInt(splitedEventDateExtra[2]);
                                    dayIncrement = dayIncrement + 1;
                                    splittedDay = String.valueOf(dayIncrement);

                                    wholeDate = splittedYear + " " + splittedMonth + " " + splittedDay;
                                    if ((date5.equalsIgnoreCase(wholeDate))) {
                                        getDockActivity().replaceDockableFragment(CavalliDetailFragment.newInstance(calendarList.get(x), "comingFromCalendar"), "CavalliDetailFragment");
                                    }
                                }
                            }
                        } else {
                            UIHelper.showShortToastInCenter(getDockActivity(), "No event marked against this date.");
                        }
                    }
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), "No event marked against this date.");
                }
            }
        });*/

    }

    private boolean isSelectedDate(CalendarDay date) {

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        boolean isSelected = false;
        //Date calDate = new Date();
        selectedPosition = 0;

        if (calendarList.size() > 0) {

            for (int i = 0; i < calendarList.size(); i++) {
                String calendarDate = "";
                try {
                    calendarDate = dateFormat.format(formatter.parse(calendarList.get(i).getEventDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                String selectedDate = dateFormat.format(date.getDate());

                if (selectedDate.equalsIgnoreCase(calendarDate)) {
                    isSelected = true;
                    selectedPosition = i;
                }
            }
            if (isSelected)
                return true;
            else {
                return false;
            }
        } else {
            return false;
        }

      /*  if (calendarView.getSelectedDates().size() > 0) {

            for (int i = 0; i < calendarView.getSelectedDates().size(); i++) {
                calDate = calendarView.getSelectedDates().get(i).getDate();
                String calendarDate = dateFormat.format(calDate);

                String selectedDate = dateFormat.format(date.getDate());

                if (selectedDate.equalsIgnoreCase(calendarDate)) {
                    isSelected = true;
                    selectedPosition=i;
                }
            }
            if (isSelected)
                return true;
            else {
                return false;
            }
        } else {
            return false;
        }*/
    }

    private void forwardAndBackwardCalendarListners() {

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                DAY = "1";
                MONTH = String.valueOf(calendarView.getCurrentDate().getMonth() + 1);
                YEAR = String.valueOf(calendarView.getCurrentDate().getYear());
                DATE = DAY + "-" + MONTH + "-" + YEAR;
                getCallendarEvents();
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        //    titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.hideTwoTabsLayout();
        /*
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
            }
        }, getDockActivity());
        */
        titleBar.setSubHeading(getString(R.string.event_calendar));
        titleBar.getAndShowBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        UIHelper.showShortToastInCenter(getDockActivity(), "checking ");
    }

    private void getCallendarEvents() {
        serviceHelper.enqueueCall(webService.calendarEvents(DATE + ""), WebServiceConstants.CALENDAR_EVENTS);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.CALENDAR_EVENTS:
                calendarList = (ArrayList<EntityCavalliNights>) result;
                int marks = 0;
                for(int i = 0; i < calendarList.size(); i++){
                    if(calendarList.get(i).getIsMarked() == 1){
                        marks += 1;
                    }
                }
//                Log.i("testing", marks + "");
                setCallendarData(calendarList);

                break;
        }
    }

    private void setCallendarData(ArrayList<EntityCavalliNights> calendarList) {

        stringArrayList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        calendarView.removeDecorators();
        calendarView.invalidateDecorators();

        ArrayList<EventDecorator> eventDecorators = new ArrayList<>();

        for (int i = 0; i < calendarList.size(); i++) {

            intYEAR = Integer.valueOf(DateHelper.getFormatedDate(AppConstants.DateFormat_YMDHMS, AppConstants.DateFormat_yyy,
                    calendarList.get(i).getEventDate()));
            intMonth = (DateHelper.getFormatedDateCalendar(AppConstants.DateFormat_YMDHMS, AppConstants.DateFormat_DMY5,
                    String.valueOf(calendarList.get(i).getEventDate()))).getMonth();

            //intDay = Integer.valueOf(DateHelper.getFormatedDate(AppConstants.DateFormat_YMDHMS, "dd", calendarList.get(i).getEventDate()));
            intDay = Integer.valueOf(DateHelper.getCalendarLocalDate(calendarList.get(i).getEventDate()));

            EVENT_DATE_FOR_ARRAY = String.valueOf(intYEAR + " " + intMonth + " " + intDay);
            stringArrayList.add(EVENT_DATE_FOR_ARRAY);

            calendar.set(intYEAR, intMonth, intDay - 1);
            calendarView.clearSelection();
//            calendarView.setDateSelected(calendar, true);
//            calendarView.invalidate();
            SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.DateFormat_YMDHMS);
            Date myDate = null;
            try {
                myDate = dateFormat.parse(calendarList.get(i).getEventDate());
                CalendarDay day = CalendarDay.from(myDate);
                eventDecorators.add(new EventDecorator(calendarList.get(i).getIs_grand(), calendarList.get(i).getIsMarked(), day));

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

        calendarView.addDecorators(eventDecorators);

    }

    @OnClick(R.id.btn_view_events)
    public void onViewClicked() {
        getDockActivity().replaceDockableFragment(CavalliNightsFragment.newInstance(), "CavalliNightsFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        // calendarView.clearSelection();
      /*  DAY = "1";
        MONTH = String.valueOf(calendarView.getCurrentDate().getMonth());
        YEAR = String.valueOf(calendarView.getCurrentDate().getYear());
        DATE = DAY + "-" + MONTH + "-" + YEAR;
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        getCallendarEvents();*/
    }

    class EventDecorator implements DayViewDecorator {
        private int grand, marked;
        private CalendarDay date;

        public EventDecorator(int grand, int marked, CalendarDay date) {
            this.marked = marked;
            this.grand = grand;
            this.date = date;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return this.date.toString().equals(day.toString());
        }

        @Override
        public void decorate(DayViewFacade view) {
            if (marked == 1) {
                view.setBackgroundDrawable(getDockActivity().getResources().getDrawable(R.drawable.circle));
            }

            if (grand == 1) {
                view.setBackgroundDrawable(getDockActivity().getResources().getDrawable(R.drawable.circle_gold));
            }
        }
    }
}
