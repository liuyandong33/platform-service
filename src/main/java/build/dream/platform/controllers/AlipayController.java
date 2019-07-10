package build.dream.platform.controllers;

import build.dream.platform.constants.Constants;
import build.dream.platform.services.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/alipay")
public class AlipayController {
    @Autowired
    private AlipayService alipayService;

    @RequestMapping(value = "/cacheAlipayDeveloperAccounts")
    @ResponseBody
    public String cacheAlipayDeveloperAccounts() {
        alipayService.cacheAlipayDeveloperAccounts();
        return Constants.SUCCESS;
    }

    @RequestMapping(value = "/cacheAlipayAccounts")
    @ResponseBody
    public String cacheAlipayAccounts() {
        alipayService.cacheAlipayAccounts();
        return Constants.SUCCESS;
    }
}
