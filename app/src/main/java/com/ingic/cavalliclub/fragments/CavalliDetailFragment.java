package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.EntityCavalliNights;
import com.ingic.cavalliclub.entities.EntityMarkUnmarkCalendar;
import com.ingic.cavalliclub.entities.EntityPushCheck;
import com.ingic.cavalliclub.entities.EntityUpcomingEvent;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.helpers.DateHelper;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class CavalliDetailFragment extends BaseFragment {


    @BindView(R.id.iv_drinks_detail)
    ImageView ivDrinksDetail;
    @BindView(R.id.txt_heading)
    AnyTextView txtHeading;
    @BindView(R.id.tv_description)
    AnyTextView tvDescription;
    @BindView(R.id.tv_date)
    AnyTextView tvDate;
    @BindView(R.id.tv_time)
    AnyTextView tvTime;
    @BindView(R.id.btn_add_inquiry)
    Button btnAddInquiry;
    Unbinder unbinder;
    private static String CavalliNightDetailKey = "CavalliNightDetailKey";
    private static String UpComingEventsKey = "UpComingEventsKey";
    EntityCavalliNights entityCavalliNights;
    EntityUpcomingEvent entityUpcomingEvent;
    ImageLoader imageLoader;
    /* String splittedDate;
     String splittedTime;
     String newTime;*/
    @BindView(R.id.tv_location)
    AnyTextView tvLocation;
    @BindView(R.id.btn_mark_unmark)
    Button btnMarkUnmark;
    EntityPushCheck entityPushCheck;
    EntityMarkUnmarkCalendar entityMarkUnmarkCalendar;
    @BindView(R.id.btn_reserve_event)
    Button btnReserveEvent;
    private static String COMING_FROM = "";

    public static CavalliDetailFragment newInstance(EntityCavalliNights entityCavalliNights) {
        Bundle args = new Bundle();
        args.putString(CavalliNightDetailKey, new Gson().toJson(entityCavalliNights));
        CavalliDetailFragment fragment = new CavalliDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static BaseFragment newInstance(EntityUpcomingEvent entityUpcomingEvent) {
        Bundle args = new Bundle();
        args.putString(UpComingEventsKey, new Gson().toJson(entityUpcomingEvent));
        CavalliDetailFragment fragment = new CavalliDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CavalliDetailFragment newInstance() {
        Bundle args = new Bundle();

        CavalliDetailFragment fragment = new CavalliDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CavalliDetailFragment newInstance(EntityUpcomingEvent entityUpcomingEvent, String ComingFrom) {
        Bundle args = new Bundle();
        args.putString(UpComingEventsKey, new Gson().toJson(entityUpcomingEvent));
        COMING_FROM = ComingFrom;
        CavalliDetailFragment fragment = new CavalliDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CavalliDetailFragment newInstance(EntityCavalliNights entityCavalliNights, String ComingFrom) {
        Bundle args = new Bundle();
        args.putString(CavalliNightDetailKey, new Gson().toJson(entityCavalliNights));
        COMING_FROM = ComingFrom;
        CavalliDetailFragment fragment = new CavalliDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            CavalliNightDetailKey = getArguments().getString(CavalliNightDetailKey);
            UpComingEventsKey = getArguments().getString(UpComingEventsKey);
        }
        if (CavalliNightDetailKey != null) {
            entityCavalliNights = new Gson().fromJson(CavalliNightDetailKey, EntityCavalliNights.class);
        }
        if (UpComingEventsKey != null) {
            entityUpcomingEvent = new Gson().fromJson(UpComingEventsKey, EntityUpcomingEvent.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cavalli_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageLoader = ImageLoader.getInstance();

        getMainActivity().hideBottomTab();
        setCavalliNightDetailData();
        setUpcomingEventDetailData();
        setButtonData();

        if (COMING_FROM.equalsIgnoreCase("ComingFromMyProfile")) {
            btnReserveEvent.setVisibility(View.GONE);
        } else if (COMING_FROM.equalsIgnoreCase("ComingFromHome")) {
            btnReserveEvent.setVisibility(View.VISIBLE);
        } else if (COMING_FROM.equalsIgnoreCase("comingFromCalendar")) {
            btnReserveEvent.setVisibility(View.VISIBLE);
        }
    }

    private void setButtonData() {
        // if (entityUpcomingEvent != null && entityUpcomingEvent.getId() != null) {
        if (entityUpcomingEvent != null && entityUpcomingEvent.getParentId() != null) {
            serviceHelper.enqueueCall(webService.getMarkStatus(entityUpcomingEvent.getParentId() + ""), WebServiceConstants.PUSH_CHECKER);
        } else if (entityCavalliNights != null && entityCavalliNights.getParentId() != null) {
            serviceHelper.enqueueCall(webService.getMarkStatus(entityCavalliNights.getParentId() + ""), WebServiceConstants.PUSH_CHECKER);
        }
    }

    private void setCavalliNightDetailData() {

        if (entityCavalliNights != null) {

            if (entityCavalliNights != null && entityCavalliNights.getEventImages() != null && entityCavalliNights.getEventImages().size() > 0 && entityCavalliNights.getEventImages().get(0) != null && entityCavalliNights.getEventImages().size() > 0 && entityCavalliNights.getEventImages().get(0).getImageUrl() != null && entityCavalliNights.getEventImages() != null && entityCavalliNights.getEventImages().get(0).getImageUrl() != null)
                imageLoader.displayImage(entityCavalliNights.getEventImages().get(0).getImageUrl(), ivDrinksDetail);
            if (entityCavalliNights.getTitle() != null)
                txtHeading.setText(entityCavalliNights.getTitle() + "");
            if (entityCavalliNights.getDescription() != null)
                tvDescription.setText(entityCavalliNights.getDescription() + "");
            if (entityCavalliNights.getEventDate() != null) {
                tvTime.setText(DateHelper.getLocalTimeAmPm(entityCavalliNights.getEventDate()));
                tvDate.setText(DateHelper.getLocalDate(entityCavalliNights.getEventDate()));
            }

            String styledTextLocation = "<b><font color='#7D7F82'>Location: </font></b><font color='#A49262'>" + entityCavalliNights.getLocation() + "" + "</font>";
            tvLocation.setText(Html.fromHtml(styledTextLocation), TextView.BufferType.SPANNABLE);

            String styledTextTime = "<b><font color='#7D7F82'>Time: </font></b><font color='#A49262'>" + DateHelper.getLocalTimeAmPm(entityCavalliNights.getEventDate()) + "</font>";
            tvTime.setText(Html.fromHtml(styledTextTime), TextView.BufferType.SPANNABLE);

            String styledTextDate = "<b><font color='#7D7F82'>Date: </font></b><font color='#A49262'> " + DateHelper.getLocalDateEvent(entityCavalliNights.getEventDate()) + "</font>";
            tvDate.setText(Html.fromHtml(styledTextDate), TextView.BufferType.SPANNABLE);

               /* if (entityCavalliNights.getEventDate() != null) {
                String str = entityCavalliNights.getEventDate();
                String[] splited = str.split("\\s");
                splittedDate = splited[0];
                splittedTime = splited[1];
            }
*/
           /* try {
                String _24HourTime = splittedTime;
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                Date _24HourDt = _24HourSDF.parse(_24HourTime);
            *//*System.out.println(_24HourDt);
            System.out.println(_12HourSDF.format(_24HourDt));*//*
                newTime = _12HourSDF.format(_24HourDt);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            /* if (entityCavalliNights.getEvent_schedule().equalsIgnoreCase(AppConstants.ONE_TIME)) {
                if (splittedDate != null)
                    tvDate.setText(DateHelper.getLocalDate(entityCavalliNights.getEventDate()));

            } else if (entityCavalliNights.getEvent_schedule().equalsIgnoreCase(AppConstants.RECURRING)) {
                if (entityCavalliNights.getEventDay() != null && !entityCavalliNights.getEventDay().equals("")) {
                    String name = entityCavalliNights.getEventDay();
                    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                    String styledTextDate = "<b><font color='#7D7F82'>Day: </font></b><font color='#A49262'> " + name + "</font>";
                    tvDate.setText(Html.fromHtml(styledTextDate), TextView.BufferType.SPANNABLE);
                }
            }*/

        }
    }

    private void setUpcomingEventDetailData() {

        if (entityUpcomingEvent != null) {

            if (entityUpcomingEvent != null && entityUpcomingEvent.getEventImages() != null && entityUpcomingEvent.getEventImages().get(0) != null && entityUpcomingEvent.getEventImages().size() != 0 && entityUpcomingEvent.getEventImages().get(0).getImageUrl() != null && entityUpcomingEvent.getEventImages() != null && entityUpcomingEvent.getEventImages().get(0).getImageUrl() != null)
                imageLoader.displayImage(entityUpcomingEvent.getEventImages().get(0).getImageUrl(), ivDrinksDetail);
            if (entityUpcomingEvent.getTitle() != null)
                txtHeading.setText(entityUpcomingEvent.getTitle() + "");
            if (entityUpcomingEvent.getDescription() != null)
                tvDescription.setText(entityUpcomingEvent.getDescription() + "");

            if (entityUpcomingEvent.getEventDate() != null) {
                tvTime.setText(DateHelper.getLocalTimeAmPm(entityUpcomingEvent.getEventDate()));
                tvDate.setText(DateHelper.getLocalDate(entityUpcomingEvent.getEventDate()));
            }

            String styledTextLocation = "<b><font color='#7D7F82'>Location: </font></b><font color='#A49262'>" + entityUpcomingEvent.getLocation() + "" + "</font>";
            tvLocation.setText(Html.fromHtml(styledTextLocation), TextView.BufferType.SPANNABLE);

            String styledTextDate = "<b><font color='#7D7F82'>Date: </font></b><font color='#A49262'> " + DateHelper.getLocalDateEvent(entityUpcomingEvent.getEventDate()) + "</font>";
            tvDate.setText(Html.fromHtml(styledTextDate), TextView.BufferType.SPANNABLE);

            String styledTextTime = "<b><font color='#7D7F82'>Time: </font></b><font color='#A49262'>" +  DateHelper.getLocalTimeAmPm(entityUpcomingEvent.getEventDate()) + "</font>";
            tvTime.setText(Html.fromHtml(styledTextTime), TextView.BufferType.SPANNABLE);
/*
            if (entityUpcomingEvent.getIsMarked() == 0) {
                btnMarkUnmark.setText("Mark to my Calendar");
            } else {
                btnMarkUnmark.setText("Unmark from my Calendar");
            }*/

   /* if (entityUpcomingEvent.getEventDate() != null) {
                String str = entityUpcomingEvent.getEventDate();
                String[] splited = str.split("\\s");
                splittedDate = splited[0];
                splittedTime = splited[1];
            }

            try {
                String _24HourTime = splittedTime;
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                Date _24HourDt = _24HourSDF.parse(_24HourTime);
            *//*System.out.println(_24HourDt);
            System.out.println(_12HourSDF.format(_24HourDt));*//*
                newTime = _12HourSDF.format(_24HourDt);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDockActivity().onBackPressed();
                /*
                if (COMING_FROM.equalsIgnoreCase("ComingFromMyProfile")) {
                    getDockActivity().replaceDockableFragment(UpcomingEventsFragment.newInstance(), "UpcomingEventsFragment");
                } else if (COMING_FROM.equalsIgnoreCase("ComingFromHome")) {
                    getDockActivity().replaceDockableFragment(CavalliNightsFragment.newInstance(), "CavalliNightsFragment");
                } else if (COMING_FROM.equalsIgnoreCase("comingFromCalendar")) {
                    getDockActivity().replaceDockableFragment(EventsBookingFragment.newInstance(), "EventsBookingFragment");
                } else {
                    getDockActivity().replaceDockableFragment(CavalliNightsFragment.newInstance(), "CavalliNightsFragment");
                }
                */
            }
        }, getDockActivity());
        titleBar.hideTwoTabsLayout();
        // titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.details));
    }

    @OnClick({R.id.btn_mark_unmark, R.id.btn_add_inquiry, R.id.btn_reserve_event})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_mark_unmark:
                markunmarkCalendar();
                break;
            case R.id.btn_add_inquiry:
                if (entityCavalliNights != null && entityCavalliNights.getId() != null) {
                    getDockActivity().replaceDockableFragment(EventInquiryFragment.newInstance(entityCavalliNights.getId()), "EventInquiryFragment");
                } else if (entityUpcomingEvent != null && entityUpcomingEvent.getId() != null) {
                    getDockActivity().replaceDockableFragment(EventInquiryFragment.newInstance(entityUpcomingEvent.getId()), "EventInquiryFragment");
                }
                break;
            case R.id.btn_reserve_event:
                if (entityCavalliNights != null)
                    getDockActivity().replaceDockableFragment(EventReserveNowFragment.newInstance(entityCavalliNights), "EventReserveNowFragment");
                else
                    btnReserveEvent.setVisibility(View.GONE);
                break;
        }
    }

    private void markunmarkCalendar() {
        if (entityCavalliNights != null && entityCavalliNights.getParentId() != null) {
            serviceHelper.enqueueCall(webService.markUnmarkCalendar(entityCavalliNights.getParentId() + ""), WebServiceConstants.MARK_UNMARK_EVENTS);
        } else if (entityUpcomingEvent != null && entityUpcomingEvent.getParentId() != null) {
            serviceHelper.enqueueCall(webService.markUnmarkCalendar(entityUpcomingEvent.getParentId() + ""), WebServiceConstants.MARK_UNMARK_EVENTS);
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.MARK_UNMARK_EVENTS:
                entityMarkUnmarkCalendar = (EntityMarkUnmarkCalendar) result;
                recheckButtontext(entityMarkUnmarkCalendar);
                break;

            case WebServiceConstants.PUSH_CHECKER:
                entityPushCheck = (EntityPushCheck) result;
                if (entityPushCheck.getIs_marked() == 0) {
                    btnMarkUnmark.setText("Mark to my Calendar");
                } else {
                    btnMarkUnmark.setText("Unmark from my Calendar");
                }
                break;
        }
    }

    private void recheckButtontext(EntityMarkUnmarkCalendar result) {
        if (result.getIsMarked() == 0) {
            btnMarkUnmark.setText("Mark to my Calendar");
        } else {
            btnMarkUnmark.setText("Unmark from my Calendar");
        }
    }
}