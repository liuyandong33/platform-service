package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.AgentContract;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.SerialNumberGenerator;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.AgentContractMapper;
import build.dream.platform.mappers.SequenceMapper;
import build.dream.platform.mappers.UniversalMapper;
import build.dream.platform.models.agentcontract.AuditAgentContractModel;
import build.dream.platform.models.agentcontract.SaveAgentContractModel;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AgentContractService {
    @Autowired
    private AgentContractMapper agentContractMapper;
    @Autowired
    private SequenceMapper sequenceMapper;
    @Autowired
    private UniversalMapper universalMapper;

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
}
