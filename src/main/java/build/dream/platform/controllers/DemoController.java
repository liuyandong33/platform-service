package build.dream.platform.controllers;

import build.dream.common.models.newland.BarcodePayModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.NewLandUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/demo")
public class DemoController {
    @RequestMapping(value = "/demo")
    @ResponseBody
    public String demo() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            BarcodePayModel barcodePayModel = ApplicationHandler.instantiateObject(BarcodePayModel.class, requestParameters);
            Map<String, String> result = NewLandUtils.barcodePay("1", "1", barcodePayModel);
            return GsonUtils.toJson(result);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
