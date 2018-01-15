package com.flipkart.android.register.pojo.appInstall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by VectoR on 22-11-2017.
 */

public class AppInstall {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("os")
    @Expose
    private String os;
    @SerializedName("clickType")
    @Expose
    private String clickType;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("affExtParam1")
    @Expose
    private String affExtParam1;
    @SerializedName("installCount")
    @Expose
    private Integer installCount;
    @SerializedName("commissionPerInstall")
    @Expose
    private Integer commissionPerInstall;
    @SerializedName("totalCommission")
    @Expose
    private Integer totalCommission;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getClickType() {
        return clickType;
    }

    public void setClickType(String clickType) {
        this.clickType = clickType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAffExtParam1() {
        return affExtParam1;
    }

    public void setAffExtParam1(String affExtParam1) {
        this.affExtParam1 = affExtParam1;
    }

    public Integer getInstallCount() {
        return installCount;
    }

    public void setInstallCount(Integer installCount) {
        this.installCount = installCount;
    }

    public Integer getCommissionPerInstall() {
        return commissionPerInstall;
    }

    public void setCommissionPerInstall(Integer commissionPerInstall) {
        this.commissionPerInstall = commissionPerInstall;
    }

    public Integer getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(Integer totalCommission) {
        this.totalCommission = totalCommission;
    }
}
