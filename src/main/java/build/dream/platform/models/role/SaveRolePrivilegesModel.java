package build.dream.platform.models.role;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.util.List;

public class SaveRolePrivilegesModel extends BasicModel {
    @NotNull
    private Long roleId;

    @NotNull
    private List<Long> appPrivilegeIds;

    @NotNull
    private List<Long> posPrivilegeIds;

    @NotNull
    private List<Long> backgroundPrivilegeIds;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getAppPrivilegeIds() {
        return appPrivilegeIds;
    }

    public void setAppPrivilegeIds(List<Long> appPrivilegeIds) {
        this.appPrivilegeIds = appPrivilegeIds;
    }

    public List<Long> getPosPrivilegeIds() {
        return posPrivilegeIds;
    }

    public void setPosPrivilegeIds(List<Long> posPrivilegeIds) {
        this.posPrivilegeIds = posPrivilegeIds;
    }

    public List<Long> getBackgroundPrivilegeIds() {
        return backgroundPrivilegeIds;
    }

    public void setBackgroundPrivilegeIds(List<Long> backgroundPrivilegeIds) {
        this.backgroundPrivilegeIds = backgroundPrivilegeIds;
    }
}
