package build.dream.platform.models.role;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.platform.constants.Constants;
import org.apache.commons.lang.ArrayUtils;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;

public class SaveRolePrivilegesModel extends BasicModel {
    @NotNull
    private BigInteger roleId;

    @NotNull
    private List<BigInteger> appPrivilegeIds;

    @NotNull
    private List<BigInteger> posPrivilegeIds;

    @NotNull
    private List<BigInteger> backgroundPrivilegeIds;

    public BigInteger getRoleId() {
        return roleId;
    }

    public void setRoleId(BigInteger roleId) {
        this.roleId = roleId;
    }

    public List<BigInteger> getAppPrivilegeIds() {
        return appPrivilegeIds;
    }

    public void setAppPrivilegeIds(List<BigInteger> appPrivilegeIds) {
        this.appPrivilegeIds = appPrivilegeIds;
    }

    public List<BigInteger> getPosPrivilegeIds() {
        return posPrivilegeIds;
    }

    public void setPosPrivilegeIds(List<BigInteger> posPrivilegeIds) {
        this.posPrivilegeIds = posPrivilegeIds;
    }

    public List<BigInteger> getBackgroundPrivilegeIds() {
        return backgroundPrivilegeIds;
    }

    public void setBackgroundPrivilegeIds(List<BigInteger> backgroundPrivilegeIds) {
        this.backgroundPrivilegeIds = backgroundPrivilegeIds;
    }
}
