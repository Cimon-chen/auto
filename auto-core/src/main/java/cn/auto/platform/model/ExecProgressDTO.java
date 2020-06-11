package cn.auto.platform.model;

/**
 * Created by chenmeng on 2017/4/19.
 */
public class ExecProgressDTO {
    private Integer execId;
    private Integer suitId;
    private String status;
    private Double progress;
    private Integer buildId;
    private String executor;
    private Integer success;
    private Integer fail;
    private Integer skip;
    private Integer count;
    private Double passRate;
    private Long duration;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFail() {
        return fail;
    }

    public void setFail(Integer fail) {
        this.fail = fail;
    }

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getPassRate() {
        return passRate;
    }

    public void setPassRate(Double passRate) {
        this.passRate = passRate;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public Double getProgress() {

        return progress;
    }

    public Integer getExecId() {
        return execId;
    }

    public Integer getSuitId() {
        return suitId;
    }

    public String getStatus() {
        return status;
    }


    public void setExecId(Integer execId) {

        this.execId = execId;
    }

    public void setSuitId(Integer suitId) {
        this.suitId = suitId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
