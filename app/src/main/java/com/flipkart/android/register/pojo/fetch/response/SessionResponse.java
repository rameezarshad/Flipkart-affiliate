package com.flipkart.android.register.pojo.fetch.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by VectoR on 15-11-2017.
 */

public class SessionResponse {

    @SerializedName("accountId")
    @Expose
    public String accountId;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("firstName")
    @Expose
    public String firstName;
    @SerializedName("flipkartFirstUser")
    @Expose
    public Boolean flipkartFirstUser;
    @SerializedName("isLoggedIn")
    @Expose
    public Boolean isLoggedIn;
    @SerializedName("lastName")
    @Expose
    public String lastName;
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
