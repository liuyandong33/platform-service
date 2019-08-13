package build.dream.platform.mappers;

import build.dream.common.domains.saas.SystemUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemUserMapper {
    SystemUser findByLoginNameOrEmailOrMobile(@Param("loginName") String loginName);
}
