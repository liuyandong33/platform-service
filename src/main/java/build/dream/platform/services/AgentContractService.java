package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.Agent;
import build.dream.common.saas.domains.AgentContract;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.SerialNumberGenerator;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.AgentContractMapper;
import build.dream.platform.mappers.AgentMapper;
import build.dream.platform.mappers.SequenceMapper;
import build.dream.platform.mappers.UniversalMapper;
import build.dream.platform.models.agentcontract.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AgentContractService {
    @Autowired
    private AgentContractMapper agentContractMapper;
    @Autowired
    private SequenceMapper sequenceMapper;
    @Autowired
    private UniversalMapper universalMapper;
    @Autowired
    private AgentMapper agentMapper;

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
            String contractNumber = "HT" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + SerialNumberGenerator.nextSerialNumber(8, sequenceMapper.currentValue("agent_contract_number"));
            agentContract.setContractNumber(contractNumber);
            agentContract.setAgentId(saveAgentContractModel.getAgentId());
            agentContract.setStartTime(saveAgentContractModel.getStartTime());
            agentContract.setEndTime(saveAgentContractModel.getEndTime());
            agentContract.setStatus(Constants.AGENT_CONTRACT_STATUS_UNAUDITED);
            agentContract.setCreateUserId(userId);
            agentContract.setLastUpdateUserId(userId);
            agentContract.setLastUpdateRemark("新增代理商合同！");
            agentContractMapper.insert(agentContract);
        } else {
            SearchModel searchModel = new SearchModel();
            searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, agentContractId);
            searchModel.addSearchCondition("agent_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, saveAgentContractModel.getAgentId());
            AgentContract agentContract = agentContractMapper.find(searchModel);
            Validate.notNull(agentContract, "代理商合同不存在！");
            Validate.isTrue(agentContract.getStatus() == Constants.AGENT_CONTRACT_STATUS_UNAUDITED, "只有未审核状态的代理商合同才能修改！");

            agentContract.setStartTime(saveAgentContractModel.getStartTime());
            agentContract.setEndTime(saveAgentContractModel.getEndTime());
            agentContract.setLastUpdateUserId(userId);
            agentContract.setLastUpdateRemark("修改代理商合同！");
            agentContractMapper.update(agentContract);
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
        SearchModel searchModel = new SearchModel();
        searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, auditAgentContractModel.getAgentContractId());
        searchModel.addSearchCondition("agent_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, auditAgentContractModel.getAgentId());
        AgentContract agentContract = agentContractMapper.find(searchModel);
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
        long count = universalMapper.universalCount(parameters);
        Validate.isTrue(count == 0, "此合同与其他的合同在时间上存在冲突！");

        if (date.after(agentContract.getStartTime())) {
            agentContract.setStatus(Constants.AGENT_CONTRACT_STATUS_EXECUTING);
        } else {
            agentContract.setStatus(Constants.AGENT_CONTRACT_STATUS_UNEXECUTED);
        }
        agentContract.setLastUpdateUserId(auditAgentContractModel.getUserId());
        agentContract.setLastUpdateRemark("审核合同！");
        agentContractMapper.update(agentContract);

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
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, terminateAgentContractModel.getAgentContractId());
        searchModel.addSearchCondition("agent_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, terminateAgentContractModel.getAgentId());
        AgentContract agentContract = agentContractMapper.find(searchModel);
        Validate.notNull(agentContract, "代理商合同不存在！");
        Validate.isTrue(agentContract.getStatus() == Constants.AGENT_CONTRACT_STATUS_EXECUTING, "只有执行中的代理商合同才能进行终止操作！");

        agentContract.setStatus(Constants.AGENT_CONTRACT_STATUS_TERMINATED);
        agentContract.setLastUpdateUserId(terminateAgentContractModel.getUserId());
        agentContract.setLastUpdateRemark("终止代理合同！");
        agentContractMapper.update(agentContract);

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
        long count = universalMapper.universalCount(countParameters);
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        if (count > 0) {
            Map<String, Object> rowsParameters = new HashMap<String, Object>();
            rowsParameters.put("sql", String.format(sql, StringUtils.join(columns, ", ")) + searchCondition.toString() + " LIMIT #{offset}, #{maxResults}");
            rowsParameters.putAll(namedParameters);
            rowsParameters.put("offset", (listAgentContractsModel.getPage() - 1) * listAgentContractsModel.getRows());
            rowsParameters.put("maxResults", listAgentContractsModel.getRows());
            rows = universalMapper.executeQuery(rowsParameters);
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
        SearchModel agentSearchModel = new SearchModel(true);
        agentSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, obtainAgentContractInfoModel.getAgentId());
        Agent agent = agentMapper.find(agentSearchModel);
        Validate.notNull(agent, "代理商不存在！");

        SearchModel agentContractSearchModel = new SearchModel(true);
        agentContractSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, obtainAgentContractInfoModel.getAgentContractId());
        agentContractSearchModel.addSearchCondition("agent_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, obtainAgentContractInfoModel.getAgentId());
        AgentContract agentContract = agentContractMapper.find(agentContractSearchModel);
        Validate.notNull(agentContract, "代理商合同不存在！");

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("agent", agent);
        data.put("agentContract", agentContract);

        return new ApiRest(data, "获取代理商合同信息成功！");
    }
}
