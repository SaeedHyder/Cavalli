package com.application.cavalliclub.ui.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.application.cavalliclub.R;
import com.application.cavalliclub.interfaces.ViewPagerItemClickListner;
import com.application.cavalliclub.ui.views.TouchImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by ahmedsyed on 10/18/2018.
 */


public class ViewPagerAdapterMenu extends PagerAdapter {
    Context context;
    ArrayList<String> images;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader;
    private ViewPagerItemClickListner viewPagerItemClickListner;


    public ViewPagerAdapterMenu(Context context, ArrayList<String> images, ViewPagerItemClickListner viewPagerItemClickListner) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = ImageLoader.getInstance();
        this.viewPagerItemClickListner = viewPagerItemClickListner;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.pager_item_menu, container, false);

        TouchImageView imageView = (TouchImageView) itemView.findViewById(R.id.imageView);
       // ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageLoader.displayImage(images.get(position), imageView);
        //  imageView.setImageResource(images[position]);

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
                viewPagerItemClickListner.pagerClick(position);
            }
        });

        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }


}

