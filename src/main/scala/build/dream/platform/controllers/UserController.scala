package build.dream.platform.controllers

import java.io._
import java.lang.Double
import java.math.BigInteger
import java.net.URLEncoder
import java.util.regex.Pattern
import java.util.{ArrayList, HashMap, List, Map}

import build.dream.common.annotations.ApiRestAction
import build.dream.common.api.ApiRest
import build.dream.common.controllers.BasicController
import build.dream.common.utils._
import build.dream.platform.models.user.{BatchDeleteUserModel, BatchGetUsersModel, ObtainAllPrivilegesModel, ObtainUserInfoModel}
import build.dream.platform.services.UserService
import build.dream.platform.utils.PoiUtils
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.apache.commons.lang.Validate
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy
import org.apache.poi.ss.usermodel._
import org.apache.poi.xssf.usermodel.{XSSFCell, XSSFCellStyle, _}
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, ResponseBody}
import org.springframework.web.multipart.MultipartHttpServletRequest

@Controller
@RequestMapping(value = Array("/user"))
class UserController extends BasicController {
    /**
      * 获取用户信息
      *
      * @return
      */
    @RequestMapping(value = Array("/obtainUserInfo"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_UTF8_VALUE))
    @ResponseBody
    @ApiRestAction(modelClass = classOf[ObtainUserInfoModel], serviceClass = classOf[UserService], serviceMethodName = "obtainUserInfo", error = "获取用户信息失败")
    def obtainUserInfo: String = {
        null
    }

    /**
      * 批量获取用户信息
      *
      * @return
      */
    @RequestMapping(value = Array("/batchGetUsers"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_UTF8_VALUE))
    @ResponseBody
    @ApiRestAction(modelClass = classOf[BatchGetUsersModel], serviceClass = classOf[UserService], serviceMethodName = "batchGetUsers", error = "获取用户信息失败")
    def batchGetUsers: String = {
        null
    }

    /**
      * 获取用户所有的权限
      *
      * @return
      */
    @RequestMapping(value = Array("/obtainAllPrivileges"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_UTF8_VALUE))
    @ResponseBody
    @ApiRestAction(modelClass = classOf[ObtainAllPrivilegesModel], serviceClass = classOf[UserService], serviceMethodName = "obtainAllPrivileges", error = "获取用户权限失败")
    def obtainAllPrivileges: String = {
        null
    }

    /**
      * 批量删除用户
      *
      * @return
      */
    @RequestMapping(value = Array("/batchDeleteUser"), method = Array(RequestMethod.POST), produces = Array(MediaType.APPLICATION_JSON_UTF8_VALUE))
    @ResponseBody
    @ApiRestAction(modelClass = classOf[BatchDeleteUserModel], serviceClass = classOf[UserService], serviceMethodName = "batchDeleteUser", error = "批量删除用户失败")
    def batchDeleteUser: String = {
        null
    }

    /**
      * 上传商品信息
      *
      * @param httpServletRequest
      * @return
      */
    @RequestMapping(value = Array("/uploadGoods"), method = Array(RequestMethod.POST), produces = Array(MediaType.APPLICATION_JSON_UTF8_VALUE))
    @ResponseBody
    def uploadGoods(httpServletRequest: HttpServletRequest): String = {
        val requestParameters: Map[String, String] = ApplicationHandler.getRequestParameters
        val methodCaller: MethodCaller = () => {
            val tenantId: String = "1"
            val tenantCode: String = "61011888"
            val branchId: String = "1"

            Validate.isTrue(httpServletRequest.isInstanceOf[MultipartHttpServletRequest], "请上传商品信息！")
            val multipartHttpServletRequest: MultipartHttpServletRequest = httpServletRequest.asInstanceOf[MultipartHttpServletRequest]
            val goodsInfoFile = multipartHttpServletRequest.getFile("goodsInfoFile")
            Validate.notNull(goodsInfoFile, "请上传商品信息！")

            val xssfWorkbook: XSSFWorkbook = new XSSFWorkbook(goodsInfoFile.getInputStream)
            val xssfSheet: XSSFSheet = xssfWorkbook.getSheetAt(0)

            val xssfDrawing: XSSFDrawing = xssfSheet.createDrawingPatriarch()
            val lastRowNum: Int = xssfSheet.getLastRowNum
            val goodsInfos: List[Map[String, Object]] = new ArrayList[Map[String, Object]]()

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

            val salePricePattern: Pattern = Pattern.compile("^(([1-9]\\d{0,7})|(([0]\\.\\d{1,2}|[1-9]\\d{0,7}\\.\\d{1,2})))$")
            val memberPricePattern: Pattern = Pattern.compile("^(([1-9]\\d{0,7})|(([0]\\.\\d{1,2}|[1-9]\\d{0,7}\\.\\d{1,2})))$")
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
                val salePrice: String = obtainCellValue(nameXSSFCell)
                var bigDecimalSalePrice: BigDecimal = null
                if (!salePricePattern.matcher(salePrice).matches()) {
                    val xssfComment: XSSFComment = obtainXSSFComment(xssfDrawing, "销售价格格式错误，应为小于99999999.99的最多两位小数的数字！")
                    salePriceXSSFCell.setCellComment(xssfComment)
                    salePriceXSSFCell.setCellStyle(abnormalXssfCellStyle)
                    isNormal = false
                } else {
                    bigDecimalSalePrice = BigDecimal.valueOf(Double.valueOf(salePrice))
                    salePriceXSSFCell.setCellStyle(normalXssfCellStyle)
                }

                val memberPriceXSSFCell: XSSFCell = xssfRow.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK)
                val memberPrice: String = obtainCellValue(memberPriceXSSFCell)
                var bigDecimalMemberPrice: BigDecimal = null
                if (!memberPricePattern.matcher(memberPrice).matches()) {
                    val xssfComment: XSSFComment = obtainXSSFComment(xssfDrawing, "会员价格格式错误，应为小于99999999.99的最多两位小数的数字！")
                    memberPriceXSSFCell.setCellComment(xssfComment)
                    memberPriceXSSFCell.setCellStyle(abnormalXssfCellStyle)
                    isNormal = false
                } else {
                    bigDecimalMemberPrice = BigDecimal.valueOf(Double.valueOf(memberPrice))
                    memberPriceXSSFCell.setCellStyle(normalXssfCellStyle)
                }

                if (isNormal) {
                    val goodsInfo: Map[String, Object] = new HashMap[String, Object]()
                    goodsInfo.put("code", code)
                    goodsInfo.put("barCode", barCode)
                    goodsInfo.put("name", name)
                    goodsInfo.put("salePrice", bigDecimalSalePrice)
                    goodsInfo.put("memberPrice", bigDecimalMemberPrice)
                    goodsInfos.add(goodsInfo)
                }
            }
            if (isNormal) {
                val importGoodsRequestParameters: Map[String, String] = new HashMap[String, String]()
                importGoodsRequestParameters.put("tenantId", tenantId)
                importGoodsRequestParameters.put("tenantCode", tenantCode)
                importGoodsRequestParameters.put("branchId", branchId)
                importGoodsRequestParameters.put("zipGoodsInfos", ZipUtils.zipText(GsonUtils.toJson(goodsInfos)))

                val partitionCode: String = ""
                val importGoodsApiRest = ProxyUtils.doPostWithRequestParameters(partitionCode, "", "goods", "importGoods", importGoodsRequestParameters)
                Validate.isTrue(importGoodsApiRest.isSuccessful, importGoodsApiRest.getError)
            } else {
                val tmpdir: String = System.getProperty("java.io.tmpdir")
                xssfWorkbook.write(new FileOutputStream(tmpdir + File.separator + tenantId + "_" + branchId + ".xls"))
            }
            val apiRest: ApiRest = new ApiRest()
            apiRest.setSuccessful(true)
            apiRest.setMessage("导入商品信息成功！")
            apiRest
        }
        ApplicationHandler.callMethod(methodCaller, "导入商品档案失败", requestParameters)
    }

    def obtainXSSFComment(xssfDrawing: XSSFDrawing, comment: String): XSSFComment = {
        val xssfComment: XSSFComment = xssfDrawing.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, 1, 1, 1, 1))
        xssfComment.setString(comment)
        xssfComment
    }

    def obtainCellValue(cell: Cell): String = {
        var cellValue: String = null
        val cellType: CellType = cell.getCellTypeEnum
        if (cellType == CellType.STRING) {
            cellValue = cell.getStringCellValue
        } else if (cellType == CellType.NUMERIC) {
            cellValue = cell.getNumericCellValue.toString
        }
        cellValue
    }

    @RequestMapping(value = Array("/download"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_UTF8_VALUE))
    @ResponseBody
    def download(): Unit = {
        val tenantId = ""
        val branchId = ""
        val tmpdir: String = System.getProperty("java.io.tmpdir")
        val file: File = new File(tmpdir + File.separator + tenantId + "_" + branchId + ".xls")
        val inputStream: InputStream = new FileInputStream(file)
        var length: Int = 0
        val buffer: Array[Byte] = new Array[Byte](1024)
        val httpServletResponse: HttpServletResponse = ApplicationHandler.getHttpServletResponse
        httpServletResponse.setContentType(MimeMappingUtils.obtainMimeTypeByExtension("xls"))
        val outputStream: OutputStream = httpServletResponse.getOutputStream
        while (length != -1) {
            length = inputStream.read(buffer, 0, 1024)
            outputStream.write(buffer, 0, length);
        }
        inputStream.close()
        file.delete()
        outputStream.close()
    }

    @RequestMapping(value = Array("/downloadGoodsTemplate"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_UTF8_VALUE))
    @ResponseBody
    def downloadGoodsTemplate: Unit = {
        val sheetName: String = "商品"
        val titles: Array[String] = Array("编码", "条码", "店内码", "商品名称", "分类", "单位", "进价", "售价", "配送价", "会员价", "状态", "称重PLU")
        val categories: List[Map[String, Object]] = new ArrayList[Map[String, Object]]()
        val category: Map[String, Object] = new HashMap[String, Object]()
        category.put("name", "烧烤")
        category.put("id", BigInteger.TEN)
        categories.add(category)

        val units: List[Map[String, Object]] = new ArrayList[Map[String, Object]]()
        val unit: Map[String, Object] = new HashMap[String, Object]()
        unit.put("name", "份")
        unit.put("id", BigInteger.ONE)
        units.add(unit)

        val statuses: Array[String] = Array("正常", "停售")
        val workbook: Workbook = PoiUtils.buildGoodsTemplate(sheetName, titles, categories, units, statuses)

        val fileName: String = "商品导入模板"
        val httpServletResponse: HttpServletResponse = ApplicationHandler.getHttpServletResponse
        httpServletResponse.setContentType(MimeMappingUtils.obtainMimeTypeByExtension("xls"))
        httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx")
        val outputStream: OutputStream = httpServletResponse.getOutputStream
        workbook.write(outputStream)
        workbook.close()
        outputStream.close()
    }
}
