package com.ingic.cavalliclub.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.activities.DockActivity;
import com.ingic.cavalliclub.activities.MainActivity;
import com.ingic.cavalliclub.fragments.GalleryFragment;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.helpers.BasePreferenceHelper;
import com.ingic.cavalliclub.interfaces.GalleryGvItemClick;
import com.ingic.cavalliclub.retrofit.EntityGalleryCategories;
import com.ingic.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderGalleryCategories extends ViewBinder<EntityGalleryCategories> {

    @BindView(R.id.tv_guest_list)
    AnyTextView tvGuestList;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    private DockActivity dockActivity;
    private MainActivity mainActivity;
    private BasePreferenceHelper basePreferenceHelper;
    private GalleryGvItemClick galleryGvItemClick;
    private ImageLoader imageLoader;
    String image;

    public BinderGalleryCategories(DockActivity dockActivity, MainActivity mainActivity, BasePreferenceHelper basePreferenceHelper,GalleryGvItemClick galleryGvItemClick) {
        super(R.layout.item_gv_gallery_categories);
        imageLoader = ImageLoader.getInstance();
        this.dockActivity = dockActivity;
        this.mainActivity = mainActivity;
        this.basePreferenceHelper = basePreferenceHelper;
        this.galleryGvItemClick=galleryGvItemClick;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final EntityGalleryCategories entity, int position, int grpPosition, View view, final Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.txtCategories.setText(entity.getName() + "");

        viewHolder.txtCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryGvItemClick.OnClickService(entity.getId());
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_categories)
        AnyTextView txtCategories;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
