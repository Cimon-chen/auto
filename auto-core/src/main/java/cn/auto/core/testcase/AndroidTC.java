package cn.auto.core.testcase;

import cn.auto.core.utils.Consts;
import cn.auto.core.webdriver.DriverRequest;
import cn.auto.core.webdriver.EngineFactory;
import cn.auto.core.webdriver.app.AndroidDriverRequest;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by chenmeng on 2019/8/26.
 */
public class AndroidTC extends TC {
    @Override
    public WebDriver init(Map<String, String> paras) throws Exception {
        DriverRequest driverRequest;
        Map<String, String> result = paras.entrySet().stream().filter(e -> Consts.SERVER.equalsIgnoreCase(e.getKey()) ||
                Consts.UDID.equalsIgnoreCase(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (2 == result.size()) {
            driverRequest = new AndroidDriverRequest(paras);
        } else {
            throw new Exception("缺少必要参数！");
        }
        return EngineFactory.engine(driverRequest);
    }
}
