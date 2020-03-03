package build.dream.platform.models.activity;

import build.dream.common.constraints.VerifyJsonSchema;
import build.dream.common.models.BasicModel;
import build.dream.platform.constants.Constants;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
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
    private Long userId;

    @VerifyJsonSchema(value = Constants.SPECIAL_GOODS_ACTIVITY_INFOS_SCHEMA_FILE_PATH)
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<SpecialGoodsActivityInfo> getSpecialGoodsActivityInfos() {
        return specialGoodsActivityInfos;
    }

    public void setSpecialGoodsActivityInfos(List<SpecialGoodsActivityInfo> specialGoodsActivityInfos) {
        this.specialGoodsActivityInfos = specialGoodsActivityInfos;
    }

    public static class SpecialGoodsActivityInfo {
        private Long goodsId;
        private Long goodsSpecificationId;
        private Integer discountType;
        private Double tenantSpecialPrice;
        private Double agentSpecialPrice;
        private Double tenantDiscountRate;
        private Double agentDiscountRate;

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

        public Integer getDiscountType() {
            return discountType;
        }

        public void setDiscountType(Integer discountType) {
            this.discountType = discountType;
        }

        public Double getTenantSpecialPrice() {
            return tenantSpecialPrice;
        }

        public void setTenantSpecialPrice(Double tenantSpecialPrice) {
            this.tenantSpecialPrice = tenantSpecialPrice;
        }

        public Double getAgentSpecialPrice() {
            return agentSpecialPrice;
        }

        public void setAgentSpecialPrice(Double agentSpecialPrice) {
            this.agentSpecialPrice = agentSpecialPrice;
        }

        public Double getTenantDiscountRate() {
            return tenantDiscountRate;
        }

        public void setTenantDiscountRate(Double tenantDiscountRate) {
            this.tenantDiscountRate = tenantDiscountRate;
        }

        public Double getAgentDiscountRate() {
            return agentDiscountRate;
        }

        public void setAgentDiscountRate(Double agentDiscountRate) {
            this.agentDiscountRate = agentDiscountRate;
        }
    }
}
