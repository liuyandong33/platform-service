package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.platform.models.mqtt.ObtainMqttConfigModel;
import build.dream.platform.services.MqttService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/mqtt")
public class MqttController {
    /**
     * 获取MQTT配置
     *
     * @return
     */
    @RequestMapping(value = "/obtainMqttConfig", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainMqttConfigModel.class, serviceClass = MqttService.class, serviceMethodName = "obtainMqttConfig", error = "获取MQTT配置失败")
    public String obtainMqttConfig() {
        return null;
    }
}
