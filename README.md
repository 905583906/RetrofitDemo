# RetrofitDemo
Demo how to build a network architecture using Retrofit. This Project is all by myself.

Tips:

1. This “okhttp3:logging-interceptor” is used to show logs of internet conversation. It’s very useful while debug. The early retrofit2 versions can’t show logs and complained by some programmers. Some experts use these various way to show logs, now, this is the original way.

2. About Base Tools (I’m opening for Android work opportunities all over the world, if you’re interesting in me, just contact alex9xu@hotmail.com):
(1) Use addInterceptor to show logs and add “public params”, it means that those params will in every request(both get and post request).
(2) It’s no different in “Debug” mode and “Release” mode, except “HttpLoggingInterceptor ” to show logs.
(3) “BASE_COM_URL” means such as “http://test.hello.com/”

3. This api use “Get” to submit params (you can simply change to post by replace “@POST” to “@GET”). All the params stored in Map, you can add more params. Notice, I use ArrayMap which is Android-specific, and ArrayMap requires less memory compare to HashMap.

4. The request is http://www.weather.com.cn/adat/sk/1...  and these are four params (two public params). Ok, done, it will work.