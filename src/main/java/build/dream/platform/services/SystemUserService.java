package build.dream.platform.services;

import build.dream.common.saas.domains.SystemUser;
import build.dream.common.utils.SearchModel;
import build.dream.platform.utils.DatabaseHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SystemUserService {
    @Transactional(readOnly = true)
    public List<SystemUser> findAll(SearchModel searchModel) {
        return DatabaseHelper.findAll(SystemUser.class, searchModel);
    }
}
