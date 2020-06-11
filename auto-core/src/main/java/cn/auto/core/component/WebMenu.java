package cn.auto.core.component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by chenmeng on 2016/4/18.
 */
public abstract class WebMenu extends WebPage {
    public WebMenu(WebDriver driver){
        super(driver);
    }

    protected abstract void openExpandMenu(String pName,String name) throws Exception;

    protected abstract List<WebElement> getMenuList() throws Exception;

    /**
     * 打开菜单列表，根据菜单名打开，子类传入菜单列表
     * @param name
     * @throws Exception
     */
    public void openMenu(String name) throws Exception{
        List<WebElement> menuList = getMenuList();
        if(!menuList.isEmpty()){
            for(WebElement m:menuList){
                if(name.equals(m.getText().trim())){
                    click(m);
                    waitForPageLoad();
                    break;
                }
            }
        }
    }
}
