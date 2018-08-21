package com.login.social.providers;

import com.login.exception.ResourceNotFoundException;
import com.login.model.UserBean;
import com.login.repository.UserRepository;
import com.login.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

@Service
public class TwitterProvider {
    private static final String TWITTER = "twitter";

    @Autowired
    BaseProvider baseProvider ;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;

    public UserBean populateUserDetailsFromTwitter(String token){
        org.springframework.social.twitter.api.Twitter twitter = new TwitterTemplate(token);
        TwitterProfile twitterProfile = twitter.userOperations().getUserProfile();
        if(twitterProfile == null || Long.toString(twitterProfile.getId()) == null) {
            throw new ResourceNotFoundException("Token is invalid");
        }
        UserBean userBean = userRepository.findByUserId(Long.toString(twitterProfile.getId()));
        if (userBean == null) {
            userBean = new UserBean();
        }
        userBean.setUserId(Long.toString(twitterProfile.getId()));
        userBean.setFullName(twitterProfile.getName());
        userBean.setAvatar(twitterProfile.getProfileImageUrl());
        userBean.setProvider(TWITTER);
        userBean.setPassword(TWITTER);
        if (baseProvider.checkLoginSocial(userBean)) {
            baseProvider.saveUserDetails(userBean);
            baseProvider.autoLoginUser(userBean);
            return userBean;
        }
        return null;
    }
}
