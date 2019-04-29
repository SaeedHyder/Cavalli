package com.application.cavalliclub.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.helpers.BasePreferenceHelper;
import com.application.cavalliclub.interfaces.GalleryGvItemClick;
import com.application.cavalliclub.interfaces.RecyclerViewColorInterface;
import com.application.cavalliclub.retrofit.EntityGalleryCategories;
import com.application.cavalliclub.ui.views.AnyTextView;

import java.util.ArrayList;

public class AdapterRecyclerViewDays extends RecyclerView.Adapter<AdapterRecyclerViewDays.MyViewHolder> {

    private ArrayList<EntityGalleryCategories> entityDays;
    private DockActivity context;
    public GalleryGvItemClick galleryGvItemClick;
    private BasePreferenceHelper preferenceHelper;
    private RecyclerViewColorInterface ColorInterface;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public AnyTextView updates_description;
        public LinearLayout ll_rv_main;


        public MyViewHolder(View view) {
            super(view);
            updates_description = (AnyTextView) view.findViewById(R.id.updates_description);
            ll_rv_main = (LinearLayout) view.findViewById(R.id.ll_rv_main);
        }
    }

    public AdapterRecyclerViewDays(ArrayList<EntityGalleryCategories> entityDays, DockActivity dockActivity, GalleryGvItemClick galleryGvItemClick, BasePreferenceHelper preferenceHelper, RecyclerViewColorInterface ColorInterface) {
        this.entityDays = entityDays;
        this.context = dockActivity;
        this.galleryGvItemClick = galleryGvItemClick;
        this.preferenceHelper = preferenceHelper;
        this.ColorInterface=ColorInterface;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_gallery_days, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final EntityGalleryCategories entityGalleryCategories = entityDays.get(position);
        holder.updates_description.setText(entityGalleryCategories.getName());
        holder.updates_description.setTextColor(context.getResources().getColor(R.color.black));

        holder.ll_rv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryGvItemClick.OnClickService(entityGalleryCategories.getId());
                preferenceHelper.setDay(entityGalleryCategories.getId());
                entityGalleryCategories.setSelected(true);
                ColorInterface.changeColor(entityGalleryCategories,position);
                holder.updates_description.setTextColor(context.getResources().getColor(R.color.app_golden));
            }
        });

        if(entityGalleryCategories.getSelected()){
            holder.updates_description.setTextColor(context.getResources().getColor(R.color.app_golden));
        } else {
            holder.updates_description.setTextColor(context.getResources().getColor(R.color.black));
        }

    }

    @Override
    public int getItemCount() {
        return entityDays.size();
    }
}
