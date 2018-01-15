package com.flipkart.android.register.pojo.registerApp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by VectoR on 20-11-2017.
 */

public class RegisterAppResponse {

    @SerializedName("RESPONSE")
    public Response response;
    @SerializedName("SESSION")
    public Session session;
    @SerializedName("STATUS_CODE")
    public Integer statusCode;
}
