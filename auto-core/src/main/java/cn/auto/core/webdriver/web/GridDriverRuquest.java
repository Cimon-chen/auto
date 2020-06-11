package cn.auto.core.webdriver.web;

import cn.auto.core.webdriver.DriverRequest;

/**
 * Created by chenmeng on 2019/8/21.
 */
public class GridDriverRuquest implements DriverRequest{
    @Override
    public Engine engineType() {
        return Engine.Web;
    }

    @Override
    public Browser browser() {
        return null;
    }
}
