package com.spot.gaid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;


public class MainActivity extends AppCompatActivity {

    MyAsyncTask asyncTask = new MyAsyncTask();
    String GAID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;
        asyncTask.delegate = new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                //Here you will receive the result fired from async class
                //of onPostExecute(result) method.

                GAID = output;

                TextView gaid = findViewById(R.id.gaidNum);
                String g = "GAID = "+ GAID;
                gaid.setText(g);

                TextView appid = findViewById(R.id.appsNum);
                String appsFlyerId = AppsFlyerLib.getInstance().getAppsFlyerUID(context);
                String a = "AppID = "+appsFlyerId;
                appid.setText(a);


                TextView w = findViewById(R.id.web);
                String URL = "WebUrl = "+"https://link&apid="+appsFlyerId +"&gaid="+GAID;
                w.setText(URL);

                // Задача в том чтобы перенести код со строчки 38-50 в метод onCreate
                // Если это возможно
            }
        };
        asyncTask.execute();
    }

    public class MyAsyncTask extends  AsyncTask<Void, Void, String> {
        public AsyncResponse delegate = null;
        @Override
        protected String doInBackground(Void... params) {
            AdvertisingIdClient.Info idInfo = null;
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String advertId = null;
            try {
                advertId = idInfo.getId();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return advertId;
        }


        @Override
        protected void onPostExecute(String advertId) {
            delegate.processFinish(advertId);
            //Toast.makeText(getApplicationContext(), advertId, Toast.LENGTH_SHORT).show();
        }
    };

}

