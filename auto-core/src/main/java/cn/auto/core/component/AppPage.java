package cn.auto.core.component;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenmeng on 2019/8/19.
 */
public abstract class AppPage extends BasePage {
    private final static transient Logger logger = LoggerFactory.getLogger(AppPage.class);
    private AppiumDriver driver;
    private AppiumFieldDecorator decorator;
    private Touch touch;

    private long IMPLICITLY_WAIT = 2000;
    private int EXPLICITLY_WAIT = 10;//单位秒


    public AppPage(WebDriver driver) {
        super(driver);
        this.driver = (AppiumDriver) driver;
        this.driver.manage().timeouts().implicitlyWait(IMPLICITLY_WAIT, TimeUnit.MILLISECONDS);
        this.decorator = new AppiumFieldDecorator(this.driver, Duration.ofSeconds(5));
        this.touch = new Touch(this.driver);
        PageFactory.initElements(this.decorator, this);
    }

    public abstract String getUrl();

    public void openPage() throws Exception {
        driver.get(getUrl());
    }


    /**
     * 获取touch
     *
     * @return
     */
    public Touch touch() {
        return this.touch;
    }

    /**
     * 输入文本
     *
     * @param element
     * @param text
     */
    public void inputText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }


    public void pressKey(AndroidKey key) {
        AndroidDriver androidDriver = (AndroidDriver) driver();
        androidDriver.pressKey(new KeyEvent().withKey(key));
    }


    /**
     * 点击元素，是否滑动到该元素
     *
     * @param element
     */
    public void click(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            throw new WebDriverException("点击失败", e);
        }
    }

    public AppiumDriver driver() {
        return this.driver;
    }


    /**
     * 根据locator滚动查找并点击
     *
     * @param by
     */
    public void click(By by) {
        try {
            driver.findElement(by).click();
        } catch (Exception e) {
            throw new WebDriverException("点击失败", e);
        }
    }


    /**
     * 切换到原生APP
     *
     * @throws Exception
     */
    public void switch2Native() throws Exception {
        Set<String> contexts = driver.getContextHandles();
        logger.info("【切换前Context:" + driver.getContext() + "】");
        for (String context : contexts) {
            if (context.contains("NATIVE_APP")) {
                driver.context(context);
                logger.info("【切换后Context:" + driver.getContext() + "】");
                break;
            }
        }

    }

    /**
     * 切换到web浏览器，wap测试使用chrome浏览器，context为：CHROMIUM
     *
     * @throws Exception
     */
    public void switch2WebView() throws Exception {
        Set<String> contexts = driver.getContextHandles();
        logger.info("【切换前Context:" + driver.getContext() + "】");
        for (String context : contexts) {
            if (context.contains("WEBVIEW")) {
                driver.context(context);
                break;
            } else if (context.contains("CHROMIUM")) {
                driver.context(context);
            }
        }
        logger.info("【切换后Context:" + driver.getContext() + "】");
    }

    public WebElement toast(String message) throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, EXPLICITLY_WAIT);
        WebElement toast = wait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(message)));
        return toast;
    }


    /**
     * 判断APP是否安装
     *
     * @param bundleId
     * @return
     * @throws Exception
     */
    public boolean isAppInstalled(String bundleId) throws Exception {
        return driver.isAppInstalled(bundleId);
    }


    public void select(WebElement e, String value) throws Exception {
        Select select = new Select(e);
        select.selectByVisibleText(value);
    }


    /**
     * 滑动到制定元素
     *
     * @param udid
     * @param target
     */
    public void swipe2Element(String udid, WebElement target) {
        int i = 1;
        while (true) {
            touch.adbSwipeUp(udid, 1);
            pause(500);
            i++;
            if (isElementExist(target) || i >= 5) {
                break;
            }
        }
    }
}
