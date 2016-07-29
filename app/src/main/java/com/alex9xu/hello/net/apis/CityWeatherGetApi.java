package com.alex9xu.hello.net.apis;

import android.support.v4.util.ArrayMap;
import com.alex9xu.hello.config.AppConfigInterface;
import com.alex9xu.hello.model.WeatherResult;

import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Alex9Xu@hotmail.com on 2016/7/14
 */

public interface CityWeatherGetApi {
    @GET(AppConfigInterface.GET_WEATHER)
    Call<WeatherResult> getClassify(@QueryMap ArrayMap<String,String> paramMap);
}