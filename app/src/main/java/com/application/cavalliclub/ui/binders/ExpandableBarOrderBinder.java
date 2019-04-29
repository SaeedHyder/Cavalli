package com.application.cavalliclub.ui.binders;

import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.entities.BarCategoriesEntity;
import com.application.cavalliclub.fragments.MixDrinksFragment;
import com.application.cavalliclub.helpers.BasePreferenceHelper;
import com.application.cavalliclub.ui.adapters.AdapterRecyclerViewBarOrdering;
import com.application.cavalliclub.ui.viewbinders.abstracts.ExpandableListViewBinder;
import com.application.cavalliclub.ui.views.AnyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ExpandableBarOrderBinder extends ExpandableListViewBinder<BarCategoriesEntity, BarCategoriesEntity> {

    DockActivity context;
    private AdapterRecyclerViewBarOrdering mAdapter;


    public ExpandableBarOrderBinder(DockActivity context, BasePreferenceHelper prefHelper) {
        super(R.layout.item_elv_parent, R.layout.item_elv_child);
        this.context = context;
    }

    @Override
    public BaseGroupViewHolder createGroupViewHolder(View view) {
        return new ParentViewHolder(view);
    }

    @Override
    public BaseGroupViewHolder createChildViewHolder(View view) {
        return new ChildViewHolder(view);
    }

    @Override
    public void bindGroupView(final BarCategoriesEntity entity, int position, int grpPosition, View view, Activity activity) {

        ParentViewHolder parentViewHolder = (ParentViewHolder) view.getTag();
        parentViewHolder.tvDrinkName.setText(entity.getName() + "");

        parentViewHolder.llSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity != null)
                    context.replaceDockableFragment(MixDrinksFragment.newInstance(entity), "MixDrinksFragment");
            }
        });
    }

    @Override
    public void bindChildView(BarCategoriesEntity entity, int position, int grpPosition, View view, Activity activity) {

        ChildViewHolder childViewHolder = (ChildViewHolder) view.getTag();
        if (entity != null && entity.getProducts() != null && entity.getProducts().size() != 0) {
            //  bindData((ArrayList<ProductBarCategoriesEntity>) entity.getProducts(), childViewHolder);
            bindData(entity, childViewHolder, position);
        } else {

        }
    }

    private void bindData(final BarCategoriesEntity userCollection, ChildViewHolder childViewHolder, int position) {

        mAdapter = new AdapterRecyclerViewBarOrdering(userCollection, context);
        childViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        childViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        childViewHolder.recyclerView.setAdapter(mAdapter);
    }

    static class ParentViewHolder extends BaseGroupViewHolder {
        @BindView(R.id.ll_see_all)
        LinearLayout llSeeAll;
        @BindView(R.id.tv_drink_name)
        AnyTextView tvDrinkName;

        ParentViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder extends BaseGroupViewHolder {
        @BindView(R.id.recycler_view)
        RecyclerView recyclerView;
        @BindView(R.id.ll_linear)
        LinearLayout ll_linear;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
