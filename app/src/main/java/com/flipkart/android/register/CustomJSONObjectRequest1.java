package com.flipkart.android.register;

/**
 * Created by VectoR on 14-09-2017.
 */

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
import java.util.HashMap;
import java.util.Map;

public class CustomJSONObjectRequest1 extends Request {

    private Listener listener;
    Priority mPriority;
    HashMap<String, String> headers = new HashMap<>();
    HashMap<String, String> params = new HashMap<>();

    public CustomJSONObjectRequest1(int method, String url, HashMap<String, String> getHeaders,
                                    HashMap<String, String> params,
                                    Listener responseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.params = params;
        this.headers = getHeaders;
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
    protected Map<String,String> getParams(){
        return params;
    }
}