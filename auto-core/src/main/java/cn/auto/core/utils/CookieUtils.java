package cn.auto.core.utils;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by chenmeng on 2016/4/1.
 */
public class CookieUtils {

    public static void saveCookie(WebDriver driver, String filePath, String domain) {
        File cf = new File(filePath);
        try {
            if (cf.exists()) {
                cf.delete();
            }
            cf.createNewFile();
            FileOutputStream fos = new FileOutputStream(cf);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(osw);
            for (Cookie cookie : driver.manage().getCookies()) {
                String v = cookie.getValue();
                if ("".equals(v)) {
                    v = null;
                }
                String d = domain;
                if (StringUtils.isNotBlank(cookie.getDomain())) {
                    d = cookie.getDomain();
                }
                bufferedWriter.write(cookie.getName() + ";" + v + ";" + d + ";" +
                        cookie.getPath() + ";" + cookie.getExpiry() + ";" + cookie.isSecure() + ";" + System.currentTimeMillis());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean addCookie(WebDriver driver, String filePath) {
        boolean flag = false;
        File cf = new File(filePath);
        if (!cf.exists()) {
            return flag;
        }
        try {
            FileReader reader = new FileReader(cf);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ";");
                while (tokenizer.hasMoreTokens()) {
                    String name = tokenizer.nextToken();
                    String value = tokenizer.nextToken();
                    String domain = tokenizer.nextToken();
                    String path = tokenizer.nextToken();
                    Date expiry = null;
                    String e;
                    if (!(e = tokenizer.nextToken()).equals("null")) {
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
                        expiry = sdf.parse(e);
                    }
                    boolean isSecure = new Boolean(tokenizer.nextToken()).booleanValue();
                    String effectTime = tokenizer.nextToken();
                    if ((System.currentTimeMillis() - Long.parseLong(effectTime)) > 15 * 60 * 1000) {
                        flag = false;
                    } else {
                        Cookie cookie = new Cookie(name, value, domain, path, expiry, isSecure);
                        driver.manage().addCookie(cookie);
                        flag = true;
                    }
                }
            }
            bufferedReader.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public static boolean addCookieNew(WebDriver driver, String cookieFile, String domain) throws Exception {
        boolean flag = false;
        File cf = new File(cookieFile);
        if (!cf.exists()) {
            return flag;
        }
        StringBuilder sb = new StringBuilder();
        try {
            Files.lines(Paths.get(cookieFile)).forEach(sb::append);
            String[] colists = sb.toString().split(";");
            for (String co : colists) {
                String key = co.substring(0, co.indexOf("="));
                String value = co.substring(co.indexOf("=") + 1);
                if (value.contains("\"")) {
                    value = value.replace("\"", "");
                }
                /*SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
                Date expiry = sdf.parse("Wed Sep 18 09:27:09 CST 2019");
                sdf.parse()*/
                Cookie cookie = new Cookie(key, value, domain, "/", null, false);
                driver.manage().addCookie(cookie);
                System.out.println(key +" 添加成功");
                flag = true;

            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public static void delCookie(WebDriver driver) {
        driver.manage().deleteAllCookies();
    }
}
