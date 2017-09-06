package build.dream.platform.mappers;

import build.dream.common.saas.domains.SystemUser;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemUserMapper {
    int insert(SystemUser systemUser);
    int update(SystemUser systemUser);
    SystemUser findByLoginNameOrEmailOrMobile(@Param("loginName") String loginName);
    SystemUser find(SearchModel searchModel);
    List<SystemUser> findAll(SearchModel searchModel);
    List<SystemUser> findAllPaged(SearchModel searchModel);
}
