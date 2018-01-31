package build.dream.platform.models.order;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class DeleteOrderModel extends BasicModel {
    @NotNull
    private BigInteger orderInfoId;
    @NotNull
    private BigInteger userId;

    public BigInteger getOrderInfoId() {
        return orderInfoId;
    }

    public void setOrderInfoId(BigInteger orderInfoId) {
        this.orderInfoId = orderInfoId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }
}
