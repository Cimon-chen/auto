package cn.auto.core.webdriver.web;

import cn.auto.core.utils.CoreConfig;
import cn.auto.core.webdriver.DriverRequest;
import cn.auto.core.webdriver.Engine;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenmeng on 2018/1/16.
 */
public class WebEngine implements Engine {
    private static final transient Logger logger = LoggerFactory.getLogger(WebEngine.class);
    private WebDriver driver;
    private DesiredCapabilities capabilities;

    private WebDriverRequest request;

    public WebEngine(DriverRequest request) {
        this.request = (WebDriverRequest) request;
    }


    public WebDriver driver() throws Exception {
        switch (this.request.browser()) {
            case Chrome: {
                String driverFile = "chromedriver-" + CoreConfig.CHROME_DRIVER_VERSION + ".exe";
                System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, CoreConfig.DRIVER_DIR + driverFile);
                ChromeDriverService.Builder builder = new ChromeDriverService.Builder();
                builder = builder.usingDriverExecutable(new File(CoreConfig.DRIVER_DIR + driverFile));
                if (CoreConfig.CHROME_PORT_ISFIXED) {
                    builder.usingPort(Integer.valueOf(CoreConfig.CHROME_PORT));
                } else {
                    builder.usingAnyFreePort();
                }
                driver = new ChromeDriver(options());
                break;
            }
            case IE: {
                System.setProperty(InternetExplorerDriverService.IE_DRIVER_EXE_PROPERTY, CoreConfig.DRIVER_DIR + "IEDriverServer.exe");
                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                ieOptions.introduceFlakinessByIgnoringSecurityDomains();
                ieOptions.takeFullPageScreenshot();
                ieOptions.destructivelyEnsureCleanSession();
                driver = new InternetExplorerDriver(ieOptions);
                break;
            }
            case FireFox: {
                System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, CoreConfig.DRIVER_DIR + "geckodriver.exe");
                driver = new FirefoxDriver();
                break;
            }
            default: {
                throw new Exception("不支持的浏览器类型！");
            }
        }
        driver.manage().timeouts().implicitlyWait(CoreConfig.DRIVER_IMPLICITLY_WAIT, TimeUnit.MILLISECONDS);
        return driver;
    }

    /*private void registerListener() {
        driver = new EventFiringWebDriver(driver).register(new PageEventListener());
    }*/

    private DesiredCapabilities capabilities() {
        ChromeOptions options = new ChromeOptions();
        capabilities = DesiredCapabilities.chrome();
        options.addArguments(Arrays.asList("--test-type", "--start-maximized", "--ignore-certificate-errors",
                "--disable-popup-blocking", "--disable-infobars"));
        if (StringUtils.isNotBlank(CoreConfig.CHROME_BIN)) {
            options.setBinary(CoreConfig.CHROME_BIN);
        }
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        return capabilities;
    }

    /**
     * ChromeOptions
     *
     * @return
     */
    private ChromeOptions options() {
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.plugins", 1);
        prefs.put("profile.content_settings.plugin_whitelist.adobe-flash-player", 1);
        prefs.put("profile.content_settings.exceptions.plugins.*,*.per_resource.adobe-flash-player", 1);
        prefs.put("PluginsAllowedForUrls", "www.xyz.cn,www.91xinbei.cn");// Enable Flash for this site
        options.setExperimentalOption("prefs", prefs);
        options.setAcceptInsecureCerts(true);
        options.addArguments("--test-type", "--start-maximized", "--ignore-certificate-errors",
                "--disable-popup-blocking", "--disable-infobars", "--disable-notifications");
        if (StringUtils.isNotBlank(CoreConfig.CHROME_BIN)) {
            options.setBinary(CoreConfig.CHROME_BIN);
        }
//        options.addArguments("--headless");//谷歌59版本以上提供了无头模式，可以不打开浏览器来执行用例
        return options;
    }
}
