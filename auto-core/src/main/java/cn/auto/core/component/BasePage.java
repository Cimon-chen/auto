package cn.auto.core.component;

import cn.auto.core.data.DataChecker;
import cn.auto.core.utils.CoreConfig;
import com.google.common.base.Function;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenmeng on 2019/8/19.
 */
public class BasePage {

    private static transient Logger logger = LoggerFactory.getLogger(BasePage.class);
    private WebDriver driver;
    private JsExecutor jsExecutor;
    private DataChecker checker;

    private BasePage() {

    }

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.jsExecutor = new JsExecutor(driver);
    }

    protected BasePage(WebDriver driver, DataChecker checker) {
        this(driver);
        this.checker = checker;
    }

    public DataChecker getChecker() {
        return checker;
    }

    private void setChecker(DataChecker checker) {
        this.checker = checker;
    }

    /**
     * 获取driver
     *
     * @return
     * @throws Exception
     */
    public WebDriver driver() throws Exception {
        return driver;
    }

    /**
     * 获取jsexecutor
     *
     * @return
     */
    public JsExecutor jsExcutor() {
        return this.jsExecutor;
    }

    /**
     * 线程等待
     *
     * @param millis
     */
    public void pause(long millis) {
        pause(millis, TimeUnit.MILLISECONDS);
    }

    /**
     * 线程等待，时间单元可选
     *
     * @param time
     * @param unit
     */
    public void pause(long time, TimeUnit unit) {
        try {
            Thread.sleep(unit.toMillis(time));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断页面中是否出现了指定的内容
     *
     * @param target
     * @return
     * @throws Exception
     */
    public boolean contains(String target) {
        return driver.getPageSource().contains(target);
    }

    /**
     * 断言页面内容，出现指定内容时成功，没有出现则失败
     *
     * @param target
     * @throws Exception
     */
    public void assertStringTrue(String target) {
        Assert.assertTrue(contains(target));
    }

    /**
     * 断言页面内容，出现指定内容时失败，没有出现则成功
     *
     * @param target
     * @throws Exception
     */
    public void assertStringFalse(String target) throws Exception {
        Assert.assertFalse(contains(target));
    }

    /**
     * 断言指定内容，10s超时，10s内没500ms判断一次
     *
     * @param target
     * @throws Exception
     */
    public void assertContent(final String target)  {
        WebDriverWait wait = new WebDriverWait(driver, CoreConfig.DRIVER_IMPLICITLY_WAIT / 1000);
        try{
            wait.until((driver) -> {
                Boolean flag = false;
                try {
                    flag = driver.getPageSource().contains(target);
                } catch (Exception e) {
                }
                return flag;
            });

        }catch (Exception e){
            logger.error("Assert content Timeout.");
        }
        assertStringTrue(target);
    }

    /**
     * 断言成功
     *
     * @throws Exception
     */
    public void success() throws Exception {
        Assert.assertTrue(true);
    }

    /**
     * 断言失败
     *
     * @param target
     * @throws Exception
     */
    public void fail(String target) throws Exception {
        Assert.fail(target);
    }

    /**
     * 判断是否相等
     *
     * @param actual
     * @param expect
     * @param message
     */
    public void assertEquals(String actual, String expect, String message) {
        if (!actual.equals(expect)) {
            try {
                fail(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 根据定位器判断元素是否存在
     *
     * @param locator
     * @return
     * @throws Exception
     */
    public boolean isElementExist(By locator) {
        boolean flag;
        try {
            WebElement element = driver.findElement(locator);
            flag = null != element;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 判断页面元素是否存在
     *
     * @param element
     * @return
     * @throws Exception
     */
    public boolean isElementExist(WebElement element) {
        boolean flag;
        try {
            element.getTagName();
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 当前window关闭 切换到父窗口
     *
     * @param url
     */
    public void switchToParentWindow(String url) {
        try {
            Set<String> handles = driver.getWindowHandles();
            for (String s : handles) {
                driver.switchTo().window(s);
                if (driver.getCurrentUrl().contains(url)) {
                    logger.debug("Switch to window: "
                            + driver.getCurrentUrl() + " successfully!");
                    break;
                } else
                    continue;
            }
        } catch (NoSuchWindowException e) {
            logger.error("switch error：" + driver.getCurrentUrl());
        }
        logger.debug("current url:" + driver.getCurrentUrl());
    }

    /**
     * 切换到目标页，并且关闭其他页面
     *
     * @param url
     */
    public void switch2UrlAndCloseAthers(String url) {
        String targetHandle = null;
        try {
            Set<String> handles = driver.getWindowHandles();
            for (String s : handles) {
                driver.switchTo().window(s);
                if (!driver.getCurrentUrl().contains(url)) {
                    driver.close();
                } else {
                    targetHandle = s;
                }
            }
            driver.switchTo().window(targetHandle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换到window
     *
     * @param url
     * @param isMax
     * @throws Exception
     */
    public void switchToWindowByUrl(String url, boolean isMax) throws Exception {
        String currentHandle = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        handles.remove(currentHandle);
        for (String s : handles) {
            driver.switchTo().window(s);
            if (isMax) {
                driver.manage().window().maximize();
            }
            if (driver.getCurrentUrl().contains(url)) {
                logger.debug("Switch to window: "
                        + driver.getCurrentUrl() + " successfully!");
                break;
            } else {
                continue;
            }
        }
    }

    /**
     * 获取窗口大小
     *
     * @throws Exception
     */
    public Dimension size() throws Exception {
        return driver.manage().window().getSize();
    }

    /**
     * 根据url关闭窗口
     *
     * @param targetUrl
     * @throws Exception
     */
    public void closePage(String targetUrl) throws Exception {
        if (StringUtils.isNotBlank(targetUrl)) {
            String currentUrl = driver.getCurrentUrl();
            switchToWindowByUrl(targetUrl, true);
            driver.close();
            switchToParentWindow(currentUrl);
        } else {
            driver.close();
        }
    }

    /**
     * 关闭当前窗口
     *
     * @throws Exception
     */
    public void closePage() throws Exception {
        closePage(null);
    }

    /**
     * 页面刷新
     *
     * @throws Exception
     */
    public void refresh() throws Exception {
        driver.navigate().refresh();
    }

    /**
     * 等待页面加载完成，
     *
     * @throws Exception
     */
    public void waitForPageLoad() {
        WebDriverWait wait = new WebDriverWait(driver, CoreConfig.DRIVER_EXPLICIT_WAIT);
        wait.until(isPageLoaded());
    }

    protected Function<WebDriver, Boolean> isPageLoaded() {
        return (driver) -> {
            boolean flag;
            try {
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                flag = StringUtils.equalsIgnoreCase((String) executor.executeScript("return document.readyState"), "complete");
            } catch (Exception e) {
                flag = false;
            }
            return flag;
        };
    }

    /**
     * 等待页面中所有ajax请求完成
     *
     * @throws Exception
     */
    public void waitForAjax() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, CoreConfig.DRIVER_EXPLICIT_WAIT);
        wait.until((driver) -> {
            Boolean flag = false;
            try {
                flag = (Boolean) jsExcutor().js("return jQuery.active == 0");
            } catch (Exception e) {
            }
            return flag;
        });
    }
}
