package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.Activity;
import build.dream.common.saas.domains.Goods;
import build.dream.common.saas.domains.GoodsSpecification;
import build.dream.common.saas.domains.SpecialGoodsActivity;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.SpecialGoodsActivityMapper;
import build.dream.platform.models.activity.ObtainAllActivitiesModel;
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
        goodsSearchModel.addSearchCondition(Goods.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_IN, goodsIds);
        List<Goods> goodses = DatabaseHelper.findAll(Goods.class, goodsSearchModel);
        Map<BigInteger, Goods> goodsMap = new HashMap<BigInteger, Goods>();
        for (Goods goods : goodses) {
            goodsMap.put(goods.getId(), goods);
        }

        // 查询出涉及的所有商品规格
        SearchModel goodsSpecificationSearchModel = new SearchModel(true);
        goodsSpecificationSearchModel.addSearchCondition(GoodsSpecification.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_IN, goodsSpecificationIds);
        List<GoodsSpecification> goodsSpecifications = DatabaseHelper.findAll(GoodsSpecification.class, goodsSpecificationSearchModel);
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
            BigInteger goodsId = specialGoodsActivityInfo.getGoodsId();
            BigInteger goodsSpecificationId = specialGoodsActivityInfo.getGoodsSpecificationId();

            Goods goods = goodsMap.get(goodsId);
            Validate.notNull(goods, "商品不存在！");

            GoodsSpecification goodsSpecification = goodsSpecificationMap.get(goodsSpecificationId);
            Validate.notNull(goodsSpecification, "商品规格不存在！");

            SpecialGoodsActivity specialGoodsActivity = new SpecialGoodsActivity();
            specialGoodsActivity.setActivityId(activity.getId());
            specialGoodsActivity.setGoodsId(goodsId);
            specialGoodsActivity.setGoodsSpecificationId(goodsSpecificationId);
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
        DatabaseHelper.insertAll(specialGoodsActivities);
        return ApiRest.builder().message("保存特价商品活动成功！").successful(true).build();
    }

    @Transactional(readOnly = true)
    public ApiRest obtainAllActivities(ObtainAllActivitiesModel obtainAllActivitiesModel) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition(Activity.ColumnName.STATUS, Constants.SQL_OPERATION_SYMBOL_EQUAL, 2);
        List<Activity> activities = DatabaseHelper.findAll(Activity.class, searchModel);

        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (Activity activity : activities) {
            Map<String, Object> activityInfo = new HashMap<String, Object>();
            activityInfo.put("id", activity.getId());
            activityInfo.put("name", activity.getName());
            activityInfo.put("startTime", activity.getStartTime());
            activityInfo.put("endTime", activity.getEndTime());
            activityInfo.put("type", activity.getType());
            int type = activity.getType();
            if (type == 3) {
                activityInfo.put("details", specialGoodsActivityMapper.findSpecialGoodsActivityInfos(activity.getId()));
            }
            data.add(activityInfo);
        }
        return ApiRest.builder().data(data).message("查询活动成功！").successful(true).build();
    }
}
