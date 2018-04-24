package build.dream.platform.controllers

import java.lang.reflect.Field
import java.util.Map

import build.dream.common.api.ApiRest
import build.dream.common.controllers.BasicController
import build.dream.common.erp.catering.domains.Branch
import build.dream.common.utils.{ApplicationHandler, GsonUtils, LogUtils}
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, ResponseBody}

@Controller
@RequestMapping(value = Array("/demo"))
class DemoController extends BasicController {
    @RequestMapping(value = Array("/index"), method = Array(RequestMethod.GET))
    @ResponseBody
    def index(): String = {
        var apiRest: ApiRest = null
        val requestParameters: Map[String, String] = ApplicationHandler.getRequestParameters
        try {
            val branchClass: Class[Branch] = classOf[Branch]
            val fields: Array[Field] = branchClass.getDeclaredFields

            for (field: Field <- fields) {
                println(field.getName)
            }

            apiRest = new ApiRest()
            apiRest setMessage ("测试成功")
            apiRest setSuccessful (true)
        } catch {
            case (e: Exception) => {
                LogUtils.error("测试失败", controllerSimpleName, "index", e, requestParameters)
                apiRest = new ApiRest()
                apiRest.setMessage(e.getMessage)
                apiRest.setSuccessful(true)
            }
        }
        GsonUtils.toJson(apiRest)
    }
}
