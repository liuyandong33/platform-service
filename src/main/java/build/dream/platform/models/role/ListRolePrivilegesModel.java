package build.dream.platform.models.role;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class ListRolePrivilegesModel extends BasicModel {
    @NotNull
    private BigInteger roleId;

    public BigInteger getRoleId() {
        return roleId;
    }

    public void setRoleId(BigInteger roleId) {
        this.roleId = roleId;
    }
}
