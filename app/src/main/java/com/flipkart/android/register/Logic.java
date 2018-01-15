package com.flipkart.android.register;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flipkart.android.register.adapter.ResponseRecycler;
import com.flipkart.android.register.model.ModelView;
import com.flipkart.android.register.network.APIInterface;
import com.flipkart.android.register.network.ApiClient;
import com.flipkart.android.register.network.CallbackWithRetry;
import com.flipkart.android.register.pojo.fetch.body.FetchBody;
import com.flipkart.android.register.pojo.fetch.body.PageContext;
import com.flipkart.android.register.pojo.fetch.response.FetchResponse;
import com.flipkart.android.register.pojo.mat.MatResponse;
import com.flipkart.android.register.pojo.registerApp.body.RegisterBody;
import com.flipkart.android.register.pojo.registerApp.response.RegisterAppResponse;
import com.flipkart.android.register.pojo.registerReferral.RegisterReferralResponse;
import com.flipkart.android.register.pojo.tune.TuneResponse;
import com.flipkart.android.register.pojo.tune.body.TuneBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import retrofit2.Call;
import retrofit2.Callback;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by VectoR on 18-09-2017.
 */

public class Logic {

    private String REQUEST_TAG;
    private LinkedHashMap<String, String> headers;
    private Context context;
    private String[] dalvik_version = {"1.6.0","2.1.0"};
    private GetterSetter getterSetter;
    private GenRandomValues genRandomValues;
    private TuneEncryption tuna = new TuneEncryption("734edc954086ec9261ede3696bb21557".trim(),"heF9BATUfWuISyO8");
    private ResponseRecycler adapter;
    private ModelView modelView;
    private Worker worker;
    private String mat_url;
    private String install_type= "";
    private String referrer = "";
    private RegistrationHelper registrationHelper;

    private String appVersionName = "5.15";
    private String appVersion = "840300";
    private String sdkVersion = "4.12.0";
    private Boolean unRegistered = false;

    private APIInterface apiInterface, httpInterface;

    public Logic(String REQUEST_TAG, Context context, ResponseRecycler adapter, ModelView modelView, Worker worker, String mat_url,  String install_type, String referrer, RegistrationHelper registrationHelper){
        this.REQUEST_TAG = REQUEST_TAG;
        this.genRandomValues = new GenRandomValues();
        this.getterSetter = genRandomValues.getGetterSetter();
        this.modelView = modelView;
        this.context = context;
        this.adapter = adapter;
        this.worker = worker;
        this.mat_url = mat_url;
        this.install_type = install_type;
        this.referrer = referrer;
        this.registrationHelper = registrationHelper;
        this.apiInterface = RegistrationHelper.getApiInterface();
        this.httpInterface = RegistrationHelper.getHttpInterface();

        if(getterSetter.getDevice_Arch().contains("x86"))
            appVersion = "840350";
        else
            appVersion = "840300";

    }

    public void initRequest(){
        modelView.setLogic(this);
        modelView.setResponse("Initializing the Request");
        modelView.setStatus("Wait");
        adapter.notifyDataSetChanged();
        gettingMat();
    }

    public void test(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                modelView.setTimeStamp(new SimpleDateFormat("HH:mm:ss").format(new Date()));
                modelView.setColor(getRandomMaterialColor("400"));
                modelView.setStatus("Success");
                modelView.setResponse(mat_url);
                modelView.setLogic(Logic.this);
                adapter.notifyDataSetChanged();
            }
        }, 2000);


    }

    public void startInit(){

            REQUEST_TAG = modelView.getThread_name();
            modelView.setTimeStamp(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            modelView.setColor(getRandomMaterialColor("400"));
            registerThread();
            initRequest();


    }
    public void sendAgain(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendRequest();
            }
        }, 2000);

    }

    public void sendRequest(){

        stop = false;
        modelView.setTimeStamp(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        modelView.setColor(getRandomMaterialColor("500"));
        registerThread();
        initRequest();

    }

    public void cancelTest(){
        modelView.setStatus("Stopped");
        adapter.notifyDataSetChanged();

    }
    public void cancelRequest(){
        stop = true;
        worker.setCurrentThreads(worker.getCurrentThreads()-1);
        modelView.setStatus("Stopped");
        adapter.notifyDataSetChanged();

    }

    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = context.getResources().getIdentifier("mdcolor_" + typeColor, "array", context.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }
    public void gettingMat(){
        if(stop){
            modelView.setStatus("Stopped");
            adapter.notifyDataSetChanged();
            return;
        }

        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("action", "click");
        data.put("publisher_id", "173204");
        data.put("site_id", "106226");
        data.put("sub_ad", "");
        data.put("sub_publisher", referrer);
        data.put("sub_placement", install_type);
        data.put("sub3", "");
        data.put("site_id_android", "106226");
        data.put("site_id_ios", "107540");
        data.put("site_id_windows", "108110");
        data.put("site_id_web", "108040");
        data.put("response_format", "json");


        Call<MatResponse> call = httpInterface.getMatId("https://173204.measurementapi.com/serve", data);
        call.enqueue(new CallbackWithRetry<MatResponse>(call) {
            @Override
            public void onResponse(Call<MatResponse> call, retrofit2.Response<MatResponse> response) {
                try{
                    String data = response.body().url;

                    getterSetter.setMat(data.substring(data.indexOf("mat_click_id%3D")+15));

                    modelView.setResponse("Successfully received MAT CLICK ID: "+getterSetter.getMat()+"\nWait for 2 mins");
                    modelView.setStatus("Installing");

                    if(stop){
                        modelView.setStatus("Stopped");
                        adapter.notifyDataSetChanged();
                        return;
                    }

                    adapter.notifyDataSetChanged();

                }
                catch (NullPointerException e){
                    e.printStackTrace();
                    modelView.setResponse("Null Pointer while Getting Mat");
                    modelView.setStatus("NullPointer");
                    adapter.notifyDataSetChanged();
                    unRegisterThread();
                }
                finally {

                    final Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            sendWithDelay();


                        }
                    }, 90000);

                }

            }

            @Override
            public void onFailure(Call<MatResponse> call, Throwable t) {
                super.onFailure(call, t);
                modelView.setResponse("Something went wrong while Initialising, \n" + t.toString());
                modelView.setStatus("Failure");
                adapter.notifyDataSetChanged();
                unRegisterThread();
            }
        });
    }

    public void sendWithDelay(){
        if(stop){
            modelView.setStatus("Stopped");
            adapter.notifyDataSetChanged();
            return;
        }
        getterSetter.setInstallDate(Integer.toString((int)(System.currentTimeMillis()/1000)-(new Random().nextInt(30)+40)));
        getterSetter.setTime(System.currentTimeMillis());
        getterSetter.setChecksum(genRandomValues.checksum());
        getterSetter.setGoogle_aid(UUID.randomUUID().toString());
        getterSetter.setMat_id(UUID.randomUUID().toString());
        getterSetter.setUser_agent("Dalvik/"+dalvik_version[new Random().nextInt(dalvik_version.length)]+" (Linux; U; Android "+getterSetter.getAndroid_version()+"; "+getterSetter.getModel()+" Build/"+getterSetter.getAndroid_build()+")");
        try{
            getterSetter.setEncodedUserAgent(URLEncoder.encode(getterSetter.getUser_agent(),"utf-8"));
            String firstEncryption = "connection_type=wifi&app_name=Flipkart&app_version="+appVersion+"&app_version_name="+appVersionName+"&country_code=in&device_brand="+ URLEncoder.encode(getterSetter.getBrand(),"utf-8")+"&build="+getterSetter.getAndroid_build()+"&device_carrier="+URLEncoder.encode(getterSetter.getDevice_carrier(),"utf-8")+"&device_cpu_type="+getterSetter.getDevice_Arch()+"&device_model="+URLEncoder.encode(getterSetter.getModel(),"utf-8")+"&insdate="+getterSetter.getInstallDate()+"&installer=com.android.vending&language=en&locale=en_IN&mat_id="+getterSetter.getMat_id()+"&mobile_country_code="+getterSetter.getMcc()+"&mobile_network_code="+getterSetter.getNetwork_Code()+"&os_version="+getterSetter.getAndroid_version()+"&screen_density="+getterSetter.getScreen_density()+"&screen_layout_size="+getterSetter.getScreen_layout()+"&sdk_version="+sdkVersion+"&conversion_user_agent="+getterSetter.getEncodedUserAgent()+"&currency_code=USD&revenue=0.0&google_aid="+getterSetter.getGoogle_aid()+"&google_ad_tracking_disabled=0&install_referrer=mat_click_id%3D"+getterSetter.getMat()+"%26utm_campaign%3DInternal%26utm_content%3DInternal%26utm_medium%3D"+install_type+"%26utm_source%3Dfkrt_affiliate_network%26utm_term%3DInternal&system_date="+Long.toString(getterSetter.getTime()/1000);
            getterSetter.setFirstEncryptionHex(bytesToHex(tuna.encrypt(firstEncryption)));

        }catch (Exception e){
            e.printStackTrace();
        }
        modelView.setResponse("App is installed, \nSending Fetch Request");
        modelView.setStatus("Sending");

        adapter.notifyDataSetChanged();

        fetchRequest();

    }

    public void fetchRequest(){
        if(stop){
            modelView.setStatus("Stopped");
            adapter.notifyDataSetChanged();
            return;
        }

        headers = new LinkedHashMap<>();
        headers.put("X-User-Agent", "Mozilla/5.0 (Linux; Android "+getterSetter.getAndroid_version()+"; "+getterSetter.getModel()+" Build/"+getterSetter.getAndroid_build()+"; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/60.0.3112.116 Mobile Safari/537.36 FKUA/Retail/"+appVersion+"/Android/Mobile ("+getterSetter.getBrand()+"/"+getterSetter.getModel()+"/" + getterSetter.getDeviceHash() + ")");
        headers.put("X-Visit-Id", getterSetter.getDeviceHash() + "-" + Long.toString(getterSetter.getTime()));


        FetchBody fetchBody = new FetchBody();
        fetchBody.pageContext = new PageContext();
        fetchBody.pageUri="/";

        Call<FetchResponse> call = httpInterface.fetchPageData(fetchBody, "{\"appVersion\":\"830000\"}", headers);

        call.enqueue(new CallbackWithRetry<FetchResponse>(call) {
            @Override
            public void onResponse(Call<FetchResponse> call, retrofit2.Response<FetchResponse> response) {

                modelView.setResponse("Fetch was successful,\nRegistering the APP");
                modelView.setStatus("Register");

                adapter.notifyDataSetChanged();

                if(stop){
                    modelView.setStatus("Stopped");
                    adapter.notifyDataSetChanged();
                    return;
                }

                registerAppRequest();
            }

            @Override
            public void onFailure(Call<FetchResponse> call, Throwable t) {
                super.onFailure(call, t);
                modelView.setResponse("Something went wrong while Fetching Request, \n" + t.toString());
                modelView.setStatus("Failure");
                adapter.notifyDataSetChanged();
                unRegisterThread();

            }
        });


    }

    public void registerAppRequest(){
        if(stop){
            modelView.setStatus("Stopped");
            adapter.notifyDataSetChanged();
            return;
        }
        RegisterBody registerInfo = getRegisterInfo();
        Call<RegisterAppResponse> call = apiInterface.register(getterSetter.getChecksum(), registerInfo, headers);
        call.enqueue(new CallbackWithRetry<RegisterAppResponse>(call) {
            @Override
            public void onResponse(Call<RegisterAppResponse> call, retrofit2.Response<RegisterAppResponse> response) {

                try{
                    RegisterAppResponse registerAppResponse = response.body();

                    getterSetter.setSecure_cookie(response.headers().get("secureCookie"));
                    getterSetter.setSecure_token(registerAppResponse.session.secureToken);
                    getterSetter.setSn(registerAppResponse.session.sn);

                    modelView.setResponse("Everything seems to be fine, \nHacking into Tune");
                    modelView.setStatus("Hack 1");

                    adapter.notifyDataSetChanged();
                    if(stop){
                        modelView.setStatus("Stopped");
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    firstTuneRequest();
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                    modelView.setResponse("NullPointer while registering App");
                    modelView.setStatus("NullPointer");
                    adapter.notifyDataSetChanged();
                    unRegisterThread();

                }

            }

            @Override
            public void onFailure(Call<RegisterAppResponse> call, Throwable t) {
                super.onFailure(call, t);
                modelView.setResponse("Something went wrong while Registering APP, \n" + t.toString());
                modelView.setStatus("Failure");
                adapter.notifyDataSetChanged();
                unRegisterThread();
            }
        });


    }

    public void firstTuneRequest(){
        if(stop){
            modelView.setStatus("Stopped");
            adapter.notifyDataSetChanged();
            return;
        }
        TuneBody body = new TuneBody();
        body.data = new ArrayList();

        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("ver", sdkVersion);
        data.put("transaction_id", UUID.randomUUID().toString());
        data.put("sdk_retry_attempt", String.valueOf(0));
        data.put("sdk", "android");
        data.put("action", "session");
        data.put("advertiser_id", "189358");
        data.put("package_name", "com.flipkart.android");
        data.put("data", getterSetter.getFirstEncryptionHex());

        Call<TuneResponse> call = httpInterface.tune("https://189358.engine.mobileapptracking.com/serve", data, body);
        call.enqueue(new CallbackWithRetry<TuneResponse>(call) {
            @Override
            public void onResponse(Call<TuneResponse> call, retrofit2.Response<TuneResponse> response) {
                modelView.setResponse("First hack was Successful, \nHacking into Tune.... ");
                modelView.setStatus("Hack 2");

                adapter.notifyDataSetChanged();
                if(stop){
                    modelView.setStatus("Stopped");
                    adapter.notifyDataSetChanged();
                    return;
                }
                secondTuneRequest();
            }

            @Override
            public void onFailure(Call<TuneResponse> call, Throwable t) {
                super.onFailure(call, t);
                modelView.setResponse("Something went wrong while First Hack, \n" + t.toString());
                modelView.setStatus("Failure");
                adapter.notifyDataSetChanged();
                unRegisterThread();
            }
        });

    }
    private String secondEncryption, time_delay;

    public void secondTuneRequest(){
        if(stop){
            modelView.setStatus("Stopped");
            adapter.notifyDataSetChanged();
            return;
        }
        try{
            time_delay = String.valueOf(new Random().nextInt(2000)+500);
            secondEncryption = "connection_type=wifi&android_id_md5="+getterSetter.getDeviceHash()+"&app_name=Flipkart&app_version="+appVersion+"&app_version_name="+appVersionName+"&country_code=in&device_brand="+URLEncoder.encode(getterSetter.getBrand(),"utf-8")+"&build="+getterSetter.getAndroid_build()+"&device_carrier="+URLEncoder.encode(getterSetter.getDevice_carrier(),"utf-8")+"&device_cpu_type="+getterSetter.getDevice_Arch()+"&device_model="+URLEncoder.encode(getterSetter.getModel(),"utf-8")+"&google_aid="+getterSetter.getGoogle_aid()+"&google_ad_tracking_disabled=0&insdate="+getterSetter.getInstallDate()+"&installer=com.android.vending&install_referrer=mat_click_id%3D"+getterSetter.getMat()+"%26utm_campaign%3DInternal%26utm_content%3DInternal%26utm_medium%3D"+install_type+"%26utm_source%3Dfkrt_affiliate_network%26utm_term%3DInternal&language=en&locale=en_IN&mat_id="+getterSetter.getMat_id()+"&mobile_country_code="+getterSetter.getMcc()+"&mobile_network_code="+getterSetter.getNetwork_Code()+"&os_version="+getterSetter.getAndroid_version()+"&referrer_delay="+time_delay+"&screen_density="+getterSetter.getScreen_density()+"&screen_layout_size="+getterSetter.getScreen_layout()+"&sdk_version="+sdkVersion+"&conversion_user_agent="+getterSetter.getEncodedUserAgent()+"&currency_code=INR&revenue=0.0&system_date="+Long.toString(System.currentTimeMillis()/1000);

            getterSetter.setSecondEncryptionHex(bytesToHex(tuna.encrypt(secondEncryption)));

        }catch (Exception e){
            e.printStackTrace();
        }


        TuneBody body = new TuneBody();
        body.data = new ArrayList();

        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("ver", sdkVersion);
        data.put("transaction_id", UUID.randomUUID().toString());
        data.put("sdk_retry_attempt", String.valueOf(0));
        data.put("sdk", "android");
        data.put("action", "session");
        data.put("advertiser_id", "189358");
        data.put("package_name", "com.flipkart.android");
        data.put("data", getterSetter.getSecondEncryptionHex());

        Call<TuneResponse> call = httpInterface.tune("https://189358.engine.mobileapptracking.com/serve", data, body);
        call.enqueue(new CallbackWithRetry<TuneResponse>(call) {
            @Override
            public void onResponse(Call<TuneResponse> call, retrofit2.Response<TuneResponse> response) {

                modelView.setResponse("Second hack was also Successful, \nWe are almost done...");
                modelView.setStatus("Registering Referrer");

                adapter.notifyDataSetChanged();
                if(stop){
                    modelView.setStatus("Stopped");
                    adapter.notifyDataSetChanged();
                    return;
                }
                registerReferrer();
            }

            @Override
            public void onFailure(Call<TuneResponse> call, Throwable t) {
                super.onFailure(call, t);
                modelView.setResponse("Something went wrong while Second Hack, \n" + t.toString());
                modelView.setStatus("Failure");
                adapter.notifyDataSetChanged();
                unRegisterThread();

            }
        });

    }



    public void registerReferrer(){
        if(stop){
            modelView.setStatus("Stopped");
            adapter.notifyDataSetChanged();
            return;
        }


        headers.put("secureCookie", getterSetter.getSecure_cookie());
        headers.put("secureToken", getterSetter.getSecure_token());
        headers.put("sn", getterSetter.getSn());



        Call<RegisterReferralResponse> call = apiInterface.registerReferrer(headers, "mat_click_id="+getterSetter.getMat()+"&utm_campaign=Internal&utm_content=Internal&utm_medium="+install_type+"&utm_source=fkrt_affiliate_network&utm_term=Internal&fk_uuid="+getterSetter.getFk_uuid(),getterSetter.getDeviceHash(),getterSetter.getDevice_id(), "paid", getterSetter.getTime()/1000, getterSetter.getInstall_id());

        call.enqueue(new CallbackWithRetry<RegisterReferralResponse>(call) {
            @Override
            public void onResponse(Call<RegisterReferralResponse> call, retrofit2.Response<RegisterReferralResponse> response) {

                modelView.setResponse("Successfully Registered \nEverything's done successfully");
                modelView.setStatus("Success");
                if(stop){
                    modelView.setStatus("Stopped");
                    adapter.notifyDataSetChanged();
                    return;
                }


                adapter.notifyDataSetChanged();
                unRegisterThread();
            }

            @Override
            public void onFailure(Call<RegisterReferralResponse> call, Throwable t) {
                super.onFailure(call, t);
                modelView.setResponse("Something went wrong while Registering Referrer, \n" + t.toString());
                modelView.setStatus("Failure");
                adapter.notifyDataSetChanged();
                unRegisterThread();


            }
        });
    }

    private void registerThread(){
        worker.setCurrentThreads(worker.getCurrentThreads()+1);
    }

    private void unRegisterThread(){
        if(!unRegistered){
            worker.setCurrentThreads(worker.getCurrentThreads()-1);
            unRegistered = true;
        }
        checkToSend();
    }

    private void checkToSend(){

        if(worker.getCurrentThreads() == 0){
            if(worker.getRepeat()>0){
                registrationHelper.hideFab(true);
                registrationHelper.initializeRequest();
            }
            else {
                if(worker.getRem() > 0){
                    registrationHelper.hideFab(true);
                    worker.setConcurrent(worker.getRem());
                    worker.setRem(0);
                    registrationHelper.initializeRequest();
                }
                else {
                    registrationHelper.hideFab(false);
                }
            }

        }
    }
    private static String bytesToHex(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        int length = bArr.length;
        String str = "";
        for (int i = 0; i < length; i++) {
            if ((bArr[i] & 255) < 16) {
                str = str + "0" + Integer.toHexString(bArr[i] & 255);
            } else {
                str = str + Integer.toHexString(bArr[i] & 255);
            }
        }
        return str;
    }

    public String allGenValues(){
        StringBuilder values = new StringBuilder();
        values.append("Device Model = ");
        values.append(getterSetter.getModel());
        values.append("\n");

        values.append("Device Hash = ");
        values.append(getterSetter.getDeviceHash());
        values.append("\n");

        values.append("Device Id = ");
        values.append(getterSetter.getDevice_id());
        values.append("\n");

        values.append("Mac Address = ");
        values.append(getterSetter.getMacAddress());
        values.append("\n");

        values.append("Prip = ");
        values.append(getterSetter.getPrip());
        values.append("\n");

        values.append("Device Brand = ");
        values.append(getterSetter.getBrand());
        values.append("\n");

        values.append("Android Version = ");
        values.append(getterSetter.getAndroid_version());
        values.append("\n");

        values.append("Device Carrier = ");
        values.append(getterSetter.getDevice_carrier());
        values.append("\n");

        values.append("Install Date = ");
        values.append(getterSetter.getInstallDate());
        values.append("\n");

        values.append("Screen Layout = ");
        values.append(getterSetter.getScreen_layout());
        values.append("\n");

        values.append("User Agent = ");
        values.append(getterSetter.getUser_agent());
        values.append("\n");

        values.append("Time Delay = ");
        values.append(time_delay);
        values.append("\n");

        values.append("Sdk Version = ");
        values.append(getterSetter.getSdkVersion());
        values.append("\n");

        values.append("Security Patch Info = ");
        values.append(getterSetter.getSecurityPatchInfo());
        values.append("\n");

        values.append("VersionCode = ");
        values.append(appVersion);
        values.append("\n");

        values.append("Device Arch = ");
        values.append(getterSetter.getDevice_Arch());
        values.append("\n");


        return values.toString();

    }
    private Boolean stop = false;

    private RegisterBody getRegisterInfo(){

        RegisterBody registerBody = new RegisterBody();

        registerBody.setTimestamp(getterSetter.getTime()/1000);

        registerBody.setReferral("mat_click_id=" + getterSetter.getMat() + "&utm_campaign=Internal&utm_content=Internal&utm_medium="+install_type+"&utm_source=fkrt_affiliate_network&utm_term=Internal&fk_uuid=" + getterSetter.getFk_uuid());

        registerBody.setPrip(getterSetter.getPrip() + "%wlan0");

        registerBody.setAppUpdated(false);

        registerBody.setOSUpdated(false);

        registerBody.setInstallId(getterSetter.getInstall_id());

        registerBody.setMacAddress(getterSetter.getMacAddress());

        registerBody.setFirstLaunch(true);

        registerBody.setSecurityPatchInfo(getterSetter.getSecurityPatchInfo());

        return registerBody;

    }

}
