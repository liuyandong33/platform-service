package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.api.ApiRest;
import build.dream.common.utils.GsonUtils;
import build.dream.platform.constants.Constants;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/oss")
public class OssController {
    @RequestMapping(value = "/obtainPolicy")
    @ResponseBody
    @ApiRestAction(error = "获取签名失败")
    public String obtainPolicy() throws UnsupportedEncodingException {
        String endpoint = "oss-cn-qingdao.aliyuncs.com";
        String accessId = "LTAIzWtJmkzU0Uex";
        String accessKey = "6XIiGAie3fEPoUIhQpZMsXuPa80bwT";
        String bucket = "image-check-local";
        String dir = "user-dir";
        String host = "http://" + bucket + "." + endpoint;
        OSSClient client = new OSSClient(endpoint, accessId, accessKey);
        long expireTime = 30;
        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime);
        PolicyConditions policyConditions = new PolicyConditions();
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

        String postPolicy = client.generatePostPolicy(expiration, policyConditions);
        String encodedPolicy = Base64.encodeBase64String(postPolicy.getBytes(Constants.CHARSET_NAME_UTF_8));
        String signature = client.calculatePostSignature(postPolicy);

        Map<String, String> callback = new HashMap<String, String>();
        callback.put("callbackUrl", "http://check-local.smartpos.top/zd1/ct3/proxy/doGet");
        callback.put("callbackHost", "oss-demo.aliyuncs.com");
        callback.put("callbackBody", "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
        callback.put("callbackBodyType", "application/x-www-form-urlencoded");

        Map<String, String> data = new LinkedHashMap<String, String>();
        data.put("accessid", accessId);
        data.put("policy", encodedPolicy);
        data.put("signature", signature);
        data.put("dir", dir);
        data.put("host", host);
        data.put("expire", String.valueOf(expireEndTime / 1000));
        data.put("callback", Base64.encodeBase64String(GsonUtils.toJson(callback).getBytes(Constants.CHARSET_NAME_UTF_8)));

        ApiRest apiRest = new ApiRest();
        apiRest = new ApiRest();
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
