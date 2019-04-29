package com.application.cavalliclub.ui.binders;


import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.activities.MainActivity;
import com.application.cavalliclub.entities.EntityCategoriesGuestList;
import com.application.cavalliclub.fragments.AddMoreFriendsFragment;
import com.application.cavalliclub.helpers.BasePreferenceHelper;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderCategoriesGuestList extends ViewBinder<EntityCategoriesGuestList> {

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

    public BinderCategoriesGuestList(DockActivity dockActivity, MainActivity mainActivity, BasePreferenceHelper basePreferenceHelper) {
        super(R.layout.item_lv_categories_guest_list_listing);
        imageLoader = ImageLoader.getInstance();
        this.dockActivity = dockActivity;
        this.mainActivity = mainActivity;
        this.basePreferenceHelper = basePreferenceHelper;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new BinderCategoriesGuestList.ViewHolder(view);
    }

    @Override
    public void bindView(final EntityCategoriesGuestList entity, int position, int grpPosition, View view, final Activity activity) {
        BinderCategoriesGuestList.ViewHolder viewHolder = (BinderCategoriesGuestList.ViewHolder) view.getTag();

        viewHolder.tvGuestListCategory.setText(entity.getTitle() + "");
        viewHolder.tvDescription.setText(entity.getDescription() + "");

        viewHolder.btnAddGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.getStatus().equalsIgnoreCase("0")) {
                    UIHelper.showShortToastInCenter(dockActivity, entity.getReason());
                } else {
                    //dockActivity.replaceDockableFragment(InviteGuestFragment.newInstance(AppConstants.home, entity.getId()), "InviteGuestFragment");
                    dockActivity.replaceDockableFragment(AddMoreFriendsFragment.newInstance( entity.getId(), 3, entity.getTitle()), "AddMoreFriendsFragment");
                }
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_guest_list_category)
        AnyTextView tvGuestListCategory;
        @BindView(R.id.btn_add_guest)
        Button btnAddGuest;
        @BindView(R.id.tv_description)
        AnyTextView tvDescription;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
