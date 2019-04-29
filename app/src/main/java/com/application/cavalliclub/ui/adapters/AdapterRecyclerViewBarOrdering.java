package com.application.cavalliclub.ui.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.entities.BarCategoriesEntity;
import com.application.cavalliclub.entities.MenuCategoryEntity;
import com.application.cavalliclub.fragments.MixDrinksFragment;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;


public class AdapterRecyclerViewBarOrdering extends RecyclerView.Adapter<AdapterRecyclerViewBarOrdering.MyViewHolder> {

    private BarCategoriesEntity barList;
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

    public AdapterRecyclerViewBarOrdering(BarCategoriesEntity barList, DockActivity dockActivity) {
        this.barList = barList;
        this.context = dockActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_home, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ImageLoader imageLoader = ImageLoader.getInstance();

        final MenuCategoryEntity entityRecyclerviewBar = barList.getProducts().get(position);
        imageLoader.displayImage(entityRecyclerviewBar.getImageUrl(), holder.iv_latest_updates);
        holder.updates_description.setText(entityRecyclerviewBar.getTitle());

        holder.ll_rv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.replaceDockableFragment(MixDrinksFragment.newInstance(barList), "MixDrinksFragment");
            }
        });
    }

    @Override
    public int getItemCount() {
        return barList.getProducts().size();
    }
}