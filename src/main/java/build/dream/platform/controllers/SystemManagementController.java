package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.saas.domains.SystemPartition;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.MethodCaller;
import build.dream.common.utils.PropertyUtils;
import build.dream.common.utils.SystemPartitionUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.services.SystemPartitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/systemManagement")
public class SystemManagementController extends BasicController {
    @Autowired
    private SystemPartitionService systemPartitionService;

    @RequestMapping(value = "/refreshSystemPartitions")
    @ResponseBody
    public String refreshSystemPartitions() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            String deploymentEnvironment = PropertyUtils.getProperty(Constants.DEPLOYMENT_ENVIRONMENT);
            List<SystemPartition> systemPartitions = systemPartitionService.findAllByDeploymentEnvironment(deploymentEnvironment);
            SystemPartitionUtils.loadSystemPartitions(systemPartitions, deploymentEnvironment);
            ApiRest apiRest = new ApiRest();
            apiRest.setMessage("刷新分区配置成功！");
            apiRest.setSuccessful(true);
            return apiRest;
        };
        return ApplicationHandler.callMethod(methodCaller, "刷新分区配置失败", requestParameters);
    }
}
