package cn.auto.core.data;

import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by chenmeng on 2019/10/28.
 */
public class AreaData {
    private static Properties properties = new Properties();


    private static final List<AreaNode> counties = new ArrayList<>();

    private static final List<Integer> cis = new ArrayList<>();

    static {
        try {
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<AreaNode> getChindrenNodes(Integer pNodeId) {
        List<AreaNode> childrenNodes = counties.stream().filter(areaNode -> pNodeId.intValue() == areaNode.getpAreaId().intValue()).collect(Collectors.toList());
        childrenNodes.sort((a, b) -> a.getAreaId() - b.getAreaId());
        return childrenNodes;
    }

    public static List<AreaNode> getProvinces() throws Exception {
        return getChindrenNodes(00);
    }


    public static void main(String[] args) throws Exception {
        System.out.println(getProvinces());
    }

    public static void load() throws Exception {
        InputStreamReader isr = new InputStreamReader(AreaData.class.getClassLoader().getResourceAsStream("area.properties"), "GB2312");
        properties.load(isr);
        Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();
        entrySet.stream().forEach(entry -> {
            String[] vs = String.valueOf(entry.getValue()).split("-");
            if ("COMMON_COUNTY".equals(vs[2])) {
                cis.add(Integer.valueOf(String.valueOf(entry.getKey())));
            }
            AreaNode areaNode = new AreaNode(Integer.valueOf(String.valueOf(entry.getKey())), vs[0], Integer.valueOf(vs[1]));
            counties.add(areaNode);
        });
    }

    public static List<Integer> getCounties() throws Exception {
        return cis;
    }
}
