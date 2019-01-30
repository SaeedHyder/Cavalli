package com.ingic.cavalliclub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.EntityGallery;
import com.ingic.cavalliclub.entities.EntityGalleryList;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.interfaces.GalleryGvItemClick;
import com.ingic.cavalliclub.interfaces.RecyclerViewColorInterface;
import com.ingic.cavalliclub.retrofit.EntityGalleryCategories;
import com.ingic.cavalliclub.ui.adapters.AdapterRecyclerViewDays;
import com.ingic.cavalliclub.ui.adapters.AdapterRecyclerViewGallery;
import com.ingic.cavalliclub.ui.adapters.ArrayListAdapter;
import com.ingic.cavalliclub.ui.binders.BinderGallery;
import com.ingic.cavalliclub.ui.binders.BinderGalleryCategories;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.ExpendedGridView;
import com.ingic.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GalleryFragment extends BaseFragment implements GalleryGvItemClick, RecyclerViewColorInterface {

    Unbinder unbinder;
    ArrayList<EntityGalleryCategories> entityGalleryCategories = new ArrayList<>();
    EntityGallery entityGallery;
    ArrayList<EntityGalleryList> entityGalleryLatestPhotosLists = new ArrayList<>();
    ArrayList<EntityGalleryList> entityGalleryGridPhotosLists = new ArrayList<>();
    @BindView(R.id.gv_name)
    GridView gvName;
    @BindView(R.id.txt_updates)
    AnyTextView txtUpdates;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.gv_gallery)
    ExpendedGridView gvGallery;
    String FirstCategory = "";
    @BindView(R.id.recycler_view_days)
    RecyclerView recyclerViewDays;
    @BindView(R.id.txt_categories)
    AnyTextView txtCategories;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    //rv_latest
    private ArrayList<EntityGalleryList> userCollection;
    private AdapterRecyclerViewGallery mAdapter;
    //gv_photos
    private ArrayListAdapter<EntityGalleryList> adapter;
    //rv_days
    private ArrayList<EntityGalleryCategories> userCollectionCategories;
    private AdapterRecyclerViewDays adapterDays;
    private ArrayListAdapter<EntityGalleryCategories> adapterCategories;
    //gv_days
    private ArrayList<String> myGridCollection = new ArrayList<>();
    ArrayList<String> images=new ArrayList<>();
    public String ArrayStringContainer;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderGallery(getDockActivity(), getMainActivity(), prefHelper));
        adapterCategories = new ArrayListAdapter<>(getDockActivity(), new BinderGalleryCategories(getDockActivity(), getMainActivity(), prefHelper, this));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gvGallery.setExpanded(true);
        llMain.setVisibility(View.GONE);
        getGalleryCategories();
        getMainActivity().showBottomTab(AppConstants.home);
    }

    private void getGalleryPhotos(String id) {
        serviceHelper.enqueueCall(webService.gallery(id), WebServiceConstants.GALLERY);
    }

    private void getGalleryCategories() {
        serviceHelper.enqueueCall(webService.getGalleryCategories(), WebServiceConstants.GALLERY_CATEGORIES);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.GALLERY:
                entityGallery = (EntityGallery) result;
                entityGalleryLatestPhotosLists = entityGallery.getLatestPhoto();
                entityGalleryGridPhotosLists = entityGallery.getGenerallPhoto();
                setGalleryLatestPhotosData(entityGalleryLatestPhotosLists);
                setGalleryGridPhotos(entityGalleryGridPhotosLists);
                if (entityGalleryLatestPhotosLists != null && entityGalleryGridPhotosLists != null && (entityGalleryLatestPhotosLists.size() > 0 || entityGalleryGridPhotosLists.size() > 0)) {
                    llMain.setVisibility(View.VISIBLE);
                    txtNoData.setVisibility(View.GONE);
                } else {
                    llMain.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.VISIBLE);
                }
                break;

            case WebServiceConstants.GALLERY_CATEGORIES:
                entityGalleryCategories = (ArrayList<EntityGalleryCategories>) result;
                setGridNames(entityGalleryCategories);
                setGalleryCategoriesData(entityGalleryCategories);
                break;
        }
    }

    private void setGalleryCategoriesData(ArrayList<EntityGalleryCategories> entityGalleryCategories) {
        userCollectionCategories = new ArrayList<>();
        userCollectionCategories.addAll(entityGalleryCategories);
        bindDataCategories(userCollectionCategories);
    }

    private void bindDataCategories(ArrayList<EntityGalleryCategories> userCollectionCategories) {
        entityGalleryCategories.get(0).setSelected(true);
        adapterDays = new AdapterRecyclerViewDays(this.entityGalleryCategories, getDockActivity(), this, prefHelper, this);
        recyclerViewDays.setLayoutManager(new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewDays.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDays.setAdapter(adapterDays);
    }

    private void setGalleryLatestPhotosData(ArrayList<EntityGalleryList> entityGalleryLists) {
        userCollection = new ArrayList<>();
        userCollection.addAll(entityGalleryLists);
        bindDataLatestPhoto(userCollection);
    }

    private void bindDataLatestPhoto(ArrayList<EntityGalleryList> userCollection) {
        if (entityGalleryCategories != null && entityGalleryCategories.get(0) != null)
            mAdapter = new AdapterRecyclerViewGallery(this.entityGalleryLatestPhotosLists, getDockActivity(),getMainActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void setGalleryGridPhotos(ArrayList<EntityGalleryList> entityGalleryGridPhotosLists) {
        bindData(entityGalleryGridPhotosLists);
    }

    private void bindData(final ArrayList<EntityGalleryList> myGridCollection) {
        adapter.clearList();
        gvGallery.setAdapter(adapter);
        adapter.addAll(myGridCollection);

        gvGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < myGridCollection.size(); i++) {
                    images.add(myGridCollection.get(i).getImageUrl());
                }
                getDockActivity().replaceDockableFragment(FragmentGalleryPager.newInstance(images, position), "FragmentGalleryPager");
            }
        });
    }

    private void setGridNames(ArrayList<EntityGalleryCategories> entityGalleryCategories) {
        bindDataGridNames(entityGalleryCategories);
    }

    private void bindDataGridNames(final ArrayList<EntityGalleryCategories> myGridCollection) {
        adapter.clearList();
        gvName.setAdapter(adapterCategories);
        adapterCategories.addAll(myGridCollection);
        adapterCategories.notifyDataSetChanged();

        FirstCategory = myGridCollection.get(0).getId();
        getGalleryPhotos(FirstCategory);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
      //  titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.gallery));
    }

    @Override
    public void OnClickService(String id) {
        getGalleryPhotos(id);
    }

    @Override
    public void changeColor(EntityGalleryCategories entity, int position) {

        for (EntityGalleryCategories item : entityGalleryCategories) {
            item.setSelected(false);
        }
        entityGalleryCategories.get(position).setSelected(true);
        adapterDays.notifyDataSetChanged();
    }
}
