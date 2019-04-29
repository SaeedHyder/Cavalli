package com.application.cavalliclub.helpers;

import android.util.Log;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.DockActivity;
import com.application.cavalliclub.entities.ResponseWrapper;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.interfaces.webServiceResponseLisener;
import com.application.cavalliclub.retrofit.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceHelper<T> {
    private webServiceResponseLisener serviceResponseLisener;
    private DockActivity context;
    private WebService webService;

    public ServiceHelper(webServiceResponseLisener serviceResponseLisener, DockActivity conttext, WebService webService) {
        this.serviceResponseLisener = serviceResponseLisener;
        this.context = conttext;
        this.webService = webService;
    }

    public void enqueueCall(Call<ResponseWrapper<T>> call, final String tag) {
        if (InternetHelper.CheckInternetConectivityandShowToast(context)) {
            context.onLoadingStarted();
            call.enqueue(new Callback<ResponseWrapper<T>>() {
                @Override
                public void onResponse(Call<ResponseWrapper<T>> call, Response<ResponseWrapper<T>> response) {
                    context.onLoadingFinished();
                    if (response != null && response.body() != null && response.body().getCode() != null) {
                        if (response.body().getCode().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                            serviceResponseLisener.ResponseSuccess(response.body().getResult(), tag);
                        } else {
                            UIHelper.showShortToastInCenter(context, response.body().getMessage());
                        }
                    }else{
                        UIHelper.showShortToastInCenter(context, context.getResources().getString(R.string.no_response));
                    }
                }

                @Override
                public void onFailure(Call<ResponseWrapper<T>> call, Throwable t) {
                    context.onLoadingFinished();
                    t.printStackTrace();
                    Log.e(ServiceHelper.class.getSimpleName() + " by tag: " + tag, t.toString());
                }
            });
        }
    }
}
