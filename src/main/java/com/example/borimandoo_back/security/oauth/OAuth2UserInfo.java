package com.example.borimandoo_back.security.oauth;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getName();
}
