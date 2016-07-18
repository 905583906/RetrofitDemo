package com.alex9xu.hello.config;

/**
 * Created by Alex9Xu@hotmail.com on 2016/7/18
 */
public interface AppConfigInterface {

    /*====================Test Environment================= */
    String BASE_COM_URL = "http://www.weather.com.cn/";
    boolean isDebug = true;

    /*====================Real Environment==================== */
//    String BASE_COM_URL = "http://www.weather.com.cn/";
//    boolean isDebug = false;

    String VERSION = "1.0";
    String SERVER_CALL_VERSION = "1002";

    /**
     * 获取分类接口
     */
    String GET_WEATHER = BASE_COM_URL + "/adat/sk/101010100.html";

}
