package cn.auto.core.component;

import cn.auto.core.utils.AdbShell;
import cn.auto.core.utils.PageUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;

import java.time.Duration;

/**
 * Created by chenmeng on 2018/9/6.
 */
public class Touch {
    private TouchAction action;
    private AppiumDriver driver;

    public Touch(AppiumDriver driver) {
        this.driver = driver;
        this.action = new TouchAction(driver);
    }

    /**
     * 上滑
     *
     * @throws Exception
     */
    public void swipeUp(int num) throws Exception {
        Duration duration = Duration.ofSeconds(1);
        Dimension size = getSize();
        int width = size.getWidth();
        int height = size.getHeight();
        for (int i = 1; i < num + 1; i++) {
            action.press(PointOption.point(width / 2, height * 3 / 4)).waitAction(WaitOptions.waitOptions(duration)).moveTo(
                    PointOption.point(width / 2, height / 4)).release();
            action.perform();
        }
    }

    public void swipeDown(int num) throws Exception {
        Duration duration = Duration.ofSeconds(1);
        Dimension size = getSize();
        int width = size.getWidth();
        int height = size.getHeight();
        for (int i = 1; i < num + 1; i++) {
            action.press(PointOption.point(width / 2, height / 4)).waitAction(WaitOptions.waitOptions(duration)).moveTo(
                    PointOption.point(width / 2, height * 3 / 4)).release();
            action.perform();
        }
    }

    private Dimension getSize() {
        String currentContext = driver.getContext();
        driver.context("NATIVE_APP");
        Dimension screenSize = driver.manage().window().getSize();
        driver.context(currentContext);
        return screenSize;
    }

    public void tap(String udid, int x, int y) {
        AdbShell.tap(udid, x, y);
    }

    /**
     * adb shell 方式实现滑动
     */
    public void adbSwipeUp(String udid, int num) {
        Dimension screenSize = getSize();
        int width = screenSize.getWidth();
        int height = screenSize.getHeight();
        for (int i = 1; i < num + 1; i++) {
            AdbShell.swipe(udid, width / 2, height * 3 / 4, width / 2, height / 4);
        }
    }


    public void adbSwipeDown(String udid, int num) {
        Dimension screenSize = getSize();
        int width = screenSize.getWidth();
        int height = screenSize.getHeight();
        for (int i = 1; i < num + 1; i++) {
            AdbShell.swipe(udid, width / 2, height / 4, width / 2, height * 3 / 4);
            PageUtils.pause(500);
        }
    }

    public void adbSwipeLeft(String udid, int num) {
        Dimension screenSize = getSize();
        int width = screenSize.getWidth();
        int height = screenSize.getHeight();
        for (int i = 1; i < num + 1; i++) {
            AdbShell.swipe(udid, width * 3 / 4, height / 2, width / 4, height / 2);
            PageUtils.pause(500);
        }
    }

    public void adbSwipeRight(String udid, int num) {
        Dimension screenSize = getSize();
        int width = screenSize.getWidth();
        int height = screenSize.getHeight();
        for (int i = 1; i < num + 1; i++) {
            AdbShell.swipe(udid, width / 4, height / 2, width * 3 / 4, height / 2);
            PageUtils.pause(500);
        }
    }
}
