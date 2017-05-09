package com.easyappointments.remote.ea.service;

import android.support.compat.BuildConfig;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by matteo on 24/04/17.
 */

class LoggingInterceptor implements Interceptor {
    private final static String TAG = "CALL";

    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.d(TAG, String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Log.d(TAG, String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }
}

abstract class BaseServiceFactory {
    private final static String API_URL="/index.php/api/v1/";

    static <T> T getClient(Class<T> cls, String username, String psw, String url) {

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(5, TimeUnit.SECONDS);

        if(BuildConfig.DEBUG){
            builder.addInterceptor(new LoggingInterceptor());
        }

        String credentials = username + ":" + psw;
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        builder.addInterceptor(new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Authorization", basic)
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = builder.build();

        Retrofit retrofit =
                new Retrofit.Builder().baseUrl(url+API_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build();

        return retrofit.create(cls);
    }
}
