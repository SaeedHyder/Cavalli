package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.EntityProfileEvents;
import com.application.cavalliclub.entities.EntityUpcomingEvent;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.DateHelper;
import com.application.cavalliclub.interfaces.ViewPagerClickListner;
import com.application.cavalliclub.ui.adapters.UpcomingEventPagerAdapter;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventHistoryFragment extends BaseFragment implements ViewPagerClickListner {

    UpcomingEventPagerAdapter adapter;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tv_event_title)
    AnyTextView tvEventTitle;
    @BindView(R.id.tv_date)
    AnyTextView tvDate;
    @BindView(R.id.tv_reserved)
    AnyTextView tvReserved;
    Unbinder unbinder;
    EntityProfileEvents entityProfileEvents;
    ArrayList<EntityUpcomingEvent> entityHistoryEvent = new ArrayList<>();
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    private ArrayList<EntityUpcomingEvent> userCollection;
    private int pos = -1;

    public static EventHistoryFragment newInstance() {
        return new EventHistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new UpcomingEventPagerAdapter(getFragmentManager(), getDockActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_events, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setPagerSetting();
        getEventsProfileHistory();
        tvReserved.setVisibility(View.INVISIBLE);
    }

    private void setPagerSetting() {
        pager.setClipToPadding(false);
        pager.setPageMargin(10);
       /* pager.setPadding(20, 8, 20, 8);
        pager.setOffscreenPageLimit(3);*/
        pager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                int pageWidth = pager.getMeasuredWidth() -
                        pager.getPaddingLeft() - pager.getPaddingRight();
                int pageHeight = pager.getHeight();
                int paddingLeft = pager.getPaddingLeft();
                float transformPos = (float) (page.getLeft() -
                        (pager.getScrollX() + paddingLeft)) / pageWidth;
                int max = pageHeight / 10;

                if (transformPos < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.setAlpha(0.7f);// to make left transparent
                    page.setScaleY(0.9f);
                } else if (transformPos <= 1) { // [-1,1]
                    page.setAlpha(1f);
                    page.setScaleY(1f);
                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.setAlpha(0.7f);// to make right transparent
                    page.setScaleY(0.9f);
                }
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading("My Events");
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              getDockActivity().popFragment();
            }
        },getDockActivity());
      //  titleBar.setTitlebarBackgroundColor(R.drawable.tab_header,getDockActivity());
        titleBar.showTwoTabsLayout(false, "Upcoming Events", "History");
        titleBar.setLayout_below();
        titleBar.tabsClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().popFragment();
                getDockActivity().replaceDockableFragment(UpcomingEventsFragment.newInstance(), "UpcomingEventsFragment");
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().popFragment();
                getDockActivity().replaceDockableFragment(EventHistoryFragment.newInstance(), "EventHistoryFragment");
            }
        });
    }

    private void getEventsProfileHistory() {
        serviceHelper.enqueueCall(webService.eventsProfile(), WebServiceConstants.HISTORY_EVENTS);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.HISTORY_EVENTS:
                entityProfileEvents = (EntityProfileEvents) result;
                entityHistoryEvent = entityProfileEvents.getRecentEvents();


                if (entityHistoryEvent.size() > 0) {
                    setHistoryEventsData(entityHistoryEvent);
                    txtNoData.setVisibility(View.GONE);
                    llMain.setVisibility(View.VISIBLE);
                } else {
                    txtNoData.setVisibility(View.VISIBLE);
                    llMain.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void setHistoryEventsData(ArrayList<EntityUpcomingEvent> entityHistoryEvent) {
        userCollection = new ArrayList<>();
        userCollection.addAll(entityHistoryEvent);
        bindData(userCollection);

        if (userCollection != null && userCollection.size() > 0 && userCollection.get(0) != null) {
            if (userCollection.get(0).getTitle() != null)
                tvEventTitle.setText(userCollection.get(0).getTitle() + "");
            if (userCollection.get(0).getEventDate() != null)
                tvDate.setText(DateHelper.getPagerDate(userCollection.get(0).getEventDate()));
                //tvDate.setText(DateHelper.getFormatedDate(AppConstants.DateFormat_YMDHMS, AppConstants.DateFormat_EMD, userCollection.get(0).getEventDate()));
        }
    }

    private void bindData(final ArrayList<EntityUpcomingEvent> userCollection) {
        List<Fragment> mylist = new ArrayList<>();
        UpcomingEventsPager fragment;
        for (int i = 0; i < userCollection.size(); i++) {
            fragment = new UpcomingEventsPager();
            fragment.setCheck(true);
            fragment.setInterFaceListner(this);
            fragment.setPagerData(userCollection.get(i));
            mylist.add(fragment);
        }

        adapter = new UpcomingEventPagerAdapter(getChildFragmentManager(), userCollection, mylist);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(userCollection.size());
        pager.setCurrentItem(0, true);
        pos = 0;

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
                tvEventTitle.setText(userCollection.get(position).getTitle() + "");
                tvDate.setText(DateHelper.getPagerDate(userCollection.get(position).getEventDate()));
                //tvDate.setText(DateHelper.getFormatedDate(AppConstants.DateFormat_YMDHMS, AppConstants.DateFormat_EMD, userCollection.get(position).getEventDate()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        getMainActivity().showBottomTab(AppConstants.home);
    }

    @Override
    public void onClick() {
        /*if (pos != -1)
            getDockActivity().replaceDockableFragment(CavalliDetailFragment.newInstance(userCollection.get(pos)), "CavalliDetailFragment");*/
    }
}
