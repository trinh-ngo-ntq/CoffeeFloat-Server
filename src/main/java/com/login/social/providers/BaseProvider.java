package com.login.social.providers;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.google.api.Google;

import com.login.autologin.Autologin;
import com.login.model.UserBean;
import com.login.repository.UserRepository;
import org.springframework.social.twitter.api.Twitter;

@Configuration
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BaseProvider {

    private Facebook facebook;
    private Google google;
    private ConnectionRepository connectionRepository;
    private Twitter twitter;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    protected Autologin autologin;

    public BaseProvider(Facebook facebook, Google google, ConnectionRepository connectionRepository, Twitter twitter) {
        this.facebook = facebook;
        this.google = google;
        this.connectionRepository = connectionRepository;
        this.twitter = twitter;
    }
    
    @Transactional
    protected void saveUserDetails(UserBean userBean) {
        if (StringUtils.isNotEmpty(userBean.getPassword())) {
            userBean.setPassword(bCryptPasswordEncoder.encode(userBean.getPassword()));
        }
        userRepository.save(userBean);
    }

    public void autoLoginUser(UserBean userBean) {
        autologin.setSecuritySocialContext(userBean);
    }

    public Facebook getFacebook() {
        return facebook;
    }

    public void setFacebook(Facebook facebook) {
        this.facebook = facebook;
    }

    public ConnectionRepository getConnectionRepository() {
        return connectionRepository;
    }

    public void setConnectionRepository(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    public Google getGoogle() {
        return google;
    }

    public void setGoogle(Google google) {
        this.google = google;
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }

    public boolean checkLoginSocial(UserBean user) {
        if (user.getUserId() != null){
            return true;
        }
        return false;
    }

}
