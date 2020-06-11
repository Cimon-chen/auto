package cn.auto.core.webdriver;

import org.openqa.selenium.Dimension;

/**
 * Created by chenmeng on 2019/8/16.
 */
public interface DriverRequest {
    enum Engine {Web, Emulation, Android, iOS}

    enum Browser {
        Chrome("chrome"), IE("ie"), FireFox("firefox"), WeiXin("weixin"), Emulation("emulation");
        private String name;

        Browser(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Browser getBrowser(String name) {
            for (Browser browser : Browser.values()) {
                if (browser.getName().equals(name.toLowerCase())) {
                    return browser;
                }
            }
            return Chrome;
        }
    }

    enum Device {
        iPhone6("iPhone6", "Apple iPhone 6", new Dimension(375, 667)),
        iPhone7("iphone7", "Apple iPhone 7", new Dimension(375, 667)),
        iPhone6P("iphone6p", "Apple iPhone 6 Plus", new Dimension(414, 736)),
        iPad("ipad", "iPad", new Dimension(1024, 768)),
        Nexus10("nexus10", "Nexus 10", new Dimension(375, 667)),
        DEFALUT("defalut", "iPhone 6", new Dimension(375, 667));

        private String name;
        private String code;
        private Dimension size;

        Device(String name, String code, Dimension size) {
            this.name = name;
            this.code = code;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        public Dimension getSize() {
            return size;
        }

        public static Device getDevice(String name) {
            for (Device device : Device.values()) {
                if (device.getName().equals(name.toLowerCase())) {
                    return device;
                }
            }
            return DEFALUT;
        }
    }

    Engine engineType();

    Browser browser();


}
