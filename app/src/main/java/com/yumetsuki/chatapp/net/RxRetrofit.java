package com.yumetsuki.chatapp.net;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.yumetsuki.chatapp.App;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RxRetrofit {

    private static RxRetrofit rxRetrofit = new RxRetrofit();

    private Retrofit retrofit = buildRetrofit();

    private RxRetrofit() {}

    public static RxRetrofit getInstance() {
        return rxRetrofit;
    }

    public <T> T create(Class<T> tClass) {
        return retrofit.create(tClass);
    }

    private Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://39.106.91.186:8888/api/")
                .client(buildClient())
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder()
                                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                .create()
                        )
                ).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
    }

    private OkHttpClient buildClient() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    if (!request.url().encodedPath().contains("login")) {
                        String cookie =  App.AppContext
                                .getSharedPreferences("default", Context.MODE_PRIVATE)
                                .getString("cookie", "");
                        request = request.newBuilder()
                                .addHeader("Cookie", cookie)
                                .build();
                    }
                    return chain.proceed(request);
                })
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .callTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
    }

}
