package build.dream.platform.models.user;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ObtainUserInfoModel extends BasicModel {
    @NotNull
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
