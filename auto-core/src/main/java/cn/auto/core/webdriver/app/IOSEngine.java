package cn.auto.core.webdriver.app;

import cn.auto.core.webdriver.DriverRequest;
import cn.auto.core.webdriver.Engine;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebDriver;

import java.net.URL;

/**
 * Created by chenmeng on 2019/8/16.
 */
public class IOSEngine implements Engine {
    private IOSDriverRequest request;

    public IOSEngine(DriverRequest request) {
        this.request = (IOSDriverRequest) request;
    }

    @Override
    public WebDriver driver() throws Exception {
        Options options = new Options();
        return new IOSDriver(new URL(this.request.getServer()), options.initIOS(this.request.getUdid()));
    }
}
