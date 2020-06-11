package cn.auto.core.listener;

import cn.auto.core.annotation.Data;
import cn.auto.core.annotation.Page;
import cn.auto.core.annotation.Service;
import cn.auto.core.annotation.Task;
import cn.auto.core.data.DataFactory;
import cn.auto.core.testcase.TC;
import cn.auto.core.utils.Consts;
import cn.auto.core.utils.CoreConfig;
import cn.auto.core.webdriver.web.PageError;
import cn.auto.platform.Const;
import cn.auto.platform.MSqlSession;
import cn.auto.platform.dao.ResultMapper;
import cn.auto.platform.dao.SuitCaseMapper;
import cn.auto.platform.dao.SuitMapper;
import cn.auto.platform.model.ExecProgressDTO;
import cn.auto.platform.model.ResultDTO;
import cn.auto.platform.model.SuitCaseDTO;
import cn.auto.platform.model.SuitDTO;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenmeng on 2019/8/16.
 */
public class BaseListener extends TestListenerAdapter implements IInvokedMethodListener2, IClassListener {
    private transient static Logger logger = LoggerFactory.getLogger(BaseListener.class);
    private static Map<String, Object> pages = new HashMap<>();

    private static Map<String, Object> checkers = new HashMap<>();

    private ITestNGMethod[] beforeMethods;
    private ITestNGMethod[] afterMethods;
    private ITestNGMethod[] beforeClassMethods;
    private ITestNGMethod[] afterClassMethods;
    private ITestNGMethod[] testNGMethods;

    private SqlSession sqlSession = MSqlSession.getSqlSession();
    private ResultMapper resultMapper = sqlSession.getMapper(ResultMapper.class);
    private SuitMapper suitMapper = sqlSession.getMapper(SuitMapper.class);
    private SuitCaseMapper suitCaseMapper = sqlSession.getMapper(SuitCaseMapper.class);
    private SuitDTO suitDTO;
    private int suitId;
    private int buildId;
    private List<SuitCaseDTO> relaCases;
    private double classCounter = 0;
    private String currentClass = null;
    private String preClass = null;
    private int success = 0;
    private int fail = 0;
    private int skip = 0;
    private int count = 0;
    private long startTime = 0;


    public void onBeforeClass(ITestClass testClass, IMethodInstance mi) {
        beforeMethods = testClass.getBeforeTestMethods();
        afterMethods = testClass.getAfterTestMethods();
        beforeClassMethods = testClass.getBeforeClassMethods();
        afterClassMethods = testClass.getAfterClassMethods();
        testNGMethods = testClass.getTestMethods();
    }

    public void onAfterClass(ITestClass testClass, IMethodInstance mi) {

    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        // 更新用例执行进度
        updateProgress(tr);
        //记录执行详细结果及截图
        record(tr, shot(tr));
        //控制台日志输出
        log(tr);
        //退出webdriver
        quit(tr);
        super.onTestSuccess(tr);
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        // 更新用例执行进度
        updateProgress(tr);
        //记录执行详细结果及截图
        record(tr, shot(tr));
        //控制台日志输出
        log(tr);
        //退出webdriver
        quit(tr);
        super.onTestFailure(tr);
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        // 更新用例执行进度
        updateProgress(tr);
        //记录执行详细结果及截图
        record(tr, shot(tr));
        //控制台日志输出
        log(tr);
        //退出webdriver
        quit(tr);
        super.onTestSkipped(tr);
    }

    @Override
    public void onTestStart(ITestResult result) {
        //@Test开始执行时再确认一下是否已初始化driver，如果没有，置失败，给出明确原因
        TC testCase = (TC) result.getInstance();
        if (ArrayUtils.isNotEmpty(beforeMethods) && null == testCase.driver()) {
            result.setStatus(ITestResult.FAILURE);
            result.setThrowable(new Exception(testCase.getClass().getName() + "." + beforeMethods[0].getMethodName() + "中没有自主初始化webdriver."));
        }
    }

    /**
     * 用例集执行前先获取到suitId,用例集关联的用例
     *
     * @param context
     */
    @Override
    public void onStart(ITestContext context) {
        super.onStart(context);
        if (!CoreConfig.DEBUG) {
            suitId = Integer.parseInt(System.getProperty("suit.id"));
            buildId = Integer.parseInt(System.getProperty("build.id"));
            startTime = context.getStartDate().getTime();
            suitDTO = suitMapper.selectById(suitId);
            relaCases = suitCaseMapper.getRelaCases(suitId);
        }
    }

    /**
     * 用例集执行完成后，更新执行状态：finished
     *
     * @param testContext
     */
    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
        if (!CoreConfig.DEBUG) {
            long duration = testContext.getEndDate().getTime() - startTime;
            /*用例集当前构建号执行完成*/
            ExecProgressDTO progressDTO = suitMapper.selectExecBySuitIdAndBuildId(suitId, buildId);
            progressDTO.setDuration(duration);
            progressDTO.setProgress(100d);
            progressDTO.setStatus(Const.FINISHED);
            suitMapper.updateStatus(progressDTO);
        }
    }


    /**
     * 所有添加  @Listeners({BaseListener.class}) 注解 并且继承TC的子类的测试类都自动初始化
     * 在测试用例中直接获取Driver即可。
     * 用例中添加了@BeforeMethod的不需要自动初始化
     *
     * @param method
     * @param testResult
     * @param context
     */
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
        TC testCase = (TC) testResult.getInstance();
        WebDriver driver;
        Map xmlParams = context.getCurrentXmlTest().getAllParameters();
        if (ArrayUtils.isEmpty(testNGMethods)) {
            return;
        }
        boolean autoInit = true;
        /*if (ArrayUtils.isNotEmpty(beforeMethods) || ArrayUtils.isNotEmpty(beforeClassMethods)) {
            autoInit = false;
        }*/
        if (ArrayUtils.isNotEmpty(beforeMethods)) {
            autoInit = false;
        }
        try {
            if (autoInit && method.isTestMethod()) {
                /*从@Optianl中获取 udid server等参数 start*/
                Annotation[][] as = method.getTestMethod().getConstructorOrMethod().getMethod().getParameterAnnotations();
                Parameters parameters = method.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(Parameters.class);
                if (parameters != null) {
                    String[] paras = parameters.value();
                    if (as != null && as.length > 0 && paras != null && paras.length > 0) {
                        for (int i = 0; i < paras.length; i++) {
                            if (Consts.UDID.equalsIgnoreCase(paras[i])) {
                                if (as[i][0] instanceof Optional) {
                                    Optional op = (Optional) as[i][0];
                                    if (xmlParams.get(Consts.UDID) == null) {
                                        xmlParams.put(Consts.UDID, op.value());
                                    }
                                }
                            }
                            if (Consts.SERVER.equalsIgnoreCase(paras[i])) {
                                if (as[i][0] instanceof Optional) {
                                    Optional op = (Optional) as[i][0];
                                    if (xmlParams.get(Consts.SERVER) == null) {
                                        xmlParams.put(Consts.SERVER, op.value());
                                    }
                                }
                            }
                            if (Consts.APP_URL.equalsIgnoreCase(paras[i])) {
                                if (as[i][0] instanceof Optional) {
                                    Optional op = (Optional) as[i][0];
                                    if (xmlParams.get(Consts.APP_URL) == null) {
                                        xmlParams.put(Consts.APP_URL, op.value());
                                    }
                                }
                            }
                            if (Consts.APP_PACKAGE.equalsIgnoreCase(paras[i])) {
                                if (as[i][0] instanceof Optional) {
                                    Optional op = (Optional) as[i][0];
                                    if (xmlParams.get(Consts.APP_PACKAGE) == null) {
                                        xmlParams.put(Consts.APP_PACKAGE, op.value());
                                    }
                                }
                            }
                            if (Consts.APP_ACTIVITY.equalsIgnoreCase(paras[i])) {
                                if (as[i][0] instanceof Optional) {
                                    Optional op = (Optional) as[i][0];
                                    if (xmlParams.get(Consts.APP_ACTIVITY) == null) {
                                        xmlParams.put(Consts.APP_ACTIVITY, op.value());
                                    }
                                }
                            }
                            //是否跳过appium初始化，有些手机由于权限问题每次都需要安装appium.apk,可以通过该设置跳过
                            if (Consts.SKIP_INIT.equalsIgnoreCase(paras[i])) {
                                if (as[i][0] instanceof Optional) {
                                    Optional op = (Optional) as[i][0];
                                    if (xmlParams.get(Consts.SKIP_INIT) == null) {
                                        xmlParams.put(Consts.SKIP_INIT, op.value());
                                    }
                                }
                            }

                        }
                    }
                }
                /*从@Optianl中获取 udid server等参数 end*/
                driver = testCase.setUp(xmlParams);
                Field[] fields = testCase.getClass().getDeclaredFields();
                for (Field f : fields) {
                    /*
                     * 初始化Test中注入的page对象
                     * */
                    if (null != f.getAnnotation(Page.class)) {
                        f.setAccessible(true);
                        String pfName = f.getType().getName();
                        if (pages.containsKey(pfName)) {
                            f.set(testCase, pages.get(pfName));
                        } else {
                            Constructor constructor = f.getType().getConstructor(WebDriver.class);
                            Object pagex = constructor.newInstance(driver);
                            pages.put(pfName, pagex);
                            f.set(testCase, pagex);
                        }
                    }
                    /*
                     * 初始化Test中的Service及Service中的Page
                     * */
                    if (null != f.getAnnotation(Service.class)) {
                        f.setAccessible(true);
                        String serviceName = f.getType().getName();
                        if (pages.containsKey(serviceName)) {
                            f.set(testCase, pages.get(serviceName));
                        } else {
                            Constructor constructor = f.getType().getConstructor(WebDriver.class);
                            Object servicex = constructor.newInstance(driver);
                            Field[] fs = servicex.getClass().getDeclaredFields();
                            for (Field fd : fs) {
                                if (null != fd.getAnnotation(Page.class)) {
                                    fd.setAccessible(true);
                                    String fName = fd.getType().getName();
                                    if (pages.containsKey(fName)) {
                                        fd.set(servicex, pages.get(fName));
                                    } else {
                                        Constructor constructor1 = fd.getType().getConstructor(WebDriver.class);
                                        Object pagex1 = constructor1.newInstance(driver);
                                        pages.put(fName, pagex1);
                                        fd.set(servicex, pagex1);
                                    }
                                }
                            }
                            pages.put(serviceName, servicex);
                            f.set(testCase, servicex);
                        }
                    }
                    /**
                     * datachecker 初始化
                     */
                    if (null != f.getAnnotation(Data.class)) {
                        f.setAccessible(true);
                        String checkerName = f.getType().getName();
                        if (checkers.containsKey(checkerName)) {
                            f.set(testCase, checkers.get(checkerName));
                        } else {
                            Constructor checkCons = f.getType().getConstructor();
                            Object checkerObj = checkCons.newInstance();
                            f.set(testCase, checkerObj);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("初始化失败：" + e.getMessage());
            e.printStackTrace();
        }
        /**
         * 如果是@Test方法，则判断是否有@Task注解，有就执行注解中的SQL
         */
        if (method.isTestMethod()) {
            Task t = method.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(Task.class);
            if (t != null) {
                Map<String, String> xmlparas = method.getTestMethod().findMethodParameters(method.getTestMethod().getXmlTest());
                String[] taskName = t.taskName();
                if (taskName != null && taskName.length > 0) {
                    String[] paras = t.paras();
                    Map<String, String> pMap = new HashMap<>();
                    if (paras != null && paras.length > 0) {
                        for (String s : paras) {
                            String[] aa = s.split("=");
                            if (aa != null && aa.length == 2) {
                                String val = aa[1];
                                if (xmlparas.containsKey(val)) {
                                    val = xmlparas.get(val);
                                }
                                pMap.put(aa[0], val);
                            }
                        }
                    }
                    for (String s : taskName) {
                        try {
                            Map result = DataFactory.getData().getTask().doTask(s, pMap);
                            logger.info(String.format("任务：%s执行结果：%s", s, result.get(Consts.FLAG)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 所有添加  @Listeners({BaseListener.class}) 注解 并且继承TC的子类的测试类都自动退出驱动
     * 用例中添加了@AfterMethod的不需要自动退出驱动
     *
     * @param testResult
     */
    private void quit(ITestResult testResult) {
        TC testCase = (TC) testResult.getInstance();
        if (ArrayUtils.isEmpty(testNGMethods)) {
            return;
        }
        //默认需要自动退出
        boolean autoDestroy = true;
        if (ArrayUtils.isNotEmpty(afterMethods) || ArrayUtils.isNotEmpty(afterClassMethods)) {
            autoDestroy = false;
        }
        try {
            if (autoDestroy && null != testCase.driver()) {
                testCase.tearDown();
            }
        } catch (Exception e) {
            logger.error("退出失败：" + e.getMessage());
        }
        pages.clear();
    }

    /**
     * 截图方法，在quit前截图
     *
     * @param tr
     * @return
     */
    private String shot(ITestResult tr) {
        try {
            TC b = (TC) tr.getInstance();
            String insName = tr.getInstanceName();
            if (StringUtils.isNotBlank(insName)) {
                insName = StringUtils.substringAfterLast(insName, ".");
            }
            String filePath = CoreConfig.TEST_SRCSHOT_DIR + insName + "." + tr.getMethod().getMethodName() + "." + System.currentTimeMillis() + ".png";
            if (b.takeScreenShot(filePath)) {
                logger.info("截图：" + filePath);
                return filePath;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 控制台log
     *
     * @param tr
     */
    private void log(ITestResult tr) {
        switch (tr.getStatus()) {
            case 1:
                logger.info(tr.getInstanceName() + "." + tr.getMethod().getMethodName() + ":SUCCESS");
                break;
            case 2:
                logger.info(tr.getInstanceName() + "." + tr.getMethod().getMethodName() + ":FAILURE");
                break;
            case 3:
                logger.info(tr.getInstanceName() + "." + tr.getMethod().getMethodName() + ":SKIPPED");
                break;
        }
    }

    /**
     * 记录用例执行结果，存到at_result表，绑定Jenkins的build_id，可区别不同的构建结果。
     *
     * @param testResult
     */
    public void record(ITestResult testResult, String scrshot) {
        if (!CoreConfig.DEBUG) {
            ResultDTO resultDTO = new ResultDTO();
            String methodDesc = testResult.getMethod().getDescription();
            if (StringUtils.isNotBlank(methodDesc)) {
                methodDesc = methodDesc + "-" + testResult.getMethod().getMethodName();
            } else {
                methodDesc = testResult.getMethod().getMethodName();
            }
            resultDTO.setMethodName(methodDesc);
            resultDTO.setCaseId(testResult.getMethod().getTestClass().getXmlClass().getIndex());
//        int suitId = Integer.parseInt(testResult.getMethod().getTestClass().getXmlTest().getName());
            resultDTO.setSuitId(suitId);
            resultDTO.setVersion(suitDTO.getVersion());
            Object[] dd = testResult.getParameters();
            if (dd != null && dd.length > 0) {
                resultDTO.setDataParams(Arrays.toString(dd));
            }
            switch (testResult.getStatus()) {
                case ITestResult.SUCCESS: {
                    resultDTO.setResultStatus(Const.SUCCESS);
                    break;
                }
                case ITestResult.FAILURE: {
                    resultDTO.setResultStatus(Const.FAIL);
                    break;
                }
                case ITestResult.SKIP: {
                    resultDTO.setResultStatus(Const.SKIP);
                    break;
                }
                case ITestResult.SUCCESS_PERCENTAGE_FAILURE: {
                    resultDTO.setResultStatus(Const.FAIL);
                    break;
                }
            }
            resultDTO = addResult(resultDTO, testResult, scrshot);
            resultDTO.setBuildId(buildId);
            try {
                resultMapper.addResult(resultDTO);
            } catch (Exception e) {
                logger.error("记录执行结果异常：" + e.getMessage());
            }
        }
    }


    private ResultDTO addResult(ResultDTO resultDTO, ITestResult testResult, String scrshot) {
        if (StringUtils.isNotBlank(scrshot)) {
            resultDTO.setScrshot(CoreConfig.SCRSHOT_SHARE_URL + StringUtils.substringAfterLast(scrshot, "/"));
        }
        Throwable throwable = testResult.getThrowable();
        String tmp;
        if (throwable != null) {
            StringBuilder sb = new StringBuilder();
            tmp = testResult.getThrowable().getMessage();
            StackTraceElement[] stacks = testResult.getThrowable().getStackTrace();
            if (stacks.length > 0) {
                for (StackTraceElement element : stacks) {
                    sb.append("at " + element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")" + "\n");
                }
            }
            logger.error(tmp + "\n" + sb.toString());
            if (StringUtils.isNotBlank(sb.toString())) {
                resultDTO.setStack(sb.toString().length() > 5000 ? sb.toString().substring(0, 4999) : sb.toString());
            }
            if (StringUtils.isNotBlank(tmp)) {
                resultDTO.setMessage(tmp.length() > 500 ? tmp.substring(0, 499) : tmp);
            }
            if (throwable.getClass().isAssignableFrom(WebDriverException.class)) {
                resultDTO.setCause(Const.ERROR.WebDriver错误.name());
            } else if (throwable.getClass().isAssignableFrom(TimeoutException.class)) {
                resultDTO.setCause(Const.ERROR.访问超时.name());
            } else if (throwable.getClass().isAssignableFrom(AssertionError.class)) {
                resultDTO.setCause(Const.ERROR.断言错误.name());
            } else if (throwable.getClass().isAssignableFrom(NoSuchElementException.class)) {
                resultDTO.setCause(Const.ERROR.没有找到元素.name());
            } else if (throwable.getClass().isAssignableFrom(TestNGException.class)) {
                resultDTO.setCause(Const.ERROR.TestNG错误.name());
            } else if (throwable.getClass().isAssignableFrom(PageError.class)) {
                resultDTO.setCause(Const.ERROR.页面5XX错误.name());
            } else if (throwable.getClass().isAssignableFrom(Throwable.class)) {
                resultDTO.setCause(Const.ERROR.依赖未执行.name());
            } else if (throwable.getClass().isAssignableFrom(UnhandledAlertException.class)) {
                resultDTO.setCause(Const.ERROR.未捕获Alert.name());
            } else {
                resultDTO.setCause(Const.ERROR.未知错误.name());
            }
        } else {
            resultDTO.setCause(Const.ERROR.未知错误.name());
            resultDTO.setStack("Unknown Error");
        }
        return resultDTO;
    }


    /**
     * 更新用例集执行进度
     *
     * @param tr
     */
    public void updateProgress(ITestResult tr) {
        if (!CoreConfig.DEBUG) {
            currentClass = tr.getMethod().getTestClass().getName();
            if (!currentClass.equals(preClass)) {
                preClass = currentClass;
                ++classCounter;
            }
            ExecProgressDTO progressDTO = new ExecProgressDTO();
            progressDTO.setSuitId(suitId);
            double progress = Double.parseDouble(String.format("%.2f", classCounter / relaCases.size() * 100));
            if (progress == 100 || progress == 100.0) {
                progress = progress - 1;
            }
            progressDTO.setProgress(progress);
            progressDTO.setStatus(Const.EXECUTING);
            switch (tr.getStatus()) {
                case ITestResult.SUCCESS: {
                    ++success;
                    ++count;
                    break;
                }
                case ITestResult.FAILURE: {
                    ++fail;
                    ++count;
                    break;
                }
                case ITestResult.SKIP: {
                    ++skip;
                    ++count;
                    break;
                }
                case ITestResult.SUCCESS_PERCENTAGE_FAILURE: {
                    break;
                }
            }
            progressDTO.setSuccess(success);
            progressDTO.setFail(fail);
            progressDTO.setSkip(skip);
            progressDTO.setCount(count);
            progressDTO.setPassRate(Double.parseDouble(String.format("%.2f", (double) success / count * 100)));
            progressDTO.setDuration(System.currentTimeMillis() - startTime);
            progressDTO.setBuildId(buildId);
            suitMapper.updateProgress(progressDTO);
        }
    }


    public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {

    }

    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

    }

    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {

    }
}
