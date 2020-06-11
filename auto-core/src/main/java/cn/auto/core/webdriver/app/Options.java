package cn.auto.core.webdriver.app;


import cn.auto.core.utils.AdbShell;
import cn.auto.core.utils.Consts;
import cn.auto.core.utils.CoreConfig;
import cn.auto.platform.MSqlSession;
import cn.auto.platform.dao.SuitMapper;
import cn.auto.platform.model.DeviceDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

/**
 * Created by chenmeng on 2017/2/8.
 */
public class Options {
    private DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
    private static SqlSession sqlSession = MSqlSession.getSqlSession();
    private static SuitMapper suitMapper = sqlSession.getMapper(SuitMapper.class);

    /**
     * Appium-Default
     */
    enum AUTOMATION {
        Appium, Selendroid, UiAutomator2, XCUITest, YouiEngine
    }

    enum PLATFORM {iOS, android, FirefoxOS}


    /******************************************************************
     * appium参数参见：https://www.cnblogs.com/biyuting/p/5710971.html
     * appium官网参数介绍：https://github.com/appium/appium/blob/master/docs/en/writing-running-appium/caps.md
     * appium cap-chromeOptions：https://sites.google.com/a/chromium.org/chromedriver/capabilities
     * IOS：instruments -s devices，Android:adb devices
     *****************************************************************/

    public DesiredCapabilities init() {
        //禁用重签名
        setNoSign(true);
//        setNoReset(true);
        setUnicodeKeyboard(true);
        setResetKeyboard(true);
//        setNativeWebTap(true);
        //默认的命令间隔时间是60s,如果在60s内没有新的命令，appium则认为客户端掉线，会自动退出
        setNewCommandTimeout(60);

//        setChromedriverUseSystemExecutable(false);
        //指定chromedriver version与 webview version 对应关系
        setChromedriverChromeMappingFile(CoreConfig.MAPPING_FILE);
        setChromedriverExecutableDir(CoreConfig.WEBVIEW_CHROMEDRIVER_DIR);
        return desiredCapabilities;
    }

    public DesiredCapabilities desiredCapabilities() {
        return desiredCapabilities;
    }

    /**
     * Android 属性设置
     *
     * @param udid
     * @return
     */
    public DesiredCapabilities initAndroid(String udid) {
        init();
        setPlatformName(PLATFORM.android.name());
        DeviceDTO deviceDTO = suitMapper.getDeviceByUdid(udid);
        if (deviceDTO != null && StringUtils.isNotBlank(deviceDTO.getOsVersion())) {
            setPlatformVersion(deviceDTO.getOsVersion());
        } else {
            setPlatformVersion(AdbShell.getAndroidVersion(udid));
        }
        setAutomationName(AUTOMATION.UiAutomator2.name());
//        setPrintPageSourceOnFindFailure(true);
        setDeviceName(udid);
        setUdid(udid);
        //如果测试触屏版，需要指定浏览器
        setBrowserName("");
        // apk路径，本地路径或者http下载路径
//        setApp(CoreConfig.APP_DIR + CoreConfig.APP_ANDROID_NAME);
        setAppWaitActivity(CoreConfig.WAIT_ACTIVITY);
        setAppActivity(CoreConfig.XYZ_ACTIVITY_NAME);
        setAppPackage(CoreConfig.XYZ_PACKAGE_NAME);
        setAppWaitDuration(10000);
        return desiredCapabilities;
    }

    public DesiredCapabilities initAndroid(Map<String, String> initParas) {
        init();
        setPlatformName(PLATFORM.android.name());
        String udid = initParas.get(Consts.UDID);
        String appUrl = initParas.get(Consts.APP_URL);
        String appActivity = initParas.get(Consts.APP_ACTIVITY);
        String appPackage = initParas.get(Consts.APP_PACKAGE);
        String browserName = initParas.get(Consts.BROWSER_NAME);
        String skipInit = initParas.get(Consts.SKIP_INIT);
        DeviceDTO deviceDTO = suitMapper.getDeviceByUdid(udid);
        if (deviceDTO != null && StringUtils.isNotBlank(deviceDTO.getOsVersion())) {
            setPlatformVersion(deviceDTO.getOsVersion());
        } else {
            setPlatformVersion(AdbShell.getAndroidVersion(udid));
        }
        setAutomationName(AUTOMATION.UiAutomator2.name());
//        setPrintPageSourceOnFindFailure(true);
        setDeviceName(udid);
        setUdid(udid);
        //如果测试触屏版，需要指定浏览器
        if (StringUtils.isNotBlank(browserName)) {
            setBrowserName(browserName);
        }
        // apk路径，本地路径或者http下载路径
        if (StringUtils.isNotBlank(appUrl)) {
            setApp(appUrl);
        }
        if (StringUtils.isNotBlank(appActivity)) {
            setAppActivity(appActivity);
        } else {
            setAppActivity(CoreConfig.XYZ_ACTIVITY_NAME);
        }
        if (StringUtils.isNotBlank(appPackage)) {
            setAppPackage(appPackage);
        } else {
            setAppPackage(CoreConfig.XYZ_PACKAGE_NAME);
        }
        if (StringUtils.isNotBlank(skipInit) && "true".equalsIgnoreCase(skipInit)) {
            //跳过appium 初始化安装,UIAutomator2 only
            setSkipServerInstallation(true);
            //跳过appium 初始化安装 Android only
            setSkipDeviceInitialization(true);
        }

        /*ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("browserName", "org.chromium.webview_shell");
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);*/
//        setAppWaitActivity(CoreConfig.WAIT_ACTIVITY);
        setAppWaitDuration(10000);
        return desiredCapabilities;
    }

    public DesiredCapabilities initIOS(String udid) {
        init();
        setPlatformName(PLATFORM.iOS.name());
        setPlatformVersion(CoreConfig.IOS_VERSION);
        setDeviceName("iPhone 6");
        setAutomationName(AUTOMATION.XCUITest.name());
        setBrowserName("");
        if (StringUtils.isNotBlank(udid)) {
            setUdid(udid);
        }
        setApp(CoreConfig.APP_IOS_NAME);
        return desiredCapabilities;
    }

    public DesiredCapabilities initMobile(String udid) {
        init();
        setPlatformName(PLATFORM.android.name());
        DeviceDTO deviceDTO = suitMapper.getDeviceByUdid(udid);
        if (deviceDTO != null && StringUtils.isNotBlank(deviceDTO.getOsVersion())) {
            setPlatformVersion(deviceDTO.getOsVersion());
        } else {
            setPlatformVersion(CoreConfig.ANDROID_VERSION);
        }
        setDeviceName(udid);
        //指定打开手机浏览器：Browser-自带浏览器；Chrome-谷歌浏览器
        setBrowserName(CoreConfig.BROWSER_NAME);
//        setApp(CoreConfig.BROWSER_APP);
//        setAppPackage(CoreConfig.BROWSER_APP_PACKAGE);
//        setAppWaitActivity(CoreConfig.BROWSER_APP_ACTIVITY);
        return desiredCapabilities;
    }


    public void setAutomationName(String automationName) {
        desiredCapabilities.setCapability("automationName", automationName);
    }

    public void setSkipServerInstallation(boolean skipServerInstallation) {
        desiredCapabilities.setCapability("skipServerInstallation", skipServerInstallation);
    }

    public void setSkipDeviceInitialization(boolean skipDeviceInitialization) {
        desiredCapabilities.setCapability("skipDeviceInitialization", skipDeviceInitialization);
    }

    public void setChromedriverUseSystemExecutable(boolean chromedriverUseSystemExecutable) {
        desiredCapabilities.setCapability("chromedriverUseSystemExecutable", chromedriverUseSystemExecutable);
    }

    public void setChromedriverChromeMappingFile(String chromedriverChromeMappingFile) {
        desiredCapabilities.setCapability("chromedriverChromeMappingFile", chromedriverChromeMappingFile);
    }

    public void setChromedriverExecutableDir(String chromedriverExecutableDir) {
        desiredCapabilities.setCapability("chromedriverExecutableDir", chromedriverExecutableDir);
    }

    public void setSkipUnlock(boolean skipUnlock) {
        desiredCapabilities.setCapability("skipUnlock", skipUnlock);
    }

    public void setAppWaitDuration(int waitDuration) {
        desiredCapabilities.setCapability("appWaitDuration", waitDuration);
    }

    public void setBrowserName(String browserName) {
        desiredCapabilities.setCapability("browserName", browserName);
    }

    public void setPlatformName(String platformName) {
        desiredCapabilities.setCapability("platformName", platformName);
    }

    public void setPlatformVersion(String platformVersion) {
        desiredCapabilities.setCapability("platformVersion", platformVersion);
    }

    public void setNativeWebTap(boolean nativeWebTap) {
        desiredCapabilities.setCapability("nativeWebTap", nativeWebTap);
    }

    public void setDeviceName(String deviceName) {
        desiredCapabilities.setCapability("deviceName", deviceName);
    }

    public void setApp(String app) {
        desiredCapabilities.setCapability("app", app);
    }

    public void setAppPackage(String appPackage) {
        desiredCapabilities.setCapability("appPackage", appPackage);
    }

    public void setAppActivity(String appActivity) {
        desiredCapabilities.setCapability("appActivity", appActivity);
    }

    public void setAppWaitActivity(String appWaitActivity) {
        desiredCapabilities.setCapability("appWaitActivity", appWaitActivity);
    }

    public void setNewCommandTimeout(int newCommandTimeout) {
        desiredCapabilities.setCapability("newCommandTimeout", newCommandTimeout);
    }

    public void setNoSign(boolean noSign) {
        desiredCapabilities.setCapability("noSign", noSign);
    }

    public void setNoReset(boolean noReset) {
        desiredCapabilities.setCapability("noReset", noReset);
    }

    public void setPrintPageSourceOnFindFailure(boolean printPageSourceOnFindFailure) {
        desiredCapabilities.setCapability("printPageSourceOnFindFailure", printPageSourceOnFindFailure);
    }

    public void setFullReset(boolean fullReset) {
        desiredCapabilities.setCapability("fullReset", fullReset);
    }

    public void setUnicodeKeyboard(boolean unicodeKeyboard) {
        desiredCapabilities.setCapability("unicodeKeyboard", unicodeKeyboard);
    }

    public void setResetKeyboard(boolean resetKeyboard) {
        desiredCapabilities.setCapability("resetKeyboard", resetKeyboard);
    }

    public void setAutoWebview(boolean autoWebview) {
        desiredCapabilities.setCapability("autoWebview", true);
    }

    public void setReplace(boolean replace) {
        desiredCapabilities.setCapability("replace", replace);
    }

    public void setSessionOverride(boolean sessionOverride) {
        desiredCapabilities.setCapability("sessionOverride", sessionOverride);
    }

    public void setAutoLaunch(boolean autoLaunch) {
        desiredCapabilities.setCapability("autoLaunch", autoLaunch);
    }

    public void setUdid(String udid) {
        desiredCapabilities.setCapability("udid", udid);
    }

    public void setOrientation(boolean orientation) {
        desiredCapabilities.setCapability("orientation", orientation);
    }

    /*IOS only*/
    public void setCalendarFormat(String calendarFormat) {
        desiredCapabilities.setCapability("calendarFormat", calendarFormat);
    }

    public void setBundleId(String bundleId) {
        desiredCapabilities.setCapability("bundleId", bundleId);
    }

    public void setLaunchTimeout(String launchTimeout) {
        desiredCapabilities.setCapability("launchTimeout", launchTimeout);
    }

    public void setLocationServicesEnabled(String locationServicesEnabled) {
        desiredCapabilities.setCapability("locationServicesEnabled", locationServicesEnabled);
    }

    public void setLocationServicesAuthorized(String locationServicesAuthorized) {
        desiredCapabilities.setCapability("locationServicesAuthorized", locationServicesAuthorized);
    }

    public void setAutoAcceptAlerts(String autoAcceptAlerts) {
        desiredCapabilities.setCapability("autoAcceptAlerts", autoAcceptAlerts);
    }

    public void setNativeInstrumentsLib(String nativeInstrumentsLib) {
        desiredCapabilities.setCapability("nativeInstrumentsLib", nativeInstrumentsLib);
    }

    public void setSafariAllowPopups(String safariAllowPopups) {
        desiredCapabilities.setCapability("safariAllowPopups", safariAllowPopups);
    }

    public void setSafariIgnoreFraudWarning(String safariIgnoreFraudWarning) {
        desiredCapabilities.setCapability("safariIgnoreFraudWarning", safariIgnoreFraudWarning);
    }

    public void setSafariOpenLinksInBackground(String safariOpenLinksInBackground) {
        desiredCapabilities.setCapability("safariOpenLinksInBackground", safariOpenLinksInBackground);
    }

    public void setKeepKeyChains(String keepKeyChains) {
        desiredCapabilities.setCapability("keepKeyChains", keepKeyChains);
    }

    public void setLocalizableStringsDir(String localizableStringsDir) {
        desiredCapabilities.setCapability("localizableStringsDir", localizableStringsDir);
    }

    public void setProcessArguments(String processArguments) {
        desiredCapabilities.setCapability("processArguments", processArguments);
    }

    public void setInterKeyDelay(String interKeyDelay) {
        desiredCapabilities.setCapability("interKeyDelay", interKeyDelay);
    }

    public void setShowIOSLog(String showIOSLog) {
        desiredCapabilities.setCapability("showIOSLog", showIOSLog);
    }
}
