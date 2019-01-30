package com.ingic.cavalliclub.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.activities.DockActivity;
import com.ingic.cavalliclub.entities.LatestUpdatesEntity;
import com.ingic.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderLatestUpdatesMusic extends ViewBinder<LatestUpdatesEntity> {

    private DockActivity context;
    private ImageLoader imageLoader;

    public BinderLatestUpdatesMusic(DockActivity context) {
        super(R.layout.item_lv_latest_updates_music);
        imageLoader = ImageLoader.getInstance();
        this.context = context;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final LatestUpdatesEntity entity, int position, int grpPosition, View view, Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if (entity != null) {
            if (entity.getImageUrl() != null)
                imageLoader.displayImage(entity.getImageUrl(), viewHolder.ivLatestUpdates);
            if (entity.getTitle() != null)
                viewHolder.tvTitle.setText(entity.getTitle() + "");
            if (entity.getDescription() != null)
                viewHolder.tvDescription.setText(entity.getDescription() + "");
        }
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_latest_updates)
        ImageView ivLatestUpdates;
        @BindView(R.id.tv_title)
        AnyTextView tvTitle;
        @BindView(R.id.tv_description)
        AnyTextView tvDescription;
        @BindView(R.id.ll_main)
        LinearLayout ll_main;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
