package com.alex9xu.hello.net;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.alex9xu.hello.config.AppConfigInterface;

import java.io.IOException;

/**
 * Created by Alex9Xu@hotmail.com on 2016/7/13
 */
public class RetrofitBase {
    private static Retrofit mRetrofit;

    public static Retrofit retrofit() {
        if (mRetrofit == null) {
            OkHttpClient client;

            // Notice: The only differ of debug is: HttpLoggingInterceptor
            // Common Params: "version" and "server_call_version" will in every request
            if(!AppConfigInterface.isDebug) {
                client = new OkHttpClient.Builder()
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();
                                HttpUrl originalHttpUrl = original.url();
                                HttpUrl url = originalHttpUrl.newBuilder()
                                        .addQueryParameter("version", AppConfigInterface.VERSION)
                                        .addQueryParameter("server_call_version",
                                                AppConfigInterface.SERVER_CALL_VERSION)
                                        .build();
                                // Request customization: add request headers
                                Request.Builder requestBuilder = original.newBuilder()
                                        .url(url);
                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                        .build();
            } else {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                client = new OkHttpClient.Builder()
                        .addInterceptor(logging)
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();
                                HttpUrl originalHttpUrl = original.url();
                                HttpUrl url = originalHttpUrl.newBuilder()
                                        .addQueryParameter("version", AppConfigInterface.VERSION)
                                        .addQueryParameter("server_call_version",
                                                AppConfigInterface.SERVER_CALL_VERSION)
                                        .build();
                                // Request customization: add request headers
                                Request.Builder requestBuilder = original.newBuilder()
                                        .url(url);
                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                        .build();
            }

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(AppConfigInterface.BASE_COM_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return mRetrofit;
    }

}

