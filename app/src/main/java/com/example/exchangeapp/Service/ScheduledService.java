package com.example.exchangeapp.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.exchangeapp.Bean.Rate;
import com.example.exchangeapp.Database.RealmHelper;
import com.example.exchangeapp.MainActivity;
import com.example.exchangeapp.Preferences.SharedPreferences;
import com.example.exchangeapp.R;
import com.example.exchangeapp.Utils.Utils;
import com.example.exchangeapp.Volley.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduledService extends Service {
    public Runnable mRunnable = null;
    public ScheduledService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Handler mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                RealmHelper realmHelper = new RealmHelper();
                Rate rate = realmHelper.read("USD").get(0);
                List<Rate> allRates = realmHelper.readAll();
                if(Utils.hasInternet(getApplicationContext()))
                    sendRequest();
                mHandler.postDelayed(mRunnable, 7200 * 1000);
            }
        };
        mHandler.postDelayed(mRunnable,  7200 * 1000);

        return super.onStartCommand(intent, flags, startId);
    }

    private void sendRequest(){
        SharedPreferences sharedPreferences = new SharedPreferences(getApplicationContext());
        final RealmHelper realmHelper = new RealmHelper();

        sharedPreferences.getBaseCountry();

        final String url = "https://api.exchangeratesapi.io/latest?base=USD";

        // prepare the Request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            Gson gson = new Gson();
                            JSONObject jsonObject = response.getJSONObject("rates");
                            Map<String, Object> ratesMap = new Gson().fromJson(jsonObject.toString(), new TypeToken<HashMap<String, Object>>() {}.getType());
                            for (Map.Entry<String, Object> entry : ratesMap.entrySet()) {
                                Rate rate = realmHelper.readOne(entry.getKey());
                                if(rate == null){
                                    rate = new Rate();
                                    rate.setValue((double)entry.getValue());
                                    rate.setCountry(entry.getKey());
                                    realmHelper.save(rate);
                                }else{
                                    realmHelper.updateValue(entry.getKey(),(double)entry.getValue());
                                }
                            }
                            sendNotification();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestqueue(request);
    }

    public void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(this.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.coin_black)
                .setContentTitle("New Values")
                .setContentText("Updated values aviable!!");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this,MainActivity.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }
}

