package com.application.cavalliclub.ui.binders;


import android.app.Activity;
import android.view.View;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.entities.MenuCategoryProductAttributeEntity;
import com.application.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BinderMenuItemDetail extends ViewBinder<MenuCategoryProductAttributeEntity> {

    private DockActivity dockActivity;
    private ImageLoader imageLoader;

    public BinderMenuItemDetail(DockActivity context) {
        super(R.layout.item_lv_menu_item_detail);
        imageLoader = ImageLoader.getInstance();
        this.dockActivity = context;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(MenuCategoryProductAttributeEntity entity, int position, int grpPosition, View view, Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();


        viewHolder.tvType.setText(entity.getKey());
        if(entity.getValue() != null){
            viewHolder.txtAmount.setText("AED" + " " + NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(entity.getValue())));
        }
    }

    static class ViewHolder extends BaseViewHolder{
        @BindView(R.id.tv_type)
        AnyTextView tvType;
        @BindView(R.id.txt_amount)
        AnyTextView txtAmount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}