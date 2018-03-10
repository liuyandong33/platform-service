package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.models.agentcontract.AuditAgentContractModel;
import build.dream.platform.models.agentcontract.SaveAgentContractModel;
import build.dream.platform.services.AgentContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/agentContract")
public class AgentContractController extends BasicController {
    @Autowired
    private AgentContractService agentContractService;

    /**
     * 保存代理商合同
     *
     * @return
     */
    @RequestMapping(value = "/saveAgentContract")
    @ResponseBody
    public String saveAgentContract() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            SaveAgentContractModel saveAgentContractModel = ApplicationHandler.instantiateObject(SaveAgentContractModel.class, requestParameters);
            saveAgentContractModel.validateAndThrow();
            apiRest = agentContractService.saveAgentContract(saveAgentContractModel);
        } catch (Exception e) {
            LogUtils.error("保存代理商合同失败", controllerSimpleName, "saveAgentContract", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    /**
     * 审核代理商合同
     *
     * @return
     */
    @RequestMapping(value = "/auditAgentContract")
    @ResponseBody
    public String auditAgentContract() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            AuditAgentContractModel auditAgentContractModel = ApplicationHandler.instantiateObject(AuditAgentContractModel.class, requestParameters);
            auditAgentContractModel.validateAndThrow();
            apiRest = agentContractService.auditAgentContract(auditAgentContractModel);
        } catch (Exception e) {
            LogUtils.error("审核代理商合同", controllerSimpleName, "auditAgentContract", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
