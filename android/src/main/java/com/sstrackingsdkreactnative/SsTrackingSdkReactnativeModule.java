package com.sstrackingsdkreactnative;

import static android.content.Context.POWER_SERVICE;
import static android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.sovereign.trackingsdk.LocationSendCallBack;
import com.sovereign.trackingsdk.Logger.Logger;
import com.sovereign.trackingsdk.Logger.LoggerData;
import com.sovereign.trackingsdk.Tracking;
import com.sovereign.trackingsdk.TrackingService;

import java.util.List;
import java.util.Map;

@ReactModule(name = SsTrackingSdkReactnativeModule.NAME)
public class SsTrackingSdkReactnativeModule extends ReactContextBaseJavaModule {
    private Tracking mTrackingSDK;
//  private String token;
//  private String apiUrl;
//  private String title;
//  private String content;

    public static final String NAME = "SsTrackingSdkReactnative";

    public SsTrackingSdkReactnativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }


    // Example method
    // See https://reactnative.dev/docs/native-modules-android
//    @ReactMethod
//    public void multiply(int a, int b, Promise promise) {
//        promise.resolve(a * b);
//    }
//
//    public static native int nativeMultiply(int a, int b);

    @ReactMethod
    public boolean setCurrentActivityAndTracking() {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity != null) {
            mTrackingSDK = Tracking.newInstance(currentActivity);
            return true;
        }
        return false;
    }

    private long oldExpired = 0;
    private String oldUrl = "";
    private String oldRefreshToken = "";

    @ReactMethod
    public void init() {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            return;
        }
        Tracking.Builder builder = new Tracking.Builder(currentActivity);
        builder.setDialogRequestBackgroundLocationTitle("access background location dialog title")
            .setDialogRequestBackgroundLocationMessage("access background location dialog message")
            .setNotificationActionCloseLabel("");
        oldExpired = builder.getOldTokenExpired();
        oldUrl = builder.getOldAuthenUrl();
        oldRefreshToken = builder.getOldRefreshToken();
        mTrackingSDK = builder.build();
    }

    @ReactMethod
    public void startTracking() {
        if (BuildConfig.DEBUG) {
            Log.d("mTrackingSDK", "start tracking");
        }
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }

        mTrackingSDK.setCallback(new LocationSendCallBack() {
                @Override
                public void onSendLocationResult(String resultString) {
                    // Log.d("TEST", "Main " + resultString);
                    getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("Location-Event", resultString);
                }

                @Override
                public void onFailed(String errorString) {

                }

                @Override
                public void trackingStarted() {
                    Map<String, ?> map = mTrackingSDK.getConfig();
                    for (Map.Entry<String, ?> entry : map.entrySet()) {
                        if (BuildConfig.DEBUG) {
                            Log.d("config", entry.getKey() + ":" + entry.getValue().toString());
                        }
                    }
                }

                @Override
                public void trackingStopped() {

                }
            })
            .startTracking();
    }

    @ReactMethod
    public void setToken(String newToken) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.setAuthen(newToken);
    }

    @ReactMethod
    public void setApiUrl(String newApiUrl) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.setApiUrl(newApiUrl);
    }

    @ReactMethod
    public void setTitleAndContentTracking(String newTitle, String newContent) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.setNotificationContentTitle(newTitle);
        mTrackingSDK.setNotificationContentText(newContent);
    }

    @ReactMethod
    public void setLocationUpdateFrequency(int miliseconds) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.setLocationUpdateFrequency(miliseconds);
    }

    @ReactMethod
    public void setTrackingInterval(int miliseconds) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.setTrackingInterval(miliseconds);
    }

    @ReactMethod
    public void isTrackingSdk(Promise promise) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        promise.resolve(mTrackingSDK.isTracking());
    }

    @ReactMethod
    public void stopTracking() {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                ReactApplicationContext context = getReactApplicationContext();
                if (context != null) {
                    context.stopService(new Intent(context, TrackingService.class));
                    context.getSharedPreferences("pref_tracking", Context.MODE_PRIVATE).edit().putBoolean(TrackingService.TRACKING_STATS, false).apply();
                }
                return;
            }
        }
        if (mTrackingSDK != null) {
            mTrackingSDK.stopTracking();
        }
    }

    @ReactMethod
    public void removeTracking() {
        if (mTrackingSDK != null) {
            mTrackingSDK.removeTracking();
        }
    }

    public static final int LOG_TYPE_INFO = 1;
    public static final int LOG_TYPE_ERROR = 2;
    public static final int LOG_TYPE_API = 3;
    public static final int LOG_TYPE_SYSTEM = 4;

    @ReactMethod
    public void logTracking(int logType) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.log("Tracking", "tracking stopped", logType);
    }

    @ReactMethod
    public void getLogByData(Promise promise) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.getLogData(new Logger.OnCompleteListener<List<LoggerData>>() {
            @Override
            public void onSuccess(List<LoggerData> data) {
                promise.resolve(data.toString());
            }
        });
    }

    @ReactMethod
    public void getLogDataByType(int type, Promise promise) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.getLogDataByType(type, new Logger.OnCompleteListener<List<LoggerData>>() {
            @Override
            public void onSuccess(List<LoggerData> data) {
                promise.resolve(data.toString());
            }
        });
    }

    @ReactMethod
    public void getLogDataByFunctionName(String functionName, Promise promise) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.getLogDataByFunctionName(functionName, new Logger.OnCompleteListener<List<LoggerData>>() {
            @Override
            public void onSuccess(List<LoggerData> data) {
                promise.resolve(data.toString());
            }
        });
    }

    @ReactMethod
    public void setLogLimitDay(int days) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.setLogLimitDays(days);
    }

    @ReactMethod
    public void setTrackingDriver(String driver) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.setTrackingDriver(driver);
    }

    @ReactMethod
    public void setTrackerId(String trackerId) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.setTrackerId(trackerId);
    }

    @ReactMethod
    public void setTrackingStatus(int status) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.setTrackingStatus(status);
    }

    @ReactMethod
    public void setAuthenURL(String url) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.setAuthenURL(url);
    }

    @ReactMethod
    public void setRefreshToken(String refreshToken) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.setRefreshToken(refreshToken);
    }

    @ReactMethod
    public void setTokenExpired(String expired) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        long tmp = 0;
        try {
            tmp = Long.parseLong(expired);
        } catch (Exception e) {

        }
        mTrackingSDK.setTokenExpired(tmp);
    }

    @ReactMethod
    public void setAuthenInfo(String url, String refreshToken, String expired) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        long exp = 0;
        try {
            exp = Long.parseLong(expired);
        } catch (Exception e) {
        }
        if (TextUtils.isEmpty(oldUrl) || TextUtils.isEmpty(oldRefreshToken)) {
            mTrackingSDK.setTokenExpired(exp);
            mTrackingSDK.setAuthenURL(url);
            mTrackingSDK.setRefreshToken(refreshToken);
        } else {
            if (oldUrl.equals(url)) {
                if (exp >= oldExpired) {
                    mTrackingSDK.setAuthenURL(url);
                    mTrackingSDK.setTokenExpired(exp);
                    mTrackingSDK.setRefreshToken(refreshToken);
                } else {
                    mTrackingSDK.setAuthenURL(url);
                    mTrackingSDK.setTokenExpired(oldExpired);
                    mTrackingSDK.setRefreshToken(oldRefreshToken);
                }
            } else {
                mTrackingSDK.setTokenExpired(exp);
                mTrackingSDK.setAuthenURL(url);
                mTrackingSDK.setRefreshToken(refreshToken);
            }
        }
    }

    @ReactMethod
    public void enableLoggingGPS(boolean enabled) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.setEnableLog(enabled);
    }

    @ReactMethod
    public void sendLocation(double lat, double lng) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.sendLocation(lat, lng);
    }

    @ReactMethod
    public void isIgnoreBatteryOptimization(Promise promise) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Activity activity = getCurrentActivity();
            if (activity == null) {
                return;
            }
            PowerManager pm = (PowerManager) activity.getSystemService(POWER_SERVICE);
            boolean isIgnored = pm.isIgnoringBatteryOptimizations(activity.getPackageName());
            promise.resolve(isIgnored);
        }
        promise.resolve(true);
    }

    @ReactMethod
    public void requestIgnoreBatteryOptimization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            Activity activity = getCurrentActivity();
            if (activity == null) {
                return;
            }
//            PowerManager pm = (PowerManager) activity.getSystemService(POWER_SERVICE);
//            if (!pm.isIgnoringBatteryOptimizations(activity.getPackageName())) {
                intent.setAction(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivity(intent);
//            }
        }
    }

    @ReactMethod
    public void setUseMotionSensor(boolean enabled) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.setUseMotionSensor(enabled);
    }

    @ReactMethod
    public void setUseActivityRegconition(boolean enabled) {
        if (mTrackingSDK == null) {
            if (!setCurrentActivityAndTracking()) {
                return;
            }
        }
        mTrackingSDK.setUseActivityRegconition(enabled);
        if (enabled) {
            requestARPermission();
        }
    }
    private void requestARPermission() {
        ActivityCompat.requestPermissions(
            getCurrentActivity(),
            new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
            1);
    }
}
