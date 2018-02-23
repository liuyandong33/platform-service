package build.dream.platform.mappers;

import build.dream.common.saas.domains.SystemParameter;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SystemParameterMapper {
    long insert(SystemParameter systemParameter);
    long update(SystemParameter systemParameter);
    SystemParameter find(SearchModel searchModel);
    List<SystemParameter> findAll(SearchModel searchModel);
}
