package build.dream.platform.models.jddj;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ListJDDJCodesModel extends BasicModel {
    private String searchString;

    @NotNull
    private Integer page;

    @NotNull
    private Integer rows;

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

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
}
