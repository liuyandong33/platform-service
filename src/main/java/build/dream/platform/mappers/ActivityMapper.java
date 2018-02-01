package build.dream.platform.mappers;

import build.dream.common.saas.domains.Activity;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActivityMapper {
    long insert(Activity activity);
    long update(Activity activity);
    Activity find(SearchModel searchModel);
    List<Activity> findAll(SearchModel searchModel);
    long count(SearchModel searchModel);
    List<Activity> findAllPaged(SearchModel searchModel);
}
