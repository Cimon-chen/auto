package cn.auto.core.data;

import cn.auto.core.utils.Consts;
import cn.auto.core.utils.CoreConfig;
import org.apache.commons.lang.StringUtils;

/**
 * Created by chenmeng on 2018/1/18.
 */
public class DataFactory {

    public static IData getData(String dbType) {
        if (StringUtils.isNotBlank(dbType)) {
            if (Consts.ORACLE.equals(dbType)) {
                return new DataImpl(CoreConfig.DB_URL, CoreConfig.DB_USERNAME, CoreConfig.DB_PASS);
            }
            if (Consts.MYSQL.equals(dbType)) {
                return null;
            } else {
                return new DataImpl(CoreConfig.DB_URL, CoreConfig.DB_USERNAME, CoreConfig.DB_PASS);
            }
        } else {
            return new DataImpl(CoreConfig.DB_URL, CoreConfig.DB_USERNAME, CoreConfig.DB_PASS);
        }
    }

    public static IData getData() {
        return getData(CoreConfig.DB_TYPE);
    }
}
