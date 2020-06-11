package cn.auto.core.webdriver.web;

import cn.auto.core.webdriver.DriverRequest;
import cn.auto.core.webdriver.Engine;
import org.openqa.selenium.WebDriver;

/**
 * Created by chenmeng on 2019/8/21.
 */
public class GridEngine implements Engine {
    private GridDriverRuquest driverRuquest;

    public GridEngine(DriverRequest driverRuquest) {
        this.driverRuquest = (GridDriverRuquest) driverRuquest;
    }

    @Override
    public WebDriver driver() throws Exception {
        return null;
    }
}
