package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/tenant")
public class TenantController {

    @RequestMapping(value = "/index")
    @ResponseBody
    public String index() {
        ApiRest apiRest = new ApiRest();
        apiRest.setData(ApplicationHandler.getRequestParameters());
        ApiRest apiRest1 = GsonUtils.fromJson(GsonUtils.toJson(apiRest), ApiRest.class);
        Map<String, String> data = (Map<String, String>) apiRest1.getData();
        return GsonUtils.toJson(apiRest);
    }
}
