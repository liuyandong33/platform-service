package build.dream.platform.models.order;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.platform.constants.Constants;

import javax.validation.constraints.NotNull;

public class DoPayModel extends BasicModel {
    @NotNull
    private Long orderInfoId;

    private Integer paidScene;

    private String authCode;

    private String openId;

    private String subOpenId;

    public Long getOrderInfoId() {
        return orderInfoId;
    }

    public void setOrderInfoId(Long orderInfoId) {
        this.orderInfoId = orderInfoId;
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSubOpenId() {
        return subOpenId;
    }

    public void setSubOpenId(String subOpenId) {
        this.subOpenId = subOpenId;
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
