package build.dream.platform.models.role;

import build.dream.common.constraints.InList;
import build.dream.common.models.BasicModel;
import build.dream.platform.constants.Constants;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;

public class SaveRolePrivilegesModel extends BasicModel {
    @NotNull
    private BigInteger roleId;
    @NotNull
    private List<BigInteger> privilegeIds;
    @NotNull
    @InList(value = {Constants.PRIVILEGE_TYPE_BACKGROUND, Constants.PRIVILEGE_TYPE_APP, Constants.PRIVILEGE_TYPE_POS})
    private String type;

    public BigInteger getRoleId() {
        return roleId;
    }

    public void setRoleId(BigInteger roleId) {
        this.roleId = roleId;
    }

    public List<BigInteger> getPrivilegeIds() {
        return privilegeIds;
    }

    public void setPrivilegeIds(List<BigInteger> privilegeIds) {
        this.privilegeIds = privilegeIds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
