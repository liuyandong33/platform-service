package build.dream.platform.controllers

import java.io.FileOutputStream
import java.util.{ArrayList, HashMap, List, Map}

import build.dream.common.api.ApiRest
import build.dream.common.utils.{ApplicationHandler, MethodCaller}
import build.dream.platform.models.user.{BatchDeleteUserModel, BatchGetUsersModel, ObtainAllPrivilegesModel, ObtainUserInfoModel}
import build.dream.platform.services.UserService
import javax.servlet.http.HttpServletRequest
import org.apache.commons.lang.Validate
import org.apache.poi.ss.usermodel._
import org.apache.poi.xssf.usermodel.{XSSFCell, XSSFCellStyle, _}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, ResponseBody}
import org.springframework.web.multipart.MultipartHttpServletRequest

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
            obtainUserInfoModel.validateAndThrow()
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

    /**
      * 上传商品信息
      *
      * @param httpServletRequest
      * @return
      */
    @RequestMapping(value = Array("/uploadGoods"), method = Array(RequestMethod.POST))
    @ResponseBody
    def uploadGoods(httpServletRequest: HttpServletRequest): String = {
        val requestParameters: Map[String, String] = ApplicationHandler.getRequestParameters
        val methodCaller: MethodCaller = () => {
            Validate.isTrue(httpServletRequest.isInstanceOf[MultipartHttpServletRequest], "请上传商品信息！")
            val multipartHttpServletRequest: MultipartHttpServletRequest = httpServletRequest.asInstanceOf[MultipartHttpServletRequest]
            val goodsInfoFile = multipartHttpServletRequest.getFile("goodsInfoFile")

            val xssfWorkbook: XSSFWorkbook = new XSSFWorkbook(goodsInfoFile.getInputStream)
            val xssfSheet: XSSFSheet = xssfWorkbook.getSheetAt(0)

            val xssfDrawing: XSSFDrawing = xssfSheet.createDrawingPatriarch()
            val lastRowNum: Int = xssfSheet.getLastRowNum
            val employeeInfos: List[Map[String, Object]] = new ArrayList[Map[String, Object]]()
            for (index: Int <- 1 to lastRowNum) {
                val xssfRow: XSSFRow = xssfSheet.getRow(index)
                val employeeInfo: Map[String, Object] = new HashMap[String, Object]()
                val serialNumberXSSFCell: XSSFCell = xssfRow.getCell(0)

                val serialNumber: Int = serialNumberXSSFCell.getNumericCellValue.intValue()
                val xssfCellStyle: XSSFCellStyle = xssfWorkbook.createCellStyle
                xssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)
                xssfCellStyle.setBorderTop(BorderStyle.THIN)
                xssfCellStyle.setBorderLeft(BorderStyle.THIN)
                xssfCellStyle.setBorderRight(BorderStyle.THIN)
                xssfCellStyle.setBorderBottom(BorderStyle.THIN)

                serialNumberXSSFCell.removeCellComment()

                if (serialNumber > 60) {
                    val xssfComment: XSSFComment = xssfDrawing.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, 1, 1, 1, 1))
                    xssfComment.setString("序号不能小于0")
                    serialNumberXSSFCell.setCellComment(xssfComment)

                    xssfCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex)
                } else {
                    xssfCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex)
                }
                serialNumberXSSFCell.setCellStyle(xssfCellStyle)
            }
            xssfWorkbook.write(new FileOutputStream("C:\\Users\\liuyandong\\Desktop\\456.xlsx"))
            new ApiRest()
        }
        ApplicationHandler.callMethod(methodCaller, "批量删除用户失败", requestParameters)
    }
}
