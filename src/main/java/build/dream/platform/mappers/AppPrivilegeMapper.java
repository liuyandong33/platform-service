package build.dream.platform.mappers;

import build.dream.common.saas.domains.AppPrivilege;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface AppPrivilegeMapper {
    AppPrivilege find(SearchModel searchModel);
    List<AppPrivilege> findAll(SearchModel searchModel);
    List<AppPrivilege> findAllAppPrivileges(BigInteger userId);
}
