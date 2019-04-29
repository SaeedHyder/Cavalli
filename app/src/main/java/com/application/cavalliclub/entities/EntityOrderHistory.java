package com.application.cavalliclub.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EntityOrderHistory {

    @SerializedName("pending_order")
    @Expose
    private ArrayList<EntityPendingsOrders> pendingOrder;
    @SerializedName("order_history")
    @Expose
    private ArrayList<EntityPendingsOrders> orderHistory;

    public ArrayList<EntityPendingsOrders> getPendingOrder() {
        return pendingOrder;
    }

    public void setPendingOrder(ArrayList<EntityPendingsOrders> pendingOrder) {
        this.pendingOrder = pendingOrder;
    }

    public ArrayList<EntityPendingsOrders> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(ArrayList<EntityPendingsOrders> orderHistory) {
        this.orderHistory = orderHistory;
    }
}
