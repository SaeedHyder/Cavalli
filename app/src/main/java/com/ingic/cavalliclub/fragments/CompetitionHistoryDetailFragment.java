package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.GetCompetitionHistoryEntity;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CompetitionHistoryDetailFragment extends BaseFragment {
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.titleBackBtn)
    ImageView titleBackBtn;
    @BindView(R.id.txt_heading)
    AnyTextView txtHeading;
    @BindView(R.id.txt_description)
    AnyTextView txtDescription;
    Unbinder unbinder;

    private static String competitionDetailkey = "competitionDetailkey";
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.tv_status)
    AnyTextView tvStatus;
    @BindView(R.id.ll_staus)
    LinearLayout llStaus;
    private GetCompetitionHistoryEntity competitionHistoryEntity;

    public static CompetitionHistoryDetailFragment newInstance(GetCompetitionHistoryEntity competitionHistoryEntity) {
        Bundle args = new Bundle();
        args.putString(competitionDetailkey, new Gson().toJson(competitionHistoryEntity));
        CompetitionHistoryDetailFragment fragment = new CompetitionHistoryDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CompetitionHistoryDetailFragment newInstance() {
        Bundle args = new Bundle();
        CompetitionHistoryDetailFragment fragment = new CompetitionHistoryDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            competitionDetailkey = getArguments().getString(competitionDetailkey);
            if (competitionDetailkey != null) {
                competitionHistoryEntity = new Gson().fromJson(competitionDetailkey, GetCompetitionHistoryEntity.class);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competition_history_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.hideTwoTabsLayout();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().popFragment();
            }
        },getDockActivity());
      //  titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.details));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setData();
    }

    private void setData() {

        if (competitionHistoryEntity.getTitle() != null)
            txtHeading.setText(competitionHistoryEntity.getTitle() + "");

        if (competitionHistoryEntity.getDescription() != null)
            txtDescription.setText(competitionHistoryEntity.getDescription() + "");

        if (competitionHistoryEntity.getCompetitionImages().get(0).getImageUrl() != null)
            ImageLoader.getInstance().displayImage(competitionHistoryEntity.getCompetitionImages().get(0).getImageUrl(), ivHeader);

        if (competitionHistoryEntity.getStatus().equalsIgnoreCase(getDockActivity().getString(R.string.status_pending))) {
            ivStatus.setImageResource(R.drawable.pending);
            tvStatus.setTextColor(getDockActivity().getResources().getColor(R.color.pending));
            tvStatus.setText(competitionHistoryEntity.getStatus());
        } else if (competitionHistoryEntity.getStatus().equalsIgnoreCase(getDockActivity().getString(R.string.status_won))) {
            ivStatus.setImageResource(R.drawable.reserved);
            tvStatus.setTextColor(getDockActivity().getResources().getColor(R.color.reserved));
            tvStatus.setText(competitionHistoryEntity.getStatus());
        } else if (competitionHistoryEntity.getStatus().equalsIgnoreCase(getDockActivity().getString(R.string.status_loss))) {
            ivStatus.setImageResource(R.drawable.rejected);
            tvStatus.setTextColor(getDockActivity().getResources().getColor(R.color.rejected));
            tvStatus.setText(competitionHistoryEntity.getStatus());
        }
    }
}
