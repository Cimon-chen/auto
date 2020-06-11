package cn.auto.platform;

/**
 * Created by chenmeng on 2017/4/11.
 */
public interface Const {
    String FLAG = "flag";
    String SUCCESS = "success";
    String FAIL = "fail";
    String SKIP = "skip";
    String ID = "id";
    String TEXT = "text";
    String SPLIT = ",";
    String INCLUDE_ALL = "all";
    String EXECUTING = "executing";
    String FINISHED = "finished";
    String WAITING = "waiting";
    String SUIT_WEB = "WEB";
    String SUIT_API = "API";
    String SUIT_APP = "APP";
    String SUIT_WAP = "WAP";

    String BODY = "body";
    String HEADER = "header";
    String STATUS = "status";

    enum ERROR {
        访问超时, 断言错误, 没有找到元素, 页面5XX错误, 依赖未执行, 未知错误, 未捕获Alert, 元素已过时, TestNG错误, WebDriver错误
    }
}
