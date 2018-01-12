package build.dream.platform.mappers;

import build.dream.common.saas.domains.PosRole;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface PosRoleMapper {
    long count(SearchModel searchModel);
    List<PosRole> findAllPaged(SearchModel searchModel);
    long deleteRolePrivileges(@Param("roleId") BigInteger roleId);
    long saveRolePrivileges(@Param("roleId") BigInteger roleId, @Param("privilegeIds") List<BigInteger> privilegeIds);
}
