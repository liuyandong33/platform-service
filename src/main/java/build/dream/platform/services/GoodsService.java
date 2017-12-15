package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.Goods;
import build.dream.common.saas.domains.GoodsSpecification;
import build.dream.common.saas.domains.Order;
import build.dream.common.saas.domains.OrderDetail;
import build.dream.common.utils.PagedSearchModel;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.GoodsMapper;
import build.dream.platform.mappers.GoodsSpecificationMapper;
import build.dream.platform.models.goods.ObtainAllGoodsInfosModel;
import build.dream.platform.models.goods.SaveGoodsModel;
import build.dream.platform.models.order.ObtainAllOrderInfosModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsSpecificationMapper goodsSpecificationMapper;

    @Transactional(readOnly = true)
    public ApiRest obtainAllOrderInfos(ObtainAllGoodsInfosModel obtainAllGoodsInfosModel) {
        PagedSearchModel goodsPagedSearchModel = new PagedSearchModel(true);
        goodsPagedSearchModel.setOffsetAndMaxResults(obtainAllGoodsInfosModel.getPage(), obtainAllGoodsInfosModel.getRows());
        List<Goods> goodses = goodsMapper.findAllPaged(goodsPagedSearchModel);

        SearchModel goodsSearchModel = new SearchModel(true);
        long total = goodsMapper.count(goodsSearchModel);

        List<BigInteger> goodsIds = new ArrayList<BigInteger>();
        for (Goods goods : goodses) {
            goodsIds.add(goods.getId());
        }

        SearchModel goodsSpecificationSearchModel = new SearchModel(true);
        goodsSpecificationSearchModel.addSearchCondition("goods_id", Constants.SQL_OPERATION_SYMBOL_IN, goodsIds);
        List<GoodsSpecification> goodsSpecifications = goodsSpecificationMapper.findAll(goodsSpecificationSearchModel);
        Map<BigInteger, List<GoodsSpecification>> goodsSpecificationsMap = new HashMap<BigInteger, List<GoodsSpecification>>();
        for (GoodsSpecification goodsSpecification : goodsSpecifications) {
            List<GoodsSpecification> goodsSpecificationList = goodsSpecificationsMap.get(goodsSpecification.getGoodsId());
            if (goodsSpecificationList == null) {
                goodsSpecificationList = new ArrayList<GoodsSpecification>();
                goodsSpecificationsMap.put(goodsSpecification.getGoodsId(), goodsSpecificationList);
            }
            goodsSpecificationList.add(goodsSpecification);
        }

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        for (Goods goods : goodses) {
            Map<String, Object> row = new HashMap<String, Object>();
            row.put("goods", goods);
            row.put("goodsSpecifications", goodsSpecificationsMap.get(goods.getId()));
            rows.add(row);
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("rows", rows);
        data.put("total", total);
        ApiRest apiRest = new ApiRest();
        apiRest.setData(data);
        apiRest.setMessage("获取商品信息成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveGoods(SaveGoodsModel saveGoodsModel) {
        return null;
    }
}
