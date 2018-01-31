package build.dream.platform.models.order;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.platform.constants.Constants;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;

public class SaveOrderModel extends BasicModel {
    @NotNull
    private Integer orderType;

    @NotNull
    private BigInteger userId;

    private BigInteger tenantId;
    private BigInteger agentId;

    private List<GoodsInfo> goodsInfos;

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

    public List<GoodsInfo> getGoodsInfos() {
        return goodsInfos;
    }

    public void setGoodsInfos(List<GoodsInfo> goodsInfos) {
        this.goodsInfos = goodsInfos;
    }

    public void setGoodsInfos(String goodsInfos) {
        ApplicationHandler.validateJson(goodsInfos, Constants.GOODS_INFOS_SCHEMA_FILE_PATH, "goodsInfos");
        this.goodsInfos = GsonUtils.jsonToList(goodsInfos, GoodsInfo.class);
    }

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        ApplicationHandler.inArray(new Integer[]{Constants.ORDER_TYPE_TENANT_ORDER, Constants.ORDER_TYPE_AGENT_ORDER}, orderType, "orderType");
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
        private BigInteger goodsId;
        private BigInteger goodsSpecificationId;
        private Integer quantity;
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

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public BigInteger getBranchId() {
            return branchId;
        }

        public void setBranchId(BigInteger branchId) {
            this.branchId = branchId;
        }
    }
}
