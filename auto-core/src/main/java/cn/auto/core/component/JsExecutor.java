package cn.auto.core.component;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by chenmeng on 2018/9/2.
 */
public class JsExecutor {
    private JavascriptExecutor executor;

    public JsExecutor(WebDriver driver) {
        this.executor = (JavascriptExecutor) driver;
    }

    /**
     * 执行js脚本
     *
     * @param js
     * @throws Exception
     */
    public Object js(String js) throws Exception {
        return executor.executeScript(js);
    }

    /**
     * 执行js脚本
     *
     * @param js
     * @throws Exception
     */
    public Object js(String js, Object... args) throws Exception {
        return executor.executeScript(js, args);
    }

    /**
     * 在某个元素上执行脚本
     *
     * @param js
     * @param element
     * @param name
     * @param value
     * @return
     * @throws Exception
     */
    public Object js(String js, WebElement element, String name, String value) throws Exception {
        return executor.executeScript(js, element, name, value);
    }

    /**
     * javascript 点击事件，对于有些元素selenium无法点击，可以通过js点击
     *
     * @param el
     * @throws Exception
     */
    public void jsClick(WebElement el) throws Exception {
        executor.executeScript("arguments[0].click()", el);
    }

    /**
     * 根据domId 点击
     *
     * @param domId
     * @throws Exception
     */
    public void jsClick(String domId) throws Exception {
        js("$('#" + domId + "').click()");
    }


    /**
     * H5页面元素没有绑定tap事件的，selenium中click方法点击无效，通过js注入tap事件解决
     *
     * @param el
     * @throws Exception
     */
    public void jsTapAdvance(WebElement el) throws Exception {
        executor.executeScript("arguments[0].dispatchEvent(new CustomEvent('tap', {detail: {},bubbles: true,cancelable: true}));", el);
    }

    /**
     * 设置元素属性
     *
     * @param element
     * @param attrName
     * @param value
     * @throws Exception
     */
    public void setAttribute(WebElement element, String attrName, String value) throws Exception {
        js("arguments[0].setAttribute(arguments[1],arguments[2])", element, attrName, value);
    }
}
