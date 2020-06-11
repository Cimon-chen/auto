package cn.auto.core.webdriver.web;

import cn.auto.core.webdriver.DriverRequest;

/**
 * Created by chenmeng on 2019/8/16.
 */
public class WebDriverRequest implements DriverRequest {
    private Browser browser;

    public WebDriverRequest(Browser browser) {
        this.browser = browser;
    }

    public Engine engineType() {
        return Engine.Web;
    }

    public Browser browser() {
        return this.browser;
    }

}
