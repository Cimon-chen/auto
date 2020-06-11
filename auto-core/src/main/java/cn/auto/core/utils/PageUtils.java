package cn.auto.core.utils;

import java.util.concurrent.TimeUnit;

/**
 * Created by chenmeng on 2018/9/6.
 */
public class PageUtils {
    public static void pause(long time, TimeUnit unit) {
        try {
            Thread.sleep(unit.toMillis(time));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void pause(long millis) {
        pause(millis, TimeUnit.MILLISECONDS);
    }
}
