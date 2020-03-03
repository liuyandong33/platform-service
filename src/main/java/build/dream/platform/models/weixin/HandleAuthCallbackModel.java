package build.dream.platform.models.weixin;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class HandleAuthCallbackModel extends BasicModel {
    @NotNull
    private Long tenantId;

    @NotNull
    private String componentAppId;

    @NotNull
    private String authCode;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getComponentAppId() {
        return componentAppId;
    }

    public void setComponentAppId(String componentAppId) {
        this.componentAppId = componentAppId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
