package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.saas.domains.SystemPartition;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.common.utils.PropertyUtils;
import build.dream.common.utils.SystemPartitionUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.services.SystemPartitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/systemManagement")
public class SystemManagementController extends BasicController {
    @Autowired
    private SystemPartitionService systemPartitionService;

    @RequestMapping(value = "/refreshSystemPartitions")
    @ResponseBody
    public String refreshSystemPartitions() {
        ApiRest apiRest = null;
        try {
            String deploymentEnvironment = PropertyUtils.getProperty(Constants.DEPLOYMENT_ENVIRONMENT);
            List<SystemPartition> systemPartitions = systemPartitionService.findAllByDeploymentEnvironment(deploymentEnvironment);
            SystemPartitionUtils.loadSystemPartitions(systemPartitions, deploymentEnvironment);
            apiRest = new ApiRest();
            apiRest.setMessage("刷新分区配置成功！");
            apiRest.setSuccessful(true);
        } catch (Exception e) {
            LogUtils.error("刷新分区配置失败", controllerSimpleName, "refreshSystemPartition", e);
            apiRest = new ApiRest();
            apiRest.setError(e.getMessage());
            apiRest.setSuccessful(false);
        }
        return GsonUtils.toJson(apiRest);
    }
}
