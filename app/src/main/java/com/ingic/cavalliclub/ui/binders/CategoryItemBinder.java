package com.ingic.cavalliclub.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.activities.DockActivity;
import com.ingic.cavalliclub.entities.CategoriesEnt;
import com.ingic.cavalliclub.interfaces.RecyclerViewItemListener;
import com.ingic.cavalliclub.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryItemBinder extends RecyclerViewBinder<CategoriesEnt> {

    private ImageLoader imageLoader;
    private RecyclerViewItemListener recyclerViewItemListener;
    private DockActivity dockActivity;
    private int pos;

    public CategoryItemBinder(RecyclerViewItemListener recyclerViewItemListener, DockActivity dockActivity, int i) {
        super(R.layout.row_item_header_recyclerview);
        imageLoader = ImageLoader.getInstance();
        this.recyclerViewItemListener = recyclerViewItemListener;
        this.dockActivity=dockActivity;
        this.pos=i;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(CategoriesEnt entity, final int position, final Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;

      holder.txtCategory.setText(entity.getItem());
      if(entity.isSelected()){
          holder.viewCategory.setVisibility(View.VISIBLE);
         holder.txtCategory.setTextColor(dockActivity.getResources().getColor(R.color.app_golden));
      }
      else{
          holder.viewCategory.setVisibility(View.GONE);
          holder.txtCategory.setTextColor(dockActivity.getResources().getColor(R.color.white));
      }


        holder.rlCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewItemListener.onEditItemClicked(viewHolder,position,dockActivity);
            }
        });
    }

    static class ViewHolder extends BaseViewHolder{
        @BindView(R.id.txt_category)
        AnyTextView txtCategory;
        @BindView(R.id.viewCategory)
        View viewCategory;
        @BindView(R.id.rl_category)
        RelativeLayout rlCategory;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
