package com.application.cavalliclub.interfaces;

public interface TotalSum {

    void sum(int multiplyAmount, int pos);
    void addAmount(int multiplyAmount, int pos, int amount);
    void substractAmount(int multiplyAmount, int pos, int amount);
    void onClickAdapterNotify(int position);
}
