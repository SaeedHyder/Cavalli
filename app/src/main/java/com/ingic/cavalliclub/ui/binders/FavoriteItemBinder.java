package com.ingic.cavalliclub.ui.binders;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.FavoriteEnt;
import com.ingic.cavalliclub.helpers.BasePreferenceHelper;
import com.ingic.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by saeedhyder on 1/29/2018.
 */

public class FavoriteItemBinder extends ViewBinder<FavoriteEnt> {

    private Context context;
    private ImageLoader imageLoader;
    private BasePreferenceHelper preferenceHelper;

    public FavoriteItemBinder(Context context, BasePreferenceHelper prefHelper) {
        super(R.layout.row_item_favorite);
        this.context = context;
        this.preferenceHelper = prefHelper;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(FavoriteEnt entity, int position, int grpPosition, View view, Activity activity) {
       ViewHolder viewHolder = (ViewHolder) view.getTag();

        imageLoader.displayImage(entity.getImage(),viewHolder.ivFavorite);
        viewHolder.txtTitle.setText(entity.getTitle());
        viewHolder.tvMsg.setText(entity.getDescription());

    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.ivNotification)
        ImageView ivFavorite;
        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.txt_title)
        AnyTextView txtTitle;
        @BindView(R.id.tv_msg)
        AnyTextView tvMsg;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
