package com.login.social.providers;

import com.login.config.InstagramConfig;
import com.login.exception.ResourceNotFoundException;
import com.login.repository.UserRepository;
import com.login.security.JwtService;
import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.exceptions.InstagramException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.login.model.UserBean;

@Service
public class InstagramProvider {
    private static final String INSTAGRAM = "instagram";

    @Autowired
    BaseProvider baseProvider ;
    @Autowired
    InstagramConfig instagramConfig;
    @Autowired
    JwtService jwtService;
    @Autowired
    UserRepository userRepository;

	public UserBean getInstagramUserData(String token) {
        Token token1 = new Token(token,instagramConfig.clientSecret);
        Instagram instagram = new Instagram(token1);//verify token.
        UserInfo userInfo = null;
        try {
            userInfo = instagram.getCurrentUserInfo();
        } catch (InstagramException e) {
            throw new ResourceNotFoundException("Token is invalid");
        }

        UserBean userBean = userRepository.findByUserId(userInfo.getData().getId());
        if (userBean == null) {
            userBean = new UserBean();
        }
        userBean.setEmail(userInfo.getData().getUsername());
        userBean.setUserId(userInfo.getData().getId());
        userBean.setFullName(userInfo.getData().getFullName());
        userBean.setAvatar(userInfo.getData().getProfilePicture());
        userBean.setProvider(INSTAGRAM);
        userBean.setPassword(INSTAGRAM);
        if (baseProvider.checkLoginSocial(userBean)) {
            baseProvider.saveUserDetails(userBean);
            baseProvider.autoLoginUser(userBean);
            return userBean;
        }
        return null;
    }
}
