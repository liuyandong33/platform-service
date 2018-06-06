package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.api.ApiRest;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.OSSUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping(value = "/oss")
public class OssController {
    @RequestMapping(value = "/obtainPolicy")
    @ResponseBody
    @ApiRestAction(error = "获取签名失败")
    public String obtainPolicy() throws UnsupportedEncodingException {
        String accessId = "LTAIzWtJmkzU0Uex";
        String accessKey = "6XIiGAie3fEPoUIhQpZMsXuPa80bwT";
        String endpoint = "oss-cn-qingdao.aliyuncs.com";
        String bucket = "image-check-local";
        String dir = "user-dir";

        String host = "http://" + bucket + "." + endpoint;

        Date expiration = DateUtils.addSeconds(new Date(), 300000);

        List<Object[]> conditions = new ArrayList<Object[]>();
        conditions.add(new Object[]{"content-length-range", 0, 1048576000});
        conditions.add(new Object[]{"starts-with", "$key", dir});


        Map<String, String> callback = new HashMap<String, String>();
        callback.put("callbackUrl", "https://check-local.smartpos.top/zd1/ct2/oss/obtainPolicy");
        callback.put("callbackHost", "check-local.smartpos.top");
        callback.put("callbackBody", "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
        callback.put("callbackBodyType", "application/x-www-form-urlencoded");

        Map<String, String> data = OSSUtils.obtainPolicy(accessId, accessKey, host, dir, expiration, conditions, callback);

        ApiRest apiRest = new ApiRest();
        apiRest.setData(data);
        apiRest.setSuccessful(true);
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("demo/index");
        return modelAndView;
    }
}
