package com.application.cavalliclub.ui.binders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.GetCompetitionHistoryEntity;
import com.application.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderCompetitionHistory extends ViewBinder<GetCompetitionHistoryEntity> {

    private Context context;
    private ImageLoader imageLoader;

    public BinderCompetitionHistory(Context context) {
        super(R.layout.item_lv_competition_history);
        imageLoader = ImageLoader.getInstance();
        this.context = context;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(GetCompetitionHistoryEntity entity, int position, int grpPosition, View view, Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        imageLoader.displayImage(entity.getImageUrl(), viewHolder.ivCompetition);
        viewHolder.tvCompetitionName.setText(entity.getTitle() + "");
        viewHolder.tvDescription.setText(entity.getDescription() + "");

        if (entity.getStatus().equalsIgnoreCase(context.getString(R.string.status_pending))) {
            viewHolder.ivStatus.setImageResource(R.drawable.pending);
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.pending));
            viewHolder.tvStatus.setText(entity.getStatus());
        } else if (entity.getStatus().equalsIgnoreCase(context.getString(R.string.status_won))) {
            viewHolder.ivStatus.setImageResource(R.drawable.reserved);
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.reserved));
            viewHolder.tvStatus.setText(entity.getStatus());
        } else if (entity.getStatus().equalsIgnoreCase(context.getString(R.string.status_loss))) {
            viewHolder.ivStatus.setImageResource(R.drawable.rejected);
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.rejected));
            viewHolder.tvStatus.setText(entity.getStatus());
        }
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_competition)
        ImageView ivCompetition;
        @BindView(R.id.tv_competition_name)
        AnyTextView tvCompetitionName;
        @BindView(R.id.tv_description)
        AnyTextView tvDescription;
        @BindView(R.id.iv_status)
        ImageView ivStatus;
        @BindView(R.id.tv_status)
        AnyTextView tvStatus;
        @BindView(R.id.ll_staus)
        LinearLayout llStaus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
