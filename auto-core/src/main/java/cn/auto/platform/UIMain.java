package cn.auto.platform;


import cn.auto.core.utils.Consts;
import cn.auto.platform.dao.SuitCaseMapper;
import cn.auto.platform.dao.SuitMapper;
import cn.auto.platform.model.SuitCaseDTO;
import cn.auto.platform.model.SuitDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestNG;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenmeng on 2017/4/12.
 */
public class UIMain {
    private SqlSession sqlSession;
    private SuitCaseMapper suitCaseMapper;
    private SuitMapper suitMapper;
    private static final String SUIT_ID = "suit.id";
    private static final String DEVICES = "devices";
    private static final String SERVER_VALUE = "http://localhost:%d/wd/hub";
    private static final int SERVER_PORT = 4723;
    private static final Logger logger = LoggerFactory.getLogger(UIMain.class);

    @BeforeClass
    public void setUp() throws Exception {
        sqlSession = MSqlSession.getSqlSession();
        suitMapper = sqlSession.getMapper(SuitMapper.class);
        suitCaseMapper = sqlSession.getMapper(SuitCaseMapper.class);
    }

    @AfterClass
    public void tearDown() throws Exception {
        MSqlSession.close();
    }

    @Test
    public void execute() throws Exception {
        int suitId = Integer.parseInt(System.getProperty(SUIT_ID));
//        int suitId = 53;
        String[] des;
        SuitDTO suitDTO = suitMapper.selectById(suitId);
        List<SuitCaseDTO> relaCases = suitCaseMapper.getRelaCases(suitId);
        if (!Const.SUIT_API.equalsIgnoreCase(suitDTO.getSuitType())) {
            String devices = System.getProperty(DEVICES);
            String appUrl = System.getProperty(Consts.APP_URL);
            String appPackage = System.getProperty(Consts.APP_PACKAGE);
            String appActivity = System.getProperty(Consts.APP_ACTIVITY);
            String skipInit = System.getProperty(Consts.SKIP_INIT);

            if (Const.SUIT_APP.equalsIgnoreCase(suitDTO.getSuitType()) && StringUtils.isBlank(devices)) {
                throw new Exception("APP用例必须选择执行设备");
            }
            if (Const.SUIT_WEB.equalsIgnoreCase(suitDTO.getSuitType()) && StringUtils.isBlank(devices)) {
                devices = "chrome";
            }
            if (Const.SUIT_WAP.equalsIgnoreCase(suitDTO.getSuitType())) {
                devices = "emulation";
            }
            //UI测试用例集
            TestNG testng = new TestNG();
            XmlSuite xmlSuite = new XmlSuite();//<suit>标签作为一个执行计划
            xmlSuite.setName(suitDTO.getSuitName());
            if (StringUtils.isNotBlank(devices)) {
                des = devices.split(",");
                if (des.length > 1) {
                    xmlSuite.setParallel(XmlSuite.ParallelMode.TESTS);
                }
                //如果是多个设备，创建多个test
                int dId = 0;
                for (String d : des) {
                    XmlTest xmlTest = new XmlTest(xmlSuite, 0);
                    //xmlTest.setName(d);
                    //<test>标签作为一个用例集用例集
                    xmlTest.setName(String.valueOf(suitId) + "-" + d);
                    xmlTest.setVerbose(1);
                    xmlTest.setPreserveOrder("true");
                    /*设置suit监听器*/
                    List listeners = new ArrayList();
                    if (StringUtils.isNotBlank(suitDTO.getListeners())) {
                        String[] liss = StringUtils.split(suitDTO.getListeners(), Const.SPLIT);
                        for (String lis : liss) {
                            listeners.add(lis);
                        }
                    } else {
                        //listeners.add("cn.auto.platform.ResultRecorder");
                        //自定义listener中不能再使用webdriver
                    }
                    if (listeners.size() > 0) {
                        xmlSuite.setListeners(listeners);
                    }
                    /*设置Suit参数*/
                    if (StringUtils.isNotBlank(suitDTO.getParameters())) {
                        xmlTest.setParameters(setParams(suitDTO.getParameters()));
                    }

                    //设置APP server udid参数
                    Map params = new HashMap();
                    if (Const.SUIT_APP.equalsIgnoreCase(suitDTO.getSuitType())) {
                        params.put(Consts.SERVER, String.format(SERVER_VALUE, SERVER_PORT + dId));
                        params.put(Consts.UDID, d);
                        if (StringUtils.isNotBlank(appUrl)) {
                            params.put(Consts.APP_URL, appUrl);
                        }
                        params.put(Consts.APP_PACKAGE, appPackage);
                        params.put(Consts.APP_ACTIVITY, appActivity);
                        params.put(Consts.SKIP_INIT, skipInit);
                    } else {
                        params.put(Consts.BROWSER, d);
                    }
                    dId = dId + 2;
                    xmlTest.setParameters(params);

                    /*设置class，include，exclude，parameters*/
                    List classes = new ArrayList<XmlClass>();
                    if (!relaCases.isEmpty()) {
                        for (SuitCaseDTO cas : relaCases) {
                            XmlClass xmlClass;
                            try {
                                xmlClass = new XmlClass(cas.getClasss());
                            } catch (Exception e) {
                                continue;
                            }
                            xmlClass.setIndex(cas.getCaseId());
                            if (StringUtils.isNotBlank(cas.getInclude())) {
                                if (!Const.INCLUDE_ALL.equalsIgnoreCase(cas.getInclude())) {
                                    String[] includes = StringUtils.split(cas.getInclude(), Const.SPLIT);
                                    List methods = new ArrayList<XmlInclude>();
                                    for (String inc : includes) {
                                        XmlInclude xmlInclude = new XmlInclude(inc);
                                        methods.add(xmlInclude);
                                    }
                                    xmlClass.setIncludedMethods(methods);
                                }
                                /*设置class范围的参数*/
                                if (StringUtils.isNotBlank(cas.getParameters())) {
                                    xmlClass.setParameters(setParams(cas.getParameters()));
                                }
                            }
                            classes.add(xmlClass);
                        }
                    }
                    xmlTest.setClasses(classes);
                }
            }
            logger.info(xmlSuite.toXml());
            List xmlSuitList = new ArrayList<XmlSuite>();
            xmlSuitList.add(xmlSuite);
            testng.setXmlSuites(xmlSuitList);
            testng.run();
        }
    }

    public Map setParams(String parameters) {
        Map paraMap = new HashMap();
        String[] paras = StringUtils.split(parameters, Const.SPLIT);
        for (String para : paras) {
            String[] kv = StringUtils.split(para, "=");
            if (kv.length == 2) {
                paraMap.put(kv[0], kv[1]);
            }
        }
        return paraMap;
    }
}
