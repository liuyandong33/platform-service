package build.dream.platform.models.agent;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class DeleteAgentModel extends BasicModel {
    @NotNull
    private Long agentId;

    @NotNull
    private Long userId;

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
