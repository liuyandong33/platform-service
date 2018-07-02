package build.dream.platform.models.activity;

import build.dream.common.annotations.JsonSchema;
import build.dream.common.models.BasicModel;
import build.dream.platform.constants.Constants;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class SaveSpecialGoodsActivityModel extends BasicModel {
    @NotNull
    @Length(max = 20)
    private String name;

    @NotNull
    @Length(min = 10, max = 10)
    private String startTime;

    @NotNull
    @Length(min = 10, max = 10)
    private String endTime;

    @NotNull
    private BigInteger userId;

    @JsonSchema(value = Constants.SPECIAL_GOODS_ACTIVITY_INFOS_SCHEMA_FILE_PATH)
    private List<SpecialGoodsActivityInfo> specialGoodsActivityInfos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public List<SpecialGoodsActivityInfo> getSpecialGoodsActivityInfos() {
        return specialGoodsActivityInfos;
    }

    public void setSpecialGoodsActivityInfos(List<SpecialGoodsActivityInfo> specialGoodsActivityInfos) {
        this.specialGoodsActivityInfos = specialGoodsActivityInfos;
    }

    public static class SpecialGoodsActivityInfo {
        private BigInteger goodsId;
        private BigInteger goodsSpecificationId;
        private Integer discountType;
        private BigDecimal tenantSpecialPrice;
        private BigDecimal agentSpecialPrice;
        private BigDecimal tenantDiscountRate;
        private BigDecimal agentDiscountRate;

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

        public Integer getDiscountType() {
            return discountType;
        }

        public void setDiscountType(Integer discountType) {
            this.discountType = discountType;
        }

        public BigDecimal getTenantSpecialPrice() {
            return tenantSpecialPrice;
        }

        public void setTenantSpecialPrice(BigDecimal tenantSpecialPrice) {
            this.tenantSpecialPrice = tenantSpecialPrice;
        }

        public BigDecimal getAgentSpecialPrice() {
            return agentSpecialPrice;
        }

        public void setAgentSpecialPrice(BigDecimal agentSpecialPrice) {
            this.agentSpecialPrice = agentSpecialPrice;
        }

        public BigDecimal getTenantDiscountRate() {
            return tenantDiscountRate;
        }

        public void setTenantDiscountRate(BigDecimal tenantDiscountRate) {
            this.tenantDiscountRate = tenantDiscountRate;
        }

        public BigDecimal getAgentDiscountRate() {
            return agentDiscountRate;
        }

        public void setAgentDiscountRate(BigDecimal agentDiscountRate) {
            this.agentDiscountRate = agentDiscountRate;
        }
    }
}
