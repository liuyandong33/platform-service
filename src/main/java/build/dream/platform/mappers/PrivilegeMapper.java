package build.dream.platform.mappers;

import build.dream.common.domains.saas.AppPrivilege;
import build.dream.common.domains.saas.BackgroundPrivilege;
import build.dream.common.domains.saas.PosPrivilege;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

public interface PrivilegeMapper {
    List<AppPrivilege> findAllAppPrivileges(@Param("userId") BigInteger userId);

    List<PosPrivilege> findAllPosPrivileges(@Param("userId") BigInteger userId);

    List<BackgroundPrivilege> findAllBackgroundPrivileges(@Param("userId") BigInteger userId);
}
