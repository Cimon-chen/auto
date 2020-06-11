package cn.auto.test;

import java.text.NumberFormat;

public class MainTest {

    public static void main(String[] args) {

        /*String prefix = "{\"data\":[";
        String suffix = "]}";
        String d = "{\"licenseNo\":\"%s\",\"contactInfo\":\"%s\",\"carOwner\":\"张三%s\",\"frameNo\":\"ddddddd\",\"engineNo\":\"sdfo97\",\"registerDate\":\"2020-01-01\",\"idNo\":\"321321321321\"}";
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        StringBuilder dd = new StringBuilder();
        for (int i = 1; i <= 2000; i++) {
            String lNo = "沪A" + getNum(i);
            String cNo = "180000" + getNum(i);
            dd.append(String.format(d, lNo, cNo, i) + ",");
        }
        String now = dd.toString();
        sb.append(now.substring(0, now.length() - 1)).append(suffix);
        System.out.println(sb.toString());*/
        String addp = "5  成长值 +100";
        System.out.println(addp.substring(addp.indexOf("+") + 1).trim());
    }


    public static String getNum(int number) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumIntegerDigits(5);
        formatter.setGroupingUsed(false);
        return formatter.format(number);
    }

}
