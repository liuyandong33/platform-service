package build.dream.platform.models.role;

import build.dream.common.constraints.InList;
import build.dream.common.models.BasicModel;
import build.dream.platform.constants.Constants;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class ListRolePrivilegesModel extends BasicModel {
    @NotNull
    private BigInteger roleId;
    @InList(value = {Constants.ROLE_TYPE_BACKGROUND, Constants.ROLE_TYPE_APP, Constants.ROLE_TYPE_POS})
    private String type;

    public BigInteger getRoleId() {
        return roleId;
    }

    public void setRoleId(BigInteger roleId) {
        this.roleId = roleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
