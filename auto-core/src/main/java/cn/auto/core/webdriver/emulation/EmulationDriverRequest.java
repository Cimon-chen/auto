package cn.auto.core.webdriver.emulation;

import cn.auto.core.webdriver.DriverRequest;

/**
 * Created by chenmeng on 2019/8/16.
 */
public class EmulationDriverRequest implements DriverRequest {
    private Browser browser;
    private Device device;

    public Engine engineType() {
        return Engine.Emulation;
    }

    public Browser browser() {
        return this.browser;
    }

    public Device device() {
        return this.device;
    }

    public EmulationDriverRequest(Browser browser) {
        this.browser = browser;
    }

    public EmulationDriverRequest(Browser browser, Device device) {
        this.browser = browser;
        this.device = device;
    }
}
