package cn.auto.core.utils;

import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by chenmeng on 2016/8/11.
 */
public class ParamUtils {
    /*
    * 获取sql中的参数列表
    * */
    public static String[] getParamFromString(String aSourceString, String aStartStr,
                                              String aEndStr) {
        aSourceString = aSourceString + aEndStr;
        String strSource = aSourceString;
        ArrayList strKey = new ArrayList();
        int iStartIndex = strSource.indexOf(aStartStr);
        int iStartLength = aStartStr.length();
        int iEndLength = aEndStr.length();
        String strTemp = "";
        strTemp = strSource.substring(iStartIndex + iStartLength,
                strSource.length());
        int iEndIndex = strTemp.indexOf(aEndStr)
                + strSource.substring(0, iStartIndex + iStartLength).length();
        if (iEndIndex == iStartIndex) {
            if (!strTemp.startsWith("="))
                strKey.add(strTemp);
        }
        while ((iStartIndex != -1) && (iEndIndex != -1)
                && (iStartIndex < iEndIndex)) {
            strTemp = strSource
                    .substring(iStartIndex + iStartLength, iEndIndex);
            if (!strTemp.startsWith("="))
                strKey.add(strTemp);
            strSource = strSource.substring(iEndIndex + iEndLength,
                    strSource.length());
            iStartIndex = strSource.indexOf(aStartStr);
            strTemp = strSource.substring(iStartIndex + iStartLength,
                    strSource.length());
            iEndIndex = strTemp.indexOf(aEndStr)
                    + strSource.substring(0, iStartIndex + iStartLength)
                    .length();
        }
        return (String[]) strKey.toArray(new String[0]);
    }


    /*替换sql中的变量*/
    public static String replaceParamString(String source, String s1, String s2) {
        int index = source.indexOf(s1);
        if (index == 0)
            return s2 + source.substring(s1.length());
        if (index > 0) {
            return source.substring(0, index) + s2 + source.substring(index + s1.length());
        }
        return source;
    }

    /**
     * 替换sql中的参数
     *
     * @param parameterNames
     * @param paras
     * @param sql
     * @return
     * @throws Exception
     */
    public static String setParamIntoSql(String[] parameterNames, Map paras, String sql) throws Exception {
        for (String pName : parameterNames) {
            /*逐个替换sql中的变量*/
            if (paras != null) {
                Iterator<Map.Entry<String, String>> it = paras.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    if (pName.equals(entry.getKey())) {
                        sql = replaceParamString(sql, "#{" + pName + "}", "'" + entry.getValue() + "'");
                        break;
                    }
                }
            }
        }
        return sql;
    }


    public static String transParamString(String source, String aCode) {
        String[] temp = source.split(aCode);
        String newStr = "";
        for (String s : temp) {
            newStr = newStr + "'" + s + "',";
        }
        return newStr.substring(0, newStr.lastIndexOf(","));
    }

    public static void setParams(ResultSet rs, Map sParamMap)
            throws SQLException {
        if (null != rs) {
            while (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    Object temp = null;
                    temp = sParamMap.get(rsmd.getColumnLabel(i));
                    if (null != temp) {
                        sParamMap.put(rsmd.getColumnLabel(i), temp + "," + rs.getString(i));
                    } else {
                        sParamMap.put(rsmd.getColumnLabel(i), rs.getString(i));
                    }
                }
            }
        }
    }

    /**
     * 字符转转换为二维数组
     *
     * @param source
     * @param flag
     * @return
     */
    public static int[] transStr2Ints(String source, String flag) {
        String[] ids = source.split(flag);
        int[] aa = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            aa[i] = Integer.valueOf(ids[i]);
        }
        return aa;
    }

    public static String transSqlStr(String source) {
        String[] searchList = new String[]{"to_date(", "TO_DATE(", "'yyyy-mm-dd hh24:mi:ss'", "sysdate", "SYSDATE", "'yyyy-mm-dd'", "'dd-mm-yyyy'", "'DD-MM-YYYY'", "'mm-dd-yyyy'", "'MM-DD-YYYY'", "'dd-mm-yyyy hh24:mi:ss'", "'DD-MM-YYYY HH24:MI:SS'", "'DD-MM-YYYY HH12:MI:SS'", "'dd-mm-yyyy hh12:mi:ss'", "'mm-dd-yyyy hh24:mi:ss'", "'MM-DD-YYYY HH24:MI:SS'", "'mm-dd-yyyy hh12:mi:ss'", "'MM-DD-YYYY HH12:MI:SS'", "'yyyy-mm-dd hh12:mi:ss'", "'YYYY-MM-DD HH12:MI:SS'"};
        String[] replacementList = new String[]{"str_to_date(", "STR_TO_DATE(", "'%Y-%m-%d %H:%i:%s'", "sysdate()", "SYSDATE() ", "'%Y-%m-%d'", "'%d-%m-%Y'", "'%d-%m-%Y'", "'%m-%d-%Y'", "'%m-%d-%Y'", "'%d-%m-%Y %H:%i:%s'", "'%d-%m-%Y %H:%i:%s'", "'%d-%m-%Y %h:%i:%s'", "'%d-%m-%Y %h:%i:%s'", "'%m-%d-%Y %H:%i:%s'", "'%m-%d-%Y %H:%i:%s'", "'%m-%d-%Y %h:%i:%s'", "'%m-%d-%Y %h:%i:%s'", "'%Y-%m-%d %h:%i:%s'", "'%Y-%m-%d %h:%i:%s'"};
        String rtn = StringUtils.replaceEach(source, searchList, replacementList);
        return rtn;
    }

    public String setSParamIntoSql(String[] params, Map paras, String sql) throws Exception {
        return null;
    }

    public static String replaceParamString(String source, String[] l, String aCode, String aStartStr, String aEndStr) {
        for (int i = 0; i < l.length; i++) {
            source = ParamUtils.replaceParamString(source, aStartStr + l[i] + aEndStr, aCode);
        }
        return source;
    }
}
