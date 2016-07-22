package com.alex9xu.hello.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alex9xu.hello.R;
import com.alex9xu.hello.net.NetRequestListener;
import com.alex9xu.hello.net.RetrofitBase;
import com.alex9xu.hello.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Alex9Xu@hotmail.com on 2016/7/20
 */
public class BaseActivity<T> extends AppCompatActivity {
    private String TAG = "BaseActivity";

    private List<Call<T>> mCallListUi = new ArrayList<>();
    private List<Call<T>> mCallListNonUi = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogHelper.d(TAG, "onCreate: " + this.toString());
        setContentView(R.layout.activity_base);
        // Manage Activity
        DemoApp.getInstance().addActivity(this);
    }

    protected void addToUiCallEnqueue(Call<T> call, Context context, boolean isNeedDlg, NetRequestListener listener) {
        mCallListUi.add(call);
        RetrofitBase.AddToEnqueue(call, context, isNeedDlg, listener);
    }

    protected void addToNonUiCallEnqueue(Call<T> call, Context context, boolean isNeedDlg, NetRequestListener listener) {
        mCallListNonUi.add(call);
        RetrofitBase.AddToEnqueue(call, context, isNeedDlg, listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogHelper.i(TAG, "onResume", this.toString());
        DemoApp.getInstance().setCurActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogHelper.i(TAG, "onStop", this.toString());
        for(Call<T> call : mCallListUi) {
            if(null != call && ! call.isExecuted()) {
                call.cancel();
            }
        }
        RetrofitBase.stopLoadingDlg(this);
    }

    @Override
    protected void onDestroy() {
        LogHelper.d(TAG, "onDestroy", this.toString());
        for(Call<T> call : mCallListNonUi) {
            if(null != call && ! call.isExecuted()) {
                call.cancel();
            }
        }
        DemoApp.getInstance().removeActivity(this);
        super.onDestroy();
    }

}
