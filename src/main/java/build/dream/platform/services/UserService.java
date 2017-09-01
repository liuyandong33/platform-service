package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.SystemUser;
import build.dream.common.saas.domains.Tenant;
import build.dream.platform.mappers.SystemUserMapper;
import build.dream.platform.mappers.TenantMapper;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private SystemUserMapper systemUserMapper;
    @Autowired
    private TenantMapper tenantMapper;

    @Transactional(readOnly = true)
    public ApiRest obtainUserInfo(String loginName) {
        SystemUser systemUser = systemUserMapper.findByLoginNameOrEmailOrMobile(loginName);
        Validate.notNull(systemUser, "用户不存在！");
        Tenant tenant = tenantMapper.findById(systemUser.getTenantId());
        Validate.notNull(tenant, "商户不存在！");
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("userInfo", systemUser);
        data.put("tenantInfo", tenant);
        ApiRest apiRest = new ApiRest();
        apiRest.setData(data);
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
