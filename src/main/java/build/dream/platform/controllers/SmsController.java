package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.CacheUtils;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/sms")
public class SmsController extends BasicController {
    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "/sendSms")
    @ResponseBody
    public String sendSms() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            String templateId = requestParameters.get("templateId");
            Validate.notNull(templateId, ApplicationHandler.obtainParameterErrorMessage("templateId"));

            String mobile = requestParameters.get("mobile");
            Validate.notNull(mobile, ApplicationHandler.obtainParameterErrorMessage("mobile"));

            String params = requestParameters.get("params");

            String needUp = requestParameters.get("needUp");

            apiRest = smsService.sendSms(templateId, mobile, params, needUp);
        } catch (Exception e) {
            LogUtils.error("发送短息失败", controllerSimpleName, "sendSms", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/sendVerificationCode")
    @ResponseBody
    public String sendVerificationCode() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            String phoneNumber = requestParameters.get("phoneNumber");
            Validate.notNull(phoneNumber, ApplicationHandler.obtainParameterErrorMessage("phoneNumber"));

            String needUp = requestParameters.get("needUp");

            String verificationCodeLength = requestParameters.get("verificationCodeLength");
            if (StringUtils.isNotBlank(verificationCodeLength)) {
                verificationCodeLength = "4";
            }
            String templateId = "";

            String verificationCode = RandomStringUtils.randomNumeric(Integer.valueOf(verificationCodeLength));
            String params = "[" + verificationCode + "]";
            String[] phoneNumberArray = phoneNumber.split(",");
            apiRest = smsService.sendSms(templateId, GsonUtils.toJson(phoneNumberArray), params, needUp);
            if (apiRest.isSuccessful()) {
                Map<String, String> map = new HashMap<String, String>();
                for (String mobile : phoneNumberArray) {
                    map.put(mobile, verificationCode);
                }
                CacheUtils.mset(map);
            }
        } catch (Exception e) {
            LogUtils.error("发送验证码失败", controllerSimpleName, "sendVerificationCode", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
