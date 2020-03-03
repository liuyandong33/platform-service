package build.dream.platform.models.tenant;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class UpdateTenantInfoModel extends BasicModel {
    @NotNull
    private Long id;
    private String name;
    private Integer vipSharedType;
    private Long dadaSourceId;
    @NotNull
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getDadaSourceId() {
        return dadaSourceId;
    }

    public void setDadaSourceId(Long dadaSourceId) {
        this.dadaSourceId = dadaSourceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
