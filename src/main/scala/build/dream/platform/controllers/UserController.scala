package build.dream.platform.controllers

import java.util.Map

import build.dream.common.utils.{ApplicationHandler, MethodCaller}
import build.dream.platform.models.user.{BatchDeleteUserModel, BatchGetUsersModel, ObtainAllPrivilegesModel, ObtainUserInfoModel}
import build.dream.platform.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, ResponseBody}

@Controller
@RequestMapping(value = Array("/user"))
class UserController {
    @Autowired
    private val userService: UserService = null

    /**
      * 获取用户信息
      *
      * @return
      */
    @RequestMapping(value = Array("/obtainUserInfo"), method = Array(RequestMethod.GET))
    @ResponseBody
    def obtainUserInfo: String = {
        val requestParameters: Map[String, String] = ApplicationHandler.getRequestParameters
        val methodCaller: MethodCaller = () => {
            val obtainUserInfoModel = ApplicationHandler.instantiateObject(classOf[ObtainUserInfoModel], requestParameters)
            userService.obtainUserInfo(obtainUserInfoModel)
        }
        ApplicationHandler.callMethod(methodCaller, "获取用户信息失败", requestParameters)
    }

    /**
      * 批量获取用户信息
      *
      * @return
      */
    @RequestMapping(value = Array("/batchGetUsers"), method = Array(RequestMethod.GET))
    @ResponseBody
    def batchGetUsers: String = {
        val requestParameters: Map[String, String] = ApplicationHandler.getRequestParameters
        val methodCaller: MethodCaller = () => {
            val batchGetUsersModel: BatchGetUsersModel = ApplicationHandler.instantiateObject(classOf[BatchGetUsersModel], requestParameters)
            batchGetUsersModel.validateAndThrow()
            userService.batchObtainUser(batchGetUsersModel)
        }
        ApplicationHandler.callMethod(methodCaller, "查询用户失败", requestParameters)
    }

    /**
      * 获取用户所有的权限
      *
      * @return
      */
    @RequestMapping(value = Array("/obtainAllPrivileges"), method = Array(RequestMethod.GET))
    @ResponseBody
    def obtainAllPrivileges: String = {
        val requestParameters: Map[String, String] = ApplicationHandler.getRequestParameters
        val methodCaller: MethodCaller = () => {
            val obtainAllPrivilegesModel: ObtainAllPrivilegesModel = ApplicationHandler.instantiateObject(classOf[ObtainAllPrivilegesModel], requestParameters)
            obtainAllPrivilegesModel.validateAndThrow()
            userService.obtainAllPrivileges(obtainAllPrivilegesModel)
        }
        ApplicationHandler.callMethod(methodCaller, "获取用户权限失败", requestParameters)
    }

    /**
      * 批量删除用户
      *
      * @return
      */
    @RequestMapping(value = Array("/batchDeleteUser"), method = Array(RequestMethod.POST))
    @ResponseBody
    def batchDeleteUser: String = {
        val requestParameters: Map[String, String] = ApplicationHandler.getRequestParameters
        val methodCaller: MethodCaller = () => {
            val batchDeleteUserModel: BatchDeleteUserModel = ApplicationHandler.instantiateObject(classOf[BatchDeleteUserModel], requestParameters)
            batchDeleteUserModel.validateAndThrow()
            userService.batchDeleteUser(batchDeleteUserModel)
        }
        ApplicationHandler.callMethod(methodCaller, "批量删除用户失败", requestParameters)
    }
}
