package com.application.cavalliclub.fragments;

import com.application.cavalliclub.R;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ahmedsyed on 10/9/2018.
 */
public class StartetViewPagerMenuFragment extends BaseFragment {
    public static StartetViewPagerMenuFragment newInstance() {
        Bundle args = new Bundle();

        StartetViewPagerMenuFragment fragment = new StartetViewPagerMenuFragment();
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
        View view = inflater.inflate(R.layout.fragment_starter_view_pager, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
