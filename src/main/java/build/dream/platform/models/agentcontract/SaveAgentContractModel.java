package build.dream.platform.models.agentcontract;

import build.dream.common.constraints.VerifyJsonSchema;
import build.dream.common.models.BasicModel;
import build.dream.platform.constants.Constants;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class SaveAgentContractModel extends BasicModel {
    private Long agentContractId;

    @NotNull
    private Long agentId;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    @NotNull
    private Long userId;

    @VerifyJsonSchema(value = Constants.CONTRACT_PRICE_INFOS_SCHEMA_FILE_PATH)
    private List<ContractPriceInfo> contractPriceInfos;

    public Long getAgentContractId() {
        return agentContractId;
    }

    public void setAgentContractId(Long agentContractId) {
        this.agentContractId = agentContractId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ContractPriceInfo> getContractPriceInfos() {
        return contractPriceInfos;
    }

    public void setContractPriceInfos(List<ContractPriceInfo> contractPriceInfos) {
        this.contractPriceInfos = contractPriceInfos;
    }

    public static class ContractPriceInfo {
        private Long id;
        private Long goodsId;
        private Long goodsSpecificationId;
        private Double contractPrice;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

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

        public Double getContractPrice() {
            return contractPrice;
        }

        public void setContractPrice(Double contractPrice) {
            this.contractPrice = contractPrice;
        }
    }
}
