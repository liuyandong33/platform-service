package build.dream.platform.controllers;

import build.dream.common.annotations.Action;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.platform.models.agentcontract.*;
import build.dream.platform.services.AgentContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
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
    @RequestMapping(value = "/saveAgentContract", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @Action(error = "保存代理商合同失败")
    public String saveAgentContract() throws InstantiationException, IllegalAccessException, ParseException, NoSuchFieldException {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        SaveAgentContractModel saveAgentContractModel = ApplicationHandler.instantiateObject(SaveAgentContractModel.class, requestParameters);
        String contractPriceInfos = requestParameters.get("contractPriceInfos");
        saveAgentContractModel.setContractPriceInfos(contractPriceInfos);
        saveAgentContractModel.validateAndThrow();
        return GsonUtils.toJson(agentContractService.saveAgentContract(saveAgentContractModel));
    }

    /**
     * 审核代理商合同
     *
     * @return
     */
    @RequestMapping(value = "/auditAgentContract", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @Action(error = "审核代理商合同失败")
    public String auditAgentContract() throws InstantiationException, IllegalAccessException, ParseException, NoSuchFieldException {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        AuditAgentContractModel auditAgentContractModel = ApplicationHandler.instantiateObject(AuditAgentContractModel.class, requestParameters);
        auditAgentContractModel.validateAndThrow();
        return GsonUtils.toJson(agentContractService.auditAgentContract(auditAgentContractModel));
    }

    /**
     * 终止代理商合同
     *
     * @return
     */
    @RequestMapping(value = "/terminateAgentContract", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @Action(error = "终止代理商合同失败")
    public String terminateAgentContract() throws InstantiationException, IllegalAccessException, ParseException, NoSuchFieldException {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        TerminateAgentContractModel terminateAgentContractModel = ApplicationHandler.instantiateObject(TerminateAgentContractModel.class, requestParameters);
        terminateAgentContractModel.validateAndThrow();
        return GsonUtils.toJson(agentContractService.terminateAgentContract(terminateAgentContractModel));
    }

    /**
     * 分页查询代理商合同失败
     *
     * @return
     */
    @RequestMapping(value = "/listAgentContracts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @Action(error = "查询代理商合同列表失败")
    public String listAgentContracts() throws InstantiationException, IllegalAccessException, ParseException, NoSuchFieldException {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        ListAgentContractsModel listAgentContractsModel = ApplicationHandler.instantiateObject(ListAgentContractsModel.class, requestParameters);
        listAgentContractsModel.validateAndThrow();
        return GsonUtils.toJson(agentContractService.listAgentContracts(listAgentContractsModel));
    }

    /**
     * 获取代理商合同信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainAgentContractInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @Action(error = "获取代理商合同信息失败")
    public String obtainAgentContractInfo() throws InstantiationException, IllegalAccessException, ParseException, NoSuchFieldException {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        ObtainAgentContractInfoModel obtainAgentContractInfoModel = ApplicationHandler.instantiateObject(ObtainAgentContractInfoModel.class, requestParameters);
        obtainAgentContractInfoModel.validateAndThrow();
        return GsonUtils.toJson(agentContractService.obtainAgentContractInfo(obtainAgentContractInfoModel));
    }
}
