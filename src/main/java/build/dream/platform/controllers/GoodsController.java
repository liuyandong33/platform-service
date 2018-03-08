package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.models.goods.ObtainAllGoodsInfosModel;
import build.dream.platform.models.goods.ObtainGoodsInfoModel;
import build.dream.platform.models.goods.SaveGoodsModel;
import build.dream.platform.services.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/goods")
public class GoodsController extends BasicController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 获取所有商品信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainAllGoodsInfos", method = RequestMethod.GET)
    @ResponseBody
    public String obtainAllGoodsInfos() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            ObtainAllGoodsInfosModel obtainAllGoodsInfosModel = ApplicationHandler.instantiateObject(ObtainAllGoodsInfosModel.class, requestParameters);
            obtainAllGoodsInfosModel.validateAndThrow();
            apiRest = goodsService.obtainAllGoodsInfos(obtainAllGoodsInfosModel);
        } catch (Exception e) {
            LogUtils.error("获取商品信息失败", controllerSimpleName, "obtainAllGoodsInfos", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    /**
     * 获取商品信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainGoodsInfo", method = RequestMethod.GET)
    @ResponseBody
    public String obtainGoodsInfo() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            ObtainGoodsInfoModel obtainGoodsInfoModel = ApplicationHandler.instantiateObject(ObtainGoodsInfoModel.class, requestParameters);
            obtainGoodsInfoModel.validateAndThrow();
            apiRest = goodsService.obtainGoodsInfo(obtainGoodsInfoModel);
        } catch (Exception e) {
            LogUtils.error("获取商品信息失败", controllerSimpleName, "obtainGoodsInfo", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/saveGoods", method = RequestMethod.POST)
    @ResponseBody
    public String saveGoods() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            SaveGoodsModel saveGoodsModel = ApplicationHandler.instantiateObject(SaveGoodsModel.class, requestParameters);
            String goodsSpecifications = requestParameters.get("goodsSpecifications");
            ApplicationHandler.notEmpty(goodsSpecifications, "goodsSpecifications");
            List<SaveGoodsModel.GoodsSpecificationModel> goodsSpecificationModels = GsonUtils.jsonToList(goodsSpecifications, SaveGoodsModel.GoodsSpecificationModel.class);
            saveGoodsModel.setGoodsSpecificationModels(goodsSpecificationModels);
            saveGoodsModel.validateAndThrow();

            apiRest = goodsService.saveGoods(saveGoodsModel);
        } catch (Exception e) {
            LogUtils.error("保存商品信息失败", controllerSimpleName, "saveGoods", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
