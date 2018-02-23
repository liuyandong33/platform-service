package build.dream.platform.mappers;

import build.dream.common.saas.domains.SystemParameter;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SystemParameterMapper {
    long update(SystemParameter systemParameter);
    SystemParameter find(SearchModel searchModel);
}
