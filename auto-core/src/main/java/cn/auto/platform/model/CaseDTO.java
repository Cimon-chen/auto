package cn.auto.platform.model;

/**
 * Created by chenmeng on 2017/3/21.
 */
public class CaseDTO {
    private int caseId;
    private String caseName;
    private String remark;
    private String caseStatus;
    private String caseType;
    private String severity;
    private String testPeriod;
    private String clazz;
    private Integer projectId;
    private String parameters;

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public void setTestPeriod(String testPeriod) {
        this.testPeriod = testPeriod;
    }

    public int getCaseId() {

        return caseId;
    }

    public String getCaseName() {
        return caseName;
    }

    public String getRemark() {
        return remark;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public String getCaseType() {
        return caseType;
    }

    public String getSeverity() {
        return severity;
    }

    public String getTestPeriod() {
        return testPeriod;
    }
}
