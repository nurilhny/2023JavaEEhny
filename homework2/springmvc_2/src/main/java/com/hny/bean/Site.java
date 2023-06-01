package com.hny.bean;

public class Site {
    private Integer siteId;
    private String siteName;
    private String siteAddress;
    private String phone;

    public Site() {
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "site{" +
                "siteId=" + siteId +
                ", siteName='" + siteName + '\'' +
                ", siteAddress='" + siteAddress + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
