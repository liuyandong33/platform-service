package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
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
    @ApiRestAction(modelClass = ObtainAllGoodsInfosModel.class, serviceName = "goodsService", serviceMethodName = "obtainAllGoodsInfos", error = "获取商品信息失败")
    public String obtainAllGoodsInfos() {
        return null;
    }

    /**
     * 分页查询商品列表
     *
     * @return
     */
    @RequestMapping(value = "/listGoodsInfos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListGoodsInfosModel.class, serviceName = "goodsService", serviceMethodName = "listGoodsInfos", error = "分页查询商品列表失败")
    public String listGoodsInfos() {
        return null;
    }

    /**
     * 获取商品信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainGoodsInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainGoodsInfoModel.class, serviceName = "goodsService", serviceMethodName = "obtainGoodsInfo", error = "获取商品信息失败")
    public String obtainGoodsInfo() {
        return null;
    }

    @RequestMapping(value = "/saveGoods", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveGoodsModel.class, serviceName = "goodsService", serviceMethodName = "saveGoods", error = "获取商品信息失败")
    public String saveGoods() {
        return null;
    }
}
