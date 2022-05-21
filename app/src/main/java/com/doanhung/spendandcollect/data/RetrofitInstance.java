package com.doanhung.spendandcollect.data;

import com.doanhung.spendandcollect.data.api.AuthApi;
import com.doanhung.spendandcollect.data.api.MainApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    public static final String BASE_URL = "http://192.168.0.103:3000";

    private static final HttpLoggingInterceptor loggingInterceptor =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static final OkHttpClient.Builder okHttpClient =
            new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor);


    private static final Retrofit retrofit =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(okHttpClient.build())
                    .build();

    public static AuthApi authApi = retrofit.create(AuthApi.class);
    public static MainApi mainApi = retrofit.create(MainApi.class);

}
