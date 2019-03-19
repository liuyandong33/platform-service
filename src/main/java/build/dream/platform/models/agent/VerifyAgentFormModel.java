package build.dream.platform.models.agent;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class VerifyAgentFormModel extends BasicModel {
    @NotNull
    private BigInteger agentFormId;

    @NotNull
    private BigInteger userId;

    @NotNull
    private Integer status;

    public BigInteger getAgentFormId() {
        return agentFormId;
    }

    public void setAgentFormId(BigInteger agentFormId) {
        this.agentFormId = agentFormId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
