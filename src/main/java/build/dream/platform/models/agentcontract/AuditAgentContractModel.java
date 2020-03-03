package build.dream.platform.models.agentcontract;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class AuditAgentContractModel extends BasicModel {
    @NotNull
    private Long agentContractId;
    @NotNull
    private Long agentId;
    @NotNull
    private Long userId;

    public Long getAgentContractId() {
        return agentContractId;
    }

    public void setAgentContractId(Long agentContractId) {
        this.agentContractId = agentContractId;
    }

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
