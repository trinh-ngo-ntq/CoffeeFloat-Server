package com.login.social.providers;

import com.login.repository.UserRepository;
import com.login.security.JwtService;
import org.jinstagram.Instagram;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.login.config.InstagramBuildService;
import com.login.model.UserBean;

@Service
public class InstagramProvider {
    private static final String INSTAGRAM = "instagram";

    @Autowired
    BaseProvider baseProvider ;
    @Autowired
    JwtService jwtService;
    @Autowired
    InstagramBuildService instagramObj;
    @Autowired
    UserRepository userRepository;

	public UserBean getInstagramUserData(String token) throws Exception {
        instagramObj.build();
        Instagram instagram = instagramObj.getInstagram(token);//verify token.
        UserInfo userInfo = instagram.getCurrentUserInfo();
        if(userInfo == null || userInfo.getData().getId()==null) {
            throw new Exception("Token is invalid");
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
        try {
            if (baseProvider.checkLoginSocial(userBean)) {
                String result = jwtService.generateTokenLogin(userBean.getEmail());
                userBean.setAccesstoken(result);
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        baseProvider.saveUserDetails(userBean);
        baseProvider.autoLoginUser(userBean);
        return userBean;
    }
}
