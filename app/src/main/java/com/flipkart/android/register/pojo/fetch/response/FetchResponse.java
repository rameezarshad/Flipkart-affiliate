package com.flipkart.android.register.pojo.fetch.response;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by VectoR on 19-11-2017.
 */

public class FetchResponse {

    @SerializedName("REQUEST-ID")
    public String request_id;

    @SerializedName("hashCode")
    public Integer hashCode;

    @SerializedName("ERROR_MESSAGE")
    public String errorMsg;

    @SerializedName("REQUEST")
    public Map<String, Object> request;

    @SerializedName("ERROR_CODE")
    public Integer errorCode;

    @SerializedName("CACHE_INVALIDATION_TTL")
    public String cacheInv;

    @SerializedName("SESSION")
    public SessionResponse session;

    @SerializedName("STATUS_CODE")
    public int status_code;


}
