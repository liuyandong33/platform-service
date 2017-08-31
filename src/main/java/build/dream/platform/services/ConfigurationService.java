package build.dream.platform.services;

import build.dream.common.saas.domains.Configuration;
import build.dream.platform.mappers.ConfigurationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationService {
    @Autowired
    private ConfigurationMapper configurationMapper;

    @Autowired
    public List<Configuration> findAllByDeploymentEnvironment(String deploymentEnvironment) {
        return configurationMapper.findAllByDeploymentEnvironment(deploymentEnvironment);
    }
}
