package com.application.cavalliclub.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.activities.MainActivity;
import com.application.cavalliclub.entities.MenuCategoryEntity;
import com.application.cavalliclub.helpers.BasePreferenceHelper;
import com.application.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ahmedsyed on 10/9/2018.
 */


public class StarterItemBinder extends ViewBinder<MenuCategoryEntity> {

    @BindView(R.id.tv_guest_list)
    AnyTextView tvGuestList;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    private DockActivity dockActivity;
    private MainActivity mainActivity;
    private BasePreferenceHelper basePreferenceHelper;
    private ImageLoader imageLoader;
    String image;

    public StarterItemBinder(DockActivity dockActivity, MainActivity mainActivity, BasePreferenceHelper basePreferenceHelper) {
        super(R.layout.row_item_starter);
        imageLoader = ImageLoader.getInstance();
        this.dockActivity = dockActivity;
        this.mainActivity = mainActivity;
        this.basePreferenceHelper = basePreferenceHelper;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final MenuCategoryEntity entity, int position, int grpPosition, View view, final Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

          //imageLoader.displayImage(entity.getImageUrl(), viewHolder.ivGvGallery,mainActivity.getImageLoaderRoundCornerTransformation(10));
        imageLoader.displayImage(entity.getImageUrl(), viewHolder.ivGvGallery,mainActivity.getImageLoaderRoundCornerTransformation(20));
        viewHolder.txtMenuItem.setText(entity.getTitle()+"");
    }


    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_gv_gallery)
        ImageView ivGvGallery;
        @BindView(R.id.txt_menuItem)
        AnyTextView txtMenuItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

