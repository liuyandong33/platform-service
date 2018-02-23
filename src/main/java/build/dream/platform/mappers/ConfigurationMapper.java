package build.dream.platform.mappers;

import build.dream.common.saas.domains.Configuration;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ConfigurationMapper {
    List<Configuration> findAll(SearchModel searchModel);
}
