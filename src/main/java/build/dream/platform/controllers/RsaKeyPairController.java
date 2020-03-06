package build.dream.platform.controllers;

import build.dream.platform.constants.Constants;
import build.dream.platform.services.RsaKeyPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rsaKeyPair")
public class RsaKeyPairController {
    @Autowired
    private RsaKeyPairService rsaKeyPairService;

    /**
     * 缓存商户信息
     *
     * @return
     */
    @RequestMapping(value = "/cacheTenantInfos")
    @ResponseBody
    public String cacheTenantInfos() {
        try {
            rsaKeyPairService.cacheRsaKeyPairs();
            return Constants.SUCCESS;
        } catch (Exception e) {
            return Constants.FAILURE;
        }
    }
}
