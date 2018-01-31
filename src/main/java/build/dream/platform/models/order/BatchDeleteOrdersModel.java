package build.dream.platform.models.order;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;

public class BatchDeleteOrdersModel extends BasicModel {
    @NotNull
    private List<BigInteger> orderIds;
    @NotNull
    private BigInteger userId;

    public List<BigInteger> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<BigInteger> orderIds) {
        this.orderIds = orderIds;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }
}
