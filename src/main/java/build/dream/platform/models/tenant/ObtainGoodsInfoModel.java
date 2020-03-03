package build.dream.platform.models.tenant;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ObtainGoodsInfoModel extends BasicModel {
    @NotNull
    private Long tenantId;

    @NotNull
    private Long branchId;

    @NotNull
    private Long goodsId;

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

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
