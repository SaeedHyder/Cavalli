package com.ingic.cavalliclub.ui.binders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.OurEventsEnt;
import com.ingic.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by saeedhyder on 1/30/2018.
 */

public class OurEventsItemBinder extends ViewBinder<OurEventsEnt> {

    private Context context;
    private ImageLoader imageLoader;

    public OurEventsItemBinder(Context context) {
        super(R.layout.row_item_our_events);
        imageLoader = ImageLoader.getInstance();
        this.context = context;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(OurEventsEnt entity, int position, int grpPosition, View view, Activity activity) {

        ViewHolder viewHolder=(ViewHolder)view.getTag();

        imageLoader.displayImage(entity.getImage(), viewHolder.ivLatestUpdates);
        viewHolder.tvTitle.setText(entity.getTitle());
        viewHolder.tvDescription.setText(entity.getDescription());
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_latest_updates)
        ImageView ivLatestUpdates;
        @BindView(R.id.tv_title)
        AnyTextView tvTitle;
        @BindView(R.id.tv_description)
        AnyTextView tvDescription;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
