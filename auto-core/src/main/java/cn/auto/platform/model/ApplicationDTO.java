package cn.auto.platform.model;

/**
 * Created by chenmeng on 2017/6/14.
 */
public class ApplicationDTO {
    private Integer relaId;
    private Integer publishId;
    private String isTrunk;
    private Integer svnNo;
    private String host;
    private String svnPath;
    private String datasource;
    private String createTime;
    private Integer appId;
    private String appName;
    private Double coverage;
    private Integer coBuildId;

    public Integer getCoBuildId() {
        return coBuildId;
    }

    public void setCoBuildId(Integer coBuildId) {
        this.coBuildId = coBuildId;
    }

    public Integer getRelaId() {
        return relaId;
    }

    public void setRelaId(Integer relaId) {
        this.relaId = relaId;
    }

    public Integer getPublishId() {
        return publishId;
    }

    public void setPublishId(Integer publishId) {
        this.publishId = publishId;
    }

    public String getIsTrunk() {
        return isTrunk;
    }

    public void setIsTrunk(String isTrunk) {
        this.isTrunk = isTrunk;
    }

    public Integer getSvnNo() {
        return svnNo;
    }

    public void setSvnNo(Integer svnNo) {
        this.svnNo = svnNo;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSvnPath() {
        return svnPath;
    }

    public void setSvnPath(String svnPath) {
        this.svnPath = svnPath;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Double getCoverage() {
        return coverage;
    }

    public void setCoverage(Double coverage) {
        this.coverage = coverage;
    }
}
