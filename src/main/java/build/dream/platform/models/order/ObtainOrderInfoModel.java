package build.dream.platform.models.order;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ObtainOrderInfoModel extends BasicModel {
    @NotNull
    private Long orderInfoId;

    public Long getOrderInfoId() {
        return orderInfoId;
    }

    public void setOrderInfoId(Long orderInfoId) {
        this.orderInfoId = orderInfoId;
    }
}
