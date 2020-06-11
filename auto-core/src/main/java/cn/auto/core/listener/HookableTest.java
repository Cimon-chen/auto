package cn.auto.core.listener;

import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;

/**
 * Created by chenmeng on 2019/8/29.
 */
public class HookableTest implements IHookable{
    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        if (auth()){
            System.out.println("Not Authed");
            return;
        }
        callBack.runTestMethod(testResult);
    }

    private boolean auth(){
        return true;
    }
}
