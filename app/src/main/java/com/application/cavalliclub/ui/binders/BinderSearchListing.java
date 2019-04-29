package com.application.cavalliclub.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.entities.EntityHomeSearch;
import com.application.cavalliclub.helpers.BasePreferenceHelper;
import com.application.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderSearchListing extends ViewBinder<EntityHomeSearch> {

    private DockActivity context;
    private ImageLoader imageLoader;
    private BasePreferenceHelper preferenceHelper;

    public BinderSearchListing(DockActivity context, BasePreferenceHelper prefHelper) {
        super(R.layout.item_lv_latest_updates_music);
        this.context = context;
        this.preferenceHelper = prefHelper;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final EntityHomeSearch entity, int position, int grpPosition, View view, Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if (entity != null) {
            if (entity.getImageUrl() != null)
                imageLoader.displayImage(entity.getImageUrl(), viewHolder.ivLatestUpdates);
            if (entity.getTitle() != null)
                viewHolder.tvTitle.setText(entity.getTitle() + "");
            if (entity.getDescription() != null)
                viewHolder.tvDescription.setText(entity.getDescription() + "");
        }

/*        viewHolder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.getType().equalsIgnoreCase(AppConstants.TYPE_EVENT)) {
                    if (entity.getEventTypeId().equalsIgnoreCase(AppConstants.CAVALLI_EVENTS)) {

                    } else if (entity.getEventTypeId().equalsIgnoreCase(AppConstants.MUSIC_EVENTS)) {

                    }
                } else if (entity.getType().equalsIgnoreCase(AppConstants.TYPE_NEWS)) {

                } else if (entity.getType().equalsIgnoreCase(AppConstants.TYPE_PRODUCT)) {

                }
            }
        });*/
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_latest_updates)
        ImageView ivLatestUpdates;
        @BindView(R.id.tv_title)
        AnyTextView tvTitle;
        @BindView(R.id.tv_description)
        AnyTextView tvDescription;
        @BindView(R.id.ll_main)
        LinearLayout llMain;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
