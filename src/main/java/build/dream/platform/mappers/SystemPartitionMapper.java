package build.dream.platform.mappers;

import build.dream.common.saas.domains.SystemPartition;
import build.dream.common.utils.SearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SystemPartitionMapper {
    List<SystemPartition> findAll(SearchModel searchModel);
}
