package build.dream.platform.mappers;

import build.dream.common.saas.domains.SystemPrivilege;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SystemPrivilegeMapper {
    SystemPrivilege find(SearchModel searchModel);
    List<SystemPrivilege> findAll(SearchModel searchModel);
}
