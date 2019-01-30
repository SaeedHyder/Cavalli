package com.ingic.cavalliclub.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ingic.cavalliclub.entities.EntityUpcomingEvent;

import java.util.ArrayList;
import java.util.List;

public class UpcomingEventPagerAdapter extends FragmentStatePagerAdapter {

    //getChildFragmentManager
    //SimpleFragmentStatePagerAdapter
    final int PAGE_COUNT = 3;
    private Context context;
    private ArrayList<Fragment> registeredFragment;
    private List<EntityUpcomingEvent> entityUpcomingEvents;
    private List<Fragment> myFragments;
    public static int pos = 0;

    public UpcomingEventPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
/*        this.context = context;
        registeredFragment = new ArrayList<>();
        registeredFragment.add(new MusicPager1());
        registeredFragment.add(new MusicPager1());
        registeredFragment.add(new MusicPager1());*/
    }

    public UpcomingEventPagerAdapter(FragmentManager childFragmentManager, ArrayList<EntityUpcomingEvent> entityUpcomingEvents, List<Fragment> myFragments) {
        super(childFragmentManager);
        this.entityUpcomingEvents = entityUpcomingEvents;
        this.myFragments=myFragments;
    }

    public UpcomingEventPagerAdapter(FragmentManager childFragmentManager) {
        super(childFragmentManager);
    }

    @Override
    public int getCount() {
        return myFragments.size();
    }

    public Fragment getItem(int position) {
        return myFragments.get(position);
    }

    public int getPos() {
        return pos;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    public static void setPos(int pos) {
        UpcomingEventPagerAdapter.pos = pos;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        setPos(position);
        return "Page " + position;
    }
}
