package build.dream.platform.controllers;

import build.dream.common.annotations.NotSaveAccessLog;
import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.common.utils.RSAUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/tool")
public class ToolController extends BasicController {
    @RequestMapping(value = "/generateKeyPair", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @NotSaveAccessLog
    public String generateKeyPair() {
        ApiRest apiRest = null;
        try {
            Map<String, byte[]> keyPair = RSAUtils.generateKeyPair(2048);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("publicKey", Base64.encodeBase64String(keyPair.get("publicKey")));
            data.put("privateKey", Base64.encodeBase64String(keyPair.get("privateKey")));

            apiRest = ApiRest.builder().data(data).message("生成秘钥对成功！").build();
        } catch (Exception e) {
            LogUtils.error("生成秘钥对失败", className, "generateKeyPair", e);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
