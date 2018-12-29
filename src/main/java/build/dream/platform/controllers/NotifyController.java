package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.models.notify.SaveNotifyRecordModel;
import build.dream.platform.services.NotifyService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/notify")
public class NotifyController {
    @RequestMapping(value = "/saveNotifyRecord", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveNotifyRecordModel.class, serviceClass = NotifyService.class, serviceMethodName = "", error = "保存回调记录失败")
    public String saveNotifyRecord() {
        return null;
    }
}
