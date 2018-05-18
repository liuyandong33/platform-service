package build.dream.platform.services;

import build.dream.common.saas.domains.TenantSecretKey;
import build.dream.common.utils.SearchModel;
import build.dream.platform.utils.DatabaseHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TenantSecretKeyService {
    @Transactional(readOnly = true)
    public List<TenantSecretKey> findAll(SearchModel searchModel) {
        return DatabaseHelper.findAll(TenantSecretKey.class, searchModel);
    }
}
