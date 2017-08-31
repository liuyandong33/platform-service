package build.dream.platform.mappers;

import build.dream.common.saas.domains.Configuration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConfigurationMapper {
    List<Configuration> findAllByDeploymentEnvironment(@Param("deploymentEnvironment") String deploymentEnvironment);
}
