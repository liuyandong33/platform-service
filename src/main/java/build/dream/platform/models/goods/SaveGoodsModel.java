package build.dream.platform.models.goods;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.platform.constants.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

public class SaveGoodsModel extends BasicModel {
    private Long id;
    @NotNull
    @Length(max = 20)
    private String name;

    @NotNull
    private Long goodsTypeId;

    @NotNull
    private Integer status;

    @NotNull
    private String photoUrl;

    @NotNull
    private Integer meteringMode;

    @NotNull
    private String business;

    private List<GoodsSpecificationModel> goodsSpecificationModels;

    @NotNull
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(Long goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getMeteringMode() {
        return meteringMode;
    }

    public void setMeteringMode(Integer meteringMode) {
        this.meteringMode = meteringMode;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public List<GoodsSpecificationModel> getGoodsSpecificationModels() {
        return goodsSpecificationModels;
    }

    public void setGoodsSpecificationModels(List<GoodsSpecificationModel> goodsSpecificationModels) {
        this.goodsSpecificationModels = goodsSpecificationModels;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public static class GoodsSpecificationModel extends BasicModel {
        private Long id;

        @NotNull
        @Length(max = 20)
        private String name;

        @NotNull
        private Boolean allowTenantBuy;

        @NotNull
        private Boolean allowAgentBuy;

        @NotNull
        private Integer renewalTime;

        @NotNull
        private Double tenantPrice;

        @NotNull
        private Double agentPrice;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean isAllowTenantBuy() {
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

        public Double getTenantPrice() {
            return tenantPrice;
        }

        public void setTenantPrice(Double tenantPrice) {
            this.tenantPrice = tenantPrice;
        }

        public Double getAgentPrice() {
            return agentPrice;
        }

        public void setAgentPrice(Double agentPrice) {
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
        ApplicationHandler.isTrue(Constants.BUSINESS_CATERING.equals(business) || Constants.BUSINESS_RETAIL.equals(business), "business");
    }
}
