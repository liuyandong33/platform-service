package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.Activity;
import build.dream.common.saas.domains.Goods;
import build.dream.common.saas.domains.GoodsSpecification;
import build.dream.common.saas.domains.SpecialGoodsActivity;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.GoodsMapper;
import build.dream.platform.mappers.GoodsSpecificationMapper;
import build.dream.platform.mappers.SpecialGoodsActivityMapper;
import build.dream.platform.models.activity.SaveSpecialGoodsActivityModel;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ActivityService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsSpecificationMapper goodsSpecificationMapper;
    @Autowired
    private SpecialGoodsActivityMapper specialGoodsActivityMapper;

    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveSpecialGoodsActivity(SaveSpecialGoodsActivityModel saveSpecialGoodsActivityModel) throws ParseException {
        BigInteger userId = saveSpecialGoodsActivityModel.getUserId();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DEFAULT_DATE_PATTERN);
        Date startTime = simpleDateFormat.parse(saveSpecialGoodsActivityModel.getStartTime() + " 00:00:00");
        Date endTime = simpleDateFormat.parse(saveSpecialGoodsActivityModel.getEndTime() + " 23:59:59");
        Validate.isTrue(endTime.after(startTime), "活动结束时间必须大于开始时间！");

        List<BigInteger> goodsIds = new ArrayList<BigInteger>();
        List<BigInteger> goodsSpecificationIds = new ArrayList<BigInteger>();
        List<SaveSpecialGoodsActivityModel.SpecialGoodsActivityInfo> specialGoodsActivityInfos = saveSpecialGoodsActivityModel.getSpecialGoodsActivityInfos();
        for (SaveSpecialGoodsActivityModel.SpecialGoodsActivityInfo specialGoodsActivityInfo : specialGoodsActivityInfos) {
            goodsIds.add(specialGoodsActivityInfo.getGoodsId());
            goodsSpecificationIds.add(specialGoodsActivityInfo.getGoodsSpecificationId());
        }

        // 查询出涉及的所有商品
        SearchModel goodsSearchModel = new SearchModel(true);
        goodsSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, goodsIds);
        List<Goods> goodses = goodsMapper.findAll(goodsSearchModel);
        Map<BigInteger, Goods> goodsMap = new HashMap<BigInteger, Goods>();
        for (Goods goods : goodses) {
            goodsMap.put(goods.getId(), goods);
        }

        // 查询出涉及的所有商品规格
        SearchModel goodsSpecificationSearchModel = new SearchModel(true);
        goodsSpecificationSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, goodsSpecificationIds);
        List<GoodsSpecification> goodsSpecifications = goodsSpecificationMapper.findAll(goodsSpecificationSearchModel);
        Map<BigInteger, GoodsSpecification> goodsSpecificationMap = new HashMap<BigInteger, GoodsSpecification>();
        for (GoodsSpecification goodsSpecification : goodsSpecifications) {
            goodsSpecificationMap.put(goodsSpecification.getId(), goodsSpecification);
        }

        Activity activity = new Activity();
        activity.setName(saveSpecialGoodsActivityModel.getName());
        activity.setStartTime(startTime);
        activity.setEndTime(endTime);
        activity.setType(Constants.ACTIVITY_TYPE_SPECIAL_GOODS);
        activity.setStatus(Constants.ACTIVITY_STATUS_UNEXECUTED);
        activity.setCreateUserId(userId);
        activity.setLastUpdateUserId(userId);
        activity.setLastUpdateRemark("保存活动信息！");

        List<SpecialGoodsActivity> specialGoodsActivities = new ArrayList<SpecialGoodsActivity>();
        for (SaveSpecialGoodsActivityModel.SpecialGoodsActivityInfo specialGoodsActivityInfo : specialGoodsActivityInfos) {
            Goods goods = goodsMap.get(specialGoodsActivityInfo.getGoodsId());
            Validate.notNull(goods, "商品不存在！");

            GoodsSpecification goodsSpecification = goodsSpecificationMap.get(specialGoodsActivityInfo.getGoodsSpecificationId());
            Validate.notNull(goodsSpecification, "商品规格不存在！");

            SpecialGoodsActivity specialGoodsActivity = new SpecialGoodsActivity();
            specialGoodsActivity.setActivityId(activity.getId());
            specialGoodsActivity.setGoodsId(goods.getId());
            specialGoodsActivity.setGoodsSpecificationId(goodsSpecification.getId());
            Integer discountType = specialGoodsActivity.getDiscountType();
            if (discountType == 1) {
                specialGoodsActivity.setTenantSpecialPrice(specialGoodsActivity.getTenantSpecialPrice());
                specialGoodsActivity.setAgentSpecialPrice(specialGoodsActivity.getAgentSpecialPrice());
            } else if (discountType == 2) {
                specialGoodsActivity.setTenantDiscountRate(specialGoodsActivity.getTenantDiscountRate());
                specialGoodsActivity.setAgentDiscountRate(specialGoodsActivity.getAgentDiscountRate());
            }
            specialGoodsActivity.setCreateUserId(userId);
            specialGoodsActivity.setLastUpdateUserId(userId);
            specialGoodsActivity.setLastUpdateRemark("保存特价活动！");
            specialGoodsActivities.add(specialGoodsActivity);
        }
        specialGoodsActivityMapper.insertAll(specialGoodsActivities);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("保存特价商品活动成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
