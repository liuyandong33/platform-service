package build.dream.platform.models.role;

import build.dream.common.constraints.InList;
import build.dream.common.models.BasicModel;
import build.dream.platform.constants.Constants;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class ListRolesModel extends BasicModel {
    @NotNull
    private BigInteger tenantId;
    @NotNull
    private Integer page;
    @NotNull
    private Integer rows;
    @NotNull
    @InList(value = {Constants.PRIVILEGE_TYPE_BACKGROUND, Constants.PRIVILEGE_TYPE_APP, Constants.PRIVILEGE_TYPE_POS})
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
