package build.dream.platform.controllers;

import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.MethodCaller;
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
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ObtainTenantInfoModel obtainTenantInfoModel = ApplicationHandler.instantiateObject(ObtainTenantInfoModel.class, requestParameters);
            obtainTenantInfoModel.validateAndThrow();

            return tenantService.obtainTenantInfo(obtainTenantInfoModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "查询商户信息失败", requestParameters);
    }

    @RequestMapping(value = "/findAllGoodsInfos")
    @ResponseBody
    public String findAllGoodsInfos() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            FindAllGoodsInfosModel findAllGoodsInfosModel = ApplicationHandler.instantiateObject(FindAllGoodsInfosModel.class, requestParameters);
            findAllGoodsInfosModel.validateAndThrow();

            return tenantService.findAllGoodsInfos(findAllGoodsInfosModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "查询产品购买信息失败", requestParameters);
    }

    @RequestMapping(value = "/findGoodsInfo")
    @ResponseBody
    public String findGoodsInfo() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            FindGoodsInfoModel findGoodsInfoModel = ApplicationHandler.instantiateObject(FindGoodsInfoModel.class, requestParameters);
            findGoodsInfoModel.validateAndThrow();

            return tenantService.findGoodsInfo(findGoodsInfoModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "查询产品购买信息失败", requestParameters);
    }
}
