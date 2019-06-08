package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.models.notify.SaveAsyncNotifyModel;
import build.dream.platform.services.NotifyService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/notify")
public class NotifyController {
    /**
     * 保存异步通知
     *
     * @return
     */
    @RequestMapping(value = "/saveAsyncNotify", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveAsyncNotifyModel.class, serviceClass = NotifyService.class, serviceMethodName = "saveAsyncNotify", error = "保存异步通知失败")
    public String saveAsyncNotify() {
        return null;
    }
}
