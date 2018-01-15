package com.flipkart.android.register.pojo.registerApp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by VectoR on 20-11-2017.
 */

class Response {
    @SerializedName("action")
    @Expose
    public Object action;
    @SerializedName("key")
    @Expose
    public String key;
}
