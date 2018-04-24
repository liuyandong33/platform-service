package build.dream.platform.controllers;

import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.MethodCaller;
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
public class AgentContractController {
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
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            SaveAgentContractModel saveAgentContractModel = ApplicationHandler.instantiateObject(SaveAgentContractModel.class, requestParameters);
            String contractPriceInfos = requestParameters.get("contractPriceInfos");
            saveAgentContractModel.setContractPriceInfos(contractPriceInfos);
            saveAgentContractModel.validateAndThrow();
            return agentContractService.saveAgentContract(saveAgentContractModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "保存代理商合同失败", requestParameters);
    }

    /**
     * 审核代理商合同
     *
     * @return
     */
    @RequestMapping(value = "/auditAgentContract", method = RequestMethod.POST)
    @ResponseBody
    public String auditAgentContract() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            AuditAgentContractModel auditAgentContractModel = ApplicationHandler.instantiateObject(AuditAgentContractModel.class, requestParameters);
            auditAgentContractModel.validateAndThrow();
            return agentContractService.auditAgentContract(auditAgentContractModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "审核代理商合同失败", requestParameters);
    }

    /**
     * 终止代理商合同
     *
     * @return
     */
    @RequestMapping(value = "/terminateAgentContract", method = RequestMethod.POST)
    @ResponseBody
    public String terminateAgentContract() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            TerminateAgentContractModel terminateAgentContractModel = ApplicationHandler.instantiateObject(TerminateAgentContractModel.class, requestParameters);
            terminateAgentContractModel.validateAndThrow();
            return agentContractService.terminateAgentContract(terminateAgentContractModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "终止代理商合同失败", requestParameters);
    }

    /**
     * 分页查询代理商合同失败
     *
     * @return
     */
    @RequestMapping(value = "/listAgentContracts", method = RequestMethod.GET)
    @ResponseBody
    public String listAgentContracts() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ListAgentContractsModel listAgentContractsModel = ApplicationHandler.instantiateObject(ListAgentContractsModel.class, requestParameters);
            listAgentContractsModel.validateAndThrow();
            return agentContractService.listAgentContracts(listAgentContractsModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "查询代理商合同列表失败", requestParameters);
    }

    /**
     * 获取代理商合同信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainAgentContractInfo", method = RequestMethod.GET)
    @ResponseBody
    public String obtainAgentContractInfo() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ObtainAgentContractInfoModel obtainAgentContractInfoModel = ApplicationHandler.instantiateObject(ObtainAgentContractInfoModel.class, requestParameters);
            obtainAgentContractInfoModel.validateAndThrow();
            return agentContractService.obtainAgentContractInfo(obtainAgentContractInfoModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "获取代理商合同信息失败", requestParameters);
    }
}
