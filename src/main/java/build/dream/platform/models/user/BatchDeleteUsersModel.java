package build.dream.platform.models.user;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;

public class BatchDeleteUsersModel extends BasicModel {
    @NotNull
    private BigInteger userId;

    @NotNull
    private List<BigInteger> userIds;

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public List<BigInteger> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<BigInteger> userIds) {
        this.userIds = userIds;
    }
}
