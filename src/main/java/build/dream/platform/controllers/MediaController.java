package build.dream.platform.controllers;

import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.MimeMappingUtils;
import build.dream.common.utils.OutUtils;
import build.dream.common.utils.ZXingUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
@RequestMapping(value = "/media")
public class MediaController {
    /**
     * 生成二维码
     */
    @RequestMapping(value = "/generateQRCode")
    @ResponseBody
    public void generateQRCode() throws IOException {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        String width = requestParameters.get("width");
        if (StringUtils.isBlank(width)) {
            width = "400";
        }

        String height = requestParameters.get("height");
        if (StringUtils.isBlank(height)) {
            height = "400";
        }
        String text = requestParameters.get("text");
        HttpServletResponse httpServletResponse = ApplicationHandler.getHttpServletResponse();
        httpServletResponse.setContentType(MimeMappingUtils.obtainMimeTypeByExtension(ZXingUtils.FORMAT_NAME_PNG));
        OutputStream outputStream = httpServletResponse.getOutputStream();
        ZXingUtils.generateQRCode(Integer.parseInt(width), Integer.parseInt(height), text, outputStream);
        outputStream.close();
    }

    /**
     * 下载二维码
     */
    @RequestMapping(value = "/downloadQRCode")
    @ResponseBody
    public void downloadQRCode() throws IOException {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        String width = requestParameters.get("width");
        if (StringUtils.isBlank(width)) {
            width = "400";
        }

        String height = requestParameters.get("height");
        if (StringUtils.isBlank(height)) {
            height = "400";
        }
        String text = requestParameters.get("text");
        String fileName = requestParameters.get("fileName");
        HttpServletResponse httpServletResponse = ApplicationHandler.getHttpServletResponse();
        httpServletResponse.setContentType(MimeMappingUtils.obtainMimeTypeByExtension(ZXingUtils.FORMAT_NAME_PNG));
        httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + fileName + ZXingUtils.FORMAT_NAME_PNG);
        OutputStream outputStream = httpServletResponse.getOutputStream();
        ZXingUtils.generateQRCode(Integer.parseInt(width), Integer.parseInt(height), text, outputStream);
        outputStream.close();
    }


    /**
     * 显示外部图片，绕过防盗链
     */
    @RequestMapping(value = "/doGet")
    @ResponseBody
    public ResponseEntity<byte[]> doGet() throws IOException {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        String url = requestParameters.get("url");
        return OutUtils.doGetOrdinaryWithRequestParameters(url, null, null);
    }
}
