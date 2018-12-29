package build.dream.platform.controllers

import java.io.OutputStream
import java.util.Map

import build.dream.common.utils._
import javax.servlet.http.HttpServletResponse
import org.apache.commons.lang.StringUtils
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}

@Controller
@RequestMapping(value = Array("/media"))
class MediaController {
    /**
      * 生成二维码
      */
    @RequestMapping(value = Array("/generateQRCode"))
    @ResponseBody
    def generateQRCode(): Unit = {
        val requestParameters: Map[String, String] = ApplicationHandler.getRequestParameters
        var width: String = requestParameters.get("width")
        if (StringUtils.isBlank(width)) {
            width = "400"
        }

        var height: String = requestParameters.get("height")
        if (StringUtils.isBlank(height)) {
            height = "400"
        }
        val text = requestParameters.get("text")
        val httpServletResponse: HttpServletResponse = ApplicationHandler.getHttpServletResponse
        httpServletResponse.setContentType(MimeMappingUtils.obtainMimeTypeByExtension(ZXingUtils.FORMAT_NAME_PNG))
        val outputStream: OutputStream = httpServletResponse.getOutputStream
        ZXingUtils.generateQRCode(width.toInt, height.toInt, text, outputStream)
        outputStream.close()
    }

    /**
      * 下载二维码
      */
    @RequestMapping(value = Array("/downloadQRCode"))
    @ResponseBody
    def downloadQRCode(): Unit = {
        val requestParameters: Map[String, String] = ApplicationHandler.getRequestParameters
        var width: String = requestParameters.get("width")
        if (StringUtils.isBlank(width)) {
            width = "400"
        }

        var height: String = requestParameters.get("height")
        if (StringUtils.isBlank(height)) {
            height = "400"
        }
        val text: String = requestParameters.get("text")
        val fileName: String = requestParameters.get("fileName")
        val httpServletResponse: HttpServletResponse = ApplicationHandler.getHttpServletResponse
        httpServletResponse.setContentType(MimeMappingUtils.obtainMimeTypeByExtension(ZXingUtils.FORMAT_NAME_PNG))
        httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + fileName + ZXingUtils.FORMAT_NAME_PNG)
        val outputStream: OutputStream = httpServletResponse.getOutputStream
        ZXingUtils.generateQRCode(width.toInt, height.toInt, text, outputStream)
        outputStream.close()
    }


    /**
      * 显示外部图片，绕过防盗链
      */
    @RequestMapping(value = Array("/doGet"))
    @ResponseBody
    def doGet(): ResponseEntity[Array[Byte]] = {
        val requestParameters: Map[String, String] = ApplicationHandler.getRequestParameters
        val url: String = requestParameters.get("url")
        OutUtils.doGetOrdinaryWithRequestParameters(url, null, null)
    }
}
