package com.ingic.cavalliclub.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.activities.DockActivity;
import com.ingic.cavalliclub.activities.MainActivity;
import com.ingic.cavalliclub.entities.EntityGetTotalList;
import com.ingic.cavalliclub.entities.EntityGuestListMember;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.helpers.BasePreferenceHelper;
import com.ingic.cavalliclub.helpers.ServiceHelper;
import com.ingic.cavalliclub.interfaces.RecyclerViewItemListener;
import com.ingic.cavalliclub.retrofit.WebService;
import com.ingic.cavalliclub.retrofit.WebServiceFactory;
import com.ingic.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderAddMoreFriends extends ViewBinder<EntityGuestListMember> {

    @BindView(R.id.tv_guest_list)
    AnyTextView tvGuestList;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    private DockActivity context;
    private ImageLoader imageLoader;
    ServiceHelper serviceHelper;
    WebService webService;
    BaseFragment baseFragment;
    MainActivity mainActivity;
    RecyclerViewItemListener onCLick;
    BasePreferenceHelper prefHelper;

    public BinderAddMoreFriends(DockActivity context, WebService webService, ServiceHelper serviceHelper, BaseFragment baseFragment, MainActivity mainActivity, RecyclerViewItemListener onCLick, BasePreferenceHelper prefHelper) {
        super(R.layout.item_lv_guest_list);
        imageLoader = ImageLoader.getInstance();
        this.context = context;
        this.webService = webService;
        this.serviceHelper = serviceHelper;
        this.baseFragment = baseFragment;
        this.mainActivity = mainActivity;
        this.prefHelper = prefHelper;
        this.onCLick = onCLick;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }


    @Override
    public void bindView(final EntityGuestListMember entity, final int position, int grpPosition, View view, Activity activity) {
        BinderAddMoreFriends.ViewHolder viewHolder = (BinderAddMoreFriends.ViewHolder) view.getTag();
        if (webService == null) {
            webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(context, WebServiceConstants.Local_SERVICE_URL);
        }
        if (serviceHelper == null) {
            serviceHelper = new ServiceHelper(baseFragment, context, webService);
        }

        viewHolder.tvGuestList.setText(entity.getFullName() + "");
        viewHolder.ivDelete.setVisibility(View.VISIBLE);

        prefHelper.setTotalListId(entity.getId());

        viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCLick.onEditItemClicked(entity, position, entity.getId());
            }
        });

        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCLick.onDeleteItemClicked(entity, position);
            }
        });
    }


    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_guest_list)
        AnyTextView tvGuestList;
        @BindView(R.id.iv_edit)
        ImageView ivEdit;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.ll_main)
        LinearLayout ll_main;
        @BindView(R.id.ll_title)
        LinearLayout ll_title;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
