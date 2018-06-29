package build.dream.platform.models.order;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class ObtainOrderInfoModel extends BasicModel {
    @NotNull
    private BigInteger orderInfoId;

    public BigInteger getOrderInfoId() {
        return orderInfoId;
    }

    public void setOrderInfoId(BigInteger orderInfoId) {
        this.orderInfoId = orderInfoId;
    }
}
