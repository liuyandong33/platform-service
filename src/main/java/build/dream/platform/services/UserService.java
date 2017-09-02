package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.SystemUser;
import build.dream.common.saas.domains.Tenant;
import build.dream.common.utils.SearchModel;
import build.dream.platform.mappers.SystemUserMapper;
import build.dream.platform.mappers.TenantMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        data.put("user", systemUser);
        data.put("tenant", tenant);
        ApiRest apiRest = new ApiRest();
        apiRest.setData(data);
        apiRest.setSuccessful(true);
        return apiRest;
    }

    @Transactional(readOnly = true)
    public ApiRest findAllUsers(Map<String, String> parameters) {
        String userIds = parameters.get("userIds");
        SearchModel searchModel = new SearchModel(true);
        ApiRest apiRest = null;
        if (StringUtils.isNotBlank(userIds)) {
            String[] userIdArray = StringUtils.split(userIds, ",");
            List<BigInteger> bigIntegerUserIds = new ArrayList<BigInteger>();
            for (String userId : userIdArray) {
                bigIntegerUserIds.add(BigInteger.valueOf(Long.valueOf(userId)));
            }
            searchModel.addSearchCondition("id", "IN", bigIntegerUserIds);
            List<SystemUser> systemUsers = systemUserMapper.findAll(searchModel);
            apiRest = new ApiRest(systemUsers, "查询成功！");
        }
        return apiRest;
    }
}
