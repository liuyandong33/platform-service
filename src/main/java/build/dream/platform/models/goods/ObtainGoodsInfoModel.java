package build.dream.platform.models.goods;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ObtainGoodsInfoModel extends BasicModel {
    @NotNull
    private Long goodsId;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
