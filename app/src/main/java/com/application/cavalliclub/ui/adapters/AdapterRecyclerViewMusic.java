package com.application.cavalliclub.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.entities.EntityCavalliNights;
import com.application.cavalliclub.fragments.SeeAllMusicFragment;
import com.application.cavalliclub.helpers.InternetHelper;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class AdapterRecyclerViewMusic extends RecyclerView.Adapter<AdapterRecyclerViewMusic.MyViewHolder> {

    private List<EntityCavalliNights> entityCavalliNights;
    private DockActivity context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_latest_updates;
        public AnyTextView updates_description;
        public LinearLayout ll_rv_main;

        ImageLoader imageLoader;

        public MyViewHolder(View view) {
            super(view);
            iv_latest_updates = (ImageView) view.findViewById(R.id.iv_latest_updates);
            updates_description = (AnyTextView) view.findViewById(R.id.updates_description);
            ll_rv_main = (LinearLayout) view.findViewById(R.id.ll_rv_main);
            imageLoader = ImageLoader.getInstance();
        }
    }

    public AdapterRecyclerViewMusic(List<EntityCavalliNights> entityCavalliNights, DockActivity dockActivity) {
        this.entityCavalliNights = entityCavalliNights;
        this.context = dockActivity;
    }

    @Override
    public AdapterRecyclerViewMusic.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_home, parent, false);
        return new AdapterRecyclerViewMusic.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterRecyclerViewMusic.MyViewHolder holder, final int position) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        EntityCavalliNights entity = entityCavalliNights.get(position);
        imageLoader.displayImage(entity.getImageUrl(), holder.iv_latest_updates);
        holder.updates_description.setText(entity.getDescription() + "");

        holder.ll_rv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetHelper.CheckInternetConectivityandShowToast(context)) {
                    context.replaceDockableFragment(SeeAllMusicFragment.newInstance(position), "SeeAllMusicFragment");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return entityCavalliNights.size();
    }
}
