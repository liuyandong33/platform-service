package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.domains.saas.*;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.SerialNumberGenerator;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.AgentContractPriceInfoMapper;
import build.dream.platform.models.agentcontract.*;
import build.dream.platform.utils.SequenceUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AgentContractService {
    @Autowired
    private AgentContractPriceInfoMapper agentContractPriceInfoMapper;

    /**
     * 保存代理商合同
     *
     * @param saveAgentContractModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveAgentContract(SaveAgentContractModel saveAgentContractModel) {
        BigInteger agentContractId = saveAgentContractModel.getAgentContractId();
        BigInteger userId = saveAgentContractModel.getUserId();
        if (agentContractId == null) {
            AgentContract agentContract = new AgentContract();
            String contractNumber = "HT" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + SerialNumberGenerator.nextSerialNumber(8, SequenceUtils.currentValue("agent_contract_number"));
            agentContract.setContractNumber(contractNumber);
            agentContract.setAgentId(saveAgentContractModel.getAgentId());
            agentContract.setStartTime(saveAgentContractModel.getStartTime());
            agentContract.setEndTime(saveAgentContractModel.getEndTime());
            agentContract.setStatus(Constants.AGENT_CONTRACT_STATUS_UNAUDITED);
            agentContract.setCreatedUserId(userId);
            agentContract.setUpdatedUserId(userId);
            agentContract.setUpdatedRemark("新增代理商合同！");

            List<SaveAgentContractModel.ContractPriceInfo> contractPriceInfos = saveAgentContractModel.getContractPriceInfos();
            List<BigInteger> goodsIds = new ArrayList<BigInteger>();
            List<BigInteger> goodsSpecificationIds = new ArrayList<BigInteger>();
            for (SaveAgentContractModel.ContractPriceInfo contractPriceInfo : contractPriceInfos) {
                goodsIds.add(contractPriceInfo.getGoodsId());
                goodsSpecificationIds.add(contractPriceInfo.getGoodsSpecificationId());
            }
            SearchModel goodsSearchModel = new SearchModel(true);
            goodsSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, goodsIds);
            List<Goods> goodses = DatabaseHelper.findAll(Goods.class, goodsSearchModel);

            SearchModel goodsSpecificationSearchModel = new SearchModel(true);
            goodsSpecificationSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, goodsSpecificationIds);
            List<GoodsSpecification> goodsSpecifications = DatabaseHelper.findAll(GoodsSpecification.class, goodsSpecificationSearchModel);

            Map<BigInteger, Goods> goodsMap = new HashMap<BigInteger, Goods>();
            Map<BigInteger, GoodsSpecification> goodsSpecificationMap = new HashMap<BigInteger, GoodsSpecification>();
            for (Goods goods : goodses) {
                goodsMap.put(goods.getId(), goods);
            }
            for (GoodsSpecification goodsSpecification : goodsSpecifications) {
                goodsSpecificationMap.put(goodsSpecification.getId(), goodsSpecification);
            }
            DatabaseHelper.insert(agentContract);
            List<AgentContractPriceInfo> agentContractPriceInfos = new ArrayList<AgentContractPriceInfo>();
            for (SaveAgentContractModel.ContractPriceInfo contractPriceInfo : contractPriceInfos) {
                Goods goods = goodsMap.get(contractPriceInfo.getGoodsId());
                Validate.notNull(goods, "商品不存在！");
                GoodsSpecification goodsSpecification = goodsSpecificationMap.get(contractPriceInfo.getGoodsSpecificationId());
                Validate.notNull(goodsSpecification, "商品规格不存在！");

                BigDecimal contractPrice = contractPriceInfo.getContractPrice();
                Validate.isTrue(contractPrice.compareTo(BigDecimal.ZERO) >= 0 && contractPrice.compareTo(goodsSpecification.getAgentPrice()) <= 0, "商品【" + goods.getName() + "-" + goodsSpecification.getName() + "】的合同价格错误！");

                AgentContractPriceInfo agentContractPriceInfo = new AgentContractPriceInfo();
                agentContractPriceInfo.setAgentContractId(agentContract.getId());
                agentContractPriceInfo.setGoodsId(goods.getId());
                agentContractPriceInfo.setGoodsSpecificationId(goodsSpecification.getId());
                agentContractPriceInfo.setContractPrice(contractPrice);
                agentContractPriceInfo.setCreatedUserId(userId);
                agentContractPriceInfo.setUpdatedUserId(userId);
                agentContractPriceInfo.setUpdatedRemark("保存代理商合同价格信息！");
                agentContractPriceInfos.add(agentContractPriceInfo);
            }
            DatabaseHelper.insertAll(agentContractPriceInfos);
        } else {
            SearchModel searchModel = new SearchModel();
            searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUAL, agentContractId);
            searchModel.addSearchCondition("agent_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, saveAgentContractModel.getAgentId());
            AgentContract agentContract = DatabaseHelper.find(AgentContract.class, searchModel);
            Validate.notNull(agentContract, "代理商合同不存在！");
            Validate.isTrue(agentContract.getStatus() == Constants.AGENT_CONTRACT_STATUS_UNAUDITED, "只有未审核状态的代理商合同才能修改！");

            List<BigInteger> agentContractPriceInfoIds = new ArrayList<BigInteger>();
            List<SaveAgentContractModel.ContractPriceInfo> contractPriceInfos = saveAgentContractModel.getContractPriceInfos();
            for (SaveAgentContractModel.ContractPriceInfo contractPriceInfo : contractPriceInfos) {
                agentContractPriceInfoIds.add(contractPriceInfo.getId());
            }
            SearchModel agentContractPriceInfoSearchModel = new SearchModel(true);
            agentContractPriceInfoSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, agentContractPriceInfoIds);
            agentContractPriceInfoSearchModel.addSearchCondition("agent_contract_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, agentContract.getId());
            List<AgentContractPriceInfo> agentContractPriceInfos = DatabaseHelper.findAll(AgentContractPriceInfo.class, agentContractPriceInfoSearchModel);
            Map<BigInteger, AgentContractPriceInfo> agentContractPriceInfoMap = new HashMap<BigInteger, AgentContractPriceInfo>();
            for (AgentContractPriceInfo agentContractPriceInfo : agentContractPriceInfos) {
                agentContractPriceInfoMap.put(agentContractPriceInfo.getId(), agentContractPriceInfo);
            }

            for (SaveAgentContractModel.ContractPriceInfo contractPriceInfo : contractPriceInfos) {
                AgentContractPriceInfo agentContractPriceInfo = agentContractPriceInfoMap.get(contractPriceInfo.getId());
                Validate.notNull(agentContractPriceInfo, "代理商合同价格信息不存在！");
                agentContractPriceInfo.setContractPrice(contractPriceInfo.getContractPrice());
                agentContractPriceInfo.setUpdatedUserId(userId);
                agentContractPriceInfo.setUpdatedRemark("修改合同价格信息！");
                DatabaseHelper.update(agentContractPriceInfo);
            }

            agentContract.setStartTime(saveAgentContractModel.getStartTime());
            agentContract.setEndTime(saveAgentContractModel.getEndTime());
            agentContract.setUpdatedUserId(userId);
            agentContract.setUpdatedRemark("修改代理商合同！");
            DatabaseHelper.update(agentContract);
        }
        return new ApiRest(agentContractId, "保存代理商合同成功！");
    }

    /**
     * 审核代理商合同
     *
     * @param auditAgentContractModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest auditAgentContract(AuditAgentContractModel auditAgentContractModel) {
        BigInteger agentContractId = auditAgentContractModel.getAgentContractId();
        BigInteger agentId = auditAgentContractModel.getAgentId();
        BigInteger userId = auditAgentContractModel.getUserId();

        SearchModel searchModel = new SearchModel();
        searchModel.addSearchCondition(AgentContract.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, agentContractId);
        searchModel.addSearchCondition(AgentContract.ColumnName.AGENT_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, agentId);
        AgentContract agentContract = DatabaseHelper.find(AgentContract.class, searchModel);
        Validate.notNull(agentContract, "代理商合同不存在！");
        Validate.isTrue(agentContract.getStatus() == Constants.AGENT_CONTRACT_STATUS_UNAUDITED, "只有未审核状态的代理商合同才能进行审核操作！");

        Date date = new Date();
        Validate.isTrue(agentContract.getEndTime().after(date), "合同结束时间小于当前时间，无法完成审核操作！");

        String sql = "SELECT count(1) FROM agent_contract WHERE ((start_time <= #{startTime} AND start_time >= #{startTime}) OR (end_time <= #{endTime} AND end_time >= #{endTime}) OR (start_time <= #{startTime} AND end_time >= #{endTime})) AND status IN (2, 3) AND deleted = 0 AND id != #{agentContractId};";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("sql", sql);
        parameters.put("startTime", agentContract.getStartTime());
        parameters.put("endTime", agentContract.getEndTime());
        parameters.put("agentContractId", agentContract.getId());
        long count = DatabaseHelper.universalCount(parameters);
        Validate.isTrue(count == 0, "此合同与其他的合同在时间上存在冲突！");

        if (date.after(agentContract.getStartTime())) {
            agentContract.setStatus(Constants.AGENT_CONTRACT_STATUS_EXECUTING);
        } else {
            agentContract.setStatus(Constants.AGENT_CONTRACT_STATUS_UNEXECUTED);
        }
        agentContract.setUpdatedUserId(auditAgentContractModel.getUserId());
        agentContract.setUpdatedRemark("审核合同！");
        DatabaseHelper.update(agentContract);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("审核成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 终止代理商合同
     *
     * @param terminateAgentContractModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest terminateAgentContract(TerminateAgentContractModel terminateAgentContractModel) {
        BigInteger agentContractId = terminateAgentContractModel.getAgentContractId();
        BigInteger agentId = terminateAgentContractModel.getAgentId();
        BigInteger userId = terminateAgentContractModel.getUserId();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition(AgentContract.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, agentContractId);
        searchModel.addSearchCondition(AgentContract.ColumnName.AGENT_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, agentId);
        AgentContract agentContract = DatabaseHelper.find(AgentContract.class, searchModel);
        Validate.notNull(agentContract, "代理商合同不存在！");
        Validate.isTrue(agentContract.getStatus() == Constants.AGENT_CONTRACT_STATUS_EXECUTING, "只有执行中的代理商合同才能进行终止操作！");

        agentContract.setStatus(Constants.AGENT_CONTRACT_STATUS_TERMINATED);
        agentContract.setUpdatedUserId(userId);
        agentContract.setUpdatedRemark("终止代理合同！");
        DatabaseHelper.update(agentContract);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("终止成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 分页查询代理商合同
     *
     * @param listAgentContractsModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest listAgentContracts(ListAgentContractsModel listAgentContractsModel) {
        String[] columns = {"agent_contract.contract_number", "agent_contract.start_time", "agent_contract.end_time", "agent_contract.status", "agent.id AS agent_id", "agent.code AS agent_code", "agent.name AS agent_name"};
        String sql = "SELECT %s FROM agent_contract LEFT OUTER JOIN agent ON agent.id = agent_contract.agent_id WHERE agent_contract.deleted = 0";
        StringBuilder searchCondition = new StringBuilder();
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        Integer status = listAgentContractsModel.getStatus();
        if (status != null) {
            searchCondition.append(" AND status = #{status}");
            namedParameters.put("status", status);
        }

        String keyWord = listAgentContractsModel.getKeyWord();
        if (StringUtils.isNotBlank(keyWord)) {
            searchCondition.append(" AND (agent.code LIKE #{keyWord} OR agent.name LIKE #{keyWord})");
            namedParameters.put("keyWord", "%" + keyWord + "%");
        }

        Date startTime = listAgentContractsModel.getStartTime();
        if (startTime != null) {
            searchCondition.append(" AND agent_contract.start_time >= #{startTime}");
            namedParameters.put("startTime", startTime);
        }

        Date endTime = listAgentContractsModel.getEndTime();
        if (endTime != null) {
            searchCondition.append(" AND agent_contract.end_time <= #{endTime}");
            namedParameters.put("endTime", endTime);
        }

        Map<String, Object> countParameters = new HashMap<String, Object>();
        countParameters.put("sql", String.format(sql, "COUNT(1)") + searchCondition.toString());
        countParameters.putAll(namedParameters);
        long count = DatabaseHelper.universalCount(countParameters);
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        if (count > 0) {
            Map<String, Object> rowsParameters = new HashMap<String, Object>();
            rowsParameters.put("sql", String.format(sql, StringUtils.join(columns, ", ")) + searchCondition.toString() + " LIMIT #{offset}, #{maxResults}");
            rowsParameters.putAll(namedParameters);
            rowsParameters.put("offset", (listAgentContractsModel.getPage() - 1) * listAgentContractsModel.getRows());
            rowsParameters.put("maxResults", listAgentContractsModel.getRows());
            rows = DatabaseHelper.executeQuery(rowsParameters);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", count);
        data.put("rows", rows);

        return new ApiRest(data, "查询代理商合同列表成功！");
    }

    /**
     * 获取代理商合同信息
     *
     * @param obtainAgentContractInfoModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainAgentContractInfo(ObtainAgentContractInfoModel obtainAgentContractInfoModel) {
        BigInteger agentId = obtainAgentContractInfoModel.getAgentId();
        BigInteger agentContractId = obtainAgentContractInfoModel.getAgentContractId();

        SearchModel agentSearchModel = new SearchModel(true);
        agentSearchModel.addSearchCondition(Agent.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, agentId);
        Agent agent = DatabaseHelper.find(Agent.class, agentSearchModel);
        Validate.notNull(agent, "代理商不存在！");

        SearchModel agentContractSearchModel = new SearchModel(true);
        agentContractSearchModel.addSearchCondition(AgentContract.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, agentContractId);
        agentContractSearchModel.addSearchCondition(AgentContract.ColumnName.AGENT_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, agentId);
        AgentContract agentContract = DatabaseHelper.find(AgentContract.class, agentContractSearchModel);
        Validate.notNull(agentContract, "代理商合同不存在！");

        List<Map<String, Object>> agentContractPriceInfos = agentContractPriceInfoMapper.findAllAgentContractPriceInfos(agentContract.getId());

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("agent", agent);
        data.put("agentContract", agentContract);
        data.put("agentContractPriceInfos", agentContractPriceInfos);

        return new ApiRest(data, "获取代理商合同信息成功！");
    }
}
