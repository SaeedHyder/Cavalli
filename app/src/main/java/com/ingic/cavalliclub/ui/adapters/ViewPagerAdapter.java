package com.ingic.cavalliclub.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ingic.cavalliclub.fragments.MusicPager1;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    final int PAGE_COUNT = 3;
    private Context context;
    private ArrayList<Fragment> registeredFragment;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
/*        this.context = context;
        registeredFragment = new ArrayList<>();
        registeredFragment.add(new MusicPager1());
        registeredFragment.add(new MusicPager1());
        registeredFragment.add(new MusicPager1());*/
    }

    public ViewPagerAdapter(FragmentManager childFragmentManager) {
        super(childFragmentManager);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MusicPager1.newInstance();
            case 1:
                return MusicPager1.newInstance();
            case 2:
                return MusicPager1.newInstance();
            default:
                return MusicPager1.newInstance();
        }
    }


/*    @Override
    public Fragment getItem(int position) {
        *//*return PageFragment.newInstance(position + 1);*//*
        if (registeredFragment!=null&&registeredFragment.size()<position){
            return registeredFragment.get(position);
        }else {
            return new MusicPager1();
        }
    }*/
}
