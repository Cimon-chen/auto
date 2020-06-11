package cn.auto.core.webdriver.app;

import cn.auto.core.webdriver.DriverRequest;

import java.util.Map;

/**
 * Created by chenmeng on 2019/8/16.
 */
public class AndroidDriverRequest implements DriverRequest {
    private String server;
    private String udid;
    private Map<String, String> initParas;

    @Override
    public Engine engineType() {
        return Engine.Android;
    }

    @Override
    public Browser browser() {
        return null;
    }

    public AndroidDriverRequest(String server, String udid) {
        this.server = server;
        this.udid = udid;
    }

    public AndroidDriverRequest(Map<String, String> xmlParas) {
        this.initParas = xmlParas;
    }

    public String getServer() {
        return server;
    }

    public String getUdid() {
        return udid;
    }

    public Map<String, String> getInitParas() {
        return initParas;
    }
}
