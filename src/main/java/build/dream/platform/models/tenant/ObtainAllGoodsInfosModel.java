package build.dream.platform.models.tenant;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ObtainAllGoodsInfosModel extends BasicModel {
    @NotNull
    private Long tenantId;

    @NotNull
    private Long branchId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }
}
