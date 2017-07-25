package build.dream.platform.services;

import build.dream.common.saas.domains.SystemUser;
import build.dream.common.saas.domains.Tenant;
import build.dream.platform.mappers.SystemUserMapper;
import build.dream.platform.mappers.TenantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterService {
    @Autowired
    private SystemUserMapper systemUserMapper;
    @Autowired
    private TenantMapper tenantMapper;

    @Transactional
    public Map<String, Object> registerTenant(Tenant tenant) {
        SystemUser systemUser = new SystemUser();
        systemUserMapper.insert(systemUser);
        tenantMapper.insert(tenant);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("systemUser", systemUser);
        data.put("tenant", tenant);
        return data;
    }
}
