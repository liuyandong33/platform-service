package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.Agent;
import build.dream.common.saas.domains.SystemUser;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.agent.DeleteAgentModel;
import build.dream.platform.models.agent.ListModel;
import build.dream.platform.models.agent.ObtainAgentInfoModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AgentService {
    /**
     * 获取代理商信息
     *
     * @param obtainAgentInfoModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainAgentInfo(ObtainAgentInfoModel obtainAgentInfoModel) {
        BigInteger agentId = obtainAgentInfoModel.getAgentId();
        String agentCode = obtainAgentInfoModel.getAgentCode();

        SearchModel searchModel = new SearchModel(true);
        if (agentId != null) {
            searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUAL, agentId);
        }
        if (StringUtils.isNotBlank(agentCode)) {
            searchModel.addSearchCondition("code", Constants.SQL_OPERATION_SYMBOL_EQUAL, agentCode);
        }

        Agent agent = DatabaseHelper.find(Agent.class, searchModel);
        return ApiRest.builder().data(agent).message("获取代理商信息成功！").successful(true).build();
    }

    /**
     * 分页查询代理商信息
     *
     * @param listModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest list(ListModel listModel) {
        int page = listModel.getPage();
        int rows = listModel.getRows();

        List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
        searchConditions.add(new SearchCondition("deleted", Constants.SQL_OPERATION_SYMBOL_EQUAL, 0));

        SearchModel searchModel = new SearchModel();
        searchModel.setSearchConditions(searchConditions);
        long count = DatabaseHelper.count(Agent.class, searchModel);
        List<Agent> agents = null;
        if (count > 0) {
            PagedSearchModel pagedSearchModel = new PagedSearchModel();
            pagedSearchModel.setSearchConditions(searchConditions);
            pagedSearchModel.setPage(page);
            pagedSearchModel.setRows(rows);

            agents = DatabaseHelper.findAllPaged(Agent.class, pagedSearchModel);
        } else {
            agents = new ArrayList<Agent>();
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", count);
        data.put("rows", agents);
        return ApiRest.builder().data(data).message("查询代理商信息成功！").successful(true).build();
    }

    /**
     * 删除代理商
     *
     * @param deleteAgentModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest deleteAgent(DeleteAgentModel deleteAgentModel) {
        BigInteger agentId = deleteAgentModel.getAgentId();
        BigInteger userId = deleteAgentModel.getUserId();
        Agent agent = DatabaseHelper.find(Agent.class, agentId);
        ValidateUtils.notNull(agent, "代理商不存在！");

        agent.setDeleted(true);
        agent.setUpdatedUserId(userId);
        agent.setUpdatedRemark("删除代理商信息！");
        DatabaseHelper.update(agent);

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition(SystemUser.ColumnName.AGENT_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, agentId);
        SystemUser systemUser = DatabaseHelper.find(SystemUser.class, searchModel);

        systemUser.setDeleted(true);
        systemUser.setUpdatedUserId(userId);
        systemUser.setUpdatedRemark("删除用户信息！");
        DatabaseHelper.update(systemUser);

        return ApiRest.builder().message("删除代理商信息成功！").successful(true).build();
    }
}
