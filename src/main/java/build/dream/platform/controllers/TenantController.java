package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.controllers.BasicController;
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
    @RequestMapping(value = "/obtainAllGoodsInfos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainAllGoodsInfosModel.class, serviceClass = TenantService.class, serviceMethodName = "obtainAllGoodsInfos", error = "查询产品购买信息失败")
    public String obtainAllGoodsInfos() {
        return null;
    }

    /**
     * 查询商户购买的产品
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/obtainGoodsInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainGoodsInfoModel.class, serviceClass = TenantService.class, serviceMethodName = "obtainGoodsInfo", error = "查询产品购买信息失败")
    public String obtainGoodsInfo() {
        return null;
    }

    /**
     * 获取支付账号
     *
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
     *
     * @return
     */
    @RequestMapping(value = "/obtainTenantSecretKey", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainTenantSecretKeyModel.class, serviceClass = TenantService.class, serviceMethodName = "obtainTenantSecretKey", error = "获取商户秘钥失败")
    public String obtainTenantSecretKey() {
        return null;
    }

    /**
     * 修改商户信息
     *
     * @return
     */
    @RequestMapping(value = "/updateTenantInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = UpdateTenantInfoModel.class, serviceClass = TenantService.class, serviceMethodName = "updateTenantInfo", error = "更新商户信息失败")
    public String updateTenantInfo() {
        return null;
    }

    /**
     * 查询商户列表
     *
     * @return
     */
    @RequestMapping(value = "/listTenantInfos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListTenantInfosModel.class, serviceClass = TenantService.class, serviceMethodName = "listTenantInfos", error = "查询商户信息失败")
    public String listTenantInfos() {
        return null;
    }


    @RequestMapping(value = "/updateBranchCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = UpdateBranchCountModel.class, serviceClass = TenantService.class, serviceMethodName = "changeBranchCount", error = "修改门店数量失败")
    public String updateBranchCount() {
        return null;
    }
}
