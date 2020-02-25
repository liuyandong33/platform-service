package build.dream.platform.models.role;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class ListRolesModel extends BasicModel {
    @NotNull
    private BigInteger tenantId;

    @NotNull
    @Min(value = 1)
    private Integer page;

    @NotNull
    @Min(value = 1)
    @Max(value = 500)
    private Integer rows;

    public BigInteger getTenantId() {
        return tenantId;
    }

    public void setTenantId(BigInteger tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
