package build.dream.platform.models.weixin;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ObtainWeiXinMiniProgramsModel extends BasicModel {
    @NotNull
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
