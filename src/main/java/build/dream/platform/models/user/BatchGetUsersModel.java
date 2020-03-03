package build.dream.platform.models.user;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.util.List;

public class BatchGetUsersModel extends BasicModel {
    @NotNull
    private List<Long> userIds;

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
