package com.flipkart.android.register.pojo.registerReferral;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by VectoR on 20-11-2017.
 */

public class RegisterReferralResponse {
    @SerializedName("SESSION")
    @Expose
    public SESSION sESSION;
    @SerializedName("STATUS_CODE")
    @Expose
    public Integer sTATUSCODE;
}
