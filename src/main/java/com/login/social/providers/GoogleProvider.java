package com.login.social.providers;

import com.login.exception.ResourceNotFoundException;
import com.login.repository.UserRepository;
import com.login.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.plus.Person;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.login.model.UserBean;

@Service
public class GoogleProvider {

    private static final String GOOGLE = "google";

    @Autowired
    JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BaseProvider baseProvider;

    //Login google by token
    public UserBean populateUserDetailsFromGoogle(String token){
        Google google = new GoogleTemplate(token);
        Person googleUser = google.plusOperations().getGoogleProfile();
        if (googleUser == null || googleUser.getId() == null) {
            throw new ResourceNotFoundException("Token is invalid");
        }
        UserBean userBean = userRepository.findByUserId(googleUser.getId());
        if (userBean == null) {
            userBean = new UserBean();
        }
            userBean.setEmail(googleUser.getAccountEmail());
            userBean.setUserId(googleUser.getId());
            userBean.setFullName(googleUser.getGivenName() + " " + googleUser.getFamilyName());
            userBean.setAvatar(googleUser.getImageUrl());
            userBean.setGender(googleUser.getGender());
            userBean.setProvider(GOOGLE);
            userBean.setPassword(GOOGLE);
        if (baseProvider.checkLoginSocial(userBean)) {
            baseProvider.saveUserDetails(userBean);
            baseProvider.autoLoginUser(userBean);
            return userBean;
        }
        return null;
    }

}
