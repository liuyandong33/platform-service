package build.dream.platform.models.agentcontract;

import build.dream.common.annotations.JsonSchema;
import build.dream.common.models.BasicModel;
import build.dream.platform.constants.Constants;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class SaveAgentContractModel extends BasicModel {
    private BigInteger agentContractId;

    @NotNull
    private BigInteger agentId;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    @NotNull
    private BigInteger userId;

    @JsonSchema(value = Constants.CONTRACT_PRICE_INFOS_SCHEMA_FILE_PATH)
    private List<ContractPriceInfo> contractPriceInfos;

    public BigInteger getAgentContractId() {
        return agentContractId;
    }

    public void setAgentContractId(BigInteger agentContractId) {
        this.agentContractId = agentContractId;
    }

    public BigInteger getAgentId() {
        return agentId;
    }

    public void setAgentId(BigInteger agentId) {
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

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public List<ContractPriceInfo> getContractPriceInfos() {
        return contractPriceInfos;
    }

    public void setContractPriceInfos(List<ContractPriceInfo> contractPriceInfos) {
        this.contractPriceInfos = contractPriceInfos;
    }

    public static class ContractPriceInfo {
        private BigInteger id;
        private BigInteger goodsId;
        private BigInteger goodsSpecificationId;
        private BigDecimal contractPrice;

        public BigInteger getId() {
            return id;
        }

        public void setId(BigInteger id) {
            this.id = id;
        }

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

        public BigDecimal getContractPrice() {
            return contractPrice;
        }

        public void setContractPrice(BigDecimal contractPrice) {
            this.contractPrice = contractPrice;
        }
    }
}
