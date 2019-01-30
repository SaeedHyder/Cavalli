package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.LatestUpdatesEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.helpers.InternetHelper;
import com.ingic.cavalliclub.ui.adapters.ArrayListAdapter;
import com.ingic.cavalliclub.ui.binders.BinderLatestUpdatesMusic;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LatestUpdatesMusicFragment extends BaseFragment {

    @BindView(R.id.lv_latest_updates_music)
    ListView lvLatestUpdatesMusic;
    Unbinder unbinder;
    private static int POS;
    ArrayList<LatestUpdatesEntity> latestUpdatesEntities1;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    private ArrayList<LatestUpdatesEntity> latestUpdatesMusicCollection = new ArrayList<>();
    private ArrayListAdapter<LatestUpdatesEntity> adapter;

    public static LatestUpdatesMusicFragment newInstance(int pos) {
        POS = pos;
        return new LatestUpdatesMusicFragment();
    }

    public static LatestUpdatesMusicFragment newInstance() {
        return new LatestUpdatesMusicFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderLatestUpdatesMusic(getDockActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_updates_music, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMyReservationsList();
        listViewOnClickListner();
    }

    private void getMyReservationsList() {

        bindData(latestUpdatesMusicCollection);
    }

    private void bindData(ArrayList<LatestUpdatesEntity> latestUpdatesEntities) {

        adapter.clearList();
        lvLatestUpdatesMusic.setAdapter(adapter);

        latestUpdatesEntities1 = new Gson().fromJson(prefHelper.getLatestUpdates(), new TypeToken<ArrayList<LatestUpdatesEntity>>() {
        }.getType());

        adapter.addAll(latestUpdatesEntities1);
        adapter.notifyDataSetChanged();

        scrollPosition();

        if (latestUpdatesEntities1.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvLatestUpdatesMusic.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvLatestUpdatesMusic.setVisibility(View.VISIBLE);
        }
    }

    private void scrollPosition() {

        lvLatestUpdatesMusic.smoothScrollToPosition(POS);
    }

    private void listViewOnClickListner() {
        lvLatestUpdatesMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    getDockActivity().replaceDockableFragment(LatestUpdateDetailFragment.newInstance(latestUpdatesEntities1.get(i)), "LatestUpdatesMusicFragment");
                }
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
       // titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.latest_updatess));
    }
}


