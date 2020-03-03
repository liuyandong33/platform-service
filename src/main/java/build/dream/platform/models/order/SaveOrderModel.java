package build.dream.platform.models.order;

import build.dream.common.constraints.VerifyJsonSchema;
import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.platform.constants.Constants;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class SaveOrderModel extends BasicModel {
    private static final Integer[] ORDER_TYPES = {Constants.ORDER_TYPE_TENANT_ORDER, Constants.ORDER_TYPE_AGENT_ORDER};
    @NotNull
    private Integer orderType;

    @NotNull
    private Long userId;

    private Long tenantId;
    private Long agentId;

    @NotEmpty
    @VerifyJsonSchema(value = Constants.GOODS_INFOS_SCHEMA_FILE_PATH)
    private List<GoodsInfo> goodsInfos;

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public List<GoodsInfo> getGoodsInfos() {
        return goodsInfos;
    }

    public void setGoodsInfos(List<GoodsInfo> goodsInfos) {
        this.goodsInfos = goodsInfos;
    }

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        ApplicationHandler.inArray(ORDER_TYPES, orderType, "orderType");
        if (orderType == Constants.ORDER_TYPE_TENANT_ORDER) {
            ApplicationHandler.notNull(tenantId, "tenantId");
            for (GoodsInfo goodsInfo : goodsInfos) {
                ApplicationHandler.notNull(goodsInfo.getBranchId(), "goodsInfos");
            }
        }
        if (orderType == Constants.ORDER_TYPE_AGENT_ORDER) {
            ApplicationHandler.notNull(agentId, "agentId");
        }
    }

    public static class GoodsInfo {
        private Long goodsId;
        private Long goodsSpecificationId;
        private Integer quantity;
        private Long branchId;

        public Long getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(Long goodsId) {
            this.goodsId = goodsId;
        }

        public Long getGoodsSpecificationId() {
            return goodsSpecificationId;
        }

        public void setGoodsSpecificationId(Long goodsSpecificationId) {
            this.goodsSpecificationId = goodsSpecificationId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Long getBranchId() {
            return branchId;
        }

        public void setBranchId(Long branchId) {
            this.branchId = branchId;
        }
    }
}
