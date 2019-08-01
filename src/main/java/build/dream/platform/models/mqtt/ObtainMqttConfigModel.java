package build.dream.platform.models.mqtt;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ObtainMqttConfigModel extends BasicModel {
    @NotNull
    private String partitionCode;

    @NotNull
    private String serviceName;

    public String getPartitionCode() {
        return partitionCode;
    }

    public void setPartitionCode(String partitionCode) {
        this.partitionCode = partitionCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
