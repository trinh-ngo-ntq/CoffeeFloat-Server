package com.login.model;

public class LineEntityRepos {
    private String access_token;
    private String expires_in;
    private String id_token;
    private String refresh_token;
    private String scope;
    private String token_type;

    public LineEntityRepos(String access_token, String expires_in, String id_token, String refresh_token, String scope, String token_type) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.id_token = id_token;
        this.refresh_token = refresh_token;
        this.scope = scope;
        this.token_type = token_type;
    }

    public LineEntityRepos() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }
}
