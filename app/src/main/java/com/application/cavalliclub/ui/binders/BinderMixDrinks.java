package com.application.cavalliclub.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.entities.MenuCategoryEntity;
import com.application.cavalliclub.fragments.MixDrinksDetailFragment;
import com.application.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderMixDrinks extends ViewBinder<MenuCategoryEntity> {

    private DockActivity dockActivity;
    private ImageLoader imageLoader;

    public BinderMixDrinks(DockActivity context) {
        super(R.layout.item_lv_mix_drinks);
        imageLoader = ImageLoader.getInstance();
        this.dockActivity = context;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final MenuCategoryEntity entity, int position, int grpPosition, View view, Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        imageLoader.displayImage(entity.getImageUrl(), viewHolder.ivDrink);
        viewHolder.tvDrinkName.setText(entity.getTitle());
        viewHolder.tvDescription.setText(entity.getDescription());
        if (entity.getTotal_price() != null) {
        viewHolder.tvAmount.setText("AED" + " " + NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(entity.getTotal_price())));
            /*
            if (entity.getTotal_price().length() >= 4) {
                viewHolder.tvAmount.setText("AED" + " " + entity.getTotal_price().substring(0, 1) + "," + entity.getTotal_price().substring(1, entity.getTotal_price().length()));
            } else {
                viewHolder.tvAmount.setText("AED" + " " + entity.getTotal_price());
            }
            */
        }

        viewHolder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dockActivity.replaceDockableFragment(MixDrinksDetailFragment.newInstance(entity), "MixDrinksDetailFragment");
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_drink)
        ImageView ivDrink;
        @BindView(R.id.tv_drink_name)
        AnyTextView tvDrinkName;
        @BindView(R.id.tv_description)
        AnyTextView tvDescription;
        @BindView(R.id.tv_amount)
        AnyTextView tvAmount;
        @BindView(R.id.main)
        LinearLayout main;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}