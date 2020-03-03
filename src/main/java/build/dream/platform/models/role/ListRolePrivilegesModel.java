package build.dream.platform.models.role;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ListRolePrivilegesModel extends BasicModel {
    @NotNull
    private Long roleId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
