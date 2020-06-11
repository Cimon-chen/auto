package cn.auto.platform.model;

/**
 * Created by chenmeng on 2017/3/21.
 */
public class SuitDTO {
    private Integer suitId;
    private String suitName;
    private String suitStatus;
    private String suitType;
    private String remark;
    private String listeners;
    private String parameters;
    private Integer envId;
    private String envName;
    private String execType;
    private String execExpr;
    private String testPeriod;
    private String version;
    private String executor;
    private Integer opId;
    private String createTime;

    public String getSuitType() {
        return suitType;
    }

    public void setSuitType(String suitType) {
        this.suitType = suitType;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public Integer getOpId() {
        return opId;
    }

    public void setOpId(Integer opId) {
        this.opId = opId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setListeners(String listeners) {
        this.listeners = listeners;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getListeners() {

        return listeners;
    }

    public String getParameters() {
        return parameters;
    }


    public void setSuitId(Integer suitId) {
        this.suitId = suitId;
    }

    public void setSuitName(String suitName) {
        this.suitName = suitName;
    }

    public void setSuitStatus(String suitStatus) {
        this.suitStatus = suitStatus;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setEnvId(Integer envId) {
        this.envId = envId;
    }

    public void setExecType(String execType) {
        this.execType = execType;
    }

    public void setExecExpr(String execExpr) {
        this.execExpr = execExpr;
    }

    public void setTestPeriod(String testPeriod) {
        this.testPeriod = testPeriod;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public Integer getSuitId() {

        return suitId;
    }

    public String getSuitName() {
        return suitName;
    }

    public String getSuitStatus() {
        return suitStatus;
    }

    public String getRemark() {
        return remark;
    }

    public Integer getEnvId() {
        return envId;
    }

    public String getExecType() {
        return execType;
    }

    public String getExecExpr() {
        return execExpr;
    }

    public String getTestPeriod() {
        return testPeriod;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "SuitDTO{" +
                "suitId=" + suitId +
                ", suitName='" + suitName + '\'' +
                ", suitStatus='" + suitStatus + '\'' +
                ", remark='" + remark + '\'' +
                ", listeners='" + listeners + '\'' +
                ", parameters='" + parameters + '\'' +
                ", envId=" + envId +
                ", execType='" + execType + '\'' +
                ", execExpr='" + execExpr + '\'' +
                ", testPeriod='" + testPeriod + '\'' +
                ", version='" + version + '\'' +
                ", executor='" + executor + '\'' +
                '}';
    }

    public String getExecutor() {
        return executor;
    }
}
