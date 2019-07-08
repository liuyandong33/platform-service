package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.platform.models.jddj.ListJDDJCodesModel;
import build.dream.platform.services.JDDJService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/jddj")
public class JDDJController {
    @RequestMapping(value = "/listJDDJCodes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListJDDJCodesModel.class, serviceClass = JDDJService.class, serviceMethodName = "listJDDJCodes", error = "分页查询京东到家code失败！")
    public String listJDDJCodes() {
        return null;
    }
}
