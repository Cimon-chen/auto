package cn.auto.core.testcase;

import cn.auto.core.utils.Consts;
import cn.auto.core.webdriver.DriverRequest;
import cn.auto.core.webdriver.EngineFactory;
import cn.auto.core.webdriver.emulation.EmulationDriverRequest;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by chenmeng on 2019/8/2.
 */
public class EmulationTC extends TC {

    @Override
    public WebDriver init(Map<String, String> paras) throws Exception {
        DriverRequest driverRequest;
        Map<String, String> result = paras.entrySet().stream().filter(e -> Consts.BROWSER.equalsIgnoreCase(e.getKey()) ||
                Consts.DEVICE.equalsIgnoreCase(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (0 != result.size()) {
            DriverRequest.Browser browser = DriverRequest.Browser.getBrowser(result.get(Consts.BROWSER));
            DriverRequest.Device device = DriverRequest.Device.getDevice(result.get(Consts.DEVICE));
            driverRequest = new EmulationDriverRequest(browser, device);
        } else {
            driverRequest = new EmulationDriverRequest(DriverRequest.Browser.Emulation, DriverRequest.Device.iPhone7);
        }
        return EngineFactory.engine(driverRequest);
    }
}
