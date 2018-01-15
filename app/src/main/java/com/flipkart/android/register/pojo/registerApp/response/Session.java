package com.flipkart.android.register.pojo.registerApp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by VectoR on 20-11-2017.
 */

public class Session {
    @SerializedName("accountId")
    @Expose
    public Object accountId;
    @SerializedName("email")
    @Expose
    public Object email;
    @SerializedName("firstName")
    @Expose
    public Object firstName;
    @SerializedName("flipkartFirstUser")
    @Expose
    public Boolean flipkartFirstUser;
    @SerializedName("isLoggedIn")
    @Expose
    public Boolean isLoggedIn;
    @SerializedName("lastName")
    @Expose
    public Object lastName;
    @SerializedName("nsid")
    @Expose
    public String nsid;
    @SerializedName("secureToken")
    @Expose
    public String secureToken;
    @SerializedName("sn")
    @Expose
    public String sn;
    @SerializedName("ts")
    @Expose
    public Integer ts;
    @SerializedName("vid")
    @Expose
    public String vid;
}
