package com.flipkart.android.register.pojo.mat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by VectoR on 19-11-2017.
 */

public class MatResponse {

    @SerializedName("success")
    @Expose
    public Boolean success;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("tracking_id")
    @Expose
    public String trackingId;
    @SerializedName("log_id")
    @Expose
    public String logId;

}
