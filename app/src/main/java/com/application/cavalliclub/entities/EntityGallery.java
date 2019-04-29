package com.application.cavalliclub.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EntityGallery {

    @SerializedName("latest_photo")
    @Expose
    private ArrayList<EntityGalleryList> latestPhoto;
    @SerializedName("generall_photo")
    @Expose
    private ArrayList<EntityGalleryList> generallPhoto;

    public ArrayList<EntityGalleryList> getLatestPhoto() {
        return latestPhoto;
    }

    public void setLatestPhoto(ArrayList<EntityGalleryList> latestPhoto) {
        this.latestPhoto = latestPhoto;
    }

    public ArrayList<EntityGalleryList> getGenerallPhoto() {
        return generallPhoto;
    }

    public void setGenerallPhoto(ArrayList<EntityGalleryList> generallPhoto) {
        this.generallPhoto = generallPhoto;
    }
}
