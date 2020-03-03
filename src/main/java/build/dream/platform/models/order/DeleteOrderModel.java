package build.dream.platform.models.order;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class DeleteOrderModel extends BasicModel {
    @NotNull
    private Long orderInfoId;
    @NotNull
    private Long userId;

    public Long getOrderInfoId() {
        return orderInfoId;
    }

    public void setOrderInfoId(Long orderInfoId) {
        this.orderInfoId = orderInfoId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
