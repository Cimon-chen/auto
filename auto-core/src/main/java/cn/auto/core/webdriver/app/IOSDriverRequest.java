package cn.auto.core.webdriver.app;

import cn.auto.core.webdriver.DriverRequest;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Created by chenmeng on 2019/8/16.
 */
public class IOSDriverRequest implements DriverRequest {
    private String server;
    private String udid;
    private DesiredCapabilities capabilities;

    @Override
    public Engine engineType() {
        return Engine.iOS;
    }

    @Override
    public Browser browser() {
        return null;
    }

    public IOSDriverRequest(String server, String udid) {
        this.server = server;
        this.udid = udid;
    }

    public String getServer() {
        return server;
    }

    public String getUdid() {
        return udid;
    }

    public DesiredCapabilities getCapabilities() {
        return capabilities;
    }
}
