package com.application.cavalliclub.ui.binders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.entities.EntityUpcomingHistoryGuestListProfile;
import com.application.cavalliclub.fragments.AddMoreFriendsFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.helpers.BasePreferenceHelper;
import com.application.cavalliclub.helpers.DateHelper;
import com.application.cavalliclub.helpers.InternetHelper;
import com.application.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderGuestUpcomingProfile extends ViewBinder<EntityUpcomingHistoryGuestListProfile> {

    private Context context;
    private DockActivity dockActivity;
    private ImageLoader imageLoader;
    private String splittedDate;
    private BasePreferenceHelper preferenceHelper;
    String ArrayStringContainer;

    public BinderGuestUpcomingProfile(Context context, DockActivity dockActivity, BasePreferenceHelper preferenceHelper) {
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
        BinderGuestUpcomingProfile.ViewHolder viewHolder = (BinderGuestUpcomingProfile.ViewHolder) view.getTag();

        String str = entity.getCreatedAt();
        String[] splited = str.split("\\s");
        splittedDate = splited[0];

        if (entity != null) {
            if (entity != null && entity.getGuest_list_category() != null && entity.getGuest_list_category().getTitle() != null)
                viewHolder.tvTitle.setText(entity.getGuest_list_category().getTitle() + "");
            if (splittedDate != null)
                viewHolder.tvDate.setText(DateHelper.getFormatedDate(AppConstants.DateFormat_YMD, AppConstants.DateFormat_EYMD, splittedDate));
            if (entity != null && entity.getGuestListMember() != null)
                viewHolder.tvTotalPeople.setText(entity.getGuestListMember().size() + "");

            //will always contain pending data
            viewHolder.ivStatus.setImageResource(R.drawable.pending);
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.pending));
            viewHolder.tvStatus.setText("Pending");

            viewHolder.tv_view_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (InternetHelper.CheckInternetConectivityandShowToast(dockActivity)) {
                        Gson gson = new Gson();
                        ArrayStringContainer = gson.toJson(entity.getGuestListMember());
                        preferenceHelper.setGuestMemebrList(ArrayStringContainer);
                        dockActivity.replaceDockableFragment(AddMoreFriendsFragment.newInstance(Integer.valueOf(entity.getGuest_list_category().getId()), 1, entity.getGuest_list_category().getTitle() + ""), "AddMoreFriendsFragment");
                    }
                }
            });
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
