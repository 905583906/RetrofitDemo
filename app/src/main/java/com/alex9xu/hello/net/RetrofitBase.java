package com.alex9xu.hello.net;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.alex9xu.hello.config.AppConfigInterface;
import com.alex9xu.hello.utils.LogHelper;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by Alex9Xu@hotmail.com on 2016/7/13
 */
public class RetrofitBase {
    private static final String TAG = "RetrofitBase";

    private static Retrofit mRetrofit;
//    private static Dialog mLoadingDialog;
    private static WeakReference<Context> mContextRef;

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
                                        .addQueryParameter("call_version", AppConfigInterface.TO_SERVER_VERSION)
                                        .addQueryParameter("deviceType", AppConfigInterface.DEVICE_TYPE)
                                        .build();
                                // Request customization: add request headers
                                Request.Builder requestBuilder = original.newBuilder()
                                        .addHeader("user-agent", "android")
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
                                        .addQueryParameter("version", AppConfigInterface.TO_SERVER_VERSION)
                                        .addQueryParameter("deviceType", AppConfigInterface.DEVICE_TYPE)
                                        .build();
                                // Request customization: add request headers
                                Request.Builder requestBuilder = original.newBuilder()
                                        .addHeader("user-agent", "android")
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

    // Encapsulation Request and Response
    public static <T> void AddToEnqueue(Call<T> baseCall, Context context, boolean isShowDlg,
                                        final NetRequestListener listener) {
        mContextRef = new WeakReference<>(context);
//        if(isShowDlg && null == mLoadingDialog && null != mContextRef.get()) {
//            mLoadingDialog = DialogUtil.showLoginDialog(mContextRef.get());
//            mLoadingDialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
//        }
//        if(isShowDlg) {
//            mLoadingDialog.show();
//        }

        baseCall.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, retrofit2.Response<T> response) {
                LogHelper.d(TAG, "toEnqueue, onResponse:");
                if (null != response.body()) {
                    if(response.code() == 200) {
                        LogHelper.d(TAG, "toEnqueue, onResponse Suc");
//                        if(null != mLoadingDialog) {
//                            mLoadingDialog.dismiss();
//                        }
                        listener.onRequestSuc(response.code(), response);
                    } else {
                        LogHelper.d(TAG, "toEnqueue, onResponse Fail:" + response.code());
//                        if(null != mLoadingDialog) {
//                            mLoadingDialog.dismiss();
//                        }
                        listener.onRequestFail(response.code(), response.message());
                    }
                } else {
                    LogHelper.d(TAG, "toEnqueue, onResponse Fail");
//                    if(null != mLoadingDialog) {
//                        mLoadingDialog.dismiss();
//                    }
                    listener.onRequestFail(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                LogHelper.d(TAG, "toEnqueue, onFailure Fail");
//                if(null != mLoadingDialog) {
//                    mLoadingDialog.dismiss();
//                }
                listener.onRequestFail(AppConfigInterface.RESULT_FAIL_UNKNOW, null);
            }
        });
    }

    public static void stopLoadingDlg(Context context) {
//        if(null != mContextRef && context == mContextRef.get() && null != mLoadingDialog) {
//            mLoadingDialog.dismiss();
//            mLoadingDialog = null;
//        }
    }

}
