package com.spot.gaid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    MyAsyncTask asyncTask = new MyAsyncTask();
    String GAID = null;

    @SuppressLint("CheckResult")
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

//        getAdvertId()
//                // Исполнение кода getAdvertId происходит в потоке I/O(Ввода/вывода)
//                .subscribeOn(Schedulers.io())
//                // Исполнение onSuccess/onError происходит в главном(UI) потоке
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<String>() {
//                    @Override
//                    public void onSuccess(String advertId) {
//                        // Здесь - код, исполняемый в случае успеха
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        // Здесь - в случаяе ошибки
//                    }
//                });
    }

//    // Функция на RXJava
//    private Single<String> getAdvertId() {
//        return Single.create(new SingleOnSubscribe<String>() {
//            @Override
//            public void subscribe(SingleEmitter<String> subscriber) throws Exception {
//                AdvertisingIdClient.Info idInfo = null;
//                try {
//                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
//                    subscriber.onSuccess(idInfo.getId());
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    subscriber.onError(e);
//                } catch (GooglePlayServicesRepairableException e) {
//                    subscriber.onError(e);
//                } catch (Exception e) {
//                    subscriber.onError(e);
//                }
//            }
//        });
//    }

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

