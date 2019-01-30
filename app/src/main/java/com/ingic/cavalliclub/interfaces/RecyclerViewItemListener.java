package com.ingic.cavalliclub.interfaces;

import com.ingic.cavalliclub.activities.DockActivity;

/**
 * Created on gym_image_10/19/2017.
 */

public interface RecyclerViewItemListener {
    public void onEditItemClicked(Object Ent, int position, DockActivity dockActivity);
    public void onEditItemClicked(Object Ent, int position, String id);
    public void onDeleteItemClicked(Object Ent, int position);
    public void onClickItemFilter();
}
