package cn.auto.core.webdriver;

import cn.auto.core.webdriver.app.AndroidEngine;
import cn.auto.core.webdriver.app.IOSEngine;
import cn.auto.core.webdriver.emulation.EmulationEngine;
import cn.auto.core.webdriver.web.WebEngine;
import org.openqa.selenium.WebDriver;

/**
 * Created by chenmeng on 2018/1/16.
 */
public class EngineFactory {
    public static WebDriver engine(DriverRequest request) throws Exception {
        switch (request.engineType()) {
            case Web:
                return new WebEngine(request).driver();
            case Emulation:
                return new EmulationEngine(request).driver();
            case Android:
                return new AndroidEngine(request).driver();
            case iOS:
                return new IOSEngine(request).driver();
            default:
                return new WebEngine(request).driver();
        }

    }
}
