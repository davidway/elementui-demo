package com.blockchain.dto;

import com.alibaba.fastjson.JSONObject;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class IssSearchDto {
    @NotNull
    ConfigDto configDto;
    @NotEmpty
    private String userPrivateKey;
    @NotEmpty
    private String userPublicKey;

    private String account;

    private JSONObject content;

    private JSONObject extraInfo;
    private String tHash;
    private String[] bTime;
    private String[] bHeight;
    @NotNull
    private Integer pageNo;
    @NotNull
    private Integer pageLimit;

    public String gettHash() {
        return tHash;
    }

    public void settHash(String tHash) {
        this.tHash = tHash;
    }

    public ConfigDto getConfigDto() {
        return configDto;
    }

    public void setConfigDto(ConfigDto configDto) {
        this.configDto = configDto;
    }

    public String getUserPrivateKey() {
        return userPrivateKey;
    }

    public void setUserPrivateKey(String userPrivateKey) {
        this.userPrivateKey = userPrivateKey;
    }

    public String getUserPublicKey() {
        return userPublicKey;
    }

    public void setUserPublicKey(String userPublicKey) {
        this.userPublicKey = userPublicKey;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public JSONObject getContent() {
        return content;
    }

    public void setContent(JSONObject content) {
        this.content = content;
    }

    public JSONObject getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(JSONObject extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String[] getbTime() {
        return bTime;
    }

    public void setbTime(String[] bTime) {
        this.bTime = bTime;
    }

    public String[] getbHeight() {
        return bHeight;
    }

    public void setbHeight(String[] bHeight) {
        this.bHeight = bHeight;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageLimit() {
        return pageLimit;
    }

    public void setPageLimit(Integer pageLimit) {
        this.pageLimit = pageLimit;
    }
}
