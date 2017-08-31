package build.dream.platform.mappers;

import build.dream.common.saas.domains.SystemUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemUserMapper {
    int insert(SystemUser systemUser);
    int update(SystemUser systemUser);
    SystemUser findByLoginNameOrEmailOrMobile(@Param("loginName") String loginName);
}
