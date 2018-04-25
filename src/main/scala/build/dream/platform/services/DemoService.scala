package build.dream.platform.services

import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.{ArrayList, Date, HashMap, List, Map}

import build.dream.common.api.ApiRest
import build.dream.common.saas.domains.{Activity, Goods, GoodsSpecification, SpecialGoodsActivity}
import build.dream.common.utils.SearchModel
import build.dream.platform.constants.Constants
import build.dream.platform.mappers._
import build.dream.platform.models.activity.SaveSpecialGoodsActivityModel
import org.apache.commons.lang.Validate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DemoService {
    @Autowired
    private val goodsMapper: GoodsMapper = null
    @Autowired
    private val goodsSpecificationMapper: GoodsSpecificationMapper = null
    @Autowired
    private val specialGoodsActivityMapper: SpecialGoodsActivityMapper = null
    @Autowired
    private val activityMapper: ActivityMapper = null
    @Autowired
    private val universalMapper: UniversalMapper = null

    @Transactional(rollbackFor = Array(classOf[Exception]))
    def saveSpecialGoodsActivity(saveSpecialGoodsActivityModel: SaveSpecialGoodsActivityModel): ApiRest = {
        val userId: BigInteger = saveSpecialGoodsActivityModel.getUserId
        val simpleDateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val startTime: Date = simpleDateFormat.parse(saveSpecialGoodsActivityModel.getStartTime + " 00:00:00")
        val endTime: Date = simpleDateFormat.parse(saveSpecialGoodsActivityModel.getEndTime + " 23:59:59")
        Validate.isTrue(endTime.after(startTime), "活动结束时间必须大于开始时间！")
        val goodsIds: List[BigInteger] = new ArrayList[BigInteger]
        val goodsSpecificationIds: List[BigInteger] = new ArrayList[BigInteger]
        val specialGoodsActivityInfos: List[SaveSpecialGoodsActivityModel.SpecialGoodsActivityInfo] = saveSpecialGoodsActivityModel.getSpecialGoodsActivityInfos
        for (specialGoodsActivityInfo: SaveSpecialGoodsActivityModel.SpecialGoodsActivityInfo <- specialGoodsActivityInfos) {
            goodsIds.add(specialGoodsActivityInfo.getGoodsId)
            goodsSpecificationIds.add(specialGoodsActivityInfo.getGoodsSpecificationId)
        }
        // 查询出涉及的所有商品
        val goodsSearchModel: SearchModel = new SearchModel(true)
        goodsSearchModel.addSearchCondition("id", "=", goodsIds)
        val goodses: List[Goods] = goodsMapper.findAll(goodsSearchModel)
        val goodsMap: Map[BigInteger, Goods] = new HashMap[BigInteger, Goods]
        for (goods: Goods <- goodses) {
            goodsMap.put(goods.getId, goods)
        }
        // 查询出涉及的所有商品规格
        val goodsSpecificationSearchModel: SearchModel = new SearchModel(true)
        goodsSpecificationSearchModel.addSearchCondition("id", "=", goodsSpecificationIds)
        val goodsSpecifications: List[GoodsSpecification] = goodsSpecificationMapper.findAll(goodsSpecificationSearchModel)
        val goodsSpecificationMap: Map[BigInteger, GoodsSpecification] = new HashMap[BigInteger, GoodsSpecification]
        for (goodsSpecification: GoodsSpecification <- goodsSpecifications) {
            goodsSpecificationMap.put(goodsSpecification.getId, goodsSpecification)
        }
        val activity: Activity = new Activity
        activity.setName(saveSpecialGoodsActivityModel.getName)
        activity.setStartTime(startTime)
        activity.setEndTime(endTime)
        activity.setType(Constants.ACTIVITY_TYPE_SPECIAL_GOODS)
        activity.setStatus(Constants.ACTIVITY_STATUS_UNEXECUTED)
        activity.setCreateUserId(userId)
        activity.setLastUpdateUserId(userId)
        activity.setLastUpdateRemark("保存活动信息！")
        activityMapper.insert(activity)

        val specialGoodsActivities: List[SpecialGoodsActivity] = new ArrayList[SpecialGoodsActivity]
        for (specialGoodsActivityInfo: SpecialGoodsActivity <- specialGoodsActivityInfos) {
            val goods: Goods = goodsMap.get(specialGoodsActivityInfo.getGoodsId)
            Validate.notNull(goods, "商品不存在！")

            val goodsSpecification: GoodsSpecification = goodsSpecificationMap.get(specialGoodsActivityInfo.getGoodsSpecificationId)
            Validate.notNull(goodsSpecification, "商品规格不存在！")

            val specialGoodsActivity: SpecialGoodsActivity = new SpecialGoodsActivity
            specialGoodsActivity.setActivityId(activity.getId)
            specialGoodsActivity.setGoodsId(goods.getId)
            specialGoodsActivity.setGoodsSpecificationId(goodsSpecification.getId)
            val discountType: Integer = specialGoodsActivity.getDiscountType
            if (discountType == 1) {
                specialGoodsActivity.setTenantSpecialPrice(specialGoodsActivity.getTenantSpecialPrice)
                specialGoodsActivity.setAgentSpecialPrice(specialGoodsActivity.getAgentSpecialPrice)
            }
            else if (discountType == 2) {
                specialGoodsActivity.setTenantDiscountRate(specialGoodsActivity.getTenantDiscountRate)
                specialGoodsActivity.setAgentDiscountRate(specialGoodsActivity.getAgentDiscountRate)
            }
            specialGoodsActivity.setCreateUserId(userId)
            specialGoodsActivity.setLastUpdateUserId(userId)
            specialGoodsActivity.setLastUpdateRemark("保存特价活动！")
            specialGoodsActivities.add(specialGoodsActivity)
        }
        specialGoodsActivityMapper.insertAll(specialGoodsActivities)
        val apiRest: ApiRest = new ApiRest
        apiRest.setMessage("保存特价商品活动成功！")
        apiRest.setSuccessful(true)
        apiRest
    }
}
