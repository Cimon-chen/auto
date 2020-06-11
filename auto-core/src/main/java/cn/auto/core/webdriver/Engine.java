package cn.auto.core.webdriver;

import org.openqa.selenium.WebDriver;

/**
 * Created by chenmeng on 2018/1/16.
 */
public interface Engine {
    WebDriver driver() throws Exception;
}
