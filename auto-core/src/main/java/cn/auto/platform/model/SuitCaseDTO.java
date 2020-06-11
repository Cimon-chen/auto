package cn.auto.platform.model;

/**
 * Created by chenmeng on 2017/4/12.
 */
public class SuitCaseDTO {
    private Integer suitId;
    private String suitName;
    private Integer caseId;
    private String caseName;
    private String caseStatus;
    private String caseType;
    private String classs;
    private String include;
    private String exclude;
    private Integer apiId;
    private String req;
    private String res;
    private String rule;
    private String executor;
    private Integer taskEnv;
    private Integer envId;
    private Integer preTask;
    private Integer postTask;

    public Integer getPreTask() {
        return preTask;
    }

    public void setPreTask(Integer preTask) {
        this.preTask = preTask;
    }

    public Integer getPostTask() {
        return postTask;
    }

    public void setPostTask(Integer postTask) {
        this.postTask = postTask;
    }

    public Integer getEnvId() {
        return envId;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public Integer getTaskEnv() {
        return taskEnv;
    }

    public void setTaskEnv(Integer taskEnv) {
        this.taskEnv = taskEnv;
    }

    public void setEnvId(Integer envId) {
        this.envId = envId;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public void setClasss(String classs) {
        this.classs = classs;
    }

    public void setInclude(String include) {
        this.include = include;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }


    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getCaseStatus() {

        return caseStatus;
    }

    public String getCaseType() {
        return caseType;
    }

    public String getClasss() {
        return classs;
    }

    public String getInclude() {
        return include;
    }

    public String getExclude() {
        return exclude;
    }


    public String getParameters() {
        return parameters;
    }

    private String parameters;
    private Integer relaId;

    public void setSuitId(Integer suitId) {
        this.suitId = suitId;
    }

    public void setSuitName(String suitName) {
        this.suitName = suitName;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public void setRelaId(Integer relaId) {
        this.relaId = relaId;
    }

    public Integer getSuitId() {

        return suitId;
    }

    public String getSuitName() {
        return suitName;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public String getCaseName() {
        return caseName;
    }

    public Integer getRelaId() {
        return relaId;
    }
}
