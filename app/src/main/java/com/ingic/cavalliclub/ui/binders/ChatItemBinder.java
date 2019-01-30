package com.ingic.cavalliclub.ui.binders;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.EntityMessagesThread;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.helpers.BasePreferenceHelper;
import com.ingic.cavalliclub.helpers.DateHelper;
import com.ingic.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChatItemBinder extends ViewBinder<EntityMessagesThread> {

    private Context context;
    private ImageLoader imageLoader;
    private BasePreferenceHelper preferenceHelper;
    String splittedDate;
    String splittedTime;

    public ChatItemBinder(Context context, BasePreferenceHelper prefHelper) {
        super(R.layout.row_item_chat);
        this.context = context;
        this.preferenceHelper = prefHelper;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(EntityMessagesThread entity, int position, int grpPosition, View view, Activity activity) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if (entity!=null) {
            String str = entity.getCreatedAt();
            String[] splited = str.split("\\s");
            splittedDate = splited[0];
            splittedTime = splited[1];
        }

        if ((preferenceHelper.getSignUpUser().getId() + "").equalsIgnoreCase(entity.getSenderId())) {
            imageLoader.displayImage(entity.getSenderDetail().getImageUrl(), viewHolder.imageViewRight);
            viewHolder.txtReceiverChatRight.setText(entity.getMessage());
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.tv_date_time_right.setText(DateHelper.getFormatedDate(AppConstants.DateFormat_YMDHMS, AppConstants.DateFormat_DMY4, entity.getCreatedAt()));
           // viewHolder.tv_time_right.setText(DateHelper.getFormatedDate(AppConstants.TF, AppConstants.DateFormat_HM, splittedTime));

            viewHolder.tv_date_time_right.setText(DateHelper.getLocalDate(entity.getCreatedAt()) + " | " + DateHelper.getLocalTime(entity.getCreatedAt()));
            //viewHolder.tv_time_right.setText(DateHelper.getLocalTime(entity.getCreatedAt()));
        } else {
            viewHolder.txtSenderChatLeft.setText(entity.getMessage());
            viewHolder.RightLayout.setVisibility(View.GONE);
            viewHolder.tv_date_time_left.setText(DateHelper.getFormatedDate(AppConstants.DateFormat_YMDHMS, AppConstants.DateFormat_DMY4, entity.getCreatedAt()));
            //viewHolder.tv_time_left.setText(DateHelper.getFormatedDate(AppConstants.TF, AppConstants.DateFormat_HM, splittedTime));

            viewHolder.tv_date_time_left.setText(DateHelper.getLocalDate(entity.getCreatedAt()) + " | " + DateHelper.getLocalTime(entity.getCreatedAt()));
            //viewHolder.tv_time_left.setText(DateHelper.getLocalTime(entity.getCreatedAt()));
        }
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.imageViewLeft)
        ImageView imageViewLeft;
        @BindView(R.id.cardViewLeft)
        CardView cardViewLeft;
        @BindView(R.id.txtSenderChatLeft)
        AnyTextView txtSenderChatLeft;
        @BindView(R.id.leftLayoutChild)
        LinearLayout leftLayoutChild;
        @BindView(R.id.leftLayout)
        RelativeLayout leftLayout;
        @BindView(R.id.imageViewRight)
        ImageView imageViewRight;
        @BindView(R.id.cardViewRight)
        CardView cardViewRight;
        @BindView(R.id.txtReceiverChatRight)
        AnyTextView txtReceiverChatRight;
        @BindView(R.id.rightLayout)
        LinearLayout rightLayout;
        @BindView(R.id.RightLayout)
        RelativeLayout RightLayout;
        @BindView(R.id.tv_date_time_left)
        AnyTextView tv_date_time_left;
        //@BindView(R.id.tv_time_left)
        //AnyTextView tv_time_left;
        @BindView(R.id.tv_date_time_right)
        AnyTextView tv_date_time_right;
        //@BindView(R.id.tv_time_right)
        //AnyTextView tv_time_right;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
