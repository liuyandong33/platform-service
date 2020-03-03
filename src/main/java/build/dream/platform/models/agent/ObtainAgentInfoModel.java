package build.dream.platform.models.agent;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ValidateUtils;
import org.apache.commons.lang.StringUtils;

public class ObtainAgentInfoModel extends BasicModel {
    private Long agentId;

    private String agentCode;

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    @Override
    public boolean validate() {
        return super.validate() && (agentId != null || StringUtils.isNotBlank(agentCode));
    }

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        ValidateUtils.isTrue(agentId != null || StringUtils.isNotBlank(agentCode), "参数agentId和agentCode不能同时为空！");
    }
}
