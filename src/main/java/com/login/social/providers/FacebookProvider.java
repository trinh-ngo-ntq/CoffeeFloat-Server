package com.login.social.providers;

import com.login.model.UserBean;
import com.login.repository.UserRepository;
import com.login.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;


@Service
public class FacebookProvider {

    private static final String FACEBOOK = "facebook";
    @Autowired
    BaseProvider baseProvider;
    @Autowired
    UserRepository userRepository;


    public UserBean populateUserDetailsFromFacebook(String token) {
        Facebook facebook = new FacebookTemplate(token);
        String[] fields = {"id", "cover", "birthday", "email", "gender", "name"};
        User user = facebook.fetchObject("me", User.class, fields);
        if (user == null || user.getId() == null) {
            throw new com.login.exception.ResourceNotFoundException("Token is invalid");
        }
        UserBean userBean = userRepository.findByUserId(user.getId());
        if (userBean == null) {
            userBean = new UserBean();
        }
        userBean.setEmail(user.getEmail());
        userBean.setUserId(user.getId());
        userBean.setFullName(user.getName());
        userBean.setAvatar(user.getCover() == null ? "" : user.getCover().getSource());
        userBean.setGender(user.getGender());
        userBean.setProvider(FACEBOOK);
        userBean.setPassword(FACEBOOK);
        if (baseProvider.checkLoginSocial(userBean)) {
            baseProvider.saveUserDetails(userBean);
            baseProvider.autoLoginUser(userBean);
            return userBean;
        } else {
            return null;
        }

    }
}
