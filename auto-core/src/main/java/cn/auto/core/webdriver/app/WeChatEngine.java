package cn.auto.core.webdriver.app;

import cn.auto.core.utils.Consts;
import cn.auto.core.utils.CoreConfig;
import cn.auto.core.webdriver.DriverRequest;
import cn.auto.core.webdriver.Engine;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenmeng on 2019/8/16.
 */
public class WeChatEngine implements Engine {
    private AndroidDriverRequest request;

    public WeChatEngine(DriverRequest request) {
        this.request = (AndroidDriverRequest) request;
    }

    @Override
    public WebDriver driver() throws Exception {
        Options options = new Options();
        WebDriver driver = new AndroidDriver(new URL(this.request.getInitParas().get(Consts.SERVER)), options.initAndroid(this.request.getInitParas()));
        driver.manage().timeouts().implicitlyWait(CoreConfig.DRIVER_IMPLICITLY_WAIT, TimeUnit.MILLISECONDS);
        return driver;
    }
}
