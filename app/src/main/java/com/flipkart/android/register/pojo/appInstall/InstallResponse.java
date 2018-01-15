package com.flipkart.android.register.pojo.appInstall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by VectoR on 22-11-2017.
 */

public class InstallResponse {
    @SerializedName("appInstalls")
    @Expose
    private List<AppInstall> appInstalls = null;

    public List<AppInstall> getAppInstalls() {
        return appInstalls;
    }

    public void setAppInstalls(List<AppInstall> appInstalls) {
        this.appInstalls = appInstalls;
    }

}
