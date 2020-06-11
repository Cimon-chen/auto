package cn.auto.core.testcase;

import cn.auto.core.utils.CoreConfig;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenmeng on 2016/5/20.
 */
public abstract class TC {
    private static final transient Logger logger = LoggerFactory.getLogger(TC.class);
    private WebDriver driver;

    /**
     * 驱动初始化
     *
     * @return
     */
    public WebDriver setUp(Map<String, String> paras) {
        try {
            this.driver = init(paras);
        } catch (Exception e) {
            logger.error("webdriver初始化失败!" + e.getMessage());
        }
        return this.driver;
    }

    public WebDriver driver() {
        return this.driver;
    }

    /**
     * 模板方法，不同用例类型去实现
     *
     * @return
     * @throws Exception
     */
    public abstract WebDriver init(Map<String, String> paras) throws Exception;

    /**
     * 退出驱动，WebUI退出浏览器，Android、iOS退出APP，触屏退出手机浏览器
     */
    public void tearDown() {
        this.driver.quit();
    }


    /**
     * 失败自动截图，监听器调用
     *
     * @param path
     * @return
     */
    public boolean takeScreenShot(String path) {
        if (null != driver()) {
            File scrFile = ((TakesScreenshot) driver()).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(scrFile, new File(path), false);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public void screenShot(String fileName) {
        takeScreenShot(CoreConfig.TEST_SRCSHOT_DIR + fileName);
    }

    /**
     * 判断页面中是否出现了指定的内容
     *
     * @param target
     * @return
     * @throws Exception
     */
    public boolean contains(String target) throws Exception {
        return driver().getPageSource().contains(target);
    }

    /**
     * 断言页面内容，出现指定内容时成功，没有出现则失败
     *
     * @param target
     * @throws Exception
     */
    public void assertStringTrue(String target) throws Exception {
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
     * 断言成功
     *
     * @throws Exception
     */
    public void success() throws Exception {
        Assert.assertTrue(true);
    }


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
     * 断言失败
     *
     * @param target
     * @throws Exception
     */
    public void fail(String target) throws Exception {
        Assert.fail(target);
    }

    public void fail() throws Exception {
        Assert.fail();
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
     * 线程等待
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
}
