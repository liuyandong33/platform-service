package build.dream.platform.models.tenant;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class UpdateBranchCountModel extends BasicModel {
    @NotNull
    private Long tenantId;

    @NotNull
    private Integer changeCount;

    @NotNull
    private Integer type;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getChangeCount() {
        return changeCount;
    }

    public void setChangeCount(Integer changeCount) {
        this.changeCount = changeCount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
