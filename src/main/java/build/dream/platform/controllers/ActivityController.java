package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.platform.models.activity.ObtainAllActivitiesModel;
import build.dream.platform.models.activity.SaveSpecialGoodsActivityModel;
import build.dream.platform.services.ActivityService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/activity")
public class ActivityController {
    /**
     * 保存特价商品活动
     *
     * @return
     */
    @RequestMapping(value = "/saveSpecialGoodsActivity", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveSpecialGoodsActivityModel.class, serviceClass = ActivityService.class, serviceMethodName = "saveSpecialGoodsActivity", error = "保存特价商品活动失败")
    public String saveSpecialGoodsActivity() {
        return null;
    }

    /**
     * 获取所有活动
     *
     * @return
     */
    @RequestMapping(value = "/obtainAllActivities", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainAllActivitiesModel.class, serviceClass = ActivityService.class, serviceMethodName = "obtainAllActivities", error = "获取活动失败")
    public String obtainAllActivities() {
        return null;
    }
}
