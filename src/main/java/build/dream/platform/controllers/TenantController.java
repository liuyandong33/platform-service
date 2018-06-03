package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.platform.models.tenant.FindAllGoodsInfosModel;
import build.dream.platform.models.tenant.FindGoodsInfoModel;
import build.dream.platform.models.tenant.ObtainTenantInfoModel;
import build.dream.platform.services.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/tenant")
public class TenantController extends BasicController {
    @Autowired
    private TenantService tenantService;

    @RequestMapping(value = "/obtainTenantInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "查询商户信息失败")
    public String obtainTenantInfo() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        ObtainTenantInfoModel obtainTenantInfoModel = ApplicationHandler.instantiateObject(ObtainTenantInfoModel.class, requestParameters);
        obtainTenantInfoModel.validateAndThrow();

        return GsonUtils.toJson(tenantService.obtainTenantInfo(obtainTenantInfoModel));
    }

    @RequestMapping(value = "/findAllGoodsInfos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "查询产品购买信息失败")
    public String findAllGoodsInfos() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        FindAllGoodsInfosModel findAllGoodsInfosModel = ApplicationHandler.instantiateObject(FindAllGoodsInfosModel.class, requestParameters);
        findAllGoodsInfosModel.validateAndThrow();

        return GsonUtils.toJson(tenantService.findAllGoodsInfos(findAllGoodsInfosModel));
    }

    @RequestMapping(value = "/findGoodsInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "查询产品购买信息失败")
    public String findGoodsInfo() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        FindGoodsInfoModel findGoodsInfoModel = ApplicationHandler.instantiateObject(FindGoodsInfoModel.class, requestParameters);
        findGoodsInfoModel.validateAndThrow();

        return GsonUtils.toJson(tenantService.findGoodsInfo(findGoodsInfoModel));
    }
}
