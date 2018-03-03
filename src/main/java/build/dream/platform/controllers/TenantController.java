package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.models.tenant.FindAllGoodsInfosModel;
import build.dream.platform.models.tenant.FindGoodsInfoModel;
import build.dream.platform.models.tenant.ObtainTenantInfoModel;
import build.dream.platform.services.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/tenant")
public class TenantController extends BasicController {
    @Autowired
    private TenantService tenantService;

    @RequestMapping(value = "/obtainTenantInfo")
    @ResponseBody
    public String obtainTenantInfo() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            ObtainTenantInfoModel obtainTenantInfoModel = ApplicationHandler.instantiateObject(ObtainTenantInfoModel.class, requestParameters);
            obtainTenantInfoModel.validateAndThrow();

            apiRest = tenantService.obtainTenantInfo(obtainTenantInfoModel);
        } catch (Exception e) {
            LogUtils.error("查询商户信息失败", controllerSimpleName, "findTenantInfoById", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/findAllGoodsInfos")
    @ResponseBody
    public String findAllGoodsInfos() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            FindAllGoodsInfosModel findAllGoodsInfosModel = ApplicationHandler.instantiateObject(FindAllGoodsInfosModel.class, requestParameters);
            findAllGoodsInfosModel.validateAndThrow();

            apiRest = tenantService.findAllGoodsInfos(findAllGoodsInfosModel);
        } catch (Exception e) {
            LogUtils.error("查询产品购买信息失败", controllerSimpleName, "findAllGoodsInfos", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/findGoodsInfo")
    @ResponseBody
    public String findGoodsInfo() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            FindGoodsInfoModel findGoodsInfoModel = ApplicationHandler.instantiateObject(FindGoodsInfoModel.class, requestParameters);
            findGoodsInfoModel.validateAndThrow();

            apiRest = tenantService.findGoodsInfo(findGoodsInfoModel);
        } catch (Exception e) {
            LogUtils.error("查询产品购买信息失败", controllerSimpleName, "findGoodsInfo", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
