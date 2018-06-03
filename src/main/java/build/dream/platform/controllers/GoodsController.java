package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.platform.models.goods.ListGoodsInfosModel;
import build.dream.platform.models.goods.ObtainAllGoodsInfosModel;
import build.dream.platform.models.goods.ObtainGoodsInfoModel;
import build.dream.platform.models.goods.SaveGoodsModel;
import build.dream.platform.services.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    @RequestMapping(value = "/obtainAllGoodsInfos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "获取商品信息失败")
    public String obtainAllGoodsInfos() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        ObtainAllGoodsInfosModel obtainAllGoodsInfosModel = ApplicationHandler.instantiateObject(ObtainAllGoodsInfosModel.class, requestParameters);
        obtainAllGoodsInfosModel.validateAndThrow();
        return GsonUtils.toJson(goodsService.obtainAllGoodsInfos(obtainAllGoodsInfosModel));
    }

    /**
     * 分页查询商品列表
     *
     * @return
     */
    @RequestMapping(value = "/listGoodsInfos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "分页查询商品列表失败")
    public String listGoodsInfos() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        ListGoodsInfosModel listGoodsInfosModel = ApplicationHandler.instantiateObject(ListGoodsInfosModel.class, requestParameters);
        listGoodsInfosModel.validateAndThrow();
        return GsonUtils.toJson(goodsService.listGoodsInfos(listGoodsInfosModel));
    }

    /**
     * 获取商品信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainGoodsInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "获取商品信息失败")
    public String obtainGoodsInfo() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        ObtainGoodsInfoModel obtainGoodsInfoModel = ApplicationHandler.instantiateObject(ObtainGoodsInfoModel.class, requestParameters);
        obtainGoodsInfoModel.validateAndThrow();
        return GsonUtils.toJson(goodsService.obtainGoodsInfo(obtainGoodsInfoModel));
    }

    @RequestMapping(value = "/saveGoods", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "保存商品信息失败")
    public String saveGoods() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        SaveGoodsModel saveGoodsModel = ApplicationHandler.instantiateObject(SaveGoodsModel.class, requestParameters);
        String goodsSpecifications = requestParameters.get("goodsSpecifications");
        ApplicationHandler.notEmpty(goodsSpecifications, "goodsSpecifications");
        List<SaveGoodsModel.GoodsSpecificationModel> goodsSpecificationModels = GsonUtils.jsonToList(goodsSpecifications, SaveGoodsModel.GoodsSpecificationModel.class);
        saveGoodsModel.setGoodsSpecificationModels(goodsSpecificationModels);
        saveGoodsModel.validateAndThrow();

        return GsonUtils.toJson(goodsService.saveGoods(saveGoodsModel));
    }
}
