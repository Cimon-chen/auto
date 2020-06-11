package cn.auto.core.component;

import cn.auto.core.utils.CoreConfig;
import cn.auto.core.webdriver.web.PageError;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by chenmeng on 2019/8/19.
 */
public abstract class EmulationPage extends BasePage {
    private static transient Logger logger = LoggerFactory.getLogger(EmulationPage.class);

    private WebDriver driver;

    public EmulationPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public abstract String getUrl();

    /**
     * 单击元素
     *
     * @param element
     */
    public void click(WebElement element) {
        click(element, true);
    }

    public void click(WebElement element, boolean scroll) {
        try {
            if (scroll) {
                scrollTo(element);
            }
        } catch (Exception e) {
            logger.error("scroll error " + e.getCause());
        }
        click(element, 0);
    }


    private void click(WebElement element, long waitTime) {
        WebElement webElement = waitExplicit(waitTime).until(ExpectedConditions.elementToBeClickable(element));
        try {
            webElement.click();
        } catch (Exception e) {
            throw new WebDriverException(e);
        }
    }

    private WebDriverWait waitExplicit(long waitTime) {
        WebDriverWait wait = null;
        if (waitTime == 0) {
            wait = new WebDriverWait(driver, CoreConfig.DRIVER_EXPLICIT_WAIT);
        } else {
            wait = new WebDriverWait(driver, TimeUnit.MILLISECONDS.toSeconds(waitTime));
        }
        return wait;
    }

    /**
     * 页面滚动到元素指定位置
     *
     * @param element
     * @throws Exception
     */
    public void scrollTo(WebElement element) {
        try {
            jsExcutor().js("arguments[0].scrollIntoViewIfNeeded(true);", element);
        } catch (Exception e) {
            throw new WebDriverException(e);
        }
    }


    /**
     * 页面滚动到指定位置
     *
     * @param px
     * @throws Exception
     */
    public void scrollTo(int px) throws Exception {
        jsExcutor().js("document.documentElement.scrollTop = " + px);
    }

    /**
     * 打开页面，固定尺寸
     *
     * @param size
     */
    public void openPageSize(Dimension size) {
        try {
            open(getUrl(), size, false);
            waitForPageLoad();
            assert5xxException();
        } catch (Exception e) {
            logger.error("打开页面异常：" + e);
        }
    }

    public void assert5xxException() throws Exception {
        if (contains("404 Not Found") || contains("500 Servlet Exception") || contains("502 Bad Gateway")
                || contains("503 Service Temporarily Unvailable") || contains("504 Gateway Time-out")) {
            throw new PageError("BasePage 5xx Error");
        }
    }

    /**
     * 打开页面，最大窗口
     *
     * @throws Exception
     */
    public void openPage() throws Exception {
        open(getUrl());
        waitForPageLoad();
        assert5xxException();
    }

    /**
     * 打开页面,默认最大化窗口
     *
     * @param url
     * @throws Exception
     */
    public void open(String url) throws Exception {
        open(url, null, true);
    }

    /**
     * 打开页面，设置尺寸
     *
     * @param url
     * @param isMax
     * @throws Exception
     */
    private void open(String url, Dimension size, boolean isMax) throws Exception {
        driver.get(url);
        if (isMax) {
            driver.manage().window().maximize();
        } else {
            if (size != null) {
                driver.manage().window().setSize(size);
            }
        }
        waitForPageLoad();
    }

    /**
     * 打开url
     */
    public void navTo() {
        navTo(getUrl());
    }

    public void navTo(String url) {
        try {
            driver.navigate().to(url);
            waitForPageLoad();
        } catch (Exception e) {
            logger.error("打开" + url + "失败：" + e.getMessage());
        }
    }

    /**
     * input框 输入文本
     *
     * @param input
     * @param text
     * @throws Exception
     */
    public void inputText(WebElement input, String text) throws Exception {
        if (isElementExist(input)) {
            if (input.isDisplayed() && input.isEnabled()) {
                input.clear();
                input.sendKeys(text);
            }
        } else {
            pause(1000l);
            if (input.isDisplayed() && input.isEnabled()) {
                input.clear();
                input.sendKeys(text);
            }
        }
    }

    /**
     * 根据选项值 选择下拉框
     *
     * @param e
     * @param value
     * @throws Exception
     */
    public void select(WebElement e, String value) throws Exception {
        /*WebDriverWait wait = new WebDriverWait(driver,5000l);
        wait.until(ExpectedConditions.elementToBeSelected(e));*/
        Select select = new Select(e);
        select.selectByVisibleText(value);
    }

    /**
     * 选择第一个选项
     *
     * @param e
     * @throws Exception
     */
    public void selectFirst(WebElement e) throws Exception {
        selectByIndex(e, 0);
    }

    /**
     * 根据序号选择选项，从0开始
     *
     * @param e
     * @param index
     * @throws Exception
     */
    public void selectByIndex(WebElement e, int index) throws Exception {
        Select select = new Select(e);
        select.selectByIndex(index);
    }

    /**
     * 获取select的第一个被选中的元素。
     *
     * @param e
     * @return
     * @throws Exception
     */
    public WebElement getSelectedOption(WebElement e) throws Exception {
        Select select = new Select(e);
        return select.getFirstSelectedOption();
    }
}
