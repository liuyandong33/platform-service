package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.catering.domains.MqttConfig;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.ValidateUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.mqtt.ObtainMqttConfigModel;
import org.springframework.stereotype.Service;

@Service
public class MqttService {
    /**
     * 获取MQTT配置
     *
     * @param obtainMqttConfigModel
     * @return
     */
    public ApiRest obtainMqttConfig(ObtainMqttConfigModel obtainMqttConfigModel) {
        String partitionCode = obtainMqttConfigModel.getPartitionCode();
        String serviceName = obtainMqttConfigModel.getServiceName();

        SearchModel searchModel = SearchModel.builder()
                .autoSetDeletedFalse()
                .addSearchCondition(MqttConfig.ColumnName.PARTITION_CODE, Constants.SQL_OPERATION_SYMBOL_EQUAL, partitionCode)
                .addSearchCondition(MqttConfig.ColumnName.SERVICE_NAME, Constants.SQL_OPERATION_SYMBOL_EQUAL, serviceName)
                .build();

        MqttConfig mqttConfig = DatabaseHelper.find(MqttConfig.class, searchModel);
        ValidateUtils.notNull(mqttConfig, "MQTT配置不存在！");

        return ApiRest.builder().data(mqttConfig).className(MqttConfig.class.getName()).message("获取MQTT配置成功！").successful(true).build();
    }
}
