package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.controllers.BasicController;
import build.dream.platform.models.agentcontract.*;
import build.dream.platform.services.AgentContractService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/agentContract")
public class AgentContractController extends BasicController {
    /**
     * 保存代理商合同
     *
     * @return
     */
    @RequestMapping(value = "/saveAgentContract", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveAgentContractModel.class, serviceClass = AgentContractService.class, serviceMethodName = "saveAgentContract", error = "保存代理商合同失败")
    public String saveAgentContract() {
        return null;
    }

    /**
     * 审核代理商合同
     *
     * @return
     */
    @RequestMapping(value = "/auditAgentContract", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = AuditAgentContractModel.class, serviceClass = AgentContractService.class, serviceMethodName = "auditAgentContract", error = "审核代理商合同失败")
    public String auditAgentContract() {
        return null;
    }

    /**
     * 终止代理商合同
     *
     * @return
     */
    @RequestMapping(value = "/terminateAgentContract", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = TerminateAgentContractModel.class, serviceClass = AgentContractService.class, serviceMethodName = "terminateAgentContract", error = "终止代理商合同失败")
    public String terminateAgentContract() {
        return null;
    }

    /**
     * 分页查询代理商合同失败
     *
     * @return
     */
    @RequestMapping(value = "/listAgentContracts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListAgentContractsModel.class, serviceClass = AgentContractService.class, serviceMethodName = "listAgentContracts", error = "查询代理商合同列表失败")
    public String listAgentContracts() {
        return null;
    }

    /**
     * 获取代理商合同信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainAgentContractInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainAgentContractInfoModel.class, serviceClass = AgentContractService.class, serviceMethodName = "obtainAgentContractInfo", error = "获取代理商合同信息失败")
    public String obtainAgentContractInfo() {
        return null;
    }
}
