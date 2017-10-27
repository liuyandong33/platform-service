package build.dream.platform.models.weixin;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class DeleteWeiXinOpenPlatformApplicationModel extends BasicModel {
    @NotNull
    private String appId;
    @NotNull
    private BigInteger userId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }
}
