package build.dream.platform.models.goods;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.platform.constants.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class SaveGoodsModel extends BasicModel {
    private BigInteger id;
    @NotNull
    @Length(max = 20)
    private String name;

    @NotNull
    private BigInteger goodsTypeId;

    @NotNull
    private Integer status;

    @NotNull
    private Integer meteringMode;

    private List<GoodsSpecificationModel> goodsSpecificationModels;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(BigInteger goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMeteringMode() {
        return meteringMode;
    }

    public void setMeteringMode(Integer meteringMode) {
        this.meteringMode = meteringMode;
    }

    public List<GoodsSpecificationModel> getGoodsSpecificationModels() {
        return goodsSpecificationModels;
    }

    public void setGoodsSpecificationModels(List<GoodsSpecificationModel> goodsSpecificationModels) {
        this.goodsSpecificationModels = goodsSpecificationModels;
    }

    public static class GoodsSpecificationModel extends BasicModel {
        private BigInteger id;

        @NotNull
        @Length(max = 20)
        private String name;

        private BigInteger goodsId;

        @NotNull
        private Boolean allowTenantBuy;

        @NotNull
        private Boolean allowAgentBuy;

        @NotNull
        private Integer renewalTime;

        @NotNull
        private BigDecimal tenantPrice;

        @NotNull
        private BigDecimal agentPrice;

        public BigInteger getId() {
            return id;
        }

        public void setId(BigInteger id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigInteger getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(BigInteger goodsId) {
            this.goodsId = goodsId;
        }

        public Boolean getAllowTenantBuy() {
            return allowTenantBuy;
        }

        public void setAllowTenantBuy(Boolean allowTenantBuy) {
            this.allowTenantBuy = allowTenantBuy;
        }

        public boolean isAllowAgentBuy() {
            return allowAgentBuy;
        }

        public void setAllowAgentBuy(boolean allowAgentBuy) {
            this.allowAgentBuy = allowAgentBuy;
        }

        public Integer getRenewalTime() {
            return renewalTime;
        }

        public void setRenewalTime(Integer renewalTime) {
            this.renewalTime = renewalTime;
        }

        public BigDecimal getTenantPrice() {
            return tenantPrice;
        }

        public void setTenantPrice(BigDecimal tenantPrice) {
            this.tenantPrice = tenantPrice;
        }

        public BigDecimal getAgentPrice() {
            return agentPrice;
        }

        public void setAgentPrice(BigDecimal agentPrice) {
            this.agentPrice = agentPrice;
        }
    }

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        ApplicationHandler.isTrue(status == Constants.GOODS_STATUS_NORMAL || status == Constants.GOODS_STATUS_STOP_SALE, "status");
        ApplicationHandler.isTrue(meteringMode == Constants.GOODS_METERING_MODE_BY_TIME || meteringMode == Constants.GOODS_METERING_MODE_BY_QUANTITY, "meteringMode");
        ApplicationHandler.isTrue(CollectionUtils.isNotEmpty(goodsSpecificationModels), "goodsSpecifications");
        for (GoodsSpecificationModel goodsSpecificationModel : goodsSpecificationModels) {
            goodsSpecificationModel.validateAndThrow();
        }
    }
}
