package com.appusage;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;

import java.util.Calendar;

import java.util.Map;

@ReactModule(name = AppUsageModule.NAME)
public class AppUsageModule extends ReactContextBaseJavaModule {
  public static final String NAME = "AppUsage";
  UsageStatsManager usageStatsManager;


  public AppUsageModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b);
  }

  @ReactMethod
  public void subtract(double a, double b, Promise promise) {
    promise.resolve(a - b);
  }

  @ReactMethod
  public void getUsageLast24Hr(Callback callback) {

    usageStatsManager = (UsageStatsManager) getReactApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -1); // Get usage stats for the past 24 hours

    Log.d( "getUsageLast24Hr: ", String.valueOf(calendar.getTimeInMillis()));
    WritableArray stats = getAppUsage(calendar.getTimeInMillis(), System.currentTimeMillis());
    callback.invoke(stats);

  }

  private WritableArray getAppUsage(long startTime, long endTime) {

    usageStatsManager = (UsageStatsManager) getReactApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -1); // Get usage stats for the past 24 hours

    Map<String, UsageStats> stats = usageStatsManager.queryAndAggregateUsageStats(
      startTime,
      endTime
    );

    WritableArray writableArray = Arguments.createArray();

    for (UsageStats usageStats : stats.values()) {
      WritableMap statsMap = Arguments.createMap();
      statsMap.putString("packageName", usageStats.getPackageName());
      statsMap.putDouble("totalForegroundTime", usageStats.getTotalTimeInForeground());
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        statsMap.putDouble("totalVisibleTime", usageStats.getTotalTimeVisible());
        statsMap.putDouble("lastVisibleTime", usageStats.getLastTimeVisible());
      }
      statsMap.putDouble("lastUsageTime", usageStats.getLastTimeUsed());
      // Add more properties if needed
      writableArray.pushMap(statsMap);
    }
    return writableArray;

  }

  @ReactMethod
  public void checkPackagePermission(Promise promise) {
//    promise.resolve(1);
    boolean granted = false;
    AppOpsManager appOps = (AppOpsManager) getReactApplicationContext().getSystemService(Context.APP_OPS_SERVICE);
    int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
      android.os.Process.myUid(), getReactApplicationContext().getPackageName());

    if (mode == AppOpsManager.MODE_DEFAULT) {
      granted = (getReactApplicationContext().checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
    } else {
      granted = (mode == AppOpsManager.MODE_ALLOWED);
    }

    Log.d("getAppUsage:Grant ", String.valueOf(granted));
    promise.resolve(granted);
  }

  @ReactMethod
  public void requestUsagePermission() {
    {
      Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//Add This Flag
      getReactApplicationContext().startActivity(intent);
    }
  }


//  private boolean isSocialApp(String packageName) {
//    // Add package names of social apps that you want to track
//    // For example:
//    return packageName.equals("com.aramishrms") || // Facebook
//      packageName.equals("com.bestie") || // Instagram
//      packageName.equals("com.example.basicwebview"); // Twitter
//  }


}
