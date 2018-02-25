package build.dream.platform.models.register;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.platform.constants.Constants;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class RegisterTenantModel extends BasicModel {
    private static final String[] businesses = {Constants.BUSINESS_CATERING, Constants.BUSINESS_RETAIL};

    @NotNull
    @Length(max = 20)
    private String name;

    @NotNull
    @Length(max = 20)
    private String linkman;

    private String business;

    @NotNull
    @Length(max = 10)
    private String provinceCode;

    @NotNull
    @Length(max = 10)
    private String provinceName;

    @NotNull
    @Length(max = 10)
    private String cityCode;

    @NotNull
    @Length(max = 10)
    private String cityName;

    @NotNull
    @Length(max = 10)
    private String districtCode;

    @NotNull
    @Length(max = 10)
    private String districtName;

    @NotNull
    @Length(max = 255)
    private String address;

    @NotNull
    @Length(max = 20)
    private String longitude;

    @NotNull
    @Length(max = 20)
    private String latitude;

    @NotNull
    @Length(max = 20)
    private String contactPhone;

    @NotNull
    @Length(max = 20)
    private String password;

    @NotNull
    private BigInteger userId;

    public static String[] getBusinesses() {
        return businesses;
    }

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

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    @Override
    public boolean validate() {
        return super.validate() && ArrayUtils.contains(businesses, business);
    }

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        ApplicationHandler.inArray(businesses, business, "business");
    }
}
