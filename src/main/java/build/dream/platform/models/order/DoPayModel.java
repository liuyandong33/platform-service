package build.dream.platform.models.order;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.platform.constants.Constants;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class DoPayModel extends BasicModel {
    @NotNull
    private BigInteger orderInfoId;

    @NotNull
    private BigInteger userId;

    private Integer paidScene;

    private String authCode;

    public BigInteger getOrderInfoId() {
        return orderInfoId;
    }

    public void setOrderInfoId(BigInteger orderInfoId) {
        this.orderInfoId = orderInfoId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public Integer getPaidScene() {
        return paidScene;
    }

    public void setPaidScene(Integer paidScene) {
        this.paidScene = paidScene;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        ApplicationHandler.inArray(Constants.PAID_SCENES, paidScene, "paidScene");
        if (paidScene == Constants.PAID_SCENE_WEI_XIN_MICROPAY || paidScene == Constants.PAID_SCENE_ALIPAY_FAC_TO_FACE) {
            ApplicationHandler.notBlank(authCode, "authCode");
        }
    }
}
