package build.dream.platform.services;

import build.dream.common.saas.domains.TenantSecretKey;
import build.dream.common.utils.SearchModel;
import build.dream.platform.mappers.TenantSecretKeyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TenantSecretKeyService {
    @Autowired
    private TenantSecretKeyMapper tenantSecretKeyMapper;

    @Transactional(readOnly = true)
    public List<TenantSecretKey> findAllTenantSecretKeys() {
        SearchModel searchModel = new SearchModel(true);
        return tenantSecretKeyMapper.findAll(searchModel);
    }
}
