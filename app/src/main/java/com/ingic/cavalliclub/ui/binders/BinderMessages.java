package com.ingic.cavalliclub.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.activities.DockActivity;
import com.ingic.cavalliclub.entities.EntityMessages;
import com.ingic.cavalliclub.fragments.ChatFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.helpers.BasePreferenceHelper;
import com.ingic.cavalliclub.helpers.DateHelper;
import com.ingic.cavalliclub.helpers.InternetHelper;
import com.ingic.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BinderMessages extends ViewBinder<EntityMessages> {

    private DockActivity context;
    private ImageLoader imageLoader;
    private BasePreferenceHelper preferenceHelper;
    String splittedDate;
    String splittedTime;

    public BinderMessages(DockActivity context, BasePreferenceHelper prefHelper) {
        super(R.layout.item_messages);
        this.context = context;
        this.preferenceHelper = prefHelper;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final EntityMessages entity, int position, int grpPosition, View view, Activity activity) {

        BinderMessages.ViewHolder viewHolder = (BinderMessages.ViewHolder) view.getTag();
        if (entity != null) {

            /*if (entity.getCreatedAt() != null) {
                String str = entity.getCreatedAt();
                String[] splited = str.split("\\s");
                splittedDate = splited[0];
                splittedTime = splited[1];
            }*/

            if (entity != null && entity.getTitle() != null) {
                viewHolder.tv_title.setText(entity.getTitle() + "");
            } else {
                if (entity != null && entity.getEventDetail() != null && entity.getEventDetail().getTitle() != null)
                    viewHolder.tv_title.setText(entity.getEventDetail().getTitle() + "");
            }
            if (entity != null && entity.getMessage() != null)
                viewHolder.tv_msg.setText(entity.getMessage() + "");

            if (entity != null && entity.getCreatedAt() != null)
                viewHolder.tv_date.setText(DateHelper.getFormatedDate(AppConstants.DateFormat_YMDHMS, AppConstants.DateFormat_DMY4,
                        entity.getCreatedAt()));

            viewHolder.tv_time.setText(DateHelper.getFormatedDate1(AppConstants.DateFormat_YMDHMS, AppConstants.TF, entity.getUpdatedAt()));
          //  viewHolder.tv_time.setText(DateHelper.getLocalTime(entity.getCreatedAt()));
            viewHolder.ll_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity.getId() != null) {
                        if (InternetHelper.CheckInternetConectivityandShowToast(context)) {
                            context.replaceDockableFragment(ChatFragment.newInstance(entity.getId(), entity), "ChatFragment");
                        }
                    }
                }
            });
        }
    }

    public static class ViewHolder extends BaseViewHolder {

        ImageView ivNotification;
        AnyTextView tv_msg;
        AnyTextView tv_date;
        AnyTextView tv_time;
        AnyTextView tv_title;
        LinearLayout ll_main;

        public ViewHolder(View view) {

            ivNotification = (ImageView) view.findViewById(R.id.ivNotification);
            tv_msg = (AnyTextView) view.findViewById(R.id.tv_msg);
            tv_date = (AnyTextView) view.findViewById(R.id.tv_date);
            tv_time = (AnyTextView) view.findViewById(R.id.tv_time);
            tv_title = (AnyTextView) view.findViewById(R.id.tv_title);
            ll_main = (LinearLayout) view.findViewById(R.id.ll_main);
        }
    }
}
