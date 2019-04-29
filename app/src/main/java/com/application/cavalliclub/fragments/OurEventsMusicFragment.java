package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.OurEventsEnt;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.OurEventsItemBinder;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by saeedhyder on 1/30/2018.
 */
public class OurEventsMusicFragment extends BaseFragment {
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.lv_OurEvents)
    ListView lvOurEvents;
    Unbinder unbinder;

    private ArrayList<OurEventsEnt> collection ;
    private ArrayListAdapter<OurEventsEnt> adapter;

    public static OurEventsMusicFragment newInstance() {
        Bundle args = new Bundle();

        OurEventsMusicFragment fragment = new OurEventsMusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayListAdapter<>(getDockActivity(), new OurEventsItemBinder(getDockActivity()));
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_our_events, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().showBottomTab(AppConstants.home);

        bindData();
    }

    private void bindData() {
        collection=new ArrayList<>();
        collection.add(new OurEventsEnt( "drawable://" + R.drawable.title_header,"New Year Party Package", getString(R.string.lorem_ipsum_small)));
        collection.add(new OurEventsEnt( "drawable://" + R.drawable.title_header,"New Year Party Package", getString(R.string.lorem_ipsum_small)));
        collection.add(new OurEventsEnt( "drawable://" + R.drawable.title_header,"New Year Party Package", getString(R.string.lorem_ipsum_small)));
        collection.add(new OurEventsEnt( "drawable://" + R.drawable.title_header,"New Year Party Package", getString(R.string.lorem_ipsum_small)));

        adapter.clearList();
        lvOurEvents.setAdapter(adapter);
        adapter.addAll(collection);
        adapter.notifyDataSetChanged();
    }



    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        //titleBar.showfilterButton();
    //    titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.hideTwoTabsLayout();
        titleBar.setSubHeading(getString(R.string.our_events));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
