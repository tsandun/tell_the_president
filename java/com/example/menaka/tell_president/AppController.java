package com.example.menaka.tell_president;

/**
 * Created by Menaka on 3/25/2017.
 */
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController  {



    //public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestrQueue;
    private static AppController mInstance;
    private static Context mCtx;

    private AppController(Context context){
            mCtx=context;
        mRequestrQueue=getmRequestrQueue();

    }

public RequestQueue getmRequestrQueue(){

    if (mRequestrQueue==null){
        mRequestrQueue=Volley.newRequestQueue(mCtx.getApplicationContext());
    }
    return mRequestrQueue;
}


    public static synchronized AppController getInstance(Context context) {
        if(mInstance==null){
            mInstance=new AppController(context);

        }
        return mInstance;
    }

    public <T>void addTorequestque(Request<T> request){
            mRequestrQueue.add(request);
    }

}
