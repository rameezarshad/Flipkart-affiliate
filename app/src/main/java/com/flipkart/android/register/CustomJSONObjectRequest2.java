package com.flipkart.android.register;

/**
 * Created by VectoR on 14-09-2017.
 */

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class CustomJSONObjectRequest2 extends Request {

    private Listener listener;
    Priority mPriority;
    HashMap<String, String> headers = new HashMap<String, String>();
    String params;
    Boolean bool;
    public CustomJSONObjectRequest2(int method, String url,HashMap<String,String>getHeaders,
                                    String getParams,Boolean isTrue,
                                   Listener responseListener, ErrorListener errorListener ) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.params = getParams;
        this.headers = getHeaders;
        this.bool = isTrue;
    }

     @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            JSONObject jsonResponse = new JSONObject(jsonString);
            jsonResponse.put("headers", new JSONObject(response.headers));
            return Response.success(jsonResponse,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(Object response) {
        listener.onResponse(response);
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        return headers;
    }

    @Override
    public byte[] getBody() {
        String httpPostBody;
        if (bool){
            try {
                httpPostBody= URLEncoder.encode(this.params,"UTF-8");

            } catch (UnsupportedEncodingException exception) {
                Log.e("ERROR", "exception", exception);
                return null;
            }
            return httpPostBody.getBytes();
        }
        return this.params.getBytes();
    }

}