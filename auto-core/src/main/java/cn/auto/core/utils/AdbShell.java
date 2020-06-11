package cn.auto.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by chenmeng on 2018/8/22.
 */
public class AdbShell {
    private static Runtime runtime = Runtime.getRuntime();

    public static void tap(String udid, int x, int y) {
        try {
            runtime.exec(String.format("adb -s %s shell input tap %d %d", udid, x, y));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void swipe(String udid, int startX, int startY, int endX, int endY) {
        try {
            runtime.exec(String.format("adb -s %s shell input swipe %d %d %d %d", udid, startX, startY, endX, endY));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAndroidVersion(String udid) {
        return runValue(String.format("adb -s %s shell getprop ro.build.version.release", udid));
    }

    public static String getSDKVersion(String udid) {
        return runValue(String.format("adb -s %s shell getprop ro.build.version.sdk", udid));
    }

    public static String getCpuAbi(String udid) {
        return runValue(String.format("adb -s %s shell getprop ro.product.cpu.abi", udid));
    }

    private static String runValue(String cmd) {
        String rtn = null;
        BufferedReader br = null;
        try {
            Process process = runtime.exec(cmd);
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                rtn = line;
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return rtn;
    }

    /**
     * 启动appium
     */
    public static void startAppium(int port){
        try {
            runtime.exec("cmd /c appium --allow-insecure chromedriver_autodownload");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopAppium(){

    }

    public static void main(String[] args) {
        System.out.println(getSDKVersion("42e851b0"));;
    }
}
