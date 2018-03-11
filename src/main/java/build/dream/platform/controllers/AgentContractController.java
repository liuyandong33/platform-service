package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.models.agentcontract.*;
import build.dream.platform.services.AgentContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @RequestMapping(value = "/saveAgentContract", method = RequestMethod.POST)
    @ResponseBody
    public String saveAgentContract() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            SaveAgentContractModel saveAgentContractModel = ApplicationHandler.instantiateObject(SaveAgentContractModel.class, requestParameters);
            String contractPriceInfos = requestParameters.get("contractPriceInfos");
            saveAgentContractModel.setContractPriceInfos(contractPriceInfos);
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
    @RequestMapping(value = "/auditAgentContract", method = RequestMethod.POST)
    @ResponseBody
    public String auditAgentContract() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            AuditAgentContractModel auditAgentContractModel = ApplicationHandler.instantiateObject(AuditAgentContractModel.class, requestParameters);
            auditAgentContractModel.validateAndThrow();
            apiRest = agentContractService.auditAgentContract(auditAgentContractModel);
        } catch (Exception e) {
            LogUtils.error("审核代理商合同失败", controllerSimpleName, "auditAgentContract", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    /**
     * 终止代理商合同
     *
     * @return
     */
    @RequestMapping(value = "/terminateAgentContract", method = RequestMethod.POST)
    @ResponseBody
    public String terminateAgentContract() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            TerminateAgentContractModel terminateAgentContractModel = ApplicationHandler.instantiateObject(TerminateAgentContractModel.class, requestParameters);
            terminateAgentContractModel.validateAndThrow();
            apiRest = agentContractService.terminateAgentContract(terminateAgentContractModel);
        } catch (Exception e) {
            LogUtils.error("终止代理商合同失败", controllerSimpleName, "terminateAgentContract", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    /**
     * 分页查询代理商合同失败
     *
     * @return
     */
    @RequestMapping(value = "/listAgentContracts", method = RequestMethod.GET)
    @ResponseBody
    public String listAgentContracts() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            ListAgentContractsModel listAgentContractsModel = ApplicationHandler.instantiateObject(ListAgentContractsModel.class, requestParameters);
            listAgentContractsModel.validateAndThrow();
            apiRest = agentContractService.listAgentContracts(listAgentContractsModel);
        } catch (Exception e) {
            LogUtils.error("查询代理商合同列表失败", controllerSimpleName, "listAgentContracts", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    /**
     * 获取代理商合同信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainAgentContractInfo", method = RequestMethod.GET)
    @ResponseBody
    public String obtainAgentContractInfo() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            ObtainAgentContractInfoModel obtainAgentContractInfoModel = ApplicationHandler.instantiateObject(ObtainAgentContractInfoModel.class, requestParameters);
            obtainAgentContractInfoModel.validateAndThrow();
            apiRest = agentContractService.obtainAgentContractInfo(obtainAgentContractInfoModel);
        } catch (Exception e) {
            LogUtils.error("获取代理商合同信息失败", controllerSimpleName, "obtainAgentContractInfo", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
