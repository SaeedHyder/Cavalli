package com.application.cavalliclub.ui.binders;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.GetCompetitionEntity;
import com.application.cavalliclub.helpers.DateHelper;
import com.application.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderLatestCompetition extends ViewBinder<GetCompetitionEntity> {

    private Context context;
    private ImageLoader imageLoader;
    private static boolean isLatest;

    public BinderLatestCompetition(Context context, boolean checker) {
        super(R.layout.item_lv_latest_competitions);
        imageLoader = ImageLoader.getInstance();
        this.context = context;
        isLatest = checker;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(GetCompetitionEntity entity, int position, int grpPosition, View view, Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        imageLoader.displayImage(entity.getImageUrl(), viewHolder.ivCompetition);
        viewHolder.tvCompetitionName.setText(entity.getTitle() + "");
        viewHolder.tvDescription.setText(entity.getDescription() + "");

        if (isLatest) {
            viewHolder.llSerialDate.setVisibility(View.VISIBLE);
            viewHolder.llStaus.setVisibility(View.GONE);
            if(entity.getSerialNo()!=null)
            viewHolder.tvSerialNo.setText("Serial:" + " " + entity.getSerialNo());
            if(entity.getValidDate()!=null)
            viewHolder.tvDate.setText("Valid to" + " " +   DateHelper.dateFormat(entity.getValidDate(),"MMMM yyyy","yyyy-MM-dd"));
        }
    }

    static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_competition)
        ImageView ivCompetition;
        @BindView(R.id.tv_competition_name)
        AnyTextView tvCompetitionName;
        @BindView(R.id.tv_description)
        AnyTextView tvDescription;

        @BindView(R.id.ll_serial_date)
        LinearLayout llSerialDate;
        @BindView(R.id.tv_serial_no)
        AnyTextView tvSerialNo;
        @BindView(R.id.tv_date)
        AnyTextView tvDate;

        @BindView(R.id.ll_staus)
        LinearLayout llStaus;
        @BindView(R.id.iv_status)
        ImageView ivStatus;
        @BindView(R.id.tv_status)
        AnyTextView tvStatus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

