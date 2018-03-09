package build.dream.platform.models.goods;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ListGoodsInfosModel extends BasicModel {
    @NotNull
    private Integer page;

    @NotNull
    private Integer rows;

    private String searchString;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
