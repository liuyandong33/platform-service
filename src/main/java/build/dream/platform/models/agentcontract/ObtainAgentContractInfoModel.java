package build.dream.platform.models.agentcontract;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ObtainAgentContractInfoModel extends BasicModel {
    @NotNull
    private Long agentId;
    @NotNull
    private Long agentContractId;

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getAgentContractId() {
        return agentContractId;
    }

    public void setAgentContractId(Long agentContractId) {
        this.agentContractId = agentContractId;
    }
}
