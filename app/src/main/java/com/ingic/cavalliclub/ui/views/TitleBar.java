package com.ingic.cavalliclub.ui.views;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.activities.DockActivity;
import com.ingic.cavalliclub.entities.CategoriesEnt;
import com.ingic.cavalliclub.entities.MenuCategoryParentEntity;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.interfaces.MenuInterface;
import com.ingic.cavalliclub.interfaces.RecyclerViewItemListener;
import com.ingic.cavalliclub.ui.binders.CategoryItemBinder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TitleBar extends RelativeLayout implements RecyclerViewItemListener {

    private TextView txtTitle;
    private ImageView btnLeft;
    private ImageView btnRight2;
    private ImageView btnRight;
    private AnyTextView txtBadge;
    private ImageView header_layout;
    private LinearLayout titleTabs;
    private RelativeLayout rlCategory1;
    private RelativeLayout rlCategory2;
    private RelativeLayout rlCategory3;
    private RelativeLayout title_buttons;
    private AnyTextView txtCategories1;
    private AnyTextView txtCategories2;
    private AnyTextView txtCategories3;
    private View viewCategory1;
    private View viewCategory2;
    private View viewCategory3;
    private CustomRecyclerView rv_categories;
    private LinearLayout ll_recyclerView;
    private ImageLoader imageLoader;

    private View.OnClickListener menuButtonListener;
    private View.OnClickListener filterButtonListener;
    private OnClickListener backButtonListener;
    private OnClickListener notificationButtonListener;
    private View.OnClickListener cartButtonListener;

    private Context context;
    private ArrayList<CategoriesEnt> userCollections;
    private ArrayList<MenuCategoryParentEntity> dataCollection;
    private MenuInterface menuInterface;
    private int previousSelectedPos=0;

    public TitleBar(Context context) {
        super(context);
        this.context = context;
        initLayout(context);


    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
        if (attrs != null)
            initAttrs(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context);
        if (attrs != null)
            initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
    }

    private void bindViews() {

        txtTitle = (TextView) this.findViewById(R.id.txt_subHead);
        btnRight = (ImageView) this.findViewById(R.id.btnRight);
        btnRight2 = (ImageView) this.findViewById(R.id.btnRight2);
        btnLeft = (ImageView) this.findViewById(R.id.btnLeft);
        txtBadge = (AnyTextView) findViewById(R.id.txtBadge);
        header_layout = (ImageView) findViewById(R.id.header_layout);
        titleTabs = (LinearLayout) findViewById(R.id.title_tabs);
        rlCategory1 = (RelativeLayout) findViewById(R.id.rl_category1);
        rlCategory2 = (RelativeLayout) findViewById(R.id.rl_category2);
        rlCategory3 = (RelativeLayout) findViewById(R.id.rl_category3);
        title_buttons = (RelativeLayout) findViewById(R.id.title_buttons);
        txtCategories1 = (AnyTextView) this.findViewById(R.id.txt_categories1);
        txtCategories2 = (AnyTextView) this.findViewById(R.id.txt_categories2);
        txtCategories3 = (AnyTextView) this.findViewById(R.id.txt_categories3);
        viewCategory1 = (View) this.findViewById(R.id.viewCategory1);
        viewCategory2 = (View) this.findViewById(R.id.viewCategory2);
        viewCategory3 = (View) this.findViewById(R.id.viewCategory3);
        rv_categories = (CustomRecyclerView) this.findViewById(R.id.lv_categories);
        ll_recyclerView = (LinearLayout) this.findViewById(R.id.ll_recyclerView);


    }

    private void initLayout(Context context) {
        imageLoader = ImageLoader.getInstance();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header_main, this);
        bindViews();
    }

    public void hideButtons() {
        //  imageLoader.displayImage("drawable://" + R.drawable.title_header, header_layout);

        txtTitle.setVisibility(View.INVISIBLE);
        btnLeft.setVisibility(View.INVISIBLE);
        btnRight.setVisibility(View.INVISIBLE);
        btnRight2.setVisibility(View.INVISIBLE);
        txtBadge.setVisibility(View.GONE);
        titleTabs.setVisibility(View.GONE);
        rlCategory1.setVisibility(View.GONE);
        rlCategory2.setVisibility(View.GONE);
        rlCategory3.setVisibility(View.GONE);
        txtCategories1.setVisibility(View.GONE);
        txtCategories2.setVisibility(View.GONE);
        txtCategories3.setVisibility(View.GONE);
        viewCategory1.setVisibility(View.GONE);
        viewCategory2.setVisibility(View.GONE);
        viewCategory3.setVisibility(View.GONE);
        ll_recyclerView.setVisibility(GONE);
        header_layout.setVisibility(GONE);

    }

    public void showTwoTabsLayout(boolean Tab1, String text1, String text2) {
        titleTabs.setVisibility(VISIBLE);
        rlCategory1.setVisibility(VISIBLE);
        rlCategory2.setVisibility(VISIBLE);
      //  header_layout.setVisibility(VISIBLE);
        txtCategories1.setText(text1);
        txtCategories2.setText(text2);

        if (Tab1) {
            txtCategories1.setSelected(true);
            txtCategories2.setSelected(false);
            txtCategories1.setTextColor(getResources().getColor(R.color.app_golden));
            txtCategories2.setTextColor(getResources().getColor(R.color.white));
            txtCategories1.setVisibility(View.VISIBLE);
            txtCategories2.setVisibility(View.VISIBLE);
            viewCategory1.setVisibility(View.VISIBLE);
            viewCategory2.setVisibility(View.GONE);
        } else {
            txtCategories1.setSelected(false);
            txtCategories2.setSelected(true);
            txtCategories1.setTextColor(getResources().getColor(R.color.white));
            txtCategories2.setTextColor(getResources().getColor(R.color.app_golden));

            txtCategories1.setVisibility(View.VISIBLE);
            txtCategories2.setVisibility(View.VISIBLE);
            viewCategory1.setVisibility(View.GONE);
            viewCategory2.setVisibility(View.VISIBLE);
        }

    }

    public void showThreeTabsLayout(String tab, String text1, String text2, String text3) {
        titleTabs.setVisibility(VISIBLE);
        rlCategory1.setVisibility(VISIBLE);
        rlCategory2.setVisibility(VISIBLE);
        rlCategory3.setVisibility(VISIBLE);
     //   header_layout.setVisibility(VISIBLE);
        txtCategories1.setVisibility(View.VISIBLE);
        txtCategories2.setVisibility(View.VISIBLE);
        txtCategories3.setVisibility(View.VISIBLE);
        txtCategories1.setText(text1);
        txtCategories2.setText(text2);
        txtCategories3.setText(text3);

        if (tab.equals(AppConstants.tab1)) {
            txtCategories1.setSelected(true);
            txtCategories2.setSelected(false);
            txtCategories3.setSelected(false);
            txtCategories1.setTextColor(getResources().getColor(R.color.app_golden));
            txtCategories2.setTextColor(getResources().getColor(R.color.white));
            txtCategories3.setTextColor(getResources().getColor(R.color.white));
            viewCategory1.setVisibility(View.VISIBLE);
            viewCategory2.setVisibility(View.GONE);
            viewCategory3.setVisibility(View.GONE);
        } else if (tab.equals(AppConstants.tab2)) {
            txtCategories1.setSelected(false);
            txtCategories2.setSelected(true);
            txtCategories3.setSelected(false);
            txtCategories1.setTextColor(getResources().getColor(R.color.white));
            txtCategories2.setTextColor(getResources().getColor(R.color.app_golden));
            txtCategories3.setTextColor(getResources().getColor(R.color.white));
            viewCategory1.setVisibility(View.GONE);
            viewCategory2.setVisibility(View.VISIBLE);
            viewCategory3.setVisibility(View.GONE);
        } else if (tab.equals(AppConstants.tab3)) {
            txtCategories1.setSelected(false);
            txtCategories2.setSelected(false);
            txtCategories3.setSelected(true);
            txtCategories1.setTextColor(getResources().getColor(R.color.white));
            txtCategories2.setTextColor(getResources().getColor(R.color.white));
            txtCategories3.setTextColor(getResources().getColor(R.color.app_golden));
            viewCategory1.setVisibility(View.GONE);
            viewCategory2.setVisibility(View.GONE);
            viewCategory3.setVisibility(View.VISIBLE);
        }
    }

    public AnyTextView getTextViewCategory(int ResId) {
        return (AnyTextView) this.findViewById(ResId);
    }

    public void hideTwoTabsLayout() {
        titleTabs.setVisibility(GONE);
        rlCategory1.setVisibility(GONE);
        rlCategory2.setVisibility(GONE);
        rlCategory3.setVisibility(GONE);
        txtCategories1.setVisibility(View.GONE);
        txtCategories2.setVisibility(View.GONE);
        txtCategories3.setVisibility(View.GONE);
        viewCategory1.setVisibility(View.GONE);
        viewCategory2.setVisibility(View.GONE);
        viewCategory3.setVisibility(View.GONE);
        ll_recyclerView.setVisibility(GONE);
        header_layout.setVisibility(GONE);
    }

    public void tabsClick(OnClickListener buttonOne, OnClickListener buttonTwo) {
        rlCategory1.setOnClickListener(buttonOne);
        rlCategory2.setOnClickListener(buttonTwo);
    }

    public void threetabsClick(OnClickListener buttonOne, OnClickListener buttonTwo, OnClickListener buttonThree) {
        rlCategory1.setOnClickListener(buttonOne);
        rlCategory2.setOnClickListener(buttonTwo);
        rlCategory3.setOnClickListener(buttonThree);
    }


    public void setTitlebarBackgroundColor(int BackgroundImage) {
        header_layout.setVisibility(VISIBLE);
        header_layout.setBackgroundResource(BackgroundImage);
    }

    public void setTitlebarBackgroundColor(int BackgroundImage, DockActivity dockActivity) {
        header_layout.setVisibility(VISIBLE);
        Picasso.with(dockActivity).load(BackgroundImage).placeholder(R.drawable.tab_header).into(header_layout);
    }

    public void setLayout_below() {
        RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(titleTabs.getLayoutParams());
        params.addRule(RelativeLayout.BELOW, R.id.title_buttons);
        titleTabs.setLayoutParams(params);
    }




    public void setTitlebarBackgroundMenu(String BackgroundImage,DockActivity dockActivity) {
        header_layout.setVisibility(VISIBLE);
      //  imageLoader.displayImage(BackgroundImage, header_layout);
        Picasso.with(dockActivity).load(BackgroundImage).placeholder(R.drawable.placeholder_banner).into(header_layout);
    }

    public void setTitlebarBackgroundTransparent() {
        header_layout.setBackgroundResource(R.color.transparent);
    }

    public void showBackButton() {
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(backButtonListener);
        btnLeft.setImageResource(R.drawable.backbtn);
    }

    public void showfilterButton(OnClickListener filter) {
        btnRight2.setVisibility(View.VISIBLE);
        btnRight2.setOnClickListener(filter);
        btnRight2.setImageResource(R.drawable.filter);
    }

    public void showBackButtonAsPerRequirement(OnClickListener backbtn) {
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setImageResource(R.drawable.backbtn);
        btnLeft.setOnClickListener(backbtn);

    }

    public void showBackButtonAsPerRequirement(OnClickListener backbtn,DockActivity dockActivity) {
          /*if(dockActivity!=null){
            dockActivity.popFragment();
        }*/
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setImageResource(R.drawable.backbtn);
        btnLeft.setOnClickListener(backbtn);

    }

    public ImageView getAndShowBackButton(){
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setImageResource(R.drawable.backbtn);
        return btnLeft;
    }

    public void showMenuButton(OnClickListener backbtn) {
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(menuButtonListener);
        btnLeft.setImageResource(R.drawable.nav);
    }


    public void showCartButton(OnClickListener cartBtn) {
        btnRight2.setVisibility(View.VISIBLE);
        btnRight2.setOnClickListener(cartBtn);
        btnRight2.setImageResource(R.drawable.cart);
    }

    public void showCartButton(int Count) {
        btnRight2.setVisibility(View.VISIBLE);
        btnRight2.setOnClickListener(cartButtonListener);
        btnRight2.setImageResource(R.drawable.cart);

        if (Count > 0) {
            txtBadge.setVisibility(View.VISIBLE);
            txtBadge.setText(Count + "");
        } else {
            txtBadge.setVisibility(View.GONE);
        }
    }

    public void setSubHeading(String heading) {
        txtTitle.setVisibility(View.VISIBLE);
        txtTitle.setText(heading);
    }

    public void showNotificationButton(int Count) {
        btnRight.setVisibility(View.INVISIBLE);
        btnRight2.setVisibility(View.VISIBLE);
        btnRight2.setOnClickListener(notificationButtonListener);
        btnRight2.setImageResource(R.drawable.ic_launcher);
        if (Count > 0) {
            txtBadge.setVisibility(View.VISIBLE);
            txtBadge.setText(Count + "");
        } else {
            txtBadge.setVisibility(View.GONE);
        }
    }

    public void showTitleBar() {
        this.setVisibility(View.VISIBLE);
    }

    public void hideTitleBar() {
        this.setVisibility(View.GONE);
    }

    public void setMenuButtonListener(View.OnClickListener listener) {
        menuButtonListener = listener;
    }

    public void setFilterButtonListener(View.OnClickListener listener) {
        filterButtonListener = listener;
    }

    public void setBackButtonListener(View.OnClickListener listener) {
        backButtonListener = listener;
    }

    public void setCartButtonListener(View.OnClickListener listener) {
        cartButtonListener = listener;
    }

    public void setNotificationButtonListener(View.OnClickListener listener) {
        notificationButtonListener = listener;
    }

    public void setRecyclerViewData(DockActivity dockActivity, ArrayList<MenuCategoryParentEntity> menuCategoryParentEntities,
                                    MenuInterface menuInterface) {

        ll_recyclerView.setVisibility(VISIBLE);

        userCollections = new ArrayList<>();
        dataCollection = new ArrayList<>();
        dataCollection.addAll(menuCategoryParentEntities);

        /*for (MenuCategoryParentEntity item : menuCategoryParentEntities) {
            userCollections.add(new CategoriesEnt(item.getName() + ""));
        }*/
        for(int i=0;i<=menuCategoryParentEntities.size()-1;i++){
            if(i==0){
                userCollections.add(new CategoriesEnt(menuCategoryParentEntities.get(i).getName() + "",true));
            }else{
                userCollections.add(new CategoriesEnt(menuCategoryParentEntities.get(i).getName() + "",false));
            }
        }

        this.menuInterface = menuInterface;
        rv_categories.BindRecyclerView(new CategoryItemBinder(this, dockActivity, 0), userCollections,
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                , new DefaultItemAnimator());
    }

    @Override
    public void onEditItemClicked(Object Ent, int position, DockActivity dockActivity) {

        userCollections.get(position).setSelected(true);
        rv_categories.notifyItemChanged(position);
        if (previousSelectedPos != position) {
            userCollections.get(previousSelectedPos).setSelected(false);
            rv_categories.notifyItemChanged(previousSelectedPos);
        }
        previousSelectedPos = position;

       /* rv_categories.BindRecyclerView(new CategoryItemBinder(this, dockActivity, position), userCollections,
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                , new DefaultItemAnimator());
        rv_categories.scrollToPosition(position);*/
        menuInterface.menuItemClick(dataCollection.get(position));
    }

    @Override
    public void onEditItemClicked(Object Ent, int position, String id) {
    }

    @Override
    public void onDeleteItemClicked(Object Ent, int position) {
    }

    @Override
    public void onClickItemFilter() {
    }
}
