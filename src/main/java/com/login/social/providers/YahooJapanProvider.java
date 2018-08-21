package com.login.social.providers;

import com.login.exception.ResourceNotFoundException;
import com.login.model.UserBean;
import com.login.repository.UserRepository;
import com.login.security.JwtService;
import jp.co.yahoo.yconnect.YConnectExplicit;
import jp.co.yahoo.yconnect.core.oidc.UserInfoObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YahooJapanProvider {
    private static final String YAHOOJP = "yahoojp";
    @Autowired
    UserRepository userRepository;
    @Autowired
    YConnectExplicit yConnectExplicit;
    @Autowired
    BaseProvider baseProvider;

    public UserBean getyObject(String token) {
        UserInfoObject yObject;
        try {
            yConnectExplicit.requestUserInfo(token);
            yObject = yConnectExplicit.getUserInfoObject();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Token invalid");
        }
        UserBean userBean = userRepository.findByUserId(yObject.getUserId());
        if (userBean == null || userBean.getUserId() == null) {
            userBean = new UserBean();
        }
        userBean.setUserId(yObject.getUserId());
        userBean.setEmail(yObject.getEmail());
        userBean.setFullName(yObject.getName());
        userBean.setGender(yObject.getGender());
        userBean.setPassword(YAHOOJP);
        userBean.setProvider(YAHOOJP);

        if (baseProvider.checkLoginSocial(userBean)) {
            baseProvider.saveUserDetails(userBean);
            baseProvider.autoLoginUser(userBean);
            return userBean;
        }
        return null;
    }
}
