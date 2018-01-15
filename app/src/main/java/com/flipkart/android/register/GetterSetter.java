package com.flipkart.android.register;

/**
 * Created by VectoR on 14-09-2017.
 */

public class GetterSetter {
    private String device_id;
    private String deviceHash;
    private String macAddress;
    private String prip;
    private String install_id;
    private String fk_uuid;
    private String checksum;
    private String mat;
    private String secure_cookie;
    private String secure_token;
    private String sn;
    private String installDate;
    private Long time;
    private String firstEncryptionHex;
    private String secondEncryptionHex;
    private String google_aid;
    private String mat_id;
    private String encodedUserAgent;
    private String mcc;
    private String android_version,
                    brand, model,
                    android_build,
                    device_carrier,
                    device_Arch,
                    network_Code,
                    screen_density,
                    screen_layout,
                    user_agent;
    private int threadCount;

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(int sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    private int sdkVersion;

    public String getSecurityPatchInfo() {
        return securityPatchInfo;
    }

    public void setSecurityPatchInfo(String securityPatchInfo) {
        this.securityPatchInfo = securityPatchInfo;
    }

    private String securityPatchInfo;


    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDeviceHash() {
        return deviceHash;
    }

    public void setDeviceHash(String deviceHash) {
        this.deviceHash = deviceHash;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getPrip() {
        return prip;
    }

    public void setPrip(String prip) {
        this.prip = prip;
    }

    public String getInstall_id() {
        return install_id;
    }

    public void setInstall_id(String install_id) {
        this.install_id = install_id;
    }

    public String getFk_uuid() {
        return fk_uuid;
    }

    public void setFk_uuid(String fk_uuid) {
        this.fk_uuid = fk_uuid;
    }

    public String getChecksum() {
        return checksum;
    }
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
    public void setTime(Long time){
        this.time = time;
    }
    public Long getTime(){
        return time;
    }


    public String getMat() {
        return mat;
    }

    public void setMat(String mat) {
        this.mat = mat;
    }

    public String getSecure_cookie() {
        return secure_cookie;
    }

    public void setSecure_cookie(String secure_cookie) {
        this.secure_cookie = secure_cookie;
    }

    public String getSecure_token() {
        return secure_token;
    }

    public void setSecure_token(String secure_token) {
        this.secure_token = secure_token;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }


    public String getFirstEncryptionHex() {
        return firstEncryptionHex;
    }

    public void setFirstEncryptionHex(String firstEncryptionHex) {
        this.firstEncryptionHex = firstEncryptionHex;
    }

    public String getSecondEncryptionHex() {
        return secondEncryptionHex;
    }

    public void setSecondEncryptionHex(String secondEncryptionHex) {
        this.secondEncryptionHex = secondEncryptionHex;
    }

    public String getInstallDate() {
        return installDate;
    }

    public void setInstallDate(String installDate) {
        this.installDate = installDate;
    }


    public String getGoogle_aid() {
        return google_aid;
    }

    public void setGoogle_aid(String google_aid) {
        this.google_aid = google_aid;
    }

    public String getMat_id() {
        return mat_id;
    }

    public void setMat_id(String mat_id) {
        this.mat_id = mat_id;
    }


    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAndroid_build() {
        return android_build;
    }

    public void setAndroid_build(String android_build) {
        this.android_build = android_build;
    }

    public String getDevice_carrier() {
        return device_carrier;
    }

    public void setDevice_carrier(String device_carrier) {
        this.device_carrier = device_carrier;
    }

    public String getDevice_Arch() {
        return device_Arch;
    }

    public void setDevice_Arch(String device_Arch) {
        this.device_Arch = device_Arch;
    }

    public String getNetwork_Code() {
        return network_Code;
    }

    public void setNetwork_Code(String network_Code) {
        this.network_Code = network_Code;
    }

    public String getScreen_density() {
        return screen_density;
    }

    public void setScreen_density(String screen_density) {
        this.screen_density = screen_density;
    }

    public String getScreen_layout() {
        return screen_layout;
    }

    public void setScreen_layout(String screen_layout) {
        this.screen_layout = screen_layout;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getEncodedUserAgent() {
        return encodedUserAgent;
    }

    public void setEncodedUserAgent(String encodedUserAgent) {
        this.encodedUserAgent = encodedUserAgent;
    }


}
