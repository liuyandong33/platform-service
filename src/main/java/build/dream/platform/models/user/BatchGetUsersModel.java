package build.dream.platform.models.user;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;

public class BatchGetUsersModel extends BasicModel {
    @NotNull
    private List<BigInteger> userIds;

    public List<BigInteger> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<BigInteger> userIds) {
        this.userIds = userIds;
    }
}
