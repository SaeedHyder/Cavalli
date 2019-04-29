package com.application.cavalliclub.ui.binders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.MainActivity;
import com.application.cavalliclub.entities.MenuEntity;
import com.application.cavalliclub.helpers.BasePreferenceHelper;
import com.application.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MenuItemBinder extends ViewBinder<MenuEntity> {

    private Context context;
    private ImageLoader imageLoader;
    private BasePreferenceHelper preferenceHelper;
    private MainActivity mainActivity;

    public MenuItemBinder(Context context, BasePreferenceHelper prefHelper, MainActivity mainActivity) {
        super(R.layout.row_item_menu);
        this.context = context;
        this.preferenceHelper = prefHelper;
        this.mainActivity=mainActivity;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(MenuEntity entity, int position, int grpPosition, View view, Activity activity) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if (entity != null && entity.getName() != null && entity.getImage() != null) {
            viewHolder.txtMenuItem.setText(entity.getName());
            Picasso.with(context).load(entity.getCategoryImage()).placeholder(R.drawable.placeholder_thumb).into(viewHolder.imageView);
           // imageLoader.displayImage(entity.getCategoryImage(), ,mainActivity.getImageLoaderRoundCornerTransformation(10));
        }
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.txt_menuItem)
        AnyTextView txtMenuItem;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
