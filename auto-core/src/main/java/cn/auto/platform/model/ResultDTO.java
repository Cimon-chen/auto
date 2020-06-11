package cn.auto.platform.model;

/**
 * Created by chenmeng on 2017/4/19.
 */
public class ResultDTO {
    private Integer resultId;
    private String methodName;
    private Integer caseId;
    private Integer suitId;
    private String resultStatus;
    private String message;
    private String dataParams;
    private String cause;
    private String stack;
    private String scrshot;
    private Integer buildId;
    private Integer execId;

    public String getStack() {
        return stack;
    }

    public String getDataParams() {
        return dataParams;
    }

    public void setDataParams(String dataParams) {
        this.dataParams = dataParams;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public Integer getExecId() {
        return execId;
    }

    public void setExecId(Integer execId) {
        this.execId = execId;
    }

    private String version;

    public Integer getResultId() {
        return resultId;
    }

    public String getMethodName() {
        return methodName;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public Integer getSuitId() {
        return suitId;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public String getMessage() {
        return message;
    }

    public String getCause() {
        return cause;
    }

    public String getScrshot() {
        return scrshot;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public String getVersion() {
        return version;
    }

    public void setResultId(Integer resultId) {

        this.resultId = resultId;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public void setSuitId(Integer suitId) {
        this.suitId = suitId;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public void setScrshot(String scrshot) {
        this.scrshot = scrshot;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
