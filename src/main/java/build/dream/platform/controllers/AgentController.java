package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.platform.models.agent.DeleteAgentModel;
import build.dream.platform.models.agent.ListModel;
import build.dream.platform.models.agent.ObtainAgentInfoModel;
import build.dream.platform.models.agent.SaveAgentFormModel;
import build.dream.platform.services.AgentService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/agent")
public class AgentController {
    /**
     * 获取代理商信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainAgentInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainAgentInfoModel.class, serviceClass = AgentService.class, serviceMethodName = "obtainAgentInfo", error = "获取代理商信息失败")
    public String obtainAgentInfo() {
        return null;
    }

    /**
     * 分页查询代理商信息
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListModel.class, serviceClass = AgentService.class, serviceMethodName = "list", error = "查询代理商信息失败")
    public String list() {
        return null;
    }

    /**
     * 删除代理商信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteAgent", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = DeleteAgentModel.class, serviceClass = AgentService.class, serviceMethodName = "deleteAgent", error = "删除代理商信息失败")
    public String deleteAgent() {
        return null;
    }

    /**
     * 保存代理商申请单
     *
     * @return
     */
    @RequestMapping(value = "/saveAgentForm", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveAgentFormModel.class, serviceClass = AgentService.class, serviceMethodName = "saveAgentForm", error = "保存代理商申请单失败")
    public String saveAgentForm() {
        return null;
    }
}
