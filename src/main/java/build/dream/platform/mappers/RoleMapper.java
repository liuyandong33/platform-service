package build.dream.platform.mappers;

import build.dream.common.domains.saas.AppPrivilege;
import build.dream.common.domains.saas.BackgroundPrivilege;
import build.dream.common.domains.saas.PosPrivilege;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

public interface RoleMapper {
    long deleteTenantAppRolePrivileges(@Param("roleId") BigInteger roleId);

    long deleteTenantPosRolePrivileges(@Param("roleId") BigInteger roleId);

    long deleteTenantBackgroundRolePrivileges(@Param("roleId") BigInteger roleId);

    long saveTenantRoleAppPrivileges(@Param("roleId") BigInteger roleId, @Param("privilegeIds") List<BigInteger> privilegeIds);

    long saveTenantRolePosPrivileges(@Param("roleId") BigInteger roleId, @Param("privilegeIds") List<BigInteger> privilegeIds);

    long saveTenantRoleBackgroundPrivileges(@Param("roleId") BigInteger roleId, @Param("privilegeIds") List<BigInteger> privilegeIds);

    List<AppPrivilege> listRoleAppPrivileges(@Param("roleId") BigInteger roleId);

    List<PosPrivilege> listRolePosPrivileges(@Param("roleId") BigInteger roleId);

    List<BackgroundPrivilege> listRoleBackgroundPrivileges(@Param("roleId") BigInteger roleId);
}
