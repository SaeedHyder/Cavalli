package com.application.cavalliclub.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.entities.EntityCavalliNights;
import com.application.cavalliclub.fragments.CavalliNightsDetailFragment;
import com.application.cavalliclub.helpers.InternetHelper;
import com.application.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderSeeAllMusicFragment extends ViewBinder<EntityCavalliNights> {

    private DockActivity context;
    private ImageLoader imageLoader;

    public BinderSeeAllMusicFragment(DockActivity context) {
        super(R.layout.item_lv_latest_updates_music);
        imageLoader = ImageLoader.getInstance();
        this.context = context;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final EntityCavalliNights entity, int position, int grpPosition, View view, Activity activity) {
        BinderSeeAllMusicFragment.ViewHolder viewHolder = (BinderSeeAllMusicFragment.ViewHolder) view.getTag();
        if (entity != null) {
            if (entity.getImageUrl() != null)
                imageLoader.displayImage(entity.getImageUrl(), viewHolder.ivLatestUpdates);
            if (entity.getTitle() != null)
                viewHolder.tvTitle.setText(entity.getTitle() + "");
            if (entity.getDescription() != null)
                viewHolder.tvDescription.setText(entity.getDescription() + "");

            viewHolder.ll_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (InternetHelper.CheckInternetConectivityandShowToast(context)) {
                        context.replaceDockableFragment(CavalliNightsDetailFragment.newInstance(entity), "CavalliNightsDetailFragment");
                    }
                }
            });
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
