package com.ingic.cavalliclub.ui.binders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.activities.DockActivity;
import com.ingic.cavalliclub.entities.EntityUpcomingHistoryGuestListProfile;
import com.ingic.cavalliclub.fragments.AddMoreFriendsFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.helpers.BasePreferenceHelper;
import com.ingic.cavalliclub.helpers.DateHelper;
import com.ingic.cavalliclub.helpers.InternetHelper;
import com.ingic.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderGuestHistoryProfile extends ViewBinder<EntityUpcomingHistoryGuestListProfile> {

    private Context context;
    private DockActivity dockActivity;
    private ImageLoader imageLoader;
    private String splittedDate;
    private BasePreferenceHelper preferenceHelper;
    String ArrayStringContainer;
    private List<String> items = new ArrayList<>();

    public BinderGuestHistoryProfile(Context context, DockActivity dockActivity, BasePreferenceHelper preferenceHelper) {
        super(R.layout.item_lv_my_reservations);
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
    public void bindView(final EntityUpcomingHistoryGuestListProfile entity, int position, int grpPosition, View view, Activity activity) {
        BinderGuestHistoryProfile.ViewHolder viewHolder = (BinderGuestHistoryProfile.ViewHolder) view.getTag();

        String str = entity.getCreatedAt();
        String[] splited = str.split("\\s");
        splittedDate = splited[0];

        if (preferenceHelper.getFilterData() != null) {
            items = Arrays.asList(preferenceHelper.getFilterData().split("\\s*,\\s*"));
        }
        if (items.size() > 0 && !items.get(0).equalsIgnoreCase(AppConstants.EMPTY)) {
            for (String item : items) {


                if (entity.getGuestStatusId().equalsIgnoreCase("1") || entity.getGuestStatusId().equalsIgnoreCase("2")) {
                    viewHolder.tv_view_details.setVisibility(View.VISIBLE);
                    viewHolder.tv_view_details.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (InternetHelper.CheckInternetConectivityandShowToast(dockActivity)) {
                                Gson gson = new Gson();
                                ArrayStringContainer = gson.toJson(entity.getGuestListMember());
                                preferenceHelper.setGuestMemebrList(ArrayStringContainer);
                                dockActivity.replaceDockableFragment(AddMoreFriendsFragment.newInstance(Integer.valueOf(entity.getGuest_list_category().getId()), 0, entity.getGuest_list_category().getTitle() + ""), "AddMoreFriendsFragment");
                            }
                        }
                    });
                } else {
                    viewHolder.tv_view_details.setVisibility(View.GONE);
                }


                if (item.equalsIgnoreCase(entity.getGuestStatusId())) {
                        viewHolder.ll_main.setVisibility(View.VISIBLE);
                        if (entity != null && entity.getGuest_list_category() != null && entity.getGuest_list_category().getTitle() != null)
                            viewHolder.tvTitle.setText(entity.getGuest_list_category().getTitle() + "");
                        if (entity.getCreatedAt() != null)
                            viewHolder.tvDate.setText(DateHelper.getFormatedDate(AppConstants.DateFormat_YMD, AppConstants.DateFormat_EYMD, splittedDate));
                        if (entity != null && entity.getGuestListMember() != null)
                            viewHolder.tvTotalPeople.setText(entity.getGuestListMember().size() + "");

                        if (entity.getGuestStatusId() != null)
                            if (entity.getGuestStatusId().equalsIgnoreCase("1")) {
                                viewHolder.ivStatus.setImageResource(R.drawable.pending);
                                viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.pending));
                                viewHolder.tvStatus.setText(context.getString(R.string.status_pending));
                            } else if (entity.getGuestStatusId().equalsIgnoreCase("2")) {
                                viewHolder.ivStatus.setImageResource(R.drawable.reserved);
                                viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.reserved));
                                viewHolder.tvStatus.setText(context.getString(R.string.status_reserved));
                            } else if (entity.getGuestStatusId().equalsIgnoreCase("3")) {
                                viewHolder.ivStatus.setImageResource(R.drawable.rejected);
                                viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.rejected));
                                viewHolder.tvStatus.setText(context.getString(R.string.status_rejected));
                                viewHolder.tv_view_details.setVisibility(View.GONE);
                            } else if (entity.getGuestStatusId().equalsIgnoreCase("4")) {
                                viewHolder.ivStatus.setImageResource(R.drawable.cancel);
                                viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.rejected));
                                viewHolder.tvStatus.setText(context.getString(R.string.status_cancelled_by_user));
                                viewHolder.tv_view_details.setVisibility(View.GONE);
                            }
                        return;
                    } else {
                        viewHolder.ll_main.setVisibility(View.GONE);
                    }
                }

        } else if (preferenceHelper.getFilterData().equalsIgnoreCase(AppConstants.EMPTY)) {
            viewHolder.ll_main.setVisibility(View.VISIBLE);
            if (entity != null && entity.getGuest_list_category() != null && entity.getGuest_list_category().getTitle() != null)
                viewHolder.tvTitle.setText(entity.getGuest_list_category().getTitle());
            if (entity.getCreatedAt() != null)
                viewHolder.tvDate.setText(DateHelper.getFormatedDate(AppConstants.DateFormat_YMD, AppConstants.DateFormat_EYMD, splittedDate));
            if (entity != null && entity.getGuestListMember() != null)
                viewHolder.tvTotalPeople.setText(entity.getGuestListMember().size() + "");
            if (entity.getGuestStatusId() != null)
                viewHolder.tvStatus.setText("");

            if (entity.getGuestStatusId().equalsIgnoreCase("1")) {
                viewHolder.ivStatus.setImageResource(R.drawable.pending);
                viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.pending));
                viewHolder.tvStatus.setText(context.getString(R.string.status_pending));
            } else if (entity.getGuestStatusId().equalsIgnoreCase("2")) {
                viewHolder.ivStatus.setImageResource(R.drawable.reserved);
                viewHolder.tvStatus.setText(context.getString(R.string.status_reserved));
                viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.reserved));
            } else if (entity.getGuestStatusId().equalsIgnoreCase("3")) {
                viewHolder.ivStatus.setImageResource(R.drawable.rejected);
                viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.rejected));
                viewHolder.tvStatus.setText(context.getString(R.string.status_rejected));
                viewHolder.tv_view_details.setVisibility(View.GONE);
            } else if (entity.getGuestStatusId().equalsIgnoreCase("4")) {
                viewHolder.ivStatus.setImageResource(R.drawable.cancel);
                viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.rejected));
                viewHolder.tvStatus.setText(context.getString(R.string.status_cancelled_by_user));
                viewHolder.tv_view_details.setVisibility(View.GONE);
            }
        } else {
            viewHolder.ll_main.setVisibility(View.GONE);
        }

        if (entity.getGuestStatusId().equalsIgnoreCase("1") ||
                (entity.getGuestStatusId().equalsIgnoreCase(("2")))) {
            viewHolder.tv_view_details.setVisibility(View.VISIBLE);
            viewHolder.tv_view_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (InternetHelper.CheckInternetConectivityandShowToast(dockActivity)) {
                        Gson gson = new Gson();
                        ArrayStringContainer = gson.toJson(entity.getGuestListMember());
                        preferenceHelper.setGuestMemebrList(ArrayStringContainer);
                        dockActivity.replaceDockableFragment(AddMoreFriendsFragment.newInstance(Integer.valueOf(entity.getGuest_list_category().getId()), 0, entity.getGuest_list_category().getTitle() + ""), "AddMoreFriendsFragment");
                    }
                }
            });
        } else {
            viewHolder.tv_view_details.setVisibility(View.GONE);
        }
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_title)
        AnyTextView tvTitle;
        @BindView(R.id.tv_date)
        AnyTextView tvDate;
        @BindView(R.id.tv_total_people)
        AnyTextView tvTotalPeople;
        @BindView(R.id.iv_status)
        ImageView ivStatus;
        @BindView(R.id.tv_status)
        AnyTextView tvStatus;
        @BindView(R.id.tv_view_details)
        AnyTextView tv_view_details;
        @BindView(R.id.ll_main)
        LinearLayout ll_main;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

