package cn.auto.core.component;

import cn.auto.core.utils.CoreConfig;
import cn.auto.core.webdriver.web.PageError;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenmeng on 2019/8/19.
 */
public abstract class WebPage extends BasePage {
    private static transient Logger logger = LoggerFactory.getLogger(WebPage.class);

    private WebDriver driver;
    private Actions actions;
    private Wait wait;

    @FindBy(xpath = "//body")
    private WebElement blankPlace;

    /**
     * @return url由子类实现
     */
    public abstract String getUrl();

    /**
     * 所有页面必须继承此类，初始化所有页面元素
     *
     * @param driver
     */
    protected WebPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.actions = new Actions(driver);
        this.wait = new Wait(driver);
        PageFactory.initElements(driver, this);
    }

    public Wait waitFor() throws Exception {
        return this.wait;
    }

    /**
     * 根据url切换窗口
     */
    public void switchTo() {
        try {
            pause(2000l);//在切换到新窗口时chrome要等待一段时间，否则切换不成功
            waitForPageLoad();
            switchToWindowByUrl(getUrl(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据url切换窗口
     *
     * @param url
     */
    public void switchTo(String url) {
        try {
            pause(3000l);//在切换到新窗口时chrome要等待一段时间，否则切换不成功
            waitForPageLoad();
            switchToWindowByUrl(url, true);
        } catch (Exception e) {
            logger.error("窗口切换失败：" + e.getMessage());
        }
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

    public void back() {
        driver.navigate().back();
        waitForPageLoad();
    }

    /**
     * 根据frame ID 切换 iframe，frame
     *
     * @param frame
     */
    public void frameTo(String frame) {
        try {
            driver.switchTo().frame(frame);
        } catch (Exception e) {
            logger.error("根据元素切换frame失败：" + e.getMessage());
        }
    }

    /**
     * 根据元素切换到iframe frame
     *
     * @param frame
     */
    public void frameTo(WebElement frame) {
        try {
            driver.switchTo().frame(frame);
        } catch (Exception e) {
            logger.error("根据frameID切换frame失败：" + e.getMessage());
        }
    }

    /**
     * 切换到上级frame
     */
    public void frameToParent() {
        driver.switchTo().parentFrame();
    }

    /**
     * 切换到默认内容
     */
    public void frameToDefault() {
        driver.switchTo().defaultContent();
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
     * 打开页面，最大窗口
     *
     * @throws Exception
     */
    public void openPage(String url) throws Exception {
        open(url);
        waitForPageLoad();
        assert5xxException();
    }

    public void assert5xxException() throws Exception {
        if (contains("404 Not Found") || contains("500 Servlet Exception") || contains("502 Bad Gateway")
                || contains("503 Service Temporarily Unvailable") || contains("504 Gateway Time-out")) {
            throw new PageError("BasePage 5xx Error");
        }
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
     * 双击元素
     *
     * @param element
     */
    public void doubleClick(WebElement element) {
        doubleClick(element, 0);
    }

    /**
     * 双击元素并设置显示等待时间
     *
     * @param element
     * @param waitTime
     */
    public void doubleClick(WebElement element, long waitTime) {
        WebElement webElement = waitExplicit(waitTime).until(ExpectedConditions.elementToBeClickable(element));
        Actions actions = new Actions(driver);
        try {
            actions.doubleClick(webElement).build().perform();
        } catch (Exception e) {
            throw new WebDriverException(e);
        }
    }

    /**
     * 右击元素
     *
     * @param el
     * @param waitTime
     */
    public void contextClick(WebElement el, long waitTime) {
        WebElement webElement = waitExplicit(waitTime).until(ExpectedConditions.elementToBeClickable(el));
        Actions actions = new Actions(driver);
        actions.contextClick(webElement).build().perform();
    }

    public void drag(WebElement el, int w, int h, long waitTime) {
        Actions actions = new Actions(driver);
        actions.dragAndDropBy(el, w, h).perform();
    }


    /**
     * 选择checkbox
     *
     * @param webElement
     * @throws Exception
     */
    public void selectCheckBox(WebElement webElement) throws Exception {
        if (!webElement.isSelected()) {
            try {
                webElement.click();
            } catch (Exception e) {
                throw new WebDriverException(e);
            }
        }
    }

    /**
     * 选择多个复选框
     *
     * @param checks
     * @param targets
     * @throws Exception
     */
    public void selectCheckBoxes(List<WebElement> checks, String[] targets) throws Exception {
        for (String target : targets) {
            for (WebElement check : checks) {
                WebElement label = check.findElement(By.xpath("following-sibling::label[1]"));
                if (target.equals(label.getText().trim())) {
                    selectCheckBox(check);
                    break;
                }
            }
        }
    }

    /**
     * 取消选择checkbox
     *
     * @param webElement
     * @throws Exception
     */
    public void deselectCheckBox(WebElement webElement) throws Exception {
        if (webElement.isSelected()) {
            webElement.click();
        }
    }

    /**
     * 取消选择checkboxes
     *
     * @param checkboxes
     * @throws Exception
     */
    public void deselectAllCheckBox(List<WebElement> checkboxes) throws Exception {
        for (WebElement e : checkboxes) {
            deselectCheckBox(e);
        }
    }


    /**
     * 获取指定元素的文本内容，如果元素不存在返回空
     *
     * @param by
     * @return
     * @throws Exception
     */
    public String getText(By by) throws Exception {
        waitForElementPresent(by);
        if (isElementExist(by)) {
            return driver.findElement(by).getText().trim();
        } else {
            return "";
        }
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
    public void waitForAlert() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, CoreConfig.DRIVER_EXPLICIT_WAIT);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                Boolean flag = false;
                try {
                    driver.switchTo().alert();
                    flag = true;
                } catch (NoAlertPresentException Ex) {
                    flag = false;
                }
                return flag;
            }
        });
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

    public void catchUnexpectedAlert() {
        try {
            waitForAlert1();
            if (isAlertPresent()) {
                acceptAlert();
            }
        } catch (Exception e) {
            logger.error("catch unexpected alert.");
        }
    }


    /**
     * 切换到frame，iframe
     *
     * @param frame
     * @throws Exception
     */
    public void switchToFrame(WebElement frame) throws Exception {
        driver.switchTo().frame(frame);
    }


    public HasInputDevices getInputDevice() throws Exception {
        return (HasInputDevices) driver;
    }

    public HasTouchScreen getTouchScreen() throws Exception {
        return (HasTouchScreen) driver;
    }

    /**
     * alert 确定
     *
     * @throws Exception
     */
    public void acceptAlert() throws Exception {
        waitForAlert();
        driver.switchTo().alert().accept();
    }

    public String getAndacceptAlert() throws Exception {
        waitForAlert();
        Alert alert = driver.switchTo().alert();
        String rtn = alert.getText();
        alert.accept();
        return rtn;
    }

    /**
     * alert 取消
     *
     * @throws Exception
     */
    public void dismissAlert() throws Exception {
        waitForAlert();
        driver.switchTo().alert().dismiss();
    }

    /**
     * 判断alert是否出现
     *
     * @return
     * @throws Exception
     */
    public boolean isAlertPresent() throws Exception {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException Ex) {
            return false;
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

    /**
     * 鼠标滑过元素
     *
     * @param element
     * @throws Exception
     */
    public void mouseOver(WebElement element) throws Exception {
        Actions builder = new Actions(driver);
        builder.moveToElement(element);
        builder.perform();
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
     * 获取窗口大小
     *
     * @throws Exception
     */
    public Dimension size() throws Exception {
        return driver.manage().window().getSize();
    }


    /**
     * 移动到指定元素位置
     *
     * @param target
     * @throws Exception
     */
    public void moveToElement(WebElement target) throws Exception {
        Actions actions = new Actions(driver);
        actions.moveToElement(target).perform();
    }

    /**
     * 设置元素属性
     *
     * @param element
     * @param attrName
     * @param value
     * @throws Exception
     */
    public void setAttribute(WebElement element, String attrName, String value) throws Exception {
        jsExcutor().js("arguments[0].setAttribute(arguments[1],arguments[2])", element, attrName, value);
    }

    /**
     * 删除元素属性
     *
     * @param element
     * @param attrName
     * @throws Exception
     */
    public void removeAttribute(WebElement element, String attrName) throws Exception {
        jsExcutor().js("arguments[0].removeAttribute(arguments[1])", element, attrName);
    }

    public String getCurUrl() throws Exception {
        return driver.getCurrentUrl();
    }


    /**
     * 点击空白处失去焦点
     *
     * @throws Exception
     */
    public void blur() throws Exception {
        click(blankPlace);
    }

    /**
     * 模拟键盘事件
     *
     * @param key
     */
    public void sendKeys(Keys key) {
        actions.sendKeys(key).build().perform();
    }

    /**
     * 模拟键盘事件
     *
     * @param key
     */
    public void sendKeys(WebElement e, Keys key) {
        actions.sendKeys(e, key).build().perform();
    }

    public void esc() {
        sendKeys(Keys.ESCAPE);
    }


    /**
     * 获取actions对象
     *
     * @return
     */
    public Actions action() {
        return this.actions;
    }
}
