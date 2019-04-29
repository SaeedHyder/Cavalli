package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.MenuCategoryEntity;
import com.application.cavalliclub.entities.MenuCategoryProductAttributeEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.AppConstants;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.BinderMenuItemDetail;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MenuItemDetailFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    AnyTextView tvTitle;
    @BindView(R.id.tv_description)
    AnyTextView tvDescription;
    private static String MenuItemDetailData = "MenuItemDetailData";
    private static String productDetailkey = "productDetail";
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_price)
    AnyTextView tvPrice;
    @BindView(R.id.lv_menu_item_detail)
    ListView lvMenuItemDetail;
    private ArrayList<MenuCategoryEntity> menuCategoryEntities;
    private MenuCategoryEntity menuCategoryEntitiesWithoutArray;
    private ArrayList<MenuCategoryProductAttributeEntity> menuCategoryProductAttributeEntityArrayList;
    Unbinder unbinder;
    private ImageLoader imageLoader;
    private ArrayListAdapter<MenuCategoryProductAttributeEntity> adapter;
    private ArrayList<MenuCategoryProductAttributeEntity> userCollection;
    private List<MenuCategoryProductAttributeEntity> menuCategoryProductAttributeEntities;
    private Object result;

    public static MenuItemDetailFragment newInstance(List<MenuCategoryEntity> menuCategoryEntities) {
        Bundle args = new Bundle();
        args.putString(MenuItemDetailData, new Gson().toJson(menuCategoryEntities));
        MenuItemDetailFragment fragment = new MenuItemDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MenuItemDetailFragment newInstance(MenuCategoryEntity productDetail) {
        Bundle args = new Bundle();
        args.putString(productDetailkey, new Gson().toJson(productDetail));
        MenuItemDetailFragment fragment = new MenuItemDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public static MenuItemDetailFragment newInstance() {
        Bundle args = new Bundle();

        MenuItemDetailFragment fragment = new MenuItemDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderMenuItemDetail(getDockActivity()));

        MenuItemDetailData = getArguments().getString(MenuItemDetailData);
        productDetailkey = getArguments().getString(productDetailkey);
        if (MenuItemDetailData != null) {
            //menuCategoryEntities = new Gson().fromJson(MenuItemDetailData, MenuCategoryEntity.class);
            menuCategoryEntities = new Gson().fromJson(MenuItemDetailData, new TypeToken<ArrayList<MenuCategoryEntity>>() {
            }.getType());
        }
        if (productDetailkey != null) {
            menuCategoryEntitiesWithoutArray = new Gson().fromJson(productDetailkey, MenuCategoryEntity.class);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_item_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().showBottomTab(AppConstants.home);
        imageLoader = ImageLoader.getInstance();
        setMenuDetailData();
    }

    private void setMenuDetailData() {

        if (menuCategoryEntitiesWithoutArray.getProductImages().size() != 0 && menuCategoryEntitiesWithoutArray.getProductImages() != null && menuCategoryEntitiesWithoutArray.getProductImages().get(0).getImageUrl() != null)
            imageLoader.displayImage(menuCategoryEntitiesWithoutArray.getProductImages().get(0).getImageUrl(), ivImage);
        if (menuCategoryEntitiesWithoutArray.getTitle() != null)
            tvTitle.setText(menuCategoryEntitiesWithoutArray.getTitle());
        if (menuCategoryEntitiesWithoutArray.getDescription() != null)
            tvDescription.setText(menuCategoryEntitiesWithoutArray.getDescription());
        if (menuCategoryEntitiesWithoutArray != null && menuCategoryEntitiesWithoutArray.getProductAttributes().size() != 0
                && menuCategoryEntitiesWithoutArray.getProductAttributes().get(0) != null &&
                menuCategoryEntitiesWithoutArray.getProductAttributes().get(0).getValue() != null) {
            tvPrice.setText("AED" + " " + NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(menuCategoryEntitiesWithoutArray.getProductAttributes().get(0).getValue())));
            /*
            if (menuCategoryEntitiesWithoutArray.getProductAttributes().get(0).getValue().length() >= 4) {
                tvPrice.setText("AED" + " " + menuCategoryEntitiesWithoutArray.getProductAttributes().get(0).getValue().substring(0, 1) + "," + menuCategoryEntitiesWithoutArray.getProductAttributes().get(0).getValue().substring(1, menuCategoryEntitiesWithoutArray.getProductAttributes().get(0).getValue().length()));
            } else {
                tvPrice.setText("AED" + " " + menuCategoryEntitiesWithoutArray.getProductAttributes().get(0).getValue());
            }
            */
//            tvPrice.setText("AED" + " " + menuCategoryEntitiesWithoutArray.getProductAttributes().get(0).getValue());
        }

        //menuCategoryProductAttributeEntities = menuCategoryEntitiesWithoutArray;

        if (menuCategoryEntitiesWithoutArray.getProductAttributes().size() != 0 && menuCategoryEntitiesWithoutArray.getProductAttributes() != null)
            setListViewData(menuCategoryEntitiesWithoutArray.getProductAttributes());
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.hideTwoTabsLayout();
        //  titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.details));
    }

    private void setListViewData(List<MenuCategoryProductAttributeEntity> result) {
        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);
    }

    private void bindData(ArrayList<MenuCategoryProductAttributeEntity> userCollection) {

        adapter.clearList();
        lvMenuItemDetail.setAdapter(adapter);
        adapter.addAll(userCollection);
        adapter.notifyDataSetChanged();
    }
}
