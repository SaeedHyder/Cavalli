package com.application.cavalliclub.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.activities.MainActivity;
import com.application.cavalliclub.entities.EntityGalleryList;
import com.application.cavalliclub.fragments.FragmentGalleryPager;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class AdapterRecyclerViewGallery extends RecyclerView.Adapter<AdapterRecyclerViewGallery.MyViewHolder> {

    private ArrayList<EntityGalleryList> entityGalleryLists;
    private DockActivity context;
    ArrayList<String> images ;
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

    public AdapterRecyclerViewGallery(ArrayList<EntityGalleryList> entityGalleryLists, DockActivity context, MainActivity mainActivity) {
        this.entityGalleryLists = entityGalleryLists;
        this.context = context;
        this.mainActivity=mainActivity;
    }

    @Override
    public AdapterRecyclerViewGallery.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_gallery, parent, false);
        return new AdapterRecyclerViewGallery.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterRecyclerViewGallery.MyViewHolder holder, final int position) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        EntityGalleryList entityGalleryList = entityGalleryLists.get(position);
        imageLoader.displayImage(entityGalleryList.getImageUrl(), holder.iv_latest_updates,mainActivity.getImageLoaderRoundCornerTransformation(10));
        holder.updates_description.setVisibility(View.GONE);

        holder.ll_rv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                final DialogHelper dialogHelper = new DialogHelper(context);
                dialogHelper.imageDisplayDialog(R.layout.image_display_dialoge, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelper.hideDialog();
                    }
                }, entityGalleryLists, position);
                dialogHelper.showDialog();*/
                images=new ArrayList<>();

                for (int i = 0; i < entityGalleryLists.size(); i++) {
                    images.add(entityGalleryLists.get(i).getImageUrl());
                }
                context.addDockableFragment(FragmentGalleryPager.newInstance(images, position), "FragmentGalleryPager");
            }
        });

    }

    @Override
    public int getItemCount() {
        return entityGalleryLists.size();
    }
}
