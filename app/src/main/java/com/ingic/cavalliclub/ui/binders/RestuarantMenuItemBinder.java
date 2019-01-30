package com.ingic.cavalliclub.ui.binders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.MenuCategoryEntity;
import com.ingic.cavalliclub.helpers.BasePreferenceHelper;
import com.ingic.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RestuarantMenuItemBinder extends ViewBinder<MenuCategoryEntity> {

    private Context context;
    private ImageLoader imageLoader;
    private BasePreferenceHelper preferenceHelper;

    public RestuarantMenuItemBinder(Context context, BasePreferenceHelper prefHelper) {
        super(R.layout.row_item_restaurant_menu);
        this.context = context;
        this.preferenceHelper = prefHelper;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(MenuCategoryEntity entity, int position, int grpPosition, View view, Activity activity) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.tvTitle.setText(entity.getTitle());
        viewHolder.tvDescription.setText(entity.getDescription());
        if (entity != null && entity.getProductAttributes() != null && entity.getProductAttributes().size() != 0 && entity.getProductAttributes().get(0) != null
                && entity.getProductAttributes().get(0).getValue() != null) {
            viewHolder.tvAmount.setText("AED" + " " + NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(entity.getProductAttributes().get(0).getValue())));
//            viewHolder.tvAmount.setText("AED" + " " + entity.getProductAttributes().get(0).getValue());
        }
        imageLoader.displayImage(entity.getImageUrl(), viewHolder.ivRetaurantMenu);
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_retaurant_menu)
        ImageView ivRetaurantMenu;
        @BindView(R.id.tv_title)
        AnyTextView tvTitle;
        @BindView(R.id.tv_description)
        AnyTextView tvDescription;
        @BindView(R.id.tv_amount)
        AnyTextView tvAmount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
