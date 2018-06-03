package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.saas.domains.SystemPartition;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.PropertyUtils;
import build.dream.common.utils.SystemPartitionUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.services.SystemPartitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/systemManagement")
public class SystemManagementController extends BasicController {
    @Autowired
    private SystemPartitionService systemPartitionService;

    @RequestMapping(value = "/refreshSystemPartitions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "刷新分区配置失败")
    public String refreshSystemPartitions() throws IOException {
        String deploymentEnvironment = PropertyUtils.getProperty(Constants.DEPLOYMENT_ENVIRONMENT);
        List<SystemPartition> systemPartitions = systemPartitionService.findAllByDeploymentEnvironment(deploymentEnvironment);
        SystemPartitionUtils.loadSystemPartitions(systemPartitions, deploymentEnvironment);
        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("刷新分区配置成功！");
        apiRest.setSuccessful(true);
        return GsonUtils.toJson(apiRest);
    }
}
