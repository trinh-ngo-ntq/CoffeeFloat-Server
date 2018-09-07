package com.login.util;

public class Constants {
    public static class HTTP_STATUS {
        // SUCCESS
        public static final String OK = "200";
        public static final String CREATED = "201";
        // ERROR
        public static final String BAD_REQUEST = "400";
        public static final String UNAUTHORIZED = "401";
        public static final String FORBIDDEN = "403";
        public static final String NOT_FOUND = "200";
        public static final String INTERNAL_SERVER_ERROR = "500";

        public static final String UNPROCESSABLE_ENTITY_EXT = "4221";

    }

    public static class HTTP_STATUS_MSG {
        public static final String ERROR_COMMON = "Server error!";
        public static final String ERROR_NOT_FOUND = "Resource not found";
        public static final String ERROR_BAD_REQUEST = "Bad Request";
    }

    public class LineConst {
        public static final String urlToken = "https://api.line.me/oauth2/v2.1/token";
        public static final String urlProfile = "https://api.line.me/v2/profile";
        public static final String grantType = "grant_type";
        public static final String code = "code";
        public static final String redirectUri = "redirect_uri";
        public static final String clientId = "client_id";
        public static final String clientSecret = "client_secret";
        public static final String authorization = "Authorization";
        public static final String bearer = "Bearer ";
        public static final String urlAuthorize = "https://access.line.me/oauth2/v2.1/authorize";
        public static final String responseType = "response_type";
    }

    public class InstagramConst {
        public static final String urlAuthorize = "https://www.instagram.com/oauth/authorize/";
        public static final String redirectUri = "redirect_uri";
        public static final String clientId = "client_id";
        public static final String paramAuthorize = "response_type=code";
    }
    
    public static final class FacebookAccountKitConst {
    	public static final String ACCOUNT_KIT_LINK = "https://graph.accountkit.com/v1.3/me/?access_token=";
    }
    
}
