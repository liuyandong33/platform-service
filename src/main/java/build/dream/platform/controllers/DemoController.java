package build.dream.platform.controllers;

import build.dream.common.models.newland.BarcodePayModel;
import build.dream.common.models.newland.BarcodePosPayModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.NewLandUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.utils.WebSocketUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/demo")
public class DemoController {
    @RequestMapping(value = "/barcodePay")
    @ResponseBody
    public String barcodePay() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            BarcodePayModel barcodePayModel = ApplicationHandler.instantiateObject(BarcodePayModel.class, requestParameters);
            Map<String, String> result = NewLandUtils.barcodePay("1", "1", barcodePayModel);
            return GsonUtils.toJson(result);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/barcodePosPay")
    @ResponseBody
    public String barcodePosPay() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            BarcodePosPayModel barcodePosPayModel = ApplicationHandler.instantiateObject(BarcodePosPayModel.class, requestParameters);
            Map<String, String> result = NewLandUtils.barcodePosPay("1", "1", barcodePosPayModel);
            return GsonUtils.toJson(result);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/index")
    public ModelAndView sendInfo() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("demo/index");
        return modelAndView;
    }

    @RequestMapping(value = "/sendMessage")
    @ResponseBody
    public String sendMessage() throws IOException {
        String sessionId = ApplicationHandler.getSessionId();
        TextMessage textMessage = new TextMessage(UUID.randomUUID().toString());
        WebSocketUtils.sendMessage(sessionId, textMessage);
        return Constants.SUCCESS;
    }
}
