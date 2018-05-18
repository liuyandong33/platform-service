package build.dream.platform.services;

import build.dream.common.saas.domains.Configuration;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.utils.DatabaseHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConfigurationService {
    @Transactional(readOnly = true)
    public List<Configuration> findAllByDeploymentEnvironment(String deploymentEnvironment) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("deployment_environment", Constants.SQL_OPERATION_SYMBOL_EQUALS, deploymentEnvironment);
        return DatabaseHelper.findAll(Configuration.class, searchModel);
    }
}
