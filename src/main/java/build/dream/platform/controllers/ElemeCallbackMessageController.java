package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.services.ElemeCallbackMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/elemeCallbackMessage")
public class ElemeCallbackMessageController extends BasicController {
    @Autowired
    private ElemeCallbackMessageService elemeCallbackMessageService;

    @RequestMapping(value = "/markHandleFailureMessage")
    @ResponseBody
    public String saveElemeCallbackMessage() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            String uuid = requestParameters.get("uuid");
            ApplicationHandler.notNull(uuid, "uuid");
            apiRest = elemeCallbackMessageService.markHandleFailureMessage(uuid);
        } catch (Exception e) {
            LogUtils.error("标记处理失败消息失败", controllerSimpleName, "markHandleFailureMessage", e.getClass().getSimpleName(), e.getMessage(), requestParameters);
            apiRest = new ApiRest();
            apiRest.setError(e.getMessage());
            apiRest.setSuccessful(false);
        }
        return GsonUtils.toJson(apiRest);
    }
}
