package com.example.exchangeapp.Volley;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Mauricio Robles on 19/02/19.
 */


public class VolleySingleton {

    private static VolleySingleton mInstance;
    private static Context mContext;
    private RequestQueue requestQueue;

    private VolleySingleton(Context context) {
        mContext = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestqueue(Request<T> request) {
        requestQueue.add(request);
    }
}
