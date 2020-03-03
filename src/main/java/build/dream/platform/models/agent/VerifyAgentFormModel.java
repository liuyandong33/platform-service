package build.dream.platform.models.agent;

import build.dream.common.models.BasicModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class VerifyAgentFormModel extends BasicModel {
    @NotNull
    private Long agentFormId;

    @NotNull
    private Long userId;

    @NotNull
    private Integer status;

    @Length(max = 255)
    private String rejectReason;

    public Long getAgentFormId() {
        return agentFormId;
    }

    public void setAgentFormId(Long agentFormId) {
        this.agentFormId = agentFormId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
