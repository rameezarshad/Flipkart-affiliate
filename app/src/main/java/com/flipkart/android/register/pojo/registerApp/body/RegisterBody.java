package com.flipkart.android.register.pojo.registerApp.body;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VectoR on 20-11-2017.
 */

public class RegisterBody {

    @SerializedName("timestamp")
    public Long timestamp;
    @SerializedName("referral")
    public String referral;
    @SerializedName("prip")
    public String prip;
    @SerializedName("isAppUpdated")
    public boolean isAppUpdated;
    @SerializedName("isOSUpdated")
    public boolean isOSUpdated;
    @SerializedName("installId")
    public String installId;
    @SerializedName("macAddress")
    public String macAddress;
    @SerializedName("isFirstLaunch")
    public Boolean isFirstLaunch;
    @SerializedName("securityPatchInfo")
    public String securityPatchInfo;


    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getReferral() {
        return referral;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }

    public String getPrip() {
        return prip;
    }

    public void setPrip(String prip) {
        this.prip = prip;
    }

    public boolean isAppUpdated() {
        return isAppUpdated;
    }

    public void setAppUpdated(boolean appUpdated) {
        isAppUpdated = appUpdated;
    }

    public boolean isOSUpdated() {
        return isOSUpdated;
    }

    public void setOSUpdated(boolean OSUpdated) {
        isOSUpdated = OSUpdated;
    }

    public String getInstallId() {
        return installId;
    }

    public void setInstallId(String installId) {
        this.installId = installId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Boolean getFirstLaunch() {
        return isFirstLaunch;
    }

    public void setFirstLaunch(Boolean firstLaunch) {
        isFirstLaunch = firstLaunch;
    }

    public String getSecurityPatchInfo() {
        return securityPatchInfo;
    }

    public void setSecurityPatchInfo(String securityPatchInfo) {
        this.securityPatchInfo = securityPatchInfo;
    }

}
