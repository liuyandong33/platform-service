package build.dream.platform.models.goods;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class SaveGoodsTypeModel extends BasicModel {
    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private Boolean single;

    private String renewSql;

    private String disableSql;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getSingle() {
        return single;
    }

    public void setSingle(Boolean single) {
        this.single = single;
    }

    public String getRenewSql() {
        return renewSql;
    }

    public void setRenewSql(String renewSql) {
        this.renewSql = renewSql;
    }

    public String getDisableSql() {
        return disableSql;
    }

    public void setDisableSql(String disableSql) {
        this.disableSql = disableSql;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
