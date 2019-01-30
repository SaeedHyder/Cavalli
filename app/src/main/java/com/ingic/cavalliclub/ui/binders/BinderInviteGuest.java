package com.ingic.cavalliclub.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.activities.DockActivity;
import com.ingic.cavalliclub.entities.EntityGetTotalList;
import com.ingic.cavalliclub.entities.EntityGuestListMember;
import com.ingic.cavalliclub.fragments.AddMoreFriendsFragment;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderInviteGuest extends ViewBinder<EntityGetTotalList> {

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
    BasePreferenceHelper preferenceHelper;
    private static boolean isInviteGuest;
    String ArrayStringContainer;
    List<EntityGuestListMember> entityGuestListMember = new ArrayList<>();
    RecyclerViewItemListener onCLick;

    public BinderInviteGuest(DockActivity context, boolean checker, WebService webService, ServiceHelper serviceHelper, BaseFragment baseFragment, BasePreferenceHelper preferenceHelper, RecyclerViewItemListener onCLick) {
        super(R.layout.item_lv_guest_list);
        imageLoader = ImageLoader.getInstance();
        isInviteGuest = checker;
        this.context = context;
        this.webService = webService;
        this.serviceHelper = serviceHelper;
        this.baseFragment = baseFragment;
        this.preferenceHelper = preferenceHelper;
        this.onCLick = onCLick;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }


    @Override
    public void bindView(final EntityGetTotalList entity, final int position, int grpPosition, View view, Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (webService == null) {
            webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(context, WebServiceConstants.Local_SERVICE_URL);
        }
        if (serviceHelper == null){
            serviceHelper = new ServiceHelper(baseFragment,context,webService);
        }

        viewHolder.tvGuestList.setText(entity.getTitle()+"");

        if (isInviteGuest) {
            viewHolder.ll_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    entityGuestListMember = entity.getGuestListMember();

                    Gson gson = new Gson();
                    ArrayStringContainer = gson.toJson(entityGuestListMember);
                    preferenceHelper.setGuestMemebrList(ArrayStringContainer);
                    context.replaceDockableFragment(AddMoreFriendsFragment.newInstance(entity.getId(), 3), "AddMoreFriendsFragment");
                }
            });

            viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCLick.onEditItemClicked(entity, position, String.valueOf(entity.getId()));
                }
            });

            viewHolder.ivDelete.setVisibility(View.GONE);
        }

        if (!isInviteGuest){
            viewHolder.ivDelete.setVisibility(View.VISIBLE);
        }
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
