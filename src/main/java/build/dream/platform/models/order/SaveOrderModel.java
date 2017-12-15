package build.dream.platform.models.order;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.platform.constants.Constants;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class SaveOrderModel extends BasicModel {
    @NotNull
    private Integer orderType;

    @NotNull
    private BigInteger userId;

    private BigInteger tenantId;
    private BigInteger agentId;

    private List<OrderDetailModel> orderDetailModels;
    private List<BigInteger> goodsIds = null;
    private List<BigInteger> goodsSpecificationIds = null;

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public BigInteger getTenantId() {
        return tenantId;
    }

    public void setTenantId(BigInteger tenantId) {
        this.tenantId = tenantId;
    }

    public BigInteger getAgentId() {
        return agentId;
    }

    public void setAgentId(BigInteger agentId) {
        this.agentId = agentId;
    }

    public List<OrderDetailModel> getOrderDetailModels() {
        return orderDetailModels;
    }

    public void setOrderDetailModels(List<OrderDetailModel> orderDetailModels) {
        this.orderDetailModels = orderDetailModels;
        goodsIds = new ArrayList<BigInteger>();
        goodsSpecificationIds = new ArrayList<BigInteger>();
        for (OrderDetailModel orderDetailModel : orderDetailModels) {
            goodsIds.add(orderDetailModel.goodsId);
            goodsSpecificationIds.add(orderDetailModel.goodsSpecificationId);
        }
    }

    public List<BigInteger> getGoodsIds() {
        return goodsIds;
    }

    public List<BigInteger> getGoodsSpecificationIds() {
        return goodsSpecificationIds;
    }

    public static class OrderDetailModel extends BasicModel {
        @NotNull
        private BigInteger goodsId;

        @NotNull
        private BigInteger goodsSpecificationId;

        @NotNull
        private Integer amount;

        private BigInteger branchId;

        public BigInteger getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(BigInteger goodsId) {
            this.goodsId = goodsId;
        }

        public BigInteger getGoodsSpecificationId() {
            return goodsSpecificationId;
        }

        public void setGoodsSpecificationId(BigInteger goodsSpecificationId) {
            this.goodsSpecificationId = goodsSpecificationId;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public BigInteger getBranchId() {
            return branchId;
        }

        public void setBranchId(BigInteger branchId) {
            this.branchId = branchId;
        }

        public void validateAndThrow(Integer orderType) throws NoSuchFieldException {
            super.validateAndThrow();
            if (orderType == Constants.ORDER_TYPE_TENANT_ORDER) {
                ApplicationHandler.notNull(branchId, "orderDetails");
            }
        }
    }

    @Override
    public void validateAndThrow() throws NoSuchFieldException {
        ApplicationHandler.isTrue(CollectionUtils.isNotEmpty(orderDetailModels), "orderDetails");
        for (OrderDetailModel orderDetailModel : orderDetailModels) {
            orderDetailModel.validateAndThrow(orderType);
        }
        ApplicationHandler.isTrue(orderType == Constants.ORDER_TYPE_TENANT_ORDER || orderType == Constants.ORDER_TYPE_AGENT_ORDER, "orderType");
        if (orderType == Constants.ORDER_TYPE_TENANT_ORDER) {
            ApplicationHandler.notNull(tenantId, "tenantId");
        } else if (orderType == Constants.ORDER_TYPE_AGENT_ORDER) {
            ApplicationHandler.notNull(agentId, "agentId");
        }
        super.validateAndThrow();
    }
}
