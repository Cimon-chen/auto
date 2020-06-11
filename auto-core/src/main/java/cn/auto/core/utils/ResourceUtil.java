package cn.auto.core.utils;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;


public class ResourceUtil {
    private static transient Log log = LogFactory.getLog(ResourceUtil.class);

    /**
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String loadFileFromClassPath(String filePath) throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String tmp = null;
        StringBuilder sb = new StringBuilder();
        while (true) {
            tmp = br.readLine();
            if (tmp != null) {
                sb.append(tmp);
                sb.append("\n");
            } else {
                break;
            }
        }
        return sb.toString();
    }

    @SuppressWarnings("finally")
    public static String loadFileFromPath(String filePath) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream input = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(input, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String tmp = null;
            while (true) {
                tmp = br.readLine();
                if (tmp != null) {
                    sb.append(tmp);
                    sb.append("\n");
                } else {
                    break;
                }
            }
            br.close();
            isr.close();
            input.close();
        } catch (Exception e) {
            log.error("读取文件异常：" + e);
        } finally {
            return sb.toString();
        }
    }

    /**
     * @param filePath
     * @return
     * @throws Exception
     */
    public static URL loadURLFromClassPath(String filePath) throws Exception {
        return Thread.currentThread().getContextClassLoader().getResource(filePath);
    }

    /**
     * @param filePath
     * @return
     * @throws Exception
     */
    public static InputStream loadInputStreamFromClassPath(String filePath) throws Exception {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
    }

    /**
     * @param filePath
     * @return
     * @throws Exception
     */
    public static PropertiesConfiguration loadPropertiesConfigurationFromClassPath(String filePath) throws Exception {
        PropertiesConfiguration pc = new PropertiesConfiguration();
        pc.load(ResourceUtil.loadInputStreamFromClassPath(filePath));
        return pc;
    }

    /**
     * @param filePath
     * @return
     * @throws Exception
     */
    public static Properties loadPropertiesFromClassPath(String filePath) throws Exception {
        Properties pc = new Properties();
        pc.load(ResourceUtil.loadInputStreamFromClassPath(filePath));
        return pc;
    }

    /**
     * @param filePath
     * @param prefix
     * @param isDiscardPrefix
     * @return
     * @throws Exception
     */
    public static Properties loadPropertiesFromClassPath(String filePath, String prefix, boolean isDiscardPrefix) throws Exception {
        Properties rtn = new Properties();
        Properties pc = loadPropertiesFromClassPath(filePath);
        Set key = pc.keySet();
        for (Iterator iter = key.iterator(); iter.hasNext(); ) {
            String element = (String) iter.next();
            if (StringUtils.indexOf(element, prefix) != -1) {
                if (isDiscardPrefix == true) {
                    rtn.put(StringUtils.replace(element, prefix + ".", "").trim(), pc.get(element));
                } else {
                    rtn.put(element, pc.get(element));
                }
            }
        }
        return rtn;
    }

    /**
     * 将unicode转为汉字
     */
    public static String unicode2String(String unicode) {
        StringBuilder string = new StringBuilder();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    }

    public static void saveFile(File file, String content) throws Exception {
        OutputStreamWriter osw = null;
        FileOutputStream fos = null;
        BufferedWriter buffer = null;
        fos = new FileOutputStream(file);
        osw = new OutputStreamWriter(fos, "UTF-8");
        buffer = new BufferedWriter(osw);
        buffer.write(content);
        buffer.close();
        osw.close();
        fos.close();
    }

    public static void main(String[] args) throws Exception {
    /*InputStream is = ResourceUtil.loadInputStreamFromClassPath("config.properties");
    PropertiesConfiguration pc = new PropertiesConfiguration();
    pc.load(is);*/
        String carNo = "1231232";
        String[] obj = new String[]{"XYZ034_38", "test_aaaaaa", "鲁Q8948N", "64466464", "3", "2016-04-03", "400"};
        String[] ss = new String[]{carNo, "截图错误"};
//      ResourceUtil.appendFileContent("error.txt",ss);
//      appendFileContent("error.txt",ss);
//      clearFileContent("error.txt");
//      clearDirFiles();

    }

    public static void appendFileContent(String filePath, Object[] obj) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (obj.length != 0 && obj != null) {
            for (Object o : obj) {
                sb.append(o + "\t");
            }
            sb.append("\n");
            try {
                //true表示以追加形式写文件
                FileWriter writer = new FileWriter(filePath, true);
                writer.write(sb.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearFileContent(String filePath) throws Exception {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getNum(int number, int targetDigits) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumIntegerDigits(targetDigits);
        formatter.setGroupingUsed(false);
        return formatter.format(number);
    }

    /*public static void clearDirFiles() throws Exception{
        File src = new File(ConfigPropInfo.TEST_SRCSHOT_DIR);
        File dest = new File(ConfigPropInfo.DESTSHOT_DIR);
        if(src.exists()){
            FileUtils.copyDirectory(src,dest);
            FileUtils.cleanDirectory(src);
        }
    }*/
}
