package build.dream.platform.models.order;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;

public class BatchDeleteOrdersModel extends BasicModel {
    @NotNull
    private List<BigInteger> orderInfoIds;
    @NotNull
    private BigInteger userId;

    public List<BigInteger> getOrderInfoIds() {
        return orderInfoIds;
    }

    public void setOrderInfoIds(List<BigInteger> orderInfoIds) {
        this.orderInfoIds = orderInfoIds;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }
}
