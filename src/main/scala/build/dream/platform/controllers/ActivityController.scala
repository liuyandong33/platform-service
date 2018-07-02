package build.dream.platform.controllers

import build.dream.common.annotations.ApiRestAction
import build.dream.platform.models.activity.{ObtainAllActivitiesModel, SaveSpecialGoodsActivityModel}
import build.dream.platform.services.ActivityService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, ResponseBody}

@Controller
@RequestMapping(value = Array("/activity"))
class ActivityController {
    /**
      * 保存特价商品活动
      *
      * @return
      */
    @RequestMapping(value = Array("/saveSpecialGoodsActivity"), method = Array(RequestMethod.POST))
    @ResponseBody
    @ApiRestAction(modelClass = classOf[SaveSpecialGoodsActivityModel], serviceClass = classOf[ActivityService], serviceMethodName = "saveSpecialGoodsActivity", error = "保存特价商品活动失败")
    def saveSpecialGoodsActivity: String = {
        null
    }

    /**
      * 获取所有活动
      *
      * @return
      */
    @RequestMapping(value = Array("/obtainAllActivities"))
    @ResponseBody
    @ApiRestAction(modelClass = classOf[ObtainAllActivitiesModel], serviceClass = classOf[ActivityService], serviceMethodName = "obtainAllActivities", error = "获取活动失败")
    def obtainAllActivities: String = {
        null
    }
}
