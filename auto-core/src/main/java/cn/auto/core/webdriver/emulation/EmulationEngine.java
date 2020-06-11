package cn.auto.core.webdriver.emulation;

import cn.auto.core.utils.CoreConfig;
import cn.auto.core.webdriver.DriverRequest;
import cn.auto.core.webdriver.Engine;
import cn.auto.core.webdriver.web.WebEngine;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenmeng on 2019/8/16.
 */
public class EmulationEngine implements Engine {
    private static final transient Logger logger = LoggerFactory.getLogger(WebEngine.class);
    private WebDriver driver;
    private EmulationDriverRequest requst;

    public EmulationEngine(DriverRequest request) {
        this.requst = (EmulationDriverRequest) request;
    }

    @Override
    public WebDriver driver() throws Exception {
        if (this.requst.browser() == DriverRequest.Browser.Emulation) {
            String driverFile = "chromedriver-" + CoreConfig.CHROME_DRIVER_VERSION + ".exe";
            System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, CoreConfig.DRIVER_DIR + driverFile);
            if (StringUtils.isNotBlank(CoreConfig.CHROME_BIN)) {
                System.setProperty("webdriver.chrome.bin", CoreConfig.CHROME_BIN);
            }
            Map<String, Object> deviceMetrics = new HashMap();
            deviceMetrics.put("width", this.requst.device().getSize().getWidth());
            deviceMetrics.put("height", this.requst.device().getSize().getHeight());
            deviceMetrics.put("pixelRatio", 3.0);
            Map mobileEmulation = new HashMap();
            mobileEmulation.put("deviceMetrics", deviceMetrics);
            String ua = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1";
            mobileEmulation.put("userAgent", ua);
            /*Map<String, Object> chromeOptions = new HashMap<String, Object>();
            chromeOptions.put("mobileEmulation", mobileEmulation);*/
            ChromeOptions options = new ChromeOptions();
            options.addArguments(Arrays.asList("--enable-site-engagement-service", "--disable-infobars", "--ignore-certificate-errors"));
            options.addArguments(Arrays.asList("--window-size=" + 450 + "," + 800));
            options.setAcceptInsecureCerts(true);
            options.setExperimentalOption("mobileEmulation", mobileEmulation);
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(CoreConfig.DRIVER_IMPLICITLY_WAIT, TimeUnit.MILLISECONDS);
            return driver;
        } else
            throw new Exception("不支持的类型！");
    }
}
