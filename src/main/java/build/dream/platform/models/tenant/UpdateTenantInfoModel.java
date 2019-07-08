package build.dream.platform.models.tenant;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class UpdateTenantInfoModel extends BasicModel {
    @NotNull
    private BigInteger id;
    private String name;
    private Integer vipSharedType;
    private BigInteger dadaSourceId;
    @NotNull
    private BigInteger userId;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVipSharedType() {
        return vipSharedType;
    }

    public void setVipSharedType(Integer vipSharedType) {
        this.vipSharedType = vipSharedType;
    }

    public BigInteger getDadaSourceId() {
        return dadaSourceId;
    }

    public void setDadaSourceId(BigInteger dadaSourceId) {
        this.dadaSourceId = dadaSourceId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }
}
