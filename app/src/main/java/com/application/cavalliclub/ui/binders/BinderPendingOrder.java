package com.application.cavalliclub.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.entities.EntityOrderProduct;
import com.application.cavalliclub.entities.EntityPendingsOrders;
import com.application.cavalliclub.helpers.DateHelper;
import com.application.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderPendingOrder extends ViewBinder<EntityPendingsOrders> {

    private DockActivity context;
    private ImageLoader imageLoader;
    private static boolean isStatusChecker;
    String splittedDate;
    String splittedTime;
    String ArrayStringContainer;
    private ArrayList<EntityOrderProduct> productList = new ArrayList<>();

    public BinderPendingOrder(DockActivity context, boolean checker) {
        super(R.layout.item_lv_pending_orders);
        imageLoader = ImageLoader.getInstance();
        this.context = context;
        isStatusChecker = checker;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final EntityPendingsOrders entity, int position, int grpPosition, View view, Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.tv_oder_no.setText(entity.getOrderNo() + "");
        viewHolder.tvAmount.setText("AED " + "" + entity.getTotalAmount() + "");

        String str = entity.getCreatedAt();
        String[] splited = str.split("\\s");
        splittedDate = splited[0];
        splittedTime = splited[1];

        viewHolder.tv_date.setText(DateHelper.getLocalDate(entity.getCreatedAt()));

        viewHolder.tv_time.setText(DateHelper.getLocalTime(entity.getCreatedAt()));

        if (isStatusChecker) {
            viewHolder.llStaus.setVisibility(View.VISIBLE);
            if (entity.getOrderStatusId() != null)
                if (entity.getOrderStatusId().equalsIgnoreCase("4")) {
                    viewHolder.ivStatus.setImageResource(R.drawable.reserved);
                    viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.reserved));
                    viewHolder.tvStatus.setText("Completed");
                } else if (entity.getOrderStatusId().equalsIgnoreCase("3")) {
                    viewHolder.ivStatus.setImageResource(R.drawable.rejected);
                    viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.rejected));
                    viewHolder.tvStatus.setText("Rejected");
                } else if (entity.getOrderStatusId().equalsIgnoreCase("2")) {
                    viewHolder.ivStatus.setImageResource(R.drawable.cancel);
                    viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.rejected));
                    viewHolder.tvStatus.setText("Cancelled");
                }
        } else {
            viewHolder.llStaus.setVisibility(View.GONE);
        }
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_pending_orders)
        ImageView ivPendingOrders;
        @BindView(R.id.tv_number)
        AnyTextView tvNumber;
        @BindView(R.id.tv_title)
        AnyTextView tvTitle;
        @BindView(R.id.tv_description)
        AnyTextView tvDescription;
        @BindView(R.id.tv_amount)
        AnyTextView tvAmount;
        @BindView(R.id.iv_status)
        ImageView ivStatus;
        @BindView(R.id.tv_status)
        AnyTextView tvStatus;
        @BindView(R.id.ll_status)
        LinearLayout llStaus;
        @BindView(R.id.ll_main)
        LinearLayout ll_main;
        @BindView(R.id.tv_oder_no)
        AnyTextView tv_oder_no;
        @BindView(R.id.tv_date)
        AnyTextView tv_date;
        @BindView(R.id.tv_time)
        AnyTextView tv_time;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

