package build.dream.platform.models.eleme;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class VerifyTokenModel extends BasicModel {
    @NotNull
    private Long tenantId;

    @NotNull
    private Long branchId;

    @NotNull
    private Long userId;

    @NotNull
    private Integer elemeAccountType;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getElemeAccountType() {
        return elemeAccountType;
    }

    public void setElemeAccountType(Integer elemeAccountType) {
        this.elemeAccountType = elemeAccountType;
    }
}
