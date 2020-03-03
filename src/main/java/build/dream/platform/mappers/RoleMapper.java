package build.dream.platform.mappers;

import build.dream.common.domains.saas.AppPrivilege;
import build.dream.common.domains.saas.BackgroundPrivilege;
import build.dream.common.domains.saas.PosPrivilege;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    long deleteTenantAppRolePrivileges(@Param("roleId") Long roleId);

    long deleteTenantPosRolePrivileges(@Param("roleId") Long roleId);

    long deleteTenantBackgroundRolePrivileges(@Param("roleId") Long roleId);

    long saveTenantRoleAppPrivileges(@Param("roleId") Long roleId, @Param("privilegeIds") List<Long> privilegeIds);

    long saveTenantRolePosPrivileges(@Param("roleId") Long roleId, @Param("privilegeIds") List<Long> privilegeIds);

    long saveTenantRoleBackgroundPrivileges(@Param("roleId") Long roleId, @Param("privilegeIds") List<Long> privilegeIds);

    List<AppPrivilege> listRoleAppPrivileges(@Param("roleId") Long roleId);

    List<PosPrivilege> listRolePosPrivileges(@Param("roleId") Long roleId);

    List<BackgroundPrivilege> listRoleBackgroundPrivileges(@Param("roleId") Long roleId);
}
