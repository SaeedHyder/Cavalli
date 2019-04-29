package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.EntityCavalliNights;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.BinderSeeAllMusicFragment;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SeeAllMusicFragment extends BaseFragment {

    @BindView(R.id.lv_latest_updates_music)
    ListView lvLatestUpdatesMusic;
    Unbinder unbinder;
    private static int POS;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;

    private ArrayList<EntityCavalliNights> latestUpdatesMusicCollection = new ArrayList<>();
    private ArrayListAdapter<EntityCavalliNights> adapter;

    public static SeeAllMusicFragment newInstance(int pos) {
        POS = pos;
        return new SeeAllMusicFragment();
    }


    public static SeeAllMusicFragment newInstance() {
        return new SeeAllMusicFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderSeeAllMusicFragment(getDockActivity()));
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
        getMainActivity().hideBottomTab();
        getMusicList();
    }

    private void getMusicList() {

        bindData(latestUpdatesMusicCollection);
    }

    private void scrollPosition(){

        lvLatestUpdatesMusic.smoothScrollToPosition(POS);
    }

    private void bindData(ArrayList<EntityCavalliNights> latestUpdatesEntities) {

        adapter.clearList();
        lvLatestUpdatesMusic.setAdapter(adapter);

        ArrayList<EntityCavalliNights> entityCavalliNights = new Gson().fromJson(prefHelper.getMusicUpdates(), new TypeToken<ArrayList<EntityCavalliNights>>() {
        }.getType());

        adapter.addAll(entityCavalliNights);
        adapter.notifyDataSetChanged();

        if (entityCavalliNights.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvLatestUpdatesMusic.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvLatestUpdatesMusic.setVisibility(View.VISIBLE);
        }

        scrollPosition();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().popFragment();
            }
        },getDockActivity());
        titleBar.setSubHeading("Our Events");
    }
}
