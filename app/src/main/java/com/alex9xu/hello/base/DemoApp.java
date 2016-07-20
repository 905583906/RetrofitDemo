package com.alex9xu.hello.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.alex9xu.hello.activities.MainActivity;
import com.alex9xu.hello.utils.LogHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex9Xu@hotmail.com on 2016/7/20
 */
public class DemoApp extends Application {
    private static final String TAG = "BaseApp";
    private static WeakReference<DemoApp> mInstanceRef;

    private static boolean mIsCanLoadPic;
    // All Opened Activities
    private List<Activity> mOpenedActivities = new ArrayList<>();

    public static Context mAppContext;
    private WeakReference<Activity> mCurActivityRef;
    public static int mScreenWidth = 0;
    public static int mScreenHeight = 0;

    /**
     * Get Application
     */
    public static DemoApp getInstance() {
        if (mInstanceRef.get() == null) {
            LogHelper.e(TAG, "BaseApp instance null");
            throw new IllegalStateException();
        }
        return mInstanceRef.get();
    }

    /**
     * Get Application Context
     */
    public static Context getAppContext() {
        return getInstance().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogHelper.d(TAG, "onCreate start");
        mAppContext = getApplicationContext();
        mInstanceRef = new WeakReference<>(this);
        new Thread(){
            @Override
            public void run(){
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                initScreenSize();
            }
        }.start();
        LogHelper.d(TAG, "onCreate end");
    }

    public void initScreenSize() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
        LogHelper.d(TAG, "mScreenWidth = " + mScreenWidth + ", mScreenHeight = " + mScreenHeight);
    }

    /**
     * getScreenWidth: px
     * @return
     */
    public int getScreenWidth() {
        return mScreenWidth;
    }

    /**
     * getScreenHeight: px
     * @return
     */
    public int getScreenHeight() {
        return mScreenHeight;
    }

    /**
     * Add Opened Activity to list
     */
    public void addActivity(Activity activity) {
        mOpenedActivities.add(activity);
    }

    /**
     * remove opened activity
     *
     * @param activity current activity
     */
    public void removeActivity(Activity activity) {
        mOpenedActivities.remove(activity);
    }

    /**
     * size of current activities
     */
    public int getCurrentActivitySize() {
        return mOpenedActivities.size();
    }

    /**
     * leave only MainActivity, to get more resource for this app
     */
    public void cleanActivity() {
        int size = mOpenedActivities.size();
        for (int i = 0; i < size; i++) {
            Activity activity = mOpenedActivities.remove(0);
            if (null == activity) {
                continue;
            }

            if (activity instanceof MainActivity) {
                mOpenedActivities.add(activity);
                continue;
            }
            activity.finish();
        }
    }

    /**
     * clean all the activities to release resources
     */
    public void clearAllActivity() {
        int size = mOpenedActivities.size();
        for (int i = 0; i < size; i++) {
            Activity activity = mOpenedActivities.remove(0);
            if (null == activity) {
                continue;
            }

            activity.finish();
        }
    }

    public void setCurActivity(Activity baseActivity) {
        mCurActivityRef = new WeakReference<>(baseActivity);
    }
    /**
     * return: Notice: may be null
     */
    public Activity getCurActivity() {
        return mCurActivityRef.get();
    }

    /**
     * Check if contain this Activity
     */
    public boolean containsAct(Class clazz) {
        int size = mOpenedActivities.size();
        for (int i = 0; i < size; i++) {
            Activity act = mOpenedActivities.get(i);
            if (act.getClass().getSimpleName().equals(clazz.getSimpleName())) {
                return true;
            }
        }
        return false;
    }

}

