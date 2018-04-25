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
            Validate.notNull(goodsInfoFile, "请上传商品信息！")

            val xssfWorkbook: XSSFWorkbook = new XSSFWorkbook(goodsInfoFile.getInputStream)
            val xssfSheet: XSSFSheet = xssfWorkbook.getSheetAt(0)

            val xssfDrawing: XSSFDrawing = xssfSheet.createDrawingPatriarch()
            val lastRowNum: Int = xssfSheet.getLastRowNum
            val goodsInfos: List[Map[String, Any]] = new ArrayList[Map[String, Any]]()

            val normalXssfCellStyle: XSSFCellStyle = xssfWorkbook.createCellStyle
            normalXssfCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex)
            normalXssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)
            normalXssfCellStyle.setBorderTop(BorderStyle.THIN)
            normalXssfCellStyle.setBorderLeft(BorderStyle.THIN)
            normalXssfCellStyle.setBorderRight(BorderStyle.THIN)
            normalXssfCellStyle.setBorderBottom(BorderStyle.THIN)

            val abnormalXssfCellStyle: XSSFCellStyle = xssfWorkbook.createCellStyle
            abnormalXssfCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex)
            var isNormal: Boolean = true

            for (index: Int <- 1 to lastRowNum) {
                val xssfRow: XSSFRow = xssfSheet.getRow(index)

                val codeXSSFCell: XSSFCell = xssfRow.getCell(0)
                codeXSSFCell.removeCellComment()
                val code: String = codeXSSFCell.getStringCellValue
                if (code.length > 20) {
                    val xssfComment: XSSFComment = obtainXSSFComment(xssfDrawing, "商品编码不能超过20个字符！")
                    codeXSSFCell.setCellComment(xssfComment)
                    codeXSSFCell.setCellStyle(abnormalXssfCellStyle)
                    isNormal = false
                } else {
                    codeXSSFCell.setCellStyle(normalXssfCellStyle)
                }


                val barCodeXSSFCell: XSSFCell = xssfRow.getCell(1)
                barCodeXSSFCell.removeCellComment()
                val barCode: String = barCodeXSSFCell.getStringCellValue
                if (barCode.length > 20) {
                    val xssfComment: XSSFComment = obtainXSSFComment(xssfDrawing, "商品条码不能超过20个字符！")
                    barCodeXSSFCell.setCellComment(xssfComment)
                    barCodeXSSFCell.setCellStyle(abnormalXssfCellStyle)
                    isNormal = false
                } else {
                    barCodeXSSFCell.setCellStyle(normalXssfCellStyle)
                }

                val nameXSSFCell: XSSFCell = xssfRow.getCell(0)
                nameXSSFCell.removeCellComment()
                val name: String = nameXSSFCell.getStringCellValue
                if (name.length > 20) {
                    val xssfComment: XSSFComment = obtainXSSFComment(xssfDrawing, "商品名称不能超过20个字符！")
                    nameXSSFCell.setCellComment(xssfComment)
                    nameXSSFCell.setCellStyle(abnormalXssfCellStyle)
                    isNormal = false
                } else {
                    nameXSSFCell.setCellStyle(normalXssfCellStyle)
                }

                val salePriceXSSFCell: XSSFCell = xssfRow.getCell(0)
                salePriceXSSFCell.removeCellComment()
                val salePrice: Double = nameXSSFCell.getNumericCellValue
                if (salePrice > 999999) {
                    val xssfComment: XSSFComment = obtainXSSFComment(xssfDrawing, "商品销售价格不能超过999999！")
                    salePriceXSSFCell.setCellComment(xssfComment)
                    salePriceXSSFCell.setCellStyle(abnormalXssfCellStyle)
                    isNormal = false
                } else {
                    salePriceXSSFCell.setCellStyle(normalXssfCellStyle)
                }

                val memberPriceXSSFCell: XSSFCell = xssfRow.getCell(0)
                memberPriceXSSFCell.removeCellComment()
                val memberPrice: Double = nameXSSFCell.getNumericCellValue
                if (memberPrice > 999999) {
                    val xssfComment: XSSFComment = obtainXSSFComment(xssfDrawing, "商品销售价格不能超过999999！")
                    memberPriceXSSFCell.setCellComment(xssfComment)
                    memberPriceXSSFCell.setCellStyle(abnormalXssfCellStyle)
                    isNormal = false
                } else if (memberPrice > salePrice) {
                    val xssfComment: XSSFComment = obtainXSSFComment(xssfDrawing, "会员价格不能大于销售价格！")
                    memberPriceXSSFCell.setCellComment(xssfComment)
                    memberPriceXSSFCell.setCellStyle(abnormalXssfCellStyle)
                    isNormal = false
                } else {
                    memberPriceXSSFCell.setCellStyle(normalXssfCellStyle)
                }

                if (isNormal) {
                    val goodsInfo: Map[String, Any] = new HashMap[String, Any]()
                    goodsInfo.put("code", code)
                    goodsInfo.put("barCode", barCode)
                    goodsInfo.put("name", name)
                    goodsInfo.put("salePrice", salePrice)
                    goodsInfo.put("memberPrice", memberPrice)
                    goodsInfos.add(goodsInfo)
                }
            }
            xssfWorkbook.write(new FileOutputStream("/Users/liuyandong/Desktop/456.xlsx"))
            new ApiRest()
        }
        ApplicationHandler.callMethod(methodCaller, "上传商品档案失败", requestParameters)
    }

    def obtainXSSFComment(xssfDrawing: XSSFDrawing, comment: String): XSSFComment = {
        val xssfComment: XSSFComment = xssfDrawing.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, 1, 1, 1, 1))
        xssfComment.setString("comment")
        xssfComment
    }
}
