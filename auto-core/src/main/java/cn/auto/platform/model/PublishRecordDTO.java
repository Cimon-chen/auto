package cn.auto.platform.model;

/**
 * Created by chenmeng on 2017/6/14.
 */
public class PublishRecordDTO {
    private Integer pubId;
    private String pubName;
    private Integer jiraVersionId;
    private String testVersion;
    private String assignee;
    private String status;
    private String pubResult;
    private Integer pubCount;
    private String remark;
    private String relaSuit;
    private String isExec;
    private String testResult;
    private String hosts;
    private String createTime;
    private String updateTime;
    private Integer ftmTaskId;

    public Integer getPubId() {
        return pubId;
    }

    public void setPubId(Integer pubId) {
        this.pubId = pubId;
    }

    public String getPubName() {
        return pubName;
    }

    public void setPubName(String pubName) {
        this.pubName = pubName;
    }

    public Integer getJiraVersionId() {
        return jiraVersionId;
    }

    public void setJiraVersionId(Integer jiraVersionId) {
        this.jiraVersionId = jiraVersionId;
    }

    public String getTestVersion() {
        return testVersion;
    }

    public void setTestVersion(String testVersion) {
        this.testVersion = testVersion;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPubResult() {
        return pubResult;
    }

    public void setPubResult(String pubResult) {
        this.pubResult = pubResult;
    }

    public Integer getPubCount() {
        return pubCount;
    }

    public void setPubCount(Integer pubCount) {
        this.pubCount = pubCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRelaSuit() {
        return relaSuit;
    }

    public void setRelaSuit(String relaSuit) {
        this.relaSuit = relaSuit;
    }

    public String getIsExec() {
        return isExec;
    }

    public void setIsExec(String isExec) {
        this.isExec = isExec;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getFtmTaskId() {
        return ftmTaskId;
    }

    public void setFtmTaskId(Integer ftmTaskId) {
        this.ftmTaskId = ftmTaskId;
    }
}
