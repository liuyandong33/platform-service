package build.dream.platform.controllers;

import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.MethodCaller;
import build.dream.platform.models.goods.ListGoodsInfosModel;
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
public class GoodsController {
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
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ObtainAllGoodsInfosModel obtainAllGoodsInfosModel = ApplicationHandler.instantiateObject(ObtainAllGoodsInfosModel.class, requestParameters);
            obtainAllGoodsInfosModel.validateAndThrow();
            return goodsService.obtainAllGoodsInfos(obtainAllGoodsInfosModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "获取商品信息失败", requestParameters);
    }

    /**
     * 分页查询商品列表
     *
     * @return
     */
    @RequestMapping(value = "/listGoodsInfos", method = RequestMethod.GET)
    @ResponseBody
    public String listGoodsInfos() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ListGoodsInfosModel listGoodsInfosModel = ApplicationHandler.instantiateObject(ListGoodsInfosModel.class, requestParameters);
            listGoodsInfosModel.validateAndThrow();
            return goodsService.listGoodsInfos(listGoodsInfosModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "分页查询商品列表失败", requestParameters);
    }

    /**
     * 获取商品信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainGoodsInfo", method = RequestMethod.GET)
    @ResponseBody
    public String obtainGoodsInfo() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ObtainGoodsInfoModel obtainGoodsInfoModel = ApplicationHandler.instantiateObject(ObtainGoodsInfoModel.class, requestParameters);
            obtainGoodsInfoModel.validateAndThrow();
            return goodsService.obtainGoodsInfo(obtainGoodsInfoModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "获取商品信息失败", requestParameters);
    }

    @RequestMapping(value = "/saveGoods", method = RequestMethod.POST)
    @ResponseBody
    public String saveGoods() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            SaveGoodsModel saveGoodsModel = ApplicationHandler.instantiateObject(SaveGoodsModel.class, requestParameters);
            String goodsSpecifications = requestParameters.get("goodsSpecifications");
            ApplicationHandler.notEmpty(goodsSpecifications, "goodsSpecifications");
            List<SaveGoodsModel.GoodsSpecificationModel> goodsSpecificationModels = GsonUtils.jsonToList(goodsSpecifications, SaveGoodsModel.GoodsSpecificationModel.class);
            saveGoodsModel.setGoodsSpecificationModels(goodsSpecificationModels);
            saveGoodsModel.validateAndThrow();

            return goodsService.saveGoods(saveGoodsModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "保存商品信息失败", requestParameters);
    }
}
