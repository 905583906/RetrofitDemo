package com.alex9xu.hello.net.apis;

import android.support.v4.util.ArrayMap;

import com.alex9xu.hello.config.AppConfigInterface;
import com.alex9xu.hello.model.WeatherResult;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Alex on 2016/7/29
 */
public interface CityWeatherPostApi {
    @FormUrlEncoded
    @POST(AppConfigInterface.GET_WEATHER)
    Call<WeatherResult> getClassify(@FieldMap ArrayMap<String,String> paramMap);
}
