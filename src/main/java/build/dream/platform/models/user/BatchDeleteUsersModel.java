package build.dream.platform.models.user;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.util.List;

public class BatchDeleteUsersModel extends BasicModel {
    @NotNull
    private Long userId;

    @NotNull
    private List<Long> userIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
