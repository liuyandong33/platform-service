package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.platform.models.alipay.SaveAlipayAccountModel;
import build.dream.platform.services.AlipayService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/alipay")
public class AlipayController {
    @RequestMapping(value = "/saveAlipayAccount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveAlipayAccountModel.class, serviceClass = AlipayService.class, serviceMethodName = "saveAlipayAccount", error = "保存支付宝账号失败")
    public String saveAlipayAccount() {
        return null;
    }
}
