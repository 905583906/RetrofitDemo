package com.alex9xu.hello.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alex9xu.hello.R;
import com.alex9xu.hello.net.RetrofitBase;
import com.alex9xu.hello.utils.LogHelper;

/**
 * Created by Alex9Xu@hotmail.com on 2016/7/20
 */
public class BaseActivity extends AppCompatActivity {
    private String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogHelper.d(TAG, "onCreate: " + this.toString());
        setContentView(R.layout.activity_base);
        // Manage Activity
        DemoApp.getInstance().addActivity(this);
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
        RetrofitBase.stopLoadingDlg(this);
    }

    @Override
    protected void onDestroy() {
        LogHelper.d(TAG, "onDestroy", this.toString());
        super.onDestroy();
        DemoApp.getInstance().removeActivity(this);
    }
}
