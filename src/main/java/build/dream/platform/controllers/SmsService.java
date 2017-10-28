package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SmsService {
    public ApiRest sendSms(String templateId, String mobile, String params, String needUp) throws IOException {
        String appKey = ConfigurationUtils.getConfiguration(Constants.SMS_PLATFORM_APP_ID);
        String appSecret = ConfigurationUtils.getConfiguration(Constants.SMS_PLATFORM_APP_SECRET);
        String nonce = UUID.randomUUID().toString();
        String curTime = String.valueOf(System.currentTimeMillis() / 1000);
        String checkSum = DigestUtils.sha1Hex(appSecret + nonce + curTime);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        headers.put("AppKey", appKey);
        headers.put("Nonce", nonce);
        headers.put("CurTime", curTime);
        headers.put("CheckSum", checkSum);

        Map<String, String> sendSmsRequestParameters = new HashMap<String, String>();
        sendSmsRequestParameters.put("templateId", templateId);
        sendSmsRequestParameters.put("mobile", mobile);
        if (StringUtils.isNotBlank(params)) {
            sendSmsRequestParameters.put("params", params);
        }
        if (StringUtils.isNotBlank(needUp)) {
            sendSmsRequestParameters.put("needUp", needUp);
        }

        String requestUrl = ConfigurationUtils.getConfiguration(Constants.SMS_PLATFORM_SERVICE_URL);
        String result = WebUtils.doPostWithRequestParameters(requestUrl, headers, sendSmsRequestParameters);
        JSONObject resultJsonObject = JSONObject.fromObject(result);
        Validate.isTrue(resultJsonObject.getInt("code") == 200, resultJsonObject.getString("msg"));

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("短信发送成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
