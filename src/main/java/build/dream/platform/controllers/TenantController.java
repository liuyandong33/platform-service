package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.controllers.BasicController;
import build.dream.common.saas.domains.TenantSecretKey;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.ValidateUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.tenant.*;
import build.dream.platform.services.TenantService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/tenant")
public class TenantController extends BasicController {
    /**
     * 获取商户信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainTenantInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainTenantInfoModel.class, serviceClass = TenantService.class, serviceMethodName = "obtainTenantInfo", error = "查询商户信息失败")
    public String obtainTenantInfo() {
        return null;
    }

    /**
     * 查询门店购买的所有产品
     *
     * @return
     */
    @RequestMapping(value = "/findAllGoodsInfos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = FindAllGoodsInfosModel.class, serviceClass = TenantService.class, serviceMethodName = "findAllGoodsInfos", error = "查询产品购买信息失败")
    public String findAllGoodsInfos() {
        return null;
    }

    /**
     * 查询商户购买的产品
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/findGoodsInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = FindGoodsInfoModel.class, serviceClass = TenantService.class, serviceMethodName = "findGoodsInfo", error = "查询产品购买信息失败")
    public String findGoodsInfo() {
        return null;
    }

    /**
     * 获取支付账号
     * @return
     */
    @RequestMapping(value = "/obtainPayAccounts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainPayAccountsModel.class, serviceClass = TenantService.class, serviceMethodName = "obtainPayAccounts", error = "获取支付账号失败")
    public String obtainPayAccounts() {
        return null;
    }

    /**
     * 获取商户秘钥
     * @return
     */
    @RequestMapping(value = "/obtainTenantSecretKey", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainTenantSecretKeyModel.class, serviceClass = TenantService.class, serviceMethodName = "obtainTenantSecretKey", error = "获取商户秘钥失败")
    public String obtainTenantSecretKey() {
        return null;
    }
}
