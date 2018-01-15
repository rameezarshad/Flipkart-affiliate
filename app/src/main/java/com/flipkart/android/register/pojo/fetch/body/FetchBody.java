package com.flipkart.android.register.pojo.fetch.body;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VectoR on 14-11-2017.
 */

public class FetchBody {

    @SerializedName("pageUri")
    public String pageUri;

    @SerializedName("pageContext")
    public PageContext pageContext;


}
