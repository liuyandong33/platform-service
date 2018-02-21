package build.dream.platform.models.order;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.platform.constants.Constants;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class DoPayModel extends BasicModel {
    private static final Integer[] PAID_SCENES = {Constants.PAID_SCENE_WEI_XIN_PUBLIC_ACCOUNT, Constants.PAID_SCENE_WEI_XIN_H5, Constants.PAID_SCENE_WEI_XIN_APP, Constants.PAID_SCENE_WEI_XIN_NATIVE, Constants.PAID_SCENE_ALIPAY_MOBILE_WEBSITE, Constants.PAID_SCENE_ALIPAY_PC_WEBSITE, Constants.PAID_SCENE_ALIPAY_APP};
    @NotNull
    private BigInteger orderInfoId;

    @NotNull
    private BigInteger userId;

    private Integer paidScene;

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

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        ApplicationHandler.inArray(PAID_SCENES, paidScene, "paidScene");
    }
}
