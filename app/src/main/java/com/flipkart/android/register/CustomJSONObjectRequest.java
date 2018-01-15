package com.flipkart.android.register;

/**
 * Created by VectoR on 13-09-2017.
 */

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class CustomJSONObjectRequest extends JsonObjectRequest {

    HashMap<String, String> headers = new HashMap<String, String>();
    String params;
    Boolean bool;

    public CustomJSONObjectRequest(int method, String url, JSONObject jsonRequest,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }
    public CustomJSONObjectRequest(int method, String url, JSONObject jsonRequest, HashMap<String,String> getHeaders,
                                   String getParams,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener,Boolean isTrue) {
        super(method, url, jsonRequest, listener, errorListener);
        headers = getHeaders;
        params = getParams;
        bool = isTrue;

    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }


    @Override
    public byte[] getBody() {
        String httpPostBody;
        if (bool){
            try {
                //httpPostBody=httpPostBody+"&randomFieldFilledWithAwkwardCharacters="+ URLEncoder.encode("{{%stuffToBe Escaped/","UTF-8");
                httpPostBody=URLEncoder.encode(params,"UTF-8");

            } catch (UnsupportedEncodingException exception) {
                Log.e("ERROR", "exception", exception);
                // return null and don't pass any POST string if you encounter encoding error
                return null;
            }
            return httpPostBody.getBytes();
        }
        // usually you'd have a field with some values you'd want to escape, you need to do it yourself if overriding getBody. here's how you do it
        return params.getBytes();
    }
    @Override
    public RetryPolicy getRetryPolicy() {
        // here you can write a custom retry policy
        return super.getRetryPolicy();
    }

}