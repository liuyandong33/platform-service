package build.dream.platform.services;

import build.dream.common.saas.domains.SystemUser;
import build.dream.common.utils.SearchModel;
import build.dream.platform.mappers.SystemUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SystemUserService {
    @Autowired
    private SystemUserMapper systemUserMapper;

    @Transactional(readOnly = true)
    public List<SystemUser> findAll(SearchModel searchModel) {
        return systemUserMapper.findAll(searchModel);
    }
}
