package build.dream.platform.models.user;

import build.dream.common.constraints.InList;
import build.dream.common.models.BasicModel;
import build.dream.platform.constants.Constants;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class ObtainAllPrivilegesModel extends BasicModel {
    @NotNull
    private BigInteger userId;
    @NotNull
    @InList(value = {Constants.PRIVILEGE_TYPE_BACKGROUND, Constants.PRIVILEGE_TYPE_APP, Constants.PRIVILEGE_TYPE_POS})
    private String type;

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
