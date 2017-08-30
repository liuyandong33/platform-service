package build.dream.platform.mappers;

import build.dream.common.saas.domains.SystemUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SystemUserMapper {
    int insert(SystemUser systemUser);
    int update(SystemUser systemUser);
}
