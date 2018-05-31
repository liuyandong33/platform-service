package build.dream.platform.models.user;

import build.dream.common.constraints.InList;
import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ObtainUserInfoModel extends BasicModel {
    @NotNull
    @InList(value = {"AA", "BB"})
    private String loginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
