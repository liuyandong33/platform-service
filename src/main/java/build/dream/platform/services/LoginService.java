package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.constants.SessionConstants;
import build.dream.common.saas.domains.Agent;
import build.dream.common.saas.domains.BackgroundPrivilege;
import build.dream.common.saas.domains.SystemUser;
import build.dream.common.saas.domains.Tenant;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.CacheUtils;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.BackgroundPrivilegeMapper;
import build.dream.platform.mappers.SystemUserMapper;
import build.dream.platform.models.login.LoginModel;
import build.dream.common.utils.DatabaseHelper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginService {
    @Autowired
    private SystemUserMapper systemUserMapper;
    @Autowired
    private BackgroundPrivilegeMapper backgroundPrivilegeMapper;

    @Transactional(readOnly = true)
    public ApiRest login(LoginModel loginModel) {
        String loginName = loginModel.getLoginName();
        String password = loginModel.getPassword();
        String sessionId = loginModel.getSessionId();
        SystemUser systemUser = systemUserMapper.findByLoginNameOrEmailOrMobile(loginName);
        Validate.notNull(systemUser, "用户不存在！");
        Integer userType = systemUser.getUserType();
        Validate.isTrue(DigestUtils.md5Hex(password).equals(systemUser.getPassword()), "用户名或密码错误！");

        Map<String, String> sessionMap = new HashMap<String, String>();
        sessionMap.put(SessionConstants.KEY_USER_ID, systemUser.getId().toString());
        sessionMap.put(SessionConstants.KEY_USER_TYPE, userType.toString());
        ApplicationHandler.ifNotBlankPut(sessionMap, SessionConstants.KEY_USER_MOBILE, systemUser.getMobile());
        ApplicationHandler.ifNotBlankPut(sessionMap, SessionConstants.KEY_USER_EMAIL, systemUser.getEmail());
        if (userType == Constants.USER_TYPE_TENANT || userType == Constants.USER_TYPE_TENANT_EMPLOYEE) {
            SearchModel searchModel = new SearchModel(true);
            searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUAL, systemUser.getTenantId());
            Tenant tenant = DatabaseHelper.find(Tenant.class, searchModel);
            Validate.notNull(tenant, "商户不存在！");

            sessionMap.put(SessionConstants.KEY_TENANT_ID, tenant.getId().toString());
            sessionMap.put(SessionConstants.KEY_TENANT_CODE, tenant.getCode());
            sessionMap.put(SessionConstants.KEY_TENANT_NAME, tenant.getName());
            sessionMap.put(SessionConstants.KEY_PARTITION_CODE, tenant.getPartitionCode());
        } else if (userType == Constants.USER_TYPE_AGENT) {
            SearchModel searchModel = new SearchModel(true);
            searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUAL, systemUser.getAgentId());
            Agent agent = DatabaseHelper.find(Agent.class, searchModel);
            Validate.notNull(agent, "代理商不存在！");

            sessionMap.put(SessionConstants.KEY_AGENT_ID, agent.getId().toString());
            sessionMap.put(SessionConstants.KEY_AGENT_CODE, agent.getCode());
            sessionMap.put(SessionConstants.KEY_AGENT_NAME, agent.getName());
        }
        List<BackgroundPrivilege> backgroundPrivileges = backgroundPrivilegeMapper.findAllBackgroundPrivileges(systemUser.getId());
        List<String> authorityCodes = new ArrayList<String>();
        for (BackgroundPrivilege backgroundPrivilege : backgroundPrivileges) {
            authorityCodes.add(backgroundPrivilege.getPrivilegeCode());
        }
        sessionMap.put(SessionConstants.KEY_AUTHORITY_CODES, GsonUtils.toJson(authorityCodes));
        CacheUtils.setAttributesToSession(sessionId, sessionMap);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("登录成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
