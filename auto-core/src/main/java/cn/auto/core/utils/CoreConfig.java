package cn.auto.core.utils;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Properties;

/**
 * Created by chenmeng on 2018/1/16.
 */
public class CoreConfig {

    private static Properties properties;

    public static String TEST_DATA_DIR = null;
    public static String TEST_SQL_DIR = null;
    public static String DRIVER_DIR = null;
    public static String CHROME_DRIVER_VERSION = null;
    public static String SCRSHOT_SHARE_URL = null;
    public static String TEST_SRCSHOT_DIR = null;
    public static boolean DEBUG = false;

    /*数据库信息*/
    public static String DB_TYPE = null;
    public static String DB_URL = null;
    public static String DB_USERNAME = null;
    public static String DB_PASS = null;

    public static long DRIVER_IMPLICITLY_WAIT = 0;
    public static long DRIVER_EXPLICIT_WAIT = 0;

    /*htmlunit 代理设置*/
    public static String IS_NEED_PROXY = null;
    public static String PROXY_HOST = null;
    public static String PROXY_PORT = null;
    public static String USERNAME = null;
    public static String PASSWORD = null;

    public static String CHROME_BIN = null;
    public static boolean CHROME_PORT_ISFIXED = false;
    public static String CHROME_PORT = null;

    public static boolean IS_FAIL_RETRY = false;

    /*APP配置*/
    public static String APP_DIR = null;
    public static String APP_IOS_NAME = null;
    public static String APP_ANDROID_NAME = null;
    public static String APP_PLATFORM = null;
    public static String ANDROID_VERSION = null;
    public static String IOS_VERSION = null;
    public static String UDID = null;
    public static String IOS_UDID = null;
    public static String ANDROID_UDID = null;
    public static int WAIT_ELEMENT = 1000;
    public static String WAIT_ACTIVITY = null;
    public static String XYZ_ACTIVITY_NAME = null;
    public static String XYZ_PACKAGE_NAME = null;

    /*wap配置*/
    public static String BROWSER_NAME = null;
    public static String BROWSER_APP = null;
    public static String BROWSER_APP_PACKAGE = null;
    public static String BROWSER_APP_ACTIVITY = null;

    public static String WEBVIEW_CHROMEDRIVER_DIR = null;

    public static String MAPPING_FILE = null;


    static {
        String root = System.getProperty("user.dir");
        root = StringUtils.substringBeforeLast(root, "\\");
        if (properties == null) {
            try {
                properties = ResourceUtil.loadPropertiesFromClassPath("core-config.properties");
                if (properties == null) {
                    throw new Exception("读取文件内容失败");
                }
                DRIVER_DIR = root + File.separator + properties.getProperty("test.drivers.dir");
                if (!DRIVER_DIR.endsWith("/")) {
                    DRIVER_DIR = DRIVER_DIR + "/";
                }

                WEBVIEW_CHROMEDRIVER_DIR = DRIVER_DIR + "appium";
                WEBVIEW_CHROMEDRIVER_DIR = replaceFileSep(WEBVIEW_CHROMEDRIVER_DIR);

                MAPPING_FILE = DRIVER_DIR + "mapping.json";
                MAPPING_FILE = replaceFileSep(MAPPING_FILE);

                CHROME_DRIVER_VERSION = properties.getProperty("chrome.driver.version");
                TEST_DATA_DIR = root + File.separator + properties.getProperty("test.data.dir");
                if (!TEST_DATA_DIR.endsWith("/")) {
                    TEST_DATA_DIR = TEST_DATA_DIR + "/";
                }
                TEST_SQL_DIR = root + File.separator + properties.getProperty("test.sql.dir");
                if (!TEST_SQL_DIR.endsWith("/")) {
                    TEST_SQL_DIR = TEST_SQL_DIR + "/";
                }
                TEST_SQL_DIR = replaceFileSep(TEST_SQL_DIR);

                SCRSHOT_SHARE_URL = properties.getProperty("scrshot.share.url");
                TEST_SRCSHOT_DIR = properties.getProperty("test.scrshot.dir");

                String debug = properties.getProperty("debug.mode");
                DEBUG = StringUtils.isNotBlank(debug) ? Boolean.valueOf(debug) : false;

                /*数据库信息*/
                DB_TYPE = properties.getProperty("db.type");
                DB_URL = properties.getProperty("db.url");
                DB_USERNAME = properties.getProperty("db.username");
                DB_PASS = properties.getProperty("db.pass");

                DRIVER_IMPLICITLY_WAIT = Long.parseLong(properties.getProperty("driver.implicitly.wait"));
                DRIVER_EXPLICIT_WAIT = Long.parseLong(properties.getProperty("driver.explicit.wait"));

                /*htmlunit 代理设置*/
                IS_NEED_PROXY = properties.getProperty("isNeedProxy");
                PROXY_HOST = properties.getProperty("proxyHost");
                PROXY_PORT = properties.getProperty("proxyPort");
                USERNAME = properties.getProperty("username");
                PASSWORD = properties.getProperty("password");

                CHROME_BIN = properties.getProperty("webdriver.chrome.bin");
                if (!new File(CHROME_BIN).exists()) {
                    CHROME_BIN = "C:/Program Files (x86)/Google/Chrome/Application/chrome.exe";
                    if (!new File(CHROME_BIN).exists()) {
                        CHROME_BIN = System.getProperty("user.home") + "/AppData/Local/Google/Chrome/Application/chrome.exe";
                        CHROME_BIN = replaceFileSep(CHROME_BIN);
                    }
                }
                CHROME_PORT = properties.getProperty("webdriver.chrome.port");
                CHROME_PORT_ISFIXED = StringUtils.isBlank(CHROME_PORT) ? false : true;

                String isFailRetry = properties.getProperty("isFailRetry");
                IS_FAIL_RETRY = StringUtils.isNotBlank(isFailRetry) ? Boolean.valueOf(isFailRetry) : false;

                /*APP配置*/
                APP_DIR = root + File.separator + properties.getProperty("app.dir");
                if (!APP_DIR.endsWith("/")) {
                    APP_DIR = APP_DIR + "/";
                }
                APP_DIR = replaceFileSep(APP_DIR);
                APP_IOS_NAME = properties.getProperty("app.ios.name");
                APP_ANDROID_NAME = properties.getProperty("app.android.name");
                APP_PLATFORM = properties.getProperty("app.platform");
                WAIT_ACTIVITY = properties.getProperty("app.wait.activity");
                XYZ_ACTIVITY_NAME = properties.getProperty("xyz.activity.name");
                XYZ_PACKAGE_NAME = properties.getProperty("xyz.package.name");
                UDID = properties.getProperty("udid");
                IOS_UDID = properties.getProperty("appium.ios.default.udid");
                ANDROID_UDID = properties.getProperty("appium.android.default.udid");
                WAIT_ELEMENT = Integer.parseInt(properties.getProperty("wait.element"));
                ANDROID_VERSION = properties.getProperty("android.version");
                IOS_VERSION = properties.getProperty("ios.version");

                BROWSER_APP = properties.getProperty("browser.app");
                BROWSER_NAME = properties.getProperty("browser.name");
                BROWSER_APP_PACKAGE = properties.getProperty("browser.app.package");
                BROWSER_APP_ACTIVITY = properties.getProperty("brower.app.activity");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String replaceFileSep(String originFile) throws Exception {
        if (StringUtils.isNotBlank(originFile)) {
            originFile = StringUtils.replace(originFile, "\\", File.separator);
            originFile = StringUtils.replace(originFile, "/", File.separator);
            return originFile;
        } else
            return "";
    }

    public static void main(String[] args) {
        System.out.println(CHROME_BIN);
    }
}
