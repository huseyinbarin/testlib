package com.kirkiki.utillib;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

import static android.content.Context.ACTIVITY_SERVICE;

public final class DeviceUtil {

  private static Calendar cal = Calendar.getInstance();

  private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS", Locale.getDefault());

  public static String getCurrentTime() {
    return sdf.format(cal.getTime());
  }

  private static final AtomicLong LAST_TIME_MS = new AtomicLong();

  public static long uniqueCurrentTimeMS() {
    long now = System.currentTimeMillis();
    while (true) {
      long lastTime = LAST_TIME_MS.get();
      if (lastTime >= now) now = lastTime + 1;
      if (LAST_TIME_MS.compareAndSet(lastTime, now)) {
        return now;
      }
    }
  }

  /**
   * if mac address could not be taken from network interfaces just return the default mac (02:04:06:08:16)
   * WARNING!!!:(@SuppressLint("HardwareIds"))
   * @return macaddress of the device
   * @throws SocketException
   */
  @SuppressLint("HardwareIds") public static String getMacAddress() throws SocketException {
    List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
    String networkInterfaceName = isEmulator() ? "eth0" : "wlan0";
    for (NetworkInterface nif : all) {
      if (!nif.getName().equalsIgnoreCase(networkInterfaceName)) continue;
      byte[] macBytes = nif.getHardwareAddress();
      if (macBytes == null) {
        return "mac address could not be taken.";
      }
      StringBuilder res1 = new StringBuilder();
      for (byte b : macBytes) {
        res1.append(String.format("%02X:", b));
      }
      if (res1.length() > 0) {
        res1.deleteCharAt(res1.length() - 1);
      }
      return res1.toString();
    }

    return "02:04:06:08:16";
  }

  private static boolean isEmulator() {
    return Build.FINGERPRINT.startsWith("generic")
        || Build.FINGERPRINT.startsWith("unknown")
        || Build.MODEL.contains("google_sdk")
        || Build.MODEL.contains("Emulator")
        || Build.MODEL.contains("Android SDK built for x86")
        || Build.MANUFACTURER.contains("Genymotion")
        || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
        || "google_sdk".equals(Build.PRODUCT);
  }

  public static boolean isRuntimePermissionsNeeded() {
    return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
  }

  public static int getVersionCode(Context context) throws PackageManager.NameNotFoundException {
    int version;
    PackageInfo pInfo = context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(), 0);
    version = pInfo.versionCode;
    return version;
  }

  @SuppressWarnings("deprecation") public static boolean isThisServiceRunning(String serviceClassName, Context context) {
    ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
      if (serviceClassName.equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }
}
