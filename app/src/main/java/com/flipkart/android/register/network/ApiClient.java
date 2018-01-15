package com.flipkart.android.register.network;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by VectoR on 14-11-2017.
 */

public class ApiClient {

    public static String baseUrl = "http://mobileapi.flipkart.net/";

    /*private static Retrofit retrofit;*/

    /*private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl);

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    // No need to instantiate this class.
    private ApiClient() {
    }


    public static void changeApiBaseUrl(String newApiBaseUrl) {
        baseUrl = newApiBaseUrl;

        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl);
    }
*/

  /*  public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(


                new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .build();

                return chain.proceed(request);
            }
        }).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();



        return retrofit;
    }*/


    public static void changeApiBaseUrl(String newApiBaseUrl) {
      baseUrl = newApiBaseUrl;
    }

    public static Retrofit.Builder getBuilder(){
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create());
    }

    public static OkHttpClient.Builder addClient(){
        return new OkHttpClient.Builder();
    }

    public static HttpLoggingInterceptor addLogginInterceptor(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    public static Interceptor addInterceptor(final Map<String, String> headers){

        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();

                for (Map.Entry<String, String> pairs : headers.entrySet()) {
                    if (pairs.getValue() != null) {
                        requestBuilder.header(pairs.getKey(), pairs.getValue());
                    }
                }
                requestBuilder.method(original.method(), original.body());

                Request request = requestBuilder
                        .build();

                return chain.proceed(request);
            }
        };

    }

}
