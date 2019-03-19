package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.Goods;
import build.dream.common.saas.domains.GoodsSpecification;
import build.dream.common.saas.domains.GoodsType;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.goods.*;
import build.dream.platform.utils.GoodsUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    /**
     * 获取商品信息
     *
     * @param obtainAllGoodsInfosModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainAllGoodsInfos(ObtainAllGoodsInfosModel obtainAllGoodsInfosModel) {
        SearchModel goodsSearchModel = new SearchModel(true);
        List<Goods> goodses = DatabaseHelper.findAll(Goods.class, goodsSearchModel);

        SearchModel goodsSpecificationSearchModel = new SearchModel(true);
        List<GoodsSpecification> goodsSpecifications = DatabaseHelper.findAll(GoodsSpecification.class, goodsSpecificationSearchModel);

        Map<BigInteger, List<GoodsSpecification>> goodsSpecificationsMap = goodsSpecifications.stream().collect(Collectors.groupingBy(GoodsSpecification::getGoodsId));

        List<Map<String, Object>> goodsInfos = new ArrayList<Map<String, Object>>();
        for (Goods goods : goodses) {
            Map<String, Object> goodsInfo = GoodsUtils.buildGoodsInfo(goods);
            goodsInfo.put("specifications", GoodsUtils.buildGoodsSpecificationInfos(goodsSpecificationsMap.get(goods.getId())));
            goodsInfos.add(goodsInfo);
        }
        return ApiRest.builder().data(goodsInfos).message("获取商品信息成功！").successful(true).build();
    }

    /**
     * 分页查询商品列表
     *
     * @param listGoodsInfosModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest listGoodsInfos(ListGoodsInfosModel listGoodsInfosModel) {
        int page = listGoodsInfosModel.getPage();
        int rows = listGoodsInfosModel.getRows();
        String keyword = listGoodsInfosModel.getKeyword();

        List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
        searchConditions.add(new SearchCondition(Goods.ColumnName.DELETED_TIME, Constants.SQL_OPERATION_SYMBOL_EQUAL, 0));

        if (StringUtils.isNotBlank(keyword)) {
            searchConditions.add(new SearchCondition(Goods.ColumnName.NAME, Constants.SQL_OPERATION_SYMBOL_LIKE, "%" + keyword + "%"));
        }

        SearchModel searchModel = new SearchModel(true);
        searchModel.setSearchConditions(searchConditions);
        long count = DatabaseHelper.count(Goods.class, searchModel);

        List<Goods> goodses = null;
        if (count > 0) {
            PagedSearchModel pagedSearchModel = new PagedSearchModel(true);
            pagedSearchModel.setSearchConditions(searchConditions);
            pagedSearchModel.setPage(page);
            pagedSearchModel.setRows(rows);
            goodses = DatabaseHelper.findAllPaged(Goods.class, pagedSearchModel);
        } else {
            goodses = new ArrayList<Goods>();
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", count);
        data.put("rows", goodses);
        return ApiRest.builder().data(data).message("分页查询商品列表成功！").successful(true).build();
    }

    /**
     * 获取商品信息
     *
     * @param obtainGoodsInfoModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainGoodsInfo(ObtainGoodsInfoModel obtainGoodsInfoModel) {
        BigInteger goodsId = obtainGoodsInfoModel.getGoodsId();

        SearchModel goodsSearchModel = new SearchModel(true);
        goodsSearchModel.addSearchCondition(Goods.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, goodsId);
        Goods goods = DatabaseHelper.find(Goods.class, goodsSearchModel);
        Validate.notNull(goods, "商品不存在！");

        SearchModel goodsSpecificationSearchModel = new SearchModel(true);
        goodsSpecificationSearchModel.addSearchCondition(GoodsSpecification.ColumnName.GOODS_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, goodsId);
        List<GoodsSpecification> goodsSpecifications = DatabaseHelper.findAll(GoodsSpecification.class, goodsSpecificationSearchModel);

        Map<String, Object> goodsInfo = GoodsUtils.buildGoodsInfo(goods);
        goodsInfo.put("specifications", GoodsUtils.buildGoodsSpecificationInfos(goodsSpecifications));

        return ApiRest.builder().data(goodsInfo).message("获取商品信息成功！").successful(true).build();
    }

    /**
     * 保存商品信息
     *
     * @param saveGoodsModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveGoods(SaveGoodsModel saveGoodsModel) {
        BigInteger id = saveGoodsModel.getId();
        String name = saveGoodsModel.getName();
        BigInteger goodsTypeId = saveGoodsModel.getGoodsTypeId();
        int status = saveGoodsModel.getStatus();
        String photoUrl = saveGoodsModel.getPhotoUrl();
        int meteringMode = saveGoodsModel.getMeteringMode();
        String business = saveGoodsModel.getBusiness();
        List<SaveGoodsModel.GoodsSpecificationModel> goodsSpecificationModels = saveGoodsModel.getGoodsSpecificationModels();
        BigInteger userId = saveGoodsModel.getUserId();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition(GoodsType.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, goodsTypeId);
        GoodsType goodsType = DatabaseHelper.find(GoodsType.class, searchModel);
        Validate.notNull(goodsType, "商品类型不存在！");
        if (goodsType.isSingle()) {
            SearchModel countSearchModel = new SearchModel(true);
            countSearchModel.addSearchCondition(Goods.ColumnName.GOODS_TYPE_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, goodsTypeId);
            if (id != null) {
                searchModel.addSearchCondition(Goods.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_NOT_EQUAL, id);
            }
            long count = DatabaseHelper.count(Goods.class, searchModel);
            Validate.isTrue(count == 0, "商品类型【" + goodsType.getName() + "】下只能创建一个商品！");
        }
        Goods goods = null;
        if (id != null) {
            goods = DatabaseHelper.find(Goods.class, id);
            ValidateUtils.notNull(goods, "商品不存在！");

            goods.setName(name);
            goods.setGoodsTypeId(goodsTypeId);
            goods.setStatus(status);
            goods.setPhotoUrl(photoUrl);
            goods.setMeteringMode(meteringMode);
            goods.setBusiness(business);
            goods.setUpdatedUserId(userId);
            goods.setUpdatedRemark("修改商品信息！");
            DatabaseHelper.update(goods);

            List<BigInteger> goodsSpecificationIds = new ArrayList<BigInteger>();
            for (SaveGoodsModel.GoodsSpecificationModel goodsSpecificationModel : goodsSpecificationModels) {
                if (goodsSpecificationModel.getId() != null) {
                    goodsSpecificationIds.add(goodsSpecificationModel.getId());
                }
            }
            SearchModel goodsSpecificationSearchModel = new SearchModel(true);
            goodsSpecificationSearchModel.addSearchCondition(GoodsSpecification.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_IN, goodsSpecificationIds);
            goodsSpecificationSearchModel.addSearchCondition(GoodsSpecification.ColumnName.GOODS_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, id);
            List<GoodsSpecification> goodsSpecifications = DatabaseHelper.findAll(GoodsSpecification.class, goodsSpecificationSearchModel);
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
                    goodsSpecification.setUpdatedUserId(userId);
                    goodsSpecification.setUpdatedRemark("修改商品规格！");
                    DatabaseHelper.update(goodsSpecification);
                } else {
                    GoodsSpecification goodsSpecification = GoodsSpecification.builder()
                            .name(goodsSpecificationModel.getName())
                            .goodsId(id)
                            .allowTenantBuy(goodsSpecificationModel.isAllowTenantBuy())
                            .allowAgentBuy(goodsSpecificationModel.isAllowAgentBuy())
                            .renewalTime(goodsSpecificationModel.getRenewalTime())
                            .tenantPrice(goodsSpecificationModel.getTenantPrice())
                            .agentPrice(goodsSpecificationModel.getAgentPrice())
                            .createdUserId(userId)
                            .updatedUserId(userId)
                            .updatedRemark("新增商品规格信息！")
                            .build();
                    DatabaseHelper.insert(goodsSpecification);
                }
            }
        } else {
            goods = Goods.builder()
                    .name(name)
                    .goodsTypeId(goodsTypeId)
                    .status(status)
                    .photoUrl(photoUrl)
                    .meteringMode(meteringMode)
                    .business(business)
                    .createdUserId(userId)
                    .updatedUserId(userId)
                    .updatedRemark("新增商品信息！")
                    .build();
            DatabaseHelper.insert(goods);

            BigInteger goodsId = goods.getId();
            for (SaveGoodsModel.GoodsSpecificationModel goodsSpecificationModel : goodsSpecificationModels) {
                GoodsSpecification goodsSpecification = GoodsSpecification.builder()
                        .name(goodsSpecificationModel.getName())
                        .goodsId(goodsId)
                        .allowTenantBuy(goodsSpecificationModel.isAllowTenantBuy())
                        .allowAgentBuy(goodsSpecificationModel.isAllowAgentBuy())
                        .renewalTime(goodsSpecificationModel.getRenewalTime())
                        .tenantPrice(goodsSpecificationModel.getTenantPrice())
                        .agentPrice(goodsSpecificationModel.getAgentPrice())
                        .createdUserId(userId)
                        .updatedUserId(userId)
                        .updatedRemark("新增商品规格信息！")
                        .build();
                DatabaseHelper.insert(goodsSpecification);
            }
        }
        return ApiRest.builder().data(goods).message("保存商品信息成功！").successful(true).build();
    }

    /**
     * 保存商品类型
     *
     * @param saveGoodsTypeModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveGoodsType(SaveGoodsTypeModel saveGoodsTypeModel) {
        BigInteger id = saveGoodsTypeModel.getId();
        String name = saveGoodsTypeModel.getName();
        String description = saveGoodsTypeModel.getDescription();
        boolean single = saveGoodsTypeModel.getSingle();
        String renewSql = saveGoodsTypeModel.getRenewSql();
        String disableSql = saveGoodsTypeModel.getDisableSql();
        BigInteger userId = saveGoodsTypeModel.getUserId();

        GoodsType goodsType = null;
        if (id != null) {
            goodsType = DatabaseHelper.find(GoodsType.class, id);
            ValidateUtils.notNull(goodsType, "商品类型不存在！");

            goodsType.setName(name);
            goodsType.setDescription(StringUtils.isNotBlank(description) ? description : Constants.VARCHAR_DEFAULT_VALUE);
            goodsType.setRenewSql(StringUtils.isNotBlank(renewSql) ? renewSql : Constants.VARCHAR_DEFAULT_VALUE);
            goodsType.setDisableSql(StringUtils.isNotBlank(disableSql) ? disableSql : Constants.VARCHAR_DEFAULT_VALUE);
            goodsType.setUpdatedUserId(userId);
            goodsType.setUpdatedRemark("修改商品类型信息！");
            DatabaseHelper.update(goodsType);
        } else {
            goodsType = GoodsType.builder()
                    .name(name)
                    .description(StringUtils.isNotBlank(description) ? description : Constants.VARCHAR_DEFAULT_VALUE)
                    .single(single)
                    .renewSql(StringUtils.isNotBlank(renewSql) ? renewSql : Constants.VARCHAR_DEFAULT_VALUE)
                    .disableSql(StringUtils.isNotBlank(disableSql) ? disableSql : Constants.VARCHAR_DEFAULT_VALUE)
                    .createdUserId(userId)
                    .updatedUserId(userId)
                    .updatedRemark("新增商品类型信息！")
                    .build();
            DatabaseHelper.insert(goodsType);
        }
        return ApiRest.builder().data(goodsType).message("保存商品类型信息成功").successful(true).build();
    }

    /**
     * 查询所有商品类型
     *
     * @param listGoodsTypesModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest listGoodsTypes(ListGoodsTypesModel listGoodsTypesModel) {
        List<GoodsType> goodsTypes = DatabaseHelper.findAll(GoodsType.class);
        return ApiRest.builder().data(goodsTypes).message("查询所有商品类型成功！").successful(true).build();
    }
}
