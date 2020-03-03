package build.dream.platform.models.order;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.util.List;

public class BatchDeleteOrdersModel extends BasicModel {
    @NotNull
    private List<Long> orderInfoIds;
    @NotNull
    private Long userId;

    public List<Long> getOrderInfoIds() {
        return orderInfoIds;
    }

    public void setOrderInfoIds(List<Long> orderInfoIds) {
        this.orderInfoIds = orderInfoIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
