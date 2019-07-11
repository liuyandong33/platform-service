package build.dream.platform.models.agent;

import build.dream.common.models.BasicModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class SaveAgentFormModel extends BasicModel {
    @NotNull
    @Length(max = 20)
    private String name;

    @NotNull
    @Length(max = 20)
    private String linkman;

    @NotNull
    @Length(max = 20)
    private String mobile;

    @NotNull
    @Length(max = 20)
    private String email;

    @NotNull
    @Length(max = 20)
    private String provinceCode;

    @NotNull
    @Length(max = 20)
    private String cityCode;

    @NotNull
    @Length(max = 20)
    private String districtCode;

    @NotNull
    @Length(max = 255)
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
