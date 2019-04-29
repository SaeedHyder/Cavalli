package com.application.cavalliclub.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.application.cavalliclub.entities.EntityCavalliNights;

import java.util.ArrayList;
import java.util.List;


public class CavalliNightsViewPagerAdapter extends FragmentStatePagerAdapter {

    //getChildFragmentManager
    //SimpleFragmentStatePagerAdapter
    final int PAGE_COUNT = 3;
    private Context context;
    private ArrayList<Fragment> registeredFragment;
    private List<EntityCavalliNights> entityCavalliNights;
    private List<Fragment> myFragments;
    public static int pos = 0;

    public CavalliNightsViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
/*        this.context = context;
        registeredFragment = new ArrayList<>();
        registeredFragment.add(new MusicPager1());
        registeredFragment.add(new MusicPager1());
        registeredFragment.add(new MusicPager1());*/
    }

    public CavalliNightsViewPagerAdapter(FragmentManager childFragmentManager, ArrayList<EntityCavalliNights> entityCavalliNights,List<Fragment> myFragments) {
        super(childFragmentManager);
        this.entityCavalliNights = entityCavalliNights;
        this.myFragments=myFragments;
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
        CavalliNightsViewPagerAdapter.pos = pos;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        setPos(position);
        return "Page " + position;
    }
}
