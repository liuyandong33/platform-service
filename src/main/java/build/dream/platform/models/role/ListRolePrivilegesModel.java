package build.dream.platform.models.role;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.platform.constants.Constants;
import org.apache.commons.lang.ArrayUtils;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class ListRolePrivilegesModel extends BasicModel {
    private static final String[] TYPES = {Constants.PRIVILEGE_TYPE_BACKGROUND, Constants.PRIVILEGE_TYPE_APP, Constants.PRIVILEGE_TYPE_POS};
    @NotNull
    private BigInteger roleId;

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

    @Override
    public boolean validate() {
        return super.validate() && ArrayUtils.contains(TYPES, type);
    }

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        ApplicationHandler.inArray(TYPES, type, "type");
    }
}
