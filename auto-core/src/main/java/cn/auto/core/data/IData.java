package cn.auto.core.data;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenmeng on 2018/1/18.
 */
public interface IData {
    void getConnection();

    Map<String, String> executeQuery(String sql);

    ResultSet executeQuery2(String sql);

    Map<String, String> executeUpdate(String sql);

    Map<String, String> execute(String sql);

    Map<String, String> executeSqlBlock(String sqlBlock);

    void closeConnection();

    void commit();

    void rollback();

    IServiceSV getService();

    ITaskSV getTask();

    interface IServiceSV {
        String getNextDate(int n);

        String getSysDate();

        String getNextTime(int n);

        String getNextTime(TimeUnit unit, int n);

        String getSysTime();

        List<Map<String, String>> retrieve(String sql, Map<String, String> outParasMap);

        boolean cud(String sql, Map<String, String> paras);
    }

    interface ITaskSV {
        Map doTask(String task, Map<String, String> paras) throws Exception;
    }
}
