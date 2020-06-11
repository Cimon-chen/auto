package cn.auto.core.testcase;

import cn.auto.core.utils.Consts;
import cn.auto.core.webdriver.DriverRequest;
import cn.auto.core.webdriver.EngineFactory;
import cn.auto.core.webdriver.web.WebDriverRequest;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by chenmeng on 2019/8/2.
 */
public class WebTC extends TC {
    @Override
    public WebDriver init(Map<String, String> paras) throws Exception {
        DriverRequest driverRequest;
        Map<String, String> result = paras.entrySet().stream().filter(e -> Consts.BROWSER.equalsIgnoreCase(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (0 != result.size()) {
            driverRequest = new WebDriverRequest(DriverRequest.Browser.getBrowser(result.get(Consts.BROWSER)));
        } else {
            driverRequest = new WebDriverRequest(DriverRequest.Browser.Chrome);
        }
        return EngineFactory.engine(driverRequest);
    }
}
