package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.models.tenant.ObtainTenantInfoModel;
import build.dream.platform.services.TenantService;
import org.apache.commons.lang.math.NumberUtils;
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

    @RequestMapping(value = "/findAllGoodses")
    @ResponseBody
    public String findAllGoodses() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            String tenantId = requestParameters.get("tenantId");
            ApplicationHandler.notEmpty(tenantId, "tenantId");

            String branchId = requestParameters.get("branchId");
            ApplicationHandler.notEmpty(branchId, "branchId");

            apiRest = tenantService.findAllGoodses(NumberUtils.createBigInteger(tenantId), NumberUtils.createBigInteger(branchId));
        } catch (Exception e) {
            LogUtils.error("查询产品购买信息失败", controllerSimpleName, "findAllGoodses", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/findGoods")
    @ResponseBody
    public String findGoods() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            String tenantId = requestParameters.get("tenantId");
            ApplicationHandler.notEmpty(tenantId, "tenantId");

            String branchId = requestParameters.get("branchId");
            ApplicationHandler.notEmpty(branchId, "branchId");

            String goodsId = requestParameters.get("goodsId");
            ApplicationHandler.notEmpty(goodsId, "goodsId");

            apiRest = tenantService.findGoods(NumberUtils.createBigInteger(tenantId), NumberUtils.createBigInteger(branchId), NumberUtils.createBigInteger(goodsId));
        } catch (Exception e) {
            LogUtils.error("查询产品购买信息失败", controllerSimpleName, "findGoods", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
