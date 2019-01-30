package com.ingic.cavalliclub.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.activities.DockActivity;
import com.ingic.cavalliclub.entities.NotificationEntity;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.helpers.BasePreferenceHelper;
import com.ingic.cavalliclub.helpers.DateHelper;
import com.ingic.cavalliclub.helpers.DialogHelper;
import com.ingic.cavalliclub.interfaces.NotificationDelete;
import com.ingic.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BinderNotification extends ViewBinder<NotificationEntity> {

    private DockActivity context;
    private ImageLoader imageLoader;
    private BasePreferenceHelper preferenceHelper;
    String splittedDate;
    String splittedTime;
    private NotificationDelete notificationDelete;

    public BinderNotification(DockActivity context, BasePreferenceHelper prefHelper, NotificationDelete notificationDelete) {
        super(R.layout.fragment_notification_item);
        this.context = context;
        this.preferenceHelper = prefHelper;
        imageLoader = ImageLoader.getInstance();
        this.notificationDelete = notificationDelete;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new BinderNotification.ViewHolder(view);
    }

    @Override
    public void bindView(final NotificationEntity entity, int position, int grpPosition, View view, Activity activity) {

        BinderNotification.ViewHolder viewHolder = (BinderNotification.ViewHolder) view.getTag();

        String str = entity.getCreatedAt();
        String[] splited = str.split("\\s");
        splittedDate = splited[0];
        splittedTime = splited[1];

        viewHolder.tv_msg.setText(entity.getMessage() + "");

        viewHolder.tv_date.setText(DateHelper.getFormatedDate(AppConstants.DateFormat_YMDHMS, AppConstants.DateFormat_DMY4,
                entity.getCreatedAt()));

        try {
            String _24HourTime = splittedTime;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            /*System.out.println(_24HourDt);
            System.out.println(_12HourSDF.format(_24HourDt));*/
            viewHolder.tv_time.setText(_12HourSDF.format(_24HourDt));
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.tv_time.setText(DateHelper.getFormatedDate(AppConstants.TF, AppConstants.DateFormat_HM, splittedTime));

        viewHolder.tv_date.setText(DateHelper.getLocalDateNotification(entity.getCreatedAt()));

        viewHolder.tv_time.setText(DateHelper.getLocalTime(entity.getCreatedAt()));

        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogHelper dialogHelper = new DialogHelper(context);
                dialogHelper.initDeleteNotification(R.layout.logout_dialoge, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notificationDelete.OnClickService(entity.getId());
                        dialogHelper.hideDialog();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelper.hideDialog();
                    }
                }, "Delete Notification", "Do you sure you want to delete this notification?");

                dialogHelper.showDialog();
            }
        });
    }

    public static class ViewHolder extends BaseViewHolder {

        ImageView ivNotification;
        AnyTextView tv_msg;
        AnyTextView tv_date;
        AnyTextView tv_time;
        ImageView iv_delete;

        public ViewHolder(View view) {

            ivNotification = (ImageView) view.findViewById(R.id.ivNotification);
            tv_msg = (AnyTextView) view.findViewById(R.id.tv_msg);
            tv_date = (AnyTextView) view.findViewById(R.id.tv_date);
            tv_time = (AnyTextView) view.findViewById(R.id.tv_time);
            iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
        }
    }
}
