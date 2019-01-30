package com.ingic.cavalliclub.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.activities.DockActivity;
import com.ingic.cavalliclub.entities.EntityOrderProduct;
import com.ingic.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderPendingOrdersDetailProfile extends ViewBinder<EntityOrderProduct> {

    private DockActivity context;
    private ImageLoader imageLoader;

    public BinderPendingOrdersDetailProfile(DockActivity context) {
        super(R.layout.item_pending_orders_detail_profile);
        imageLoader = ImageLoader.getInstance();
        this.context = context;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final EntityOrderProduct entity, int position, int grpPosition, View view, Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if (entity != null) {
            if (entity.getProductDetail() != null && entity.getProductDetail().getImageUrl() != null)
                imageLoader.displayImage(entity.getProductDetail().getImageUrl(), viewHolder.ivImage);
            if (entity.getProductDetail() != null && entity.getProductDetail().getTitle() != null)
                viewHolder.tvTitle.setText(entity.getProductDetail().getTitle() + "");
            if (entity.getProductDetail() != null && entity.getProductDetail().getDescription() != null)
                viewHolder.tvDescription.setText(entity.getProductDetail().getDescription() + "");
            if (entity.getQuantity() != null)
                viewHolder.tvQuantity.setText("Qty " + entity.getQuantity() + "");
            if (entity.getPrice() != null)
                viewHolder.tvAmount.setText("AED " + entity.getPrice() + "");
        }
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_title)
        AnyTextView tvTitle;
        @BindView(R.id.tv_description)
        AnyTextView tvDescription;
        @BindView(R.id.tv_quantity)
        AnyTextView tvQuantity;
        @BindView(R.id.tv_amount)
        AnyTextView tvAmount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
