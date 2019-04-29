package com.application.cavalliclub.ui.binders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.entities.GetReservationsEntity;
import com.application.cavalliclub.fragments.UpdatedEventReserveFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.helpers.BasePreferenceHelper;
import com.application.cavalliclub.helpers.DateHelper;
import com.application.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderEventMyReservations extends ViewBinder<GetReservationsEntity> {

    private Context context;
    private DockActivity dockActivity;
    private ImageLoader imageLoader;
    private String splittedDate;
    private BasePreferenceHelper preferenceHelper;
    private List<String> items = new ArrayList<>();

    public BinderEventMyReservations(Context context, DockActivity dockActivity, BasePreferenceHelper preferenceHelper) {
        super(R.layout.item_lv_my_event_reservation);
        imageLoader = ImageLoader.getInstance();
        this.preferenceHelper = preferenceHelper;
        this.context = context;
        this.dockActivity = dockActivity;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final GetReservationsEntity entity, int position, int grpPosition, View view, Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if (preferenceHelper.getFilterData() != null) {
            items = Arrays.asList(preferenceHelper.getFilterData().split("\\s*,\\s*"));
        }
        if (items.size() > 0 && !items.get(0).equalsIgnoreCase(AppConstants.EMPTY)) {
            for (String item : items) {
                if (entity.getReservationStatusId().equalsIgnoreCase("1") || entity.getReservationStatusId().equalsIgnoreCase("2")) {
                    viewHolder.tvViewDetails.setVisibility(View.VISIBLE);
                    viewHolder.tvViewDetails.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dockActivity.replaceDockableFragment(UpdatedEventReserveFragment.newInstance(entity), "UpdatedEventReserveFragment");
                        }
                    });
                } else {
                    viewHolder.tvViewDetails.setVisibility(View.GONE);
                }

                if (item.equalsIgnoreCase(entity.getReservationStatusId())) {
                    viewHolder.llMain.setVisibility(View.VISIBLE);
                    if (entity.getTitle() != null)
                        viewHolder.tvTitle.setText(entity.getTitle());
                    if (entity.getDate() != null) {
                        viewHolder.tvDay.setText("Date: ");
                        viewHolder.tvDate.setText(DateHelper.getLocalDateEventReservation(entity.getDate()));
                    } else {
                        viewHolder.tvDay.setText("Day: ");
                        String name = entity.getDay();
                       // name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                        viewHolder.tvDate.setText(name + "");
                    }
                    if (entity.getNoPeople() != null)
                        viewHolder.tvTotalPeople.setText(entity.getNoPeople() + "");
                    if (entity.getStatus() != null)
                        if (entity.getReservationStatusId().equalsIgnoreCase("1")) {
                            viewHolder.ivStatus.setImageResource(R.drawable.pending);
                            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.pending));
                            viewHolder.tvStatus.setText(context.getString(R.string.status_pending));
                        } else if (entity.getReservationStatusId().equalsIgnoreCase("2")) {
                            viewHolder.ivStatus.setImageResource(R.drawable.reserved);
                            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.reserved));
                            viewHolder.tvStatus.setText(context.getString(R.string.status_reserved));
                        } else if (entity.getReservationStatusId().equalsIgnoreCase("3")) {
                            viewHolder.ivStatus.setImageResource(R.drawable.rejected);
                            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.rejected));
                            viewHolder.tvStatus.setText(context.getString(R.string.status_rejected));
                            viewHolder.tvViewDetails.setVisibility(View.GONE);
                        } else if (entity.getReservationStatusId().equalsIgnoreCase("4")) {
                            viewHolder.ivStatus.setImageResource(R.drawable.cancel);
                            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.rejected));
                            viewHolder.tvStatus.setText(context.getString(R.string.status_cancelled));
                            viewHolder.tvViewDetails.setVisibility(View.GONE);
                        }
                    return;
                } else {
                    viewHolder.llMain.setVisibility(View.GONE);
                }
            }
        } else if (preferenceHelper.getFilterData().equalsIgnoreCase(AppConstants.EMPTY)) {
            //viewHolder.tvViewDetails.setVisibility(View.VISIBLE);
            viewHolder.llMain.setVisibility(View.VISIBLE);
            if (entity.getTitle() != null)
                viewHolder.tvTitle.setText(entity.getTitle());
            if (entity.getDate() != null && !entity.getDate().equals("null") && !entity.getDate().equals("")) {
                viewHolder.tvDay.setText("Date: ");
                viewHolder.tvDate.setText(DateHelper.getLocalDateEventReservation(entity.getDate()));
            } else if(entity.getDay()!=null && !entity.getDay().equals("null") && !entity.getDay().equals("")){
                viewHolder.tvDay.setText("Day: ");
                String name = entity.getDay();
              //  name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                viewHolder.tvDate.setText(name + "");
            }else{
                viewHolder.tvDate.setText("-");
            }
            if (entity.getNoPeople() != null)
                viewHolder.tvTotalPeople.setText(entity.getNoPeople() + "");
            if (entity.getStatus() != null)
                viewHolder.tvStatus.setText(entity.getStatus());

            if (entity.getStatus() != null)
                if (entity.getReservationStatusId().equalsIgnoreCase("1")) {
                    viewHolder.ivStatus.setImageResource(R.drawable.pending);
                    viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.pending));
                    viewHolder.tvStatus.setText(context.getString(R.string.status_pending));
                } else if (entity.getReservationStatusId().equalsIgnoreCase("2")) {
                    viewHolder.ivStatus.setImageResource(R.drawable.reserved);
                    viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.reserved));
                    viewHolder.tvStatus.setText(context.getString(R.string.status_reserved));
                } else if (entity.getReservationStatusId().equalsIgnoreCase("3")) {
                    viewHolder.ivStatus.setImageResource(R.drawable.rejected);
                    viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.rejected));
                    viewHolder.tvStatus.setText(context.getString(R.string.status_rejected));
                    viewHolder.tvViewDetails.setVisibility(View.GONE);
                } else if (entity.getReservationStatusId().equalsIgnoreCase("4")) {
                    viewHolder.ivStatus.setImageResource(R.drawable.cancel);
                    viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.rejected));
                    viewHolder.tvStatus.setText(context.getString(R.string.status_cancelled));
                    viewHolder.tvViewDetails.setVisibility(View.GONE);
                }
        } else {
            //viewHolder.tvViewDetails.setVisibility(View.GONE);
            viewHolder.llMain.setVisibility(View.GONE);
        }
        if (entity.getReservationStatusId().equalsIgnoreCase("1") || entity.getReservationStatusId().equalsIgnoreCase("2")) {
            viewHolder.tvViewDetails.setVisibility(View.VISIBLE);
            viewHolder.tvViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dockActivity.replaceDockableFragment(UpdatedEventReserveFragment.newInstance(entity), "UpdatedEventReserveFragment");
                }
            });
        } else {
            viewHolder.tvViewDetails.setVisibility(View.GONE);
        }
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_title)
        AnyTextView tvTitle;
        @BindView(R.id.tv_view_details)
        AnyTextView tvViewDetails;
        @BindView(R.id.tv_day)
        AnyTextView tvDay;
        @BindView(R.id.tv_date)
        AnyTextView tvDate;
        @BindView(R.id.tv_total_people)
        AnyTextView tvTotalPeople;
        @BindView(R.id.iv_status)
        ImageView ivStatus;
        @BindView(R.id.tv_status)
        AnyTextView tvStatus;
        @BindView(R.id.ll_main)
        LinearLayout llMain;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
