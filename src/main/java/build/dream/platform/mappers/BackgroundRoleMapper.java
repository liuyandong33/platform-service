package build.dream.platform.mappers;

import build.dream.common.saas.domains.BackgroundRole;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BackgroundRoleMapper {
    long count(SearchModel searchModel);
    List<BackgroundRole> findAllPaged(SearchModel searchModel);
}
