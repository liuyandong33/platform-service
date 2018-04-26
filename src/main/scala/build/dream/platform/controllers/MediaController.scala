package build.dream.platform.controllers

import java.io.OutputStream
import java.util.{HashMap, Map}

import build.dream.common.utils.{ApplicationHandler, MimeMappingUtils, ProxyUtils, QRCodeUtils}
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
        httpServletResponse.setContentType(MimeMappingUtils.obtainMimeTypeByExtension(QRCodeUtils.FORMAT))
        val outputStream: OutputStream = httpServletResponse.getOutputStream
        QRCodeUtils.generateQRCode(width.toInt, height.toInt, text, outputStream)
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
        val text = requestParameters.get("text")
        val fileName = requestParameters.get("fileName")
        val httpServletResponse = ApplicationHandler.getHttpServletResponse
        httpServletResponse.setContentType(MimeMappingUtils.obtainMimeTypeByExtension(QRCodeUtils.FORMAT))
        httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + fileName + ".png")
        val outputStream: OutputStream = httpServletResponse.getOutputStream
        QRCodeUtils.generateQRCode(width.toInt, height.toInt, text, outputStream)
        outputStream.close()
    }


    /**
      * 显示外部图片，绕过防盗链
      */
    @RequestMapping(value = Array("/doGetOriginal"))
    @ResponseBody
    def doGetOriginal(): ResponseEntity[Array[Byte]] = {
        val requestParameters: Map[String, String] = ApplicationHandler.getRequestParameters
        val url: String = requestParameters.get("url")
        val doGetOriginalRequestParameters: Map[String, String] = new HashMap[String, String]
        doGetOriginalRequestParameters.put("url", url)
        ProxyUtils.doGetOriginal("out", "proxy", "doGetOriginal", doGetOriginalRequestParameters);
    }
}
