package build.dream.platform.mappers;

import build.dream.common.saas.domains.PosPrivilege;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface PosPrivilegeMapper {
    PosPrivilege find(SearchModel searchModel);
    List<PosPrivilege> findAll(SearchModel searchModel);
    List<PosPrivilege> findAllPosPrivileges(BigInteger userId);
    List<PosPrivilege> listRolePrivileges(@Param("roleId") BigInteger roleId);
}
