package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.constants.SessionConstants;
import build.dream.common.saas.domains.SystemUser;
import build.dream.common.saas.domains.Tenant;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.CacheUtils;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.SystemUserMapper;
import build.dream.platform.mappers.TenantMapper;
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
    private TenantMapper tenantMapper;

    @Transactional(readOnly = true)
    public ApiRest login(String loginName, String password, String sessionId) {
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
            searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, systemUser.getTenantId());
            Tenant tenant = tenantMapper.find(searchModel);
            Validate.notNull(tenant, "商户不存在！");

            sessionMap.put(SessionConstants.KEY_TENANT_ID, tenant.getId().toString());
            sessionMap.put(SessionConstants.KEY_TENANT_CODE, tenant.getCode());
            sessionMap.put(SessionConstants.KEY_TENANT_NAME, tenant.getName());
            sessionMap.put(SessionConstants.KEY_TENANT_PARTITION_CODE, tenant.getPartitionCode());
        }
        List<String> authorityCodes = new ArrayList<String>();
        authorityCodes.add("123");
        authorityCodes.add("456");
        sessionMap.put(SessionConstants.KEY_AUTHORITY_CODES, GsonUtils.toJson(authorityCodes));
        CacheUtils.setAttributesToSession(sessionId, sessionMap);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("登录成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
