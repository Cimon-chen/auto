package cn.auto.core.data;

import cn.auto.core.utils.Consts;
import cn.auto.core.utils.CoreConfig;
import cn.auto.core.utils.ParamUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenmeng on 2018/1/18.
 */
public class DataImpl implements IData {
    private static transient Logger log = LoggerFactory.getLogger(DataImpl.class);
    private String driver = "oracle.jdbc.OracleDriver";
    private String url = null;
    private String username = null;
    private String pass = null;
    private Connection conn = null;

    private DataImpl() {

    }

    public DataImpl(String url, String username, String pass) {
        this.url = url;
        this.username = username;
        this.pass = pass;
        getConnection();
    }

    /**
     * 获取数据生成基础服务接口
     *
     * @return
     */
    public IServiceSV getService() {
        return new DataService();
    }

    /**
     * 获取任务执行接口
     *
     * @return
     */
    public ITaskSV getTask() {
        return new DataTask();
    }

    public void getConnection() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, pass);
            conn.setAutoCommit(false);//禁用自动提交
        } catch (Exception e) {
            log.error("数据库连接异常:" + e.getMessage());
        }
    }

    public Map<String, String> executeQuery(String sql) {
        Map<String, String> map = new HashMap<String, String>();
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);
            statement.executeQuery();
            map.put(Consts.FLAG, Consts.SUCCESS);
        } catch (SQLException e) {
            map.put(Consts.FLAG, Consts.FAIL);
            map.put(Consts._EXCEPTION, e.getMessage());
            log.error("执行查询语句异常");
        }
        return map;
    }

    public ResultSet executeQuery2(String sql) {
        ResultSet rs = null;
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            rs = statement.executeQuery();
        } catch (SQLException e) {
            log.error("执行查询语句异常");
        }
        return rs;
    }

    public Map<String, String> executeUpdate(String sql) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate(sql);
            map.put(Consts.FLAG, Consts.SUCCESS);
        } catch (SQLException e) {
            map.put(Consts.FLAG, Consts.FAIL);
            map.put(Consts._EXCEPTION, e.getMessage());
            log.error("执行更新语句异常");
        }
        return map;
    }

    public Map<String, String> execute(String sql) {
        Map<String, String> map;
        if (StringUtils.startsWith(sql, "select")) {
            map = executeQuery(sql);
        } else {
            map = executeUpdate(sql);
        }
        return map;
    }

    public Map<String, String> executeSqlBlock(String sqlBlock) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            CallableStatement cst = conn.prepareCall(sqlBlock); // 执行存储过程
//			cst.registerOutParameter(1, Types.INTEGER); // 为存储过程设定返回值
            int count = cst.executeUpdate(); // 得到预编译语句更新记录或删除操作的结果
//			int id = cst.getInt(1); // 得到返回值
            map.put(Consts.FLAG, Consts.SUCCESS);
        } catch (SQLException e) {
            map.put(Consts.FLAG, Consts.FAIL);
            map.put(Consts._EXCEPTION, e.getMessage());
            log.error("执行语句块异常");
        }
        return map;
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("关闭数据库连接异常");
            }
        }
    }

    public void commit() {
        try {
            this.conn.commit();
        } catch (SQLException e) {
            log.error("提交事务异常");
        }
    }

    public void rollback() {
        try {
            this.conn.rollback();
        } catch (SQLException e) {
            log.error("回滚事务异常");
        }
    }


    public static String transSqlStr(String source) {
        String[] searchList = new String[]{"to_date(", "TO_DATE(", "'yyyy-mm-dd hh24:mi:ss'", "sysdate", "SYSDATE", "'yyyy-mm-dd'", "'dd-mm-yyyy'", "'DD-MM-YYYY'", "'mm-dd-yyyy'", "'MM-DD-YYYY'", "'dd-mm-yyyy hh24:mi:ss'", "'DD-MM-YYYY HH24:MI:SS'", "'DD-MM-YYYY HH12:MI:SS'", "'dd-mm-yyyy hh12:mi:ss'", "'mm-dd-yyyy hh24:mi:ss'", "'MM-DD-YYYY HH24:MI:SS'", "'mm-dd-yyyy hh12:mi:ss'", "'MM-DD-YYYY HH12:MI:SS'", "'yyyy-mm-dd hh12:mi:ss'", "'YYYY-MM-DD HH12:MI:SS'"};
        String[] replacementList = new String[]{"str_to_date(", "STR_TO_DATE(", "'%Y-%m-%d %H:%i:%s'", "sysdate()", "SYSDATE() ", "'%Y-%m-%d'", "'%d-%m-%Y'", "'%d-%m-%Y'", "'%m-%d-%Y'", "'%m-%d-%Y'", "'%d-%m-%Y %H:%i:%s'", "'%d-%m-%Y %H:%i:%s'", "'%d-%m-%Y %h:%i:%s'", "'%d-%m-%Y %h:%i:%s'", "'%m-%d-%Y %H:%i:%s'", "'%m-%d-%Y %H:%i:%s'", "'%m-%d-%Y %h:%i:%s'", "'%m-%d-%Y %h:%i:%s'", "'%Y-%m-%d %h:%i:%s'", "'%Y-%m-%d %h:%i:%s'"};
        String rtn = StringUtils.replaceEach(source, searchList, replacementList);
        return rtn;
    }

    public static void main(String[] args) throws SQLException {
//		String sql = "SELECT (CASE WHEN COUNT(*)>0 THEN 'true' ELSE 'false' END) flag,holiday from comframe30test.vm_holiday group by holiday";
       /* String sql = "select 'test' from dual";
        ResultSet rs = driver.executeQuery2(sql);
//		rs.next();
        ResultSetMetaData rsmd = rs.getMetaData();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            System.out.print(rsmd.getColumnName(i) + " ");
            System.out.println();
        }*/
        /*try {
            System.out.print(driver.getSysDate());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /*ResultSet set = DriverFactory.getDriver(Consts.ORACLE).executeQuery2("select login_id from cps.member_login_info where login_name='cimon010'");
        if(set != null){
            while (set.next()){
                System.out.println(set.getString(1));
            }
        }*/
/*		while(rs.next()){
            System.out.println(rs.getString(1) + " " + rs.getString(2));
//			rs.getString("workflow_id");
		}*/

//        System.out.println(DriverFactory.getDriver().getService().getNextDate(2));
        /*String sql = "select login_id from cps.member_login_info a where a.login_name like #{param1}";
        Map map = new HashMap();
        map.put("param1","%cimon%");
        List<Map<String,String>> list = DriverFactory.getDriver().getService().retrieve(sql,map);
        for(Map ma : list){
            System.out.println(ma.get("login_id"));
        }



        String[] parameterNames = ParamUtils.getParamFromString("delete from cps.sub_member_login a where a.sub_login_name=#{param1};", "#{", "}");
        for(String s:parameterNames){
            System.out.println(s);
        }*/

//        System.out.println(DriverFactory.getDriver().getService().getNextTime(TimeUnit.MINUTES,2));
        System.out.println(DataFactory.getData().getService().getNextDate(5));
    }

    /**
     * 数据查询服务
     */
    class DataService implements IServiceSV {
        private DataService() {

        }

        /**
         * 获取数据库当前时间
         *
         * @return
         */
        public String getSysDate() {
            String sql = "select to_char(sysdate,'yyyy-mm-dd') from dual";
            ResultSet rs = executeQuery2(sql);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String rtn = sdf.format(new Date(System.currentTimeMillis()));
            try {
                while (rs.next()) {
                    rtn = rs.getString(1);
                }
                closeConnection();
            } catch (SQLException e) {
                log.error("查询数据库当前时间失败：" + e);
            }
            return rtn;
        }

        /**
         * @return
         */
        public String getSysTime() {
            String sql = "select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual";
            ResultSet rs = executeQuery2(sql);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String rtn = sdf.format(new Date(System.currentTimeMillis()));
            try {
                while (rs.next()) {
                    rtn = rs.getString(1);
                }
                closeConnection();
            } catch (SQLException e) {
                log.error("查询数据库当前时间失败：" + e);
            }
            return rtn;
        }

        /**
         * 查询sql,得到结果List
         *
         * @param sql
         * @param outParasMap
         * @return
         */
        public List<Map<String, String>> retrieve(String sql, Map<String, String> outParasMap) {
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            if (StringUtils.isBlank(sql)) {
                return null;
            }
            String[] outParams = ParamUtils.getParamFromString(sql, "#{", "}");
            try {
                String newSql = ParamUtils.setParamIntoSql(outParams, outParasMap, sql);
                ResultSet resultSet = executeQuery2(newSql);
                while (resultSet.next()) {
                    Map map = new HashMap();
                    ResultSetMetaData rsmd = resultSet.getMetaData();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        String key = rsmd.getColumnName(i);
                        String value = null;
                        switch (rsmd.getColumnType(i)) {
                            case Types.NUMERIC:
                                value = String.valueOf(resultSet.getBigDecimal(i, rsmd.getScale(i)));
                                break;
                            case Types.DECIMAL:
                                value = String.valueOf(resultSet.getBigDecimal(i, rsmd.getScale(i)));
                                break;
                            case Types.VARCHAR:
                                value = resultSet.getString(i);
                                break;
                            case Types.DATE:
                                value = resultSet.getDate(i) + " " + resultSet.getTime(i);
                                break;
                            default:
                                value = resultSet.getString(i);
                                break;
                        }
                        map.put(key.toLowerCase(), value);
                    }
                    list.add(map);
                }
            } catch (Exception e) {
                log.error("查询结果失败：" + e);
            }
            return list;
        }

        /**
         * 增删改
         *
         * @param sql
         * @param outParasMap
         * @return
         */
        public boolean cud(String sql, Map<String, String> outParasMap) {
            boolean flag = false;
            if (StringUtils.isBlank(sql)) {
                return true;
            }
            String[] outParams = ParamUtils.getParamFromString(sql, "#{", "}");
            try {
                String newSql = ParamUtils.setParamIntoSql(outParams, outParasMap, sql);
                flag = executeUpdate(newSql).get(Consts.FLAG).equals("SUCCESS") ? true : false;
                if (flag) {
                    commit();
                }
            } catch (Exception e) {
                log.error("更新结果失败：" + e);
            } finally {
                closeConnection();
            }
            return flag;
        }

        /**
         * 获取数据库当前时间的后N天时间
         *
         * @param n
         * @return
         */
        public String getNextDate(int n) {
            StringBuilder sb = new StringBuilder();
            sb.append("select to_char(sysdate + " + n + ",'yyyy-mm-dd') from dual");
            ResultSet rs = executeQuery2(sb.toString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String rtn = sdf.format(new Date(System.currentTimeMillis()));
            try {
                while (rs.next()) {
                    rtn = rs.getString(1);
                }
                closeConnection();
            } catch (SQLException e) {

            }
            return rtn;
        }

        public String getNextTime(int n) {
            StringBuilder sb = new StringBuilder();
            sb.append("select to_char(sysdate + " + n + ",'yyyy-mm-dd hh24:mi:ss') from dual");
            ResultSet rs = executeQuery2(sb.toString());
            String rtn = null;
            try {
                while (rs.next()) {
                    rtn = rs.getString(1);
                }
                closeConnection();
            } catch (SQLException e) {
                log.error(e + "");
            }
            return rtn;
        }

        /**
         * 获取下一个时间，可根据秒，分，时，天获取
         *
         * @param unit
         * @param n
         * @return
         */
        public String getNextTime(TimeUnit unit, int n) {
            String sql = "select sysdate + interval '%d' %s from dual";
            switch (unit) {
                case SECONDS: {
                    sql = String.format(sql, n, "second");
                    break;
                }
                case MINUTES: {
                    sql = String.format(sql, n, "minute");
                    break;
                }
                case HOURS: {
                    sql = String.format(sql, n, "hour");
                    break;
                }
                case DAYS: {
                    sql = String.format(sql, n, "day");
                    break;
                }
            }
            return getDbTime(sql);
        }

    }

    public String getDbTime(String sql) {
        ResultSet rs = executeQuery2(sql);
        String rtn = null;
        try {
            while (rs.next()) {
                rtn = rs.getString(1);
            }
            closeConnection();
        } catch (SQLException e) {
            log.error("查询数据库当前时间失败：" + e);
        }
        return rtn;
    }

    /**
     * 任务实现类
     */
    class DataTask implements ITaskSV {
        /**
         * 任务处理方法
         *
         * @param task
         * @param outParasMap
         * @return
         * @throws Exception
         */
        public Map doTask(String task, Map<String, String> outParasMap) throws Exception {
            String filePrefix = CoreConfig.TEST_SQL_DIR;
            Map taskMap = new HashMap();
            ResultSet rs = null;
            boolean exec_flag = true;
            if (StringUtils.isNotBlank(task)) {
                //根据dbType获取数据库驱动,为空则默认为ORACLE
                String dbType = CoreConfig.DB_TYPE;
                IData driver = DataFactory.getData(dbType);
                //将任务作为事务，有一条sql执行出错就回滚。
                //按顺序循环执行任务(","分割任务)
                String[] tNames = task.split(",");
                SQLOUT:
                for (int i = 0; i < tNames.length; i++) {
                    String sqlName = tNames[i];
                    //读取任务sql文件
                    if (StringUtils.isNotBlank(sqlName)) {
                        String filePath = filePrefix + sqlName;
                        filePath = CoreConfig.replaceFileSep(filePath);
                        File sqlFile = new File(filePath);
                        if (sqlFile.exists()) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
                            String sqlLine = null;
                            int lineNo = 0;
                            while ((sqlLine = reader.readLine()) != null) {
                                lineNo = lineNo + 1;
                                if (StringUtils.isNotBlank(sqlLine)) {
                                    //判断是否包含注释"--"行注释
                                    if (sqlLine.startsWith("--")) {
                                        continue;
                                    }
                                    //判断是否包含注释"//"行注释
                                    if (sqlLine.startsWith("//")) {
                                        continue;
                                    }
                                    //判断是否包含"/**/"行注释
                                    if (sqlLine.startsWith("/*") && sqlLine.endsWith("*/")) {
                                        continue;
                                    }
                                    //判断是否包含"/**/"块注释
                                    if (sqlLine.startsWith("/*") && !sqlLine.endsWith("*/")) {
                                        while ((sqlLine = reader.readLine()) != null) {
                                            lineNo = lineNo + 1;
                                            if (sqlLine.endsWith("*/")) {
                                                break;
                                            }
                                        }
                                        continue;
                                    }
                                    //判断是否是语句块，语句块是在一行
                                    if (StringUtils.startsWithIgnoreCase(sqlLine, "begin") && StringUtils.endsWithIgnoreCase(sqlLine, "end;")) {
                                        //执行sql语句块。
                                        taskMap = driver.executeSqlBlock(sqlLine);
                                        if (Consts.FAIL.equals(taskMap.get(Consts.FLAG))) {
                                            logError(taskMap, sqlName, lineNo);
                                            exec_flag = false;
                                            break SQLOUT;
                                        }

                                    }
                                    //判断是否是语句块，语句块不在一行
                                    if (StringUtils.startsWithIgnoreCase(sqlLine, "begin") && !StringUtils.endsWithIgnoreCase(sqlLine, "end;")) {
                                        StringBuilder sqlBlock = new StringBuilder();
                                        sqlBlock.append(sqlLine);
                                        while ((sqlLine = reader.readLine()) != null) {
                                            if (Consts.MYSQL.equals(dbType)) {
                                                sqlLine = ParamUtils.transSqlStr(sqlLine);
                                            }
                                            lineNo = lineNo + 1;
                                            sqlBlock.append(sqlLine);
                                            if (StringUtils.endsWithIgnoreCase(sqlLine, "end;")) {
                                                //执行sql语句块
                                                taskMap = driver.executeSqlBlock(sqlBlock.toString());
                                                if (Consts.FAIL.equals(taskMap.get(Consts.FLAG))) {
                                                    logError(taskMap, sqlName, lineNo);
                                                    exec_flag = false;
                                                    break SQLOUT;
                                                }
                                                break;
                                            }
                                        }
                                        continue;
                                    } else {
                                        //sql文件中包含多条sql,用";"隔开
                                        if (sqlLine.trim().endsWith(";")) {
                                            sqlLine = sqlLine.substring(0, sqlLine.trim().length() - 1);
                                            if (Consts.MYSQL.equals(dbType)) {
                                                sqlLine = ParamUtils.transSqlStr(sqlLine);
                                            }
                                        }
                                        //循环逐条执行sql语句
                                        String[] strs = sqlLine.split(";");
                                        for (int j = 0; j < strs.length; j++) {
                                            /*找到sql语句中的变量*/
                                            String[] outParams = ParamUtils.getParamFromString(strs[j], "#{", "}");
                                            String[] sParams = ParamUtils.getParamFromString(strs[j], "\"", "\"");
                                            String newSql = ParamUtils.setParamIntoSql(outParams, outParasMap, strs[j]);
                                            //查询参数start
                                            if (sParams.length != 0 && StringUtils.isNotBlank(sParams[0])) {
                                                ResultSet rtn = driver.executeQuery2(newSql);
                                                ParamUtils.setParams(rtn, outParasMap);
                                                newSql = ParamUtils.setParamIntoSql(outParams, outParasMap, strs[j]);
                                            }
                                            //查询参数end
                                            taskMap = driver.execute(newSql);
                                            if (Consts.FAIL.equals(taskMap.get(Consts.FLAG))) {
                                                logError(taskMap, sqlName, lineNo);
                                                exec_flag = false;
                                                break SQLOUT;
                                            }
                                        }

                                    }
                                }
                            }
                        } else {
                            exec_flag = false;
                            log.error("任务文件不存在.");
                        }
                    } else {
                        exec_flag = false;
                        log.error("任务文件名为空。");
                    }
                }
                //有一条sql出错，任务就回滚
                if (exec_flag) {
                    taskMap.put(Consts.FLAG, Consts.SUCCESS);
                    driver.commit();
                    driver.closeConnection();
                    log.debug("任务事务状态：commit");
                } else {
                    driver.rollback();
                    driver.closeConnection();
                    log.debug("任务事务状态：rollback");
                }
            }
            return taskMap;
        }


        private void logError(Map taskMap, String sqlName, int lineNo) {
            Object message = taskMap.get(Consts._EXCEPTION);
            taskMap.put(Consts._EXCEPTION, String.format("任务：%s的第%d行语句执行异常：%s", sqlName, lineNo, message));
            log.error(String.format("任务：%s的第%d行语句执行异常：%s", sqlName, lineNo, message));
        }
    }
}
