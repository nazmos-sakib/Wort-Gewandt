package com.wortgewandt.ExternalRequest;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wortgewandt.Interface.Callback;


public class VolleyNetworkRequest {
    private static VolleyNetworkRequest instance;
    private final RequestQueue queue;
    private VolleyNetworkRequest(Context context){
        // Instantiate the RequestQueue.
         queue = Volley.newRequestQueue(context);
    }

    public synchronized static VolleyNetworkRequest getInstance(){
        if (instance==null){
            throw new NullPointerException();
        }
        return instance;
    }

    public synchronized static VolleyNetworkRequest getInstance(Context context){
        if (instance == null){
            instance = new VolleyNetworkRequest(context);
        }
        return instance;
    }

    public  void makeWebRequest(String url, Callback<String> callback){

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //textView.setText("Response is: " + response.substring(0,500));
                        callback.onCallback(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //textView.setText("That didn't work!");
                        callback.onCallback(null);
                    }
        });

        // Set the tag on the request.
        stringRequest.setTag("MainActivity");

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public RequestQueue getQueue(){
        return queue;
    }


}
