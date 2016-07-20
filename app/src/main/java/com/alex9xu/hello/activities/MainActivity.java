package com.alex9xu.hello.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.alex9xu.hello.R;
import com.alex9xu.hello.base.BaseActivity;
import com.alex9xu.hello.model.Entity.Weatherinfo;
import com.alex9xu.hello.model.WeatherResult;
import com.alex9xu.hello.net.NetRequestListener;
import com.alex9xu.hello.net.RetrofitBase;
import com.alex9xu.hello.net.apis.CityWeatherApi;
import com.alex9xu.hello.utils.LogHelper;

/**
 * Created by Alex9Xu@hotmail.com on 2016/7/14
 */

public class MainActivity extends BaseActivity {
    // Note: make all the Activities extends BaseActivity to manage
    private static final String TAG = "MainActivity";

    private TextView mTvwShowInfo;
    private Call<WeatherResult> mWeatherCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Alex9Xu@hotmail.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mTvwShowInfo = (TextView) findViewById(R.id.main_tvw_show_info);

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        // Notice: ArrayMap requires less memory in Android compare with HashMap (about 10%)
        CityWeatherApi classifyApi = RetrofitBase.retrofit().create(CityWeatherApi.class);
        ArrayMap<String,String> paramMap = new ArrayMap<>();
        paramMap.put("sortType", "1");
        paramMap.put("uid", "654321");
        mWeatherCall = classifyApi.getClassify(paramMap);

        RetrofitBase.AddToEnqueue(mWeatherCall, MainActivity.this, true, new NetRequestListener() {
            @Override
            public void onRequestSuc(int code, Response response) {
                LogHelper.d(TAG, "onRequestSuc");
                Response<WeatherResult> resultResponse = response;
                if(null != resultResponse.body().getWeatherinfo()) {
                    Weatherinfo info  = resultResponse.body().getWeatherinfo();
                    StringBuilder strBld = new StringBuilder();
                    strBld.append(info.getCity());
                    strBld.append(getString(R.string.temperature));
                    strBld.append(getString(R.string.colon));
                    strBld.append(info.getTemp());
                    mTvwShowInfo.setText(strBld.toString());
                }
            }

            @Override
            public void onRequestFail(int code, String reason) {
                LogHelper.d(TAG, "onRequestFail: " + code + ", " + reason);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Notice: If the web operate is to update UI, you can cancel it when onStop
        mWeatherCall.cancel();
    }

}
