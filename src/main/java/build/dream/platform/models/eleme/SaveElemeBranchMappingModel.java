package build.dream.platform.models.eleme;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class SaveElemeBranchMappingModel extends BasicModel {
    @NotNull
    private Long tenantId;
    @NotNull
    private Long branchId;
    @NotNull
    private Long shopId;
    @NotNull
    private Long userId;

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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
