package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.models.goods.ObtainAllGoodsInfosModel;
import build.dream.platform.models.order.ObtainAllOrderInfosModel;
import build.dream.platform.services.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/goods")
public class GoodsController extends BasicController {
    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "/obtainAllGoodsInfos", method = RequestMethod.GET)
    @ResponseBody
    public String obtainAllOrderInfos() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            ObtainAllGoodsInfosModel obtainAllGoodsInfosModel = ApplicationHandler.instantiateObject(ObtainAllGoodsInfosModel.class, requestParameters);
            obtainAllGoodsInfosModel.validateAndThrow();
            apiRest = goodsService.obtainAllOrderInfos(obtainAllGoodsInfosModel);
        } catch (Exception e) {
            LogUtils.error("获取商品信息失败", controllerSimpleName, "obtainAllOrderInfos", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
