package com.login.exception;

/**
 * A checked exception for invalid access token (fb token, google token, phone number token...)
 * <br >
 * A status code 401 will be thrown
 * 
 * @author trinhNX
 * 
 */
public class InvalidAccessToken extends Exception {
    private static final long serialVersionUID = -133601542033044270L;

    public InvalidAccessToken(String msg) {
        super(msg);
    }
    
    public InvalidAccessToken(Exception e) {
        super(e);
    }

}
