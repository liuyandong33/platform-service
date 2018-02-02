package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.*;
import build.dream.platform.models.activity.SaveSpecialGoodsActivityModel;
import build.dream.platform.services.ActivityService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping(value = "/activity")
public class ActivityController extends BasicController {
    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/saveSpecialGoodsActivity")
    @ResponseBody
    public String saveSpecialGoodsActivity() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            SaveSpecialGoodsActivityModel saveSpecialGoodsActivityModel = ApplicationHandler.instantiateObject(SaveSpecialGoodsActivityModel.class, requestParameters);
            String specialGoodsActivityInfos = requestParameters.get("specialGoodsActivityInfos");
            saveSpecialGoodsActivityModel.setSpecialGoodsActivityInfos(specialGoodsActivityInfos);
            saveSpecialGoodsActivityModel.validateAndThrow();
            apiRest = activityService.saveSpecialGoodsActivity(saveSpecialGoodsActivityModel);
        } catch (Exception e) {
            LogUtils.error("保存特价商品活动失败", controllerSimpleName, "saveSpecialGoodsActivity", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "generateQRCode")
    @ResponseBody
    public void generateQRCode() throws IOException, WriterException {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        int width = Integer.valueOf(requestParameters.get("width"));
        int height = Integer.valueOf(requestParameters.get("width"));
        String text = requestParameters.get("text");
        HttpServletResponse httpServletResponse = ApplicationHandler.getHttpServletResponse();
        httpServletResponse.setContentType(MimeMappingUtils.obtainMimeTypeByExtension(QRCodeUtils.FORMAT));
        QRCodeUtils.generateQRCode(width, height, text, httpServletResponse.getOutputStream());
    }

    @RequestMapping(value = "downloadQRCode")
    @ResponseBody
    public void downloadQRCode() throws IOException, WriterException {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        int width = Integer.valueOf(requestParameters.get("width"));
        int height = Integer.valueOf(requestParameters.get("width"));
        String text = requestParameters.get("text");
        String fileName = requestParameters.get("fileName");
        HttpServletResponse httpServletResponse = ApplicationHandler.getHttpServletResponse();
        httpServletResponse.setContentType(MimeMappingUtils.obtainMimeTypeByExtension(QRCodeUtils.FORMAT));
        httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + fileName + ".png");
        QRCodeUtils.generateQRCode(width, height, text, httpServletResponse.getOutputStream());
    }
}
