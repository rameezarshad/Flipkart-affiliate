package com.flipkart.android.register;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

/**
 * Created by VectoR on 18-09-2017.
 */

public class GenRandomValues {


    private String[] arch = new String[]{
            "armeabi",
            "x86",
            "x86-64",
            "armv82",
            "armv83",
            "armv8",
            "armv7"};

    private GetterSetter getterSetter;
    public GenRandomValues(){
        getterSetter = new GetterSetter();
        setRandom();
    }

    private void setRandom(){
        deviceId(16);
        deviceHash();
        macAddress();
        prip();
        fk_uuid();
        installId();
        setDevice_Arch();
        setFromCSV();
        getSecurityPatch();
    }

    private void setFromCSV(){

        String[] randomDevice = RegistrationHelper.devices.get(new Random().nextInt(RegistrationHelper.devices.size()));
        String[] randomVersion = RegistrationHelper.versions.get(new Random().nextInt(RegistrationHelper.versions.size()));
        String[] randomNetwork = RegistrationHelper.network_codes.get(new Random().nextInt(RegistrationHelper.network_codes.size()));
        String[] randomScreen = RegistrationHelper.screen.get(new Random().nextInt(RegistrationHelper.screen.size()));

        getterSetter.setBrand(randomDevice[0]);
        getterSetter.setModel(randomDevice[1]);
        getterSetter.setAndroid_build(randomVersion[0]);
        getterSetter.setAndroid_version(randomVersion[1]);
        getterSetter.setSdkVersion(Integer.parseInt(randomVersion[3]));

        getterSetter.setNetwork_Code(randomNetwork[0]);
        getterSetter.setMcc(randomNetwork[1]);
        getterSetter.setDevice_carrier(randomNetwork[2]);

        getterSetter.setScreen_layout(randomScreen[0]);
        getterSetter.setScreen_density(randomScreen[1]);

    }

    private void deviceId(int len) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < len) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString().toLowerCase();
        getterSetter.setDevice_id(saltStr);
    }

    private String getSaltPrip(int len) {
        String SALTCHARS = "abcdef1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < len) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    private void macAddress() {
        Random rand = new Random();
        byte[] macAddr = new byte[6];
        rand.nextBytes(macAddr);

        macAddr[0] = (byte) (macAddr[0] & (byte) 254);

        StringBuilder sb = new StringBuilder(18);
        for (byte b : macAddr) {

            if (sb.length() > 0)
                sb.append(":");

            sb.append(String.format("%02x", b));
        }

        getterSetter.setMacAddress(sb.toString());
    }

    private void prip() {
        getterSetter.setPrip( getSaltPrip(4) + "::" + getSaltPrip(4) + ":" + getSaltPrip(4) + ":" + getSaltPrip(4));
    }

    private void installId() {
        getterSetter.setInstall_id(Md5Gen.md5(getterSetter.getDeviceHash() + "_" + UUID.randomUUID().toString()));
    }

    private void deviceHash() {
        getterSetter.setDeviceHash(Md5Gen.md5(getterSetter.getDevice_id()));
    }

    private void fk_uuid() {
        getterSetter.setFk_uuid(UUID.randomUUID().toString());
    }

    public String checksum() {
        return  RegistrationHelper.getMessage(Long.toString(getterSetter.getTime() / 1000), getterSetter.getDeviceHash());
    }

    private void setDevice_Arch() {
        getterSetter.setDevice_Arch(arch[new Random().nextInt(arch.length)]);
    }

    private void getSecurityPatch(){

        String patch = null;

        if(getterSetter.getSdkVersion() >= 23){
            SimpleDateFormat dfDateTime  = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            int year = randBetween(2016, 2017);
            int month = randBetween(0, 11);


            GregorianCalendar gc = new GregorianCalendar(year, month, 1);
            int day = randBetween(1, gc.getActualMaximum(gc.DAY_OF_MONTH));

            gc.set(year, month, day);

            patch = dfDateTime.format(gc.getTime());

        }

        getterSetter.setSecurityPatchInfo(patch);
    }

    private static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

    public GetterSetter getGetterSetter(){
        return this.getterSetter;
    }
}
