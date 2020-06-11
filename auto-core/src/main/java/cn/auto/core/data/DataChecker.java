package cn.auto.core.data;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by chenmeng on 2016/9/8.
 */
public class DataChecker {
    private static final transient Logger logger = LoggerFactory.getLogger(DataChecker.class);

    /**
     * 数据库校验公共方法
     *
     * @param primaryKey
     * @param checks
     * @param method
     */
    public void check(String primaryKey, Map<String, String> checks, String method) {
        check(new String[]{primaryKey}, checks, method);
    }


    /**
     * @param params
     * @param checks
     * @param method
     */
    public void check(String[] params, Map<String, String> checks, String method) {
        Map<String, String> map = null;
        Method[] methods = this.getClass().getDeclaredMethods();
        if (StringUtils.isNotBlank(method)) {
            for (Method m : methods) {
                if (method.equals(m.getName())) {
                    try {
                        Object rtn = m.invoke(this, params);
                        if (rtn instanceof Map) {
                            map = (Map) rtn;
                        } else if (rtn instanceof List) {
                            List<Map<String, String>> list = (List) rtn;
                            map = list.get(0);
                        }
                        if (map != null && checks != null) {
                            for (Map.Entry<String, String> entry : checks.entrySet()) {
                                Assert.assertEquals(map.get(entry.getKey().toLowerCase()), entry.getValue(), "数据库字段校验不通过：" + entry.getKey());
                            }
                        }
                        break;
                    } catch (IllegalAccessException e) {
                        logger.error(e + "");
                    } catch (InvocationTargetException e) {
                        logger.error(e + "");
                    }
                }
            }
        }
    }
}
