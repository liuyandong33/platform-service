package build.dream.platform.controllers

import java.util.Map

import build.dream.common.utils.{ApplicationHandler, MethodCaller}
import build.dream.platform.models.activity.{ObtainAllActivitiesModel, SaveSpecialGoodsActivityModel}
import build.dream.platform.services.ActivityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, ResponseBody}

@Controller
@RequestMapping(value = Array("/activity"))
class ActivityController {
    @Autowired
    private val activityService: ActivityService = null

    /**
      * 保存特价商品活动
      *
      * @return
      */
    @RequestMapping(value = Array("/saveSpecialGoodsActivity"), method = Array(RequestMethod.POST))
    @ResponseBody
    def saveSpecialGoodsActivity: String = {
        val requestParameters: Map[String, String] = ApplicationHandler.getRequestParameters
        val methodCaller: MethodCaller = () => {
            val saveSpecialGoodsActivityModel: SaveSpecialGoodsActivityModel = ApplicationHandler.instantiateObject(classOf[SaveSpecialGoodsActivityModel], requestParameters)
            val specialGoodsActivityInfos: String = requestParameters.get("specialGoodsActivityInfos")
            saveSpecialGoodsActivityModel.setSpecialGoodsActivityInfos(specialGoodsActivityInfos)
            saveSpecialGoodsActivityModel.validateAndThrow()
            activityService.saveSpecialGoodsActivity(saveSpecialGoodsActivityModel)
        }
        ApplicationHandler.callMethod(methodCaller, "保存特价商品活动失败", requestParameters)
    }

    /**
      * 获取所有活动
      *
      * @return
      */
    @RequestMapping(value = Array("/obtainAllActivities"))
    @ResponseBody def obtainAllActivities: String = {
        val requestParameters: Map[String, String] = ApplicationHandler.getRequestParameters
        val methodCaller: MethodCaller = () => {
            val obtainAllActivitiesModel: ObtainAllActivitiesModel = ApplicationHandler.instantiateObject(classOf[ObtainAllActivitiesModel], requestParameters)
            obtainAllActivitiesModel.validateAndThrow()
            activityService.obtainAllActivities(obtainAllActivitiesModel)
        }
        ApplicationHandler.callMethod(methodCaller, "获取活动失败", requestParameters)
    }
}
