package com.blockchain.dto;

import com.alibaba.fastjson.JSONObject;
import org.hibernate.validator.constraints.NotEmpty;

public class IssDto {
    @NotEmpty
    ConfigDto configDto;
    @NotEmpty
    private String userPrivateKey;
    @NotEmpty
    private String userPublicKey;
    @NotEmpty
    private String account;
    @NotEmpty
    private JSONObject content;
    @NotEmpty
    private JSONObject extraInfo;

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
}
