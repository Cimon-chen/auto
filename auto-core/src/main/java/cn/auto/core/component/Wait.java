package cn.auto.core.component;

import cn.auto.core.utils.CoreConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by chenmeng on 2016/9/2.
 */
public class Wait {
    private WebDriver driver;
    private JavascriptExecutor executor;

    public Wait(WebDriver driver) {
        this.driver = driver;
        this.executor = (JavascriptExecutor)driver;
    }

    /**
     * 等待页面元素出现
     *
     * @param by
     * @throws Exception
     */
    public void waitForElementPresent(By by) throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, CoreConfig.DRIVER_EXPLICIT_WAIT);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }



    /**
     * 等待alert窗口出现
     *
     * @throws Exception
     */
    public void waitForAlert1() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.alertIsPresent());
    }

/*    public void waitElement() {
        new WebDriverWait(driver, ConfigPropInfo.DRIVER_EXPLICIT_WAIT)
                .ignoring(StaleElementReferenceException.class)
                .until(new Predicate<WebDriver>() {
                    public boolean apply(WebDriver driver) {
                        driver.findElement(By.id("checkoutLink")).click();
                        return true;
                    }
                });
    }*/


}
