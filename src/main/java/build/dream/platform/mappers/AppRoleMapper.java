package build.dream.platform.mappers;

import build.dream.common.saas.domains.AppPrivilege;
import build.dream.common.saas.domains.AppRole;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface AppRoleMapper {
    long count(SearchModel searchModel);
    List<AppRole> findAllPaged(SearchModel searchModel);
    long deleteRolePrivileges(@Param("roleId") BigInteger roleId);
    long saveRolePrivileges(@Param("roleId") BigInteger roleId, @Param("privilegeIds") List<BigInteger> privilegeIds);
    List<AppPrivilege> listRolePrivileges(@Param("roleId") BigInteger roleId);
}
