package com.flipkart.android.register.network;

import com.flipkart.android.register.pojo.appInstall.InstallResponse;
import com.flipkart.android.register.pojo.fetch.body.FetchBody;
import com.flipkart.android.register.pojo.fetch.response.FetchResponse;
import com.flipkart.android.register.pojo.mat.MatResponse;
import com.flipkart.android.register.pojo.registerApp.body.RegisterBody;
import com.flipkart.android.register.pojo.registerApp.response.RegisterAppResponse;
import com.flipkart.android.register.pojo.registerReferral.RegisterReferralResponse;
import com.flipkart.android.register.pojo.tune.TuneResponse;
import com.flipkart.android.register.pojo.tune.body.TuneBody;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by VectoR on 13-11-2017.
 */

public interface APIInterface {

    @GET
    Call<MatResponse> getMatId(@Url String url, @QueryMap Map<String, String> options);

    @POST("4/page/fetch")
    Call<FetchResponse> fetchPageData(@Body FetchBody body, @Header("X-Layout-Version") String str, @HeaderMap Map<String, String> headers);

    @POST("3/register/app")
    Call<RegisterAppResponse> register(@Header("checksum") String str, @Body RegisterBody body, @HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @POST("2/register/referral")
    Call<RegisterReferralResponse> registerReferrer(@HeaderMap Map<String, String> headers, @Field("value") String str, @Field("deviceId") String str2, @Field("rawDeviceId") String str3, @Field("channel") String str4, @Field("firstLaunch") long j, @Field("installId") String str5);

    @POST
    Call<TuneResponse> tune(@Url String url, @QueryMap Map<String, String> options, @Body TuneBody body);

    @GET("affiliate/v1/appInstall/json")
    Call<InstallResponse> getInstallReport(@QueryMap Map<String, String> options);
}
