package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.Agent;
import build.dream.common.saas.domains.AgentForm;
import build.dream.common.saas.domains.SystemUser;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.agent.*;
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

    /**
     * 保存代理商申请单
     *
     * @param saveAgentFormModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveAgentForm(SaveAgentFormModel saveAgentFormModel) {
        String name = saveAgentFormModel.getName();
        String provinceCode = saveAgentFormModel.getProvinceCode();
        String cityCode = saveAgentFormModel.getCityCode();
        String districtCode = saveAgentFormModel.getDistrictCode();
        String address = saveAgentFormModel.getAddress();

        BigInteger userId = CommonUtils.getServiceSystemUserId();
        AgentForm agentForm = AgentForm.builder()
                .name(name)
                .status(1)
                .provinceCode(provinceCode)
                .provinceName(DistrictUtils.obtainName(provinceCode))
                .cityCode(cityCode)
                .cityName(DistrictUtils.obtainName(cityCode))
                .districtCode(districtCode)
                .districtName(DistrictUtils.obtainName(districtCode))
                .address(address)
                .verifyUserId(Constants.BIGINT_DEFAULT_VALUE)
                .createdUserId(userId)
                .updatedUserId(userId)
                .updatedRemark("保存代理商申请单！")
                .build();
        DatabaseHelper.insert(agentForm);

        return ApiRest.builder().data(agentForm).message("保存代理商申请单成功！").build();
    }

    /**
     * 审核代理商申请单
     *
     * @param verifyAgentFormModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest verifyAgentForm(VerifyAgentFormModel verifyAgentFormModel) {
        BigInteger agentFormId = verifyAgentFormModel.getAgentFormId();
        BigInteger userId = verifyAgentFormModel.getUserId();
        int status = verifyAgentFormModel.getStatus();
        String rejectReason = verifyAgentFormModel.getRejectReason();

        AgentForm agentForm = DatabaseHelper.find(AgentForm.class, agentFormId);
        ValidateUtils.notNull(agentForm, "代理商申请单不存在！");
        agentForm.setVerifyUserId(userId);
        agentForm.setStatus(status);

        if (status == 2) {
            String code = "";
            String name = agentForm.getName();
            Agent agent = Agent.builder()
                    .code(code)
                    .name(name)
                    .createdUserId(userId)
                    .updatedUserId(userId)
                    .updatedRemark("新增代理商信息！")
                    .build();
            DatabaseHelper.insert(agent);

            SystemUser systemUser = SystemUser.builder()
                    .name(name)
                    .mobile("")
                    .email("")
                    .loginName(code)
                    .userType(Constants.USER_TYPE_AGENT)
                    .password("")
                    .weiXinPublicPlatformOpenId(Constants.VARCHAR_DEFAULT_VALUE)
                    .weiXinOpenPlatformOpenId(Constants.VARCHAR_DEFAULT_VALUE)
                    .tenantId(Constants.BIGINT_DEFAULT_VALUE)
                    .agentId(agent.getId())
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .createdUserId(userId)
                    .updatedUserId(userId)
                    .updatedRemark("新增用户信息！")
                    .build();
            DatabaseHelper.insert(systemUser);
        } else if (status == 3) {
            agentForm.setRejectReason(rejectReason);
        }

        agentForm.setUpdatedUserId(userId);
        agentForm.setUpdatedRemark("审核代理商申请单！");

        DatabaseHelper.update(agentForm);

        return ApiRest.builder().message("审核后代理商申请单成功！").successful(true).build();
    }
}
