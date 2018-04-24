package build.dream.platform.controllers;

import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.MethodCaller;
import build.dream.common.utils.MimeMappingUtils;
import build.dream.common.utils.QRCodeUtils;
import build.dream.platform.models.activity.ObtainAllActivitiesModel;
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

    /**
     * 保存特价商品活动
     *
     * @return
     */
    @RequestMapping(value = "/saveSpecialGoodsActivity")
    @ResponseBody
    public String saveSpecialGoodsActivity() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            SaveSpecialGoodsActivityModel saveSpecialGoodsActivityModel = ApplicationHandler.instantiateObject(SaveSpecialGoodsActivityModel.class, requestParameters);
            String specialGoodsActivityInfos = requestParameters.get("specialGoodsActivityInfos");
            saveSpecialGoodsActivityModel.setSpecialGoodsActivityInfos(specialGoodsActivityInfos);
            saveSpecialGoodsActivityModel.validateAndThrow();
            return activityService.saveSpecialGoodsActivity(saveSpecialGoodsActivityModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "保存特价商品活动失败", requestParameters);
    }

    @RequestMapping(value = "/generateQRCode")
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

    @RequestMapping(value = "/downloadQRCode")
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

    /**
     * 获取所有活动
     *
     * @return
     */
    @RequestMapping(value = "/obtainAllActivities")
    @ResponseBody
    public String obtainAllActivities() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ObtainAllActivitiesModel obtainAllActivitiesModel = ApplicationHandler.instantiateObject(ObtainAllActivitiesModel.class, requestParameters);
            obtainAllActivitiesModel.validateAndThrow();
            return activityService.obtainAllActivities(obtainAllActivitiesModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "获取活动失败", requestParameters);
    }
}
