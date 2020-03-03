package build.dream.platform.mappers;

import build.dream.common.domains.saas.AppPrivilege;
import build.dream.common.domains.saas.BackgroundPrivilege;
import build.dream.common.domains.saas.PosPrivilege;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrivilegeMapper {
    List<AppPrivilege> findAllAppPrivileges(@Param("userId") Long userId);

    List<PosPrivilege> findAllPosPrivileges(@Param("userId") Long userId);

    List<BackgroundPrivilege> findAllBackgroundPrivileges(@Param("userId") Long userId);
}
