package com.flipkart.android.register.pojo.tune;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by VectoR on 20-11-2017.
 */

public class TuneResponse {

    @SerializedName("success")
    @Expose
    public Boolean success;
    @SerializedName("tracking_id")
    @Expose
    public String trackingId;
    @SerializedName("log_id")
    @Expose
    public String logId;
    @SerializedName("site_event_type")
    @Expose
    public String siteEventType;
}
