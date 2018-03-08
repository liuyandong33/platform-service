package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.Goods;
import build.dream.common.saas.domains.GoodsSpecification;
import build.dream.common.saas.domains.GoodsType;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.GoodsMapper;
import build.dream.platform.mappers.GoodsSpecificationMapper;
import build.dream.platform.mappers.GoodsTypeMapper;
import build.dream.platform.models.goods.ObtainAllGoodsInfosModel;
import build.dream.platform.models.goods.ObtainGoodsInfoModel;
import build.dream.platform.models.goods.SaveGoodsModel;
import build.dream.platform.utils.GoodsUtils;
import org.apache.commons.lang.Validate;
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
    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    /**
     * 获取商品信息
     *
     * @param obtainAllGoodsInfosModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainAllOrderInfos(ObtainAllGoodsInfosModel obtainAllGoodsInfosModel) {
        SearchModel goodsSearchModel = new SearchModel(true);
        List<Goods> goodses = goodsMapper.findAll(goodsSearchModel);

        SearchModel goodsSpecificationSearchModel = new SearchModel(true);
        List<GoodsSpecification> goodsSpecifications = goodsSpecificationMapper.findAll(goodsSpecificationSearchModel);
        Map<BigInteger, List<Map<String, Object>>> goodsSpecificationInfoMap = new HashMap<BigInteger, List<Map<String, Object>>>();
        for (GoodsSpecification goodsSpecification : goodsSpecifications) {
            List<Map<String, Object>> goodsSpecificationInfos = goodsSpecificationInfoMap.get(goodsSpecification.getGoodsId());
            if (goodsSpecificationInfos == null) {
                goodsSpecificationInfos = new ArrayList<Map<String, Object>>();
                goodsSpecificationInfoMap.put(goodsSpecification.getGoodsId(), goodsSpecificationInfos);
            }
            goodsSpecificationInfos.add(GoodsUtils.buildGoodsSpecificationInfo(goodsSpecification));
        }

        List<Map<String, Object>> goodsInfos = new ArrayList<Map<String, Object>>();
        for (Goods goods : goodses) {
            Map<String, Object> goodsInfo = GoodsUtils.buildGoodsInfo(goods);
            goodsInfo.put("goodsSpecification", goodsSpecificationInfoMap.get(goods.getId()));
            goodsInfos.add(goodsInfo);
        }
        return new ApiRest(goodsInfos, "获取商品信息成功！");
    }

    /**
     * 获取商品信息
     *
     * @param obtainGoodsInfoModel
     * @return
     */
    public ApiRest obtainGoodsInfo(ObtainGoodsInfoModel obtainGoodsInfoModel) {
        BigInteger goodsId = obtainGoodsInfoModel.getGoodsId();
        SearchModel goodsSearchModel = new SearchModel(true);
        goodsSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, goodsId);
        Goods goods = goodsMapper.find(goodsSearchModel);
        Validate.notNull(goods, "商品不存在！");

        SearchModel goodsSpecificationSearchModel = new SearchModel(true);
        goodsSpecificationSearchModel.addSearchCondition("goods_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, goodsId);
        List<GoodsSpecification> goodsSpecifications = goodsSpecificationMapper.findAll(goodsSpecificationSearchModel);
        List<Map<String, Object>> goodsSpecificationInfos = new ArrayList<Map<String, Object>>();
        for (GoodsSpecification goodsSpecification : goodsSpecifications) {
            goodsSpecificationInfos.add(GoodsUtils.buildGoodsSpecificationInfo(goodsSpecification));
        }
        Map<String, Object> goodsInfo = GoodsUtils.buildGoodsInfo(goods);
        goodsInfo.put("goodsSpecification", goodsSpecificationInfos);

        return new ApiRest(goodsInfo, "获取商品信息成功！");
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveGoods(SaveGoodsModel saveGoodsModel) {
        BigInteger goodsTypeId = saveGoodsModel.getGoodsTypeId();
        BigInteger goodsId = saveGoodsModel.getId();
        BigInteger userId = saveGoodsModel.getUserId();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, goodsTypeId);
        GoodsType goodsType = goodsTypeMapper.find(searchModel);
        Validate.notNull(goodsType, "商品类型不存在！");
        if (goodsType.isSingle()) {
            SearchModel countSearchModel = new SearchModel(true);
            countSearchModel.addSearchCondition("goods_type_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, goodsTypeId);
            if (goodsId != null) {
                searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_NOT_EQUALS, goodsId);
            }
            long count = goodsMapper.count(searchModel);
            Validate.isTrue(count == 0, "商品类型【" + goodsType.getName() + "】下只能创建一个商品！");
        }
        Goods goods = null;
        if (goodsId != null) {
            SearchModel goodsSearchModel = new SearchModel(true);
            goodsSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, goodsId);
            goods = goodsMapper.find(goodsSearchModel);
            Validate.notNull(goods, "商品不存在！");
            goods.setName(saveGoodsModel.getName());
            goods.setGoodsTypeId(saveGoodsModel.getGoodsTypeId());
            goods.setStatus(saveGoodsModel.getStatus());
            goods.setPhotoUrl(saveGoodsModel.getPhotoUrl());
            goods.setBusiness(saveGoodsModel.getBusiness());
            goods.setLastUpdateUserId(userId);
            goods.setLastUpdateRemark("修改商品信息！");
            goodsMapper.update(goods);

            List<BigInteger> goodsSpecificationIds = new ArrayList<BigInteger>();
            List<SaveGoodsModel.GoodsSpecificationModel> goodsSpecificationModels = saveGoodsModel.getGoodsSpecificationModels();
            for (SaveGoodsModel.GoodsSpecificationModel goodsSpecificationModel : goodsSpecificationModels) {
                if (goodsSpecificationModel.getId() != null) {
                    goodsSpecificationIds.add(goodsSpecificationModel.getId());
                }
            }
            SearchModel goodsSpecificationSearchModel = new SearchModel(true);
            goodsSpecificationSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, goodsSpecificationIds);
            goodsSpecificationSearchModel.addSearchCondition("goods_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, goodsId);
            List<GoodsSpecification> goodsSpecifications = goodsSpecificationMapper.findAll(goodsSpecificationSearchModel);
            Map<BigInteger, GoodsSpecification> goodsSpecificationMap = new HashMap<BigInteger, GoodsSpecification>();
            for (GoodsSpecification goodsSpecification : goodsSpecifications) {
                goodsSpecificationMap.put(goodsSpecification.getId(), goodsSpecification);
            }

            for (SaveGoodsModel.GoodsSpecificationModel goodsSpecificationModel : goodsSpecificationModels) {
                if (goodsSpecificationModel.getId() != null) {
                    GoodsSpecification goodsSpecification = goodsSpecificationMap.get(goodsSpecificationModel.getId());
                    Validate.notNull(goodsSpecification, "商品规格不存在！");
                    goodsSpecification.setName(goodsSpecificationModel.getName());
                    goodsSpecification.setAllowTenantBuy(goodsSpecificationModel.isAllowTenantBuy());
                    goodsSpecification.setAllowAgentBuy(goodsSpecificationModel.isAllowAgentBuy());
                    goodsSpecification.setRenewalTime(goodsSpecificationModel.getRenewalTime());
                    goodsSpecification.setTenantPrice(goodsSpecificationModel.getTenantPrice());
                    goodsSpecification.setAgentPrice(goodsSpecificationModel.getAgentPrice());
                    goodsSpecification.setLastUpdateUserId(userId);
                    goodsSpecification.setLastUpdateRemark("修改商品规格！");
                    goodsSpecificationMapper.update(goodsSpecification);
                } else {
                    GoodsSpecification goodsSpecification = GoodsUtils.buildGoodsSpecification(goodsSpecificationModel.getName(), goods.getId(), goodsSpecificationModel.isAllowTenantBuy(), goodsSpecificationModel.isAllowAgentBuy(), goodsSpecificationModel.getRenewalTime(), goodsSpecificationModel.getTenantPrice(), goodsSpecificationModel.getAgentPrice(), userId);
                    goodsSpecificationMapper.insert(goodsSpecification);
                }
            }
        } else {
            goods = new Goods();
            goods.setName(saveGoodsModel.getName());
            goods.setGoodsTypeId(saveGoodsModel.getGoodsTypeId());
            goods.setStatus(saveGoodsModel.getStatus());
            goods.setPhotoUrl(saveGoodsModel.getPhotoUrl());
            goods.setBusiness(saveGoodsModel.getBusiness());
            goods.setCreateUserId(userId);
            goods.setLastUpdateUserId(userId);
            goods.setLastUpdateRemark("新增商品信息！");
            goodsMapper.insert(goods);

            List<SaveGoodsModel.GoodsSpecificationModel> goodsSpecificationModels = saveGoodsModel.getGoodsSpecificationModels();
            for (SaveGoodsModel.GoodsSpecificationModel goodsSpecificationModel : goodsSpecificationModels) {
                GoodsSpecification goodsSpecification = GoodsUtils.buildGoodsSpecification(goodsSpecificationModel.getName(), goods.getId(), goodsSpecificationModel.isAllowTenantBuy(), goodsSpecificationModel.isAllowAgentBuy(), goodsSpecificationModel.getRenewalTime(), goodsSpecificationModel.getTenantPrice(), goodsSpecificationModel.getAgentPrice(), userId);
                goodsSpecificationMapper.insert(goodsSpecification);
            }
        }
        return new ApiRest(goods, "保存商品信息成功！");
    }
}
