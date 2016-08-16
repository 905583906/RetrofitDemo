package com.alex9xu.hello.net;

import android.app.Activity;
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

import com.alex9xu.hello.R;
import com.alex9xu.hello.base.DemoApp;
import com.alex9xu.hello.config.AppConfigInterface;
import com.alex9xu.hello.utils.LogHelper;
import com.alex9xu.hello.utils.NetConnectUtil;
import com.alex9xu.hello.utils.ToastUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

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
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    HttpUrl httpUrl = original.url().newBuilder()
                            .addQueryParameter("call_version", AppConfigInterface.TO_SERVER_VERSION)
                            .addQueryParameter("deviceType", AppConfigInterface.DEVICE_TYPE)
                            .build();
                    // Request customization: add request headers
                    Request request = original.newBuilder()
                            .addHeader("user-agent", "android")
                            .addHeader("Accept", "application/x-www-form-urlencoded")
                            .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                            .url(httpUrl)
                            .build();

                    return chain.proceed(request);
                }
            }).connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false);

            // Notice: The only differ of debug is: HttpLoggingInterceptor
            if (AppConfigInterface.isDebug) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(logging);
            }

            OkHttpClient client = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(AppConfigInterface.BASE_COM_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

        }

        return mRetrofit;
    }

    // Encapsulation Request and Response
    public static <T> void AddToEnqueue(final Call<T> baseCall, final Context context, final boolean isShowDlg,
                                        final NetRequestListener listener) {
        mContextRef = new WeakReference<>(context);

        //if network has error, show it
        if (! NetConnectUtil.isNetworkConnected(DemoApp.getAppContext())) {
            ToastUtils.toastShort(R.string.network_not_connect);
            return;
        }

//        if(isShowDlg && null == mLoadingDialog) {
//            mContextRef = new WeakReference<>(context);
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
                        dismissDlg();
                        listener.onRequestSuc(response.code(), response);
                    } else {
                        LogHelper.d(TAG, "toEnqueue, onResponse Fail:" + response.code());
                        dismissDlg();
                        listener.onRequestFail(response.code(), response.message());
                    }
                } else {
                    LogHelper.d(TAG, "toEnqueue, onResponse Fail");
                    dismissDlg();
                    listener.onRequestFail(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                LogHelper.d(TAG, "toEnqueue, onFailure Fail");
                dismissDlg();
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

    private static void dismissDlg() {
//        if(null != mLoadingDialog && null != mContextRef && null != mContextRef.get()
//                && mContextRef.get() instanceof Activity
//                && ! ((Activity) mContextRef.get()).isFinishing()) {
//            mLoadingDialog.dismiss();
//            mLoadingDialog = null;
//        }
    }

}
