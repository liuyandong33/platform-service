package build.dream.platform.mappers;

import build.dream.common.saas.domains.SystemPartition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemPartitionMapper {
    List<SystemPartition> findAllByDeploymentEnvironment(@Param("deploymentEnvironment") String deploymentEnvironment);
}
