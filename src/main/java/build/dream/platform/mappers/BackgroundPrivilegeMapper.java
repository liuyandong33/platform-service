package build.dream.platform.mappers;

import build.dream.common.saas.domains.BackgroundPrivilege;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BackgroundPrivilegeMapper {
    BackgroundPrivilege find(SearchModel searchModel);
    List<BackgroundPrivilege> findAll(SearchModel searchModel);
}
