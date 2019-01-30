package com.ingic.cavalliclub.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.activities.DockActivity;
import com.ingic.cavalliclub.activities.MainActivity;
import com.ingic.cavalliclub.entities.EntityRecyclerviewHome;
import com.ingic.cavalliclub.entities.LatestUpdatesEntity;
import com.ingic.cavalliclub.fragments.LatestUpdateDetailFragment;
import com.ingic.cavalliclub.fragments.LatestUpdatesMusicFragment;
import com.ingic.cavalliclub.helpers.InternetHelper;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class AdapterRecyclerViewHome extends RecyclerView.Adapter<AdapterRecyclerViewHome.MyViewHolder> {

    /*private List<EntityRecyclerviewHome> homeList;*/
    private List<LatestUpdatesEntity> latestUpdatesEntities;
    private DockActivity context;
    static boolean isGallery;
    private MainActivity mainActivity;

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

    public AdapterRecyclerViewHome(List<LatestUpdatesEntity> latestUpdatesEntities, DockActivity dockActivity, MainActivity mainActivity, boolean checker) {
        this.latestUpdatesEntities = latestUpdatesEntities;
        /*this.homeList = homeList;*/
        this.context = dockActivity;
        isGallery = checker;
        this.mainActivity=mainActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_home, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        final LatestUpdatesEntity latestUpdatesEntity = latestUpdatesEntities.get(position);
        imageLoader.displayImage(latestUpdatesEntity.getImageUrl(), holder.iv_latest_updates,mainActivity.getImageLoaderRoundCornerTransformation(10));

        if (!isGallery) {
            holder.updates_description.setVisibility(View.VISIBLE);
            holder.updates_description.setText(latestUpdatesEntity.getTitle());

            holder.ll_rv_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (InternetHelper.CheckInternetConectivityandShowToast(context)) {
                       // context.replaceDockableFragment(LatestUpdatesMusicFragment.newInstance(position), "LatestUpdatesMusicFragment");
                        context.replaceDockableFragment(LatestUpdateDetailFragment.newInstance(latestUpdatesEntity), "LatestUpdatesMusicFragment");

                    }
                }
            });
        } else {
            holder.updates_description.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return latestUpdatesEntities.size();
    }
}